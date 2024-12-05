package digit.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import digit.application.config.Configuration;
import digit.application.kafka.Producer;
import digit.application.repository.ApplicationRepository;
import digit.application.util.DocumentAddHttpClient;
import digit.application.util.EnrichmentUtil;
import digit.application.util.ResponseInfoFactory;
import digit.application.util.WorkflowUtil;
import digit.application.validators.BPApplicationValidator;
import digit.application.web.models.*;
import digit.application.web.models.Application.StatusEnum;
import digit.application.web.models.ApplicationStatusUpdateResponse.ApplicationStatusUpdateResponseBuilder;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.common.contract.workflow.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ApplicationService {

	private final EnrichmentUtil enrichmentUtil;
	private final WorkflowUtil workflowUtil;
	private final Configuration configuration;
	private final Producer producer;
	private final ResponseInfoFactory responseInfoFactory;
	private final ApplicationRepository applicationRepository;
	private final BPApplicationValidator applicationValidator;
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private Configuration configs;
	private final DocumentAddHttpClient httpClient;

	@Autowired
	public ApplicationService(EnrichmentUtil enrichmentUtil, WorkflowUtil workflowUtil, Configuration configuration,
			Producer producer, ResponseInfoFactory responseInfoFactory, ApplicationRepository applicationRepository,
			BPApplicationValidator applicationValidator, DocumentAddHttpClient httpClient) {
		this.enrichmentUtil = enrichmentUtil;
		this.workflowUtil = workflowUtil;
		this.configuration = configuration;
		this.producer = producer;
		this.responseInfoFactory = responseInfoFactory;
		this.applicationRepository = applicationRepository;
		this.applicationValidator = applicationValidator;
		this.httpClient = httpClient;
	}

	public ApplicationResponse create(ApplicationRequest applicationRequest, List<MultipartFile> files)
			throws JsonProcessingException {
		try {
			Application application = applicationRequest.getApplication();
			ApplicationResponse response = null;

			applicationValidator.validateBPApplication(applicationRequest, files);
			this.enrichmentUtil.enrichApplicationForCreation(applicationRequest);

			log.info("Creating Application: " + application);

			int index = 0;
			List<Document> docList = new ArrayList<>();

			
			
			if (!ObjectUtils.isEmpty(files)) {
				for (MultipartFile file : files) {
					Response uploadResponse = httpClient.uploadFile(file, application.getTenantId(),
							configuration.getModuleName(), application.getApplicationNumber());

					if (uploadResponse.isSuccessful()) {
						ObjectMapper objectMapper = new ObjectMapper();

						JsonNode responseBody = objectMapper.readTree(uploadResponse.body().string());
						JsonNode filesArray = responseBody.get("files");
						if (filesArray.size() > 0) {
							JsonNode firstFile = filesArray.get(0);
							String fileStoreId = firstFile.get("fileStoreId").asText();

							Document document = Document.builder().fileStoreId(fileStoreId)
									.id(UUID.randomUUID().toString()).build();

							docList.add(document);
						} else {
							log.error("File not uploaded to fileStore service.");
							throw new IOException("Error while uploading documents.");
						}
						index++;
					} else {
						log.error("File not uploaded to fileStore service.");
						throw new IOException("Error while uploading documents.");
					}
				}
			}

			if (!docList.isEmpty()) {
				application.setDocuments(docList);
			}

			if (this.configuration.getIsWorkflowEnabled()) {
				State wfState = workflowUtil
						.callWorkFlow(workflowUtil.prepareWorkflowRequestForApplication(applicationRequest));
				application.setWfStatus(Application.WFStatusEnum.APPROVED);
			}

			application.setStatus(Application.StatusEnum.PENDING_FOR_REVIEW);
			application.setWfStatus(Application.WFStatusEnum.PENDING_FOR_REVIEW);

			// String schema = createJsonFromApplicationRequest(applicationRequest);
			// applicationRequest.getApplication().setSchema(schema);

			producer.push(configuration.getKafkaTopicApplicationCreate(), applicationRequest);
			log.info("Application pushed successfully: " + application);
			response = ApplicationResponse.builder().applications(Arrays.asList(applicationRequest.getApplication()))
					.responseInfo(responseInfoFactory
							.createResponseInfoFromRequestInfo(applicationRequest.getRequestInfo(), true))
					.build();
			/*
			 * List<Application> appString = response.getApplications();
			 */

			/*for (int i = 0; i < response.getApplications().size(); i++) {
				String applicationId = response.getApplications().get(i).getId();
				boolean isApproved = configuration.isAuto_Approve_Applications();

				ApplicationStatusUpdateRequest applicationStatusUpdateRequest = new ApplicationStatusUpdateRequest();

				applicationStatusUpdateRequest.setApplicationId(applicationId);
				applicationStatusUpdateRequest.setStatus("APPROVED");
				if (isApproved) {
					updateApplicationStatus(applicationStatusUpdateRequest);
				}
			}*/
			return response;
		} catch (IOException e) {
			log.error(e.getMessage());
			throw new RuntimeException("Error while uploading documents.");
		} catch (Exception e) {
			throw new RuntimeException("Error while adding application - " + e.getMessage());
		}
	}
	
	

	public String createJsonFromApplicationRequest(ApplicationRequest applicationRequest)
			throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		ArrayNode jsonArray = objectMapper.createArrayNode();

		Application application = applicationRequest.getApplication();
		Applicant applicant = application.getApplicant();

		addFieldToJson(jsonArray, objectMapper, "id", application.getId());
		addFieldToJson(jsonArray, objectMapper, "applicationNumber", application.getApplicationNumber());
		addFieldToJson(jsonArray, objectMapper, "individualId", application.getIndividualId());
		addFieldToJson(jsonArray, objectMapper, "programCode", application.getProgramCode());
		addFieldToJson(jsonArray, objectMapper, "status",
				application.getStatus() != null ? application.getStatus().toString() : null);

		addFieldToJson(jsonArray, objectMapper, "applicantId", applicant.getId());
		addFieldToJson(jsonArray, objectMapper, "studentName", applicant.getStudentName());
		addFieldToJson(jsonArray, objectMapper, "fatherName", applicant.getFatherName());
		addFieldToJson(jsonArray, objectMapper, "samagraId", applicant.getSamagraId());
		addFieldToJson(jsonArray, objectMapper, "currentSchoolName", applicant.getCurrentSchoolName());
		addFieldToJson(jsonArray, objectMapper, "currentSchoolAddress", applicant.getCurrentSchoolAddress());
		addFieldToJson(jsonArray, objectMapper, "currentSchoolAddressDistrict",
				applicant.getCurrentSchoolAddressDistrict());
		addFieldToJson(jsonArray, objectMapper, "currentClass", applicant.getCurrentClass());
		addFieldToJson(jsonArray, objectMapper, "previousYearMarks", applicant.getPreviousYearMarks());
		addFieldToJson(jsonArray, objectMapper, "studentType", applicant.getStudentType());
		addFieldToJson(jsonArray, objectMapper, "aadharLast4Digits", applicant.getAadharLast4Digits());
		addFieldToJson(jsonArray, objectMapper, "caste", applicant.getCaste());
		addFieldToJson(jsonArray, objectMapper, "income", applicant.getIncome());
		addFieldToJson(jsonArray, objectMapper, "gender", applicant.getGender());
		addFieldToJson(jsonArray, objectMapper, "age", String.valueOf(applicant.getAge()));
		addFieldToJson(jsonArray, objectMapper, "disability", String.valueOf(applicant.isDisability()));

		return objectMapper.writeValueAsString(jsonArray);
	}

	private void addFieldToJson(ArrayNode jsonArray, ObjectMapper objectMapper, String name, String value) {
		if (value != null) {
			ObjectNode jsonNode = objectMapper.createObjectNode();
			jsonNode.put("name", name);
			jsonNode.put("value", value);
			jsonArray.add(jsonNode);
		}
	}

	public ApplicationOrderIDUpdateResponse update(ApplicationOrderIDUpdateRequest applicationRequest) {
		String appId=applicationRequest.getApplicationId();
		String NeworderId=applicationRequest.getOrderId();
		System.out.println("appId is "+appId + "NeworderId is "+NeworderId);	
		ApplicationOrderIDUpdateResponse  response = null;
		// TODO: Write business logic to update the application
		producer.push(configuration.getUpdateApplicationOrderId(), applicationRequest);
		response = ApplicationOrderIDUpdateResponse.builder().responseMsg("OrderId Updated Succesfully").responseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(applicationRequest.getRequestInfo(),true)).build();
		return response;
	}

	public ApplicationStatusUpdateResponse updateApplicationStatus(ApplicationStatusUpdateRequest applicationRequest) {
    	String applicationid = applicationRequest.getApplicationId();
    	System.out.println("applicationid is =========>"+applicationid);	
    	String status=applicationRequest.getStatus();  
    	System.out.println("applicationid status is =========>"+status);
    	Application.WFStatusEnum statusEnum = Application.WFStatusEnum.fromValue(status);
        ApplicationStatusUpdateResponse response = null;
        
        producer.push(configuration.getKafkaTopicApplicationUpdateStatus(), applicationRequest);
        response = ApplicationStatusUpdateResponse.builder().responseMsg("Status Updated Succesfully").responseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(applicationRequest.getRequestInfo(),true)).build();
        return response;
    }
	
	//priyanka
	public ApplicationStatusUpdateResponse updateApplicationLog(ApplicationStatusUpdateRequest applicationRequest) {
    	String applicationid = applicationRequest.getApplicationId();
    	String status=applicationRequest.getStatus();
    	System.out.println("applicationid log is =========>"+status);
    	Application.WFStatusEnum statusEnum = Application.WFStatusEnum.fromValue(status);
        ApplicationStatusUpdateResponse response = null;
        producer.push(configuration.getUpdateApplicationLog(), applicationRequest);
        response = ApplicationStatusUpdateResponse.builder().responseMsg("Status Updated Succesfully").responseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(applicationRequest.getRequestInfo(),true)).build();
        return response;
    }
	
	/**
	 * method to search applications from DB based on application search criteria
	 * 
	 * @param applicationSearchRequest
	 * @return
	 */
	public ApplicationResponse search(ApplicationSearchRequest applicationSearchRequest) {

		ApplicationSearchCriteria applicationSearchCriteria = applicationSearchRequest.getCriteria();

		// TODO: Add validations here

		log.info("Enrich disbursalCriteria");
		enrichmentUtil.enrichSearchApplicationRequest(applicationSearchRequest);

		log.info("Search repository using applicationCriteria");
		List<Application> applications = applicationRepository.search(applicationSearchRequest);

		ResponseInfo responseInfo = responseInfoFactory
				.createResponseInfoFromRequestInfo(applicationSearchRequest.getRequestInfo(), true);

		return ApplicationResponse.builder().applications(applications)
				.pagination(applicationSearchRequest.getPagination()).responseInfo(responseInfo).build();
	}

	public List<AppStatResponse> GetAppStat() {
		List<AppStatResponse> statsResponse = new ArrayList<>();
		try {
			statsResponse = applicationRepository.getApplicationStats();
		} catch (Exception ex) {
			log.info("Exception" + ex.getMessage());
		}
		return statsResponse;
	}

	public List<ProviderFundStat> getProviderFundStats() {
		List<ProviderFundStat> response = new ArrayList<>();
		List<Benefit> benefits = new ArrayList<>();
		List<ApplicatonStatastics> applicatonStatastics = new ArrayList<>();
		try {
			benefits = getBenefits();
			applicatonStatastics = applicationRepository.getApplicationStatsByProgramCode();
			response = getFundsInfo(benefits, applicatonStatastics);
			if (response == null) {
				response = applicationRepository.getProviderFundStats();
			}

		} catch (Exception ex) {
			log.info("Exception" + ex.getMessage());
		}
		return response;
	}

	public List<ScholarshipStat> getScholarshipStats() {
		// List<ScholarshipStat> response=new ArrayList<>();
		List<ScholarshipStat> result = new ArrayList<>();
		List<Benefit> benefits = new ArrayList<>();
		List<ApplicatonStatastics> applicatonStatastics = new ArrayList<>();
		try {
			// response=applicationRepository.getScholarshipStats();
			benefits = getBenefits();
			applicatonStatastics = applicationRepository.getApplicationStatsByProgramCode();
			result = getTopXXXBenefitApplicationDetails(benefits, applicatonStatastics);
		} catch (Exception ex) {
			log.info("Exception" + ex.getMessage());
		}
		return result;
	}

	public List<ScholarshipDetails> getScholarshipDetails() {
		List<ScholarshipDetails> response = new ArrayList<>();
		List<ScholarshipDetails> result = new ArrayList<>();
		List<Benefit> benefits = new ArrayList<>();
		List<ApplicatonStatastics> applicatonStatastics = new ArrayList<>();
		try {
			response = applicationRepository.getScholarshipDetails();
			benefits = getBenefits();

			applicatonStatastics = applicationRepository.getApplicationStatsByProgramCode();
			result = getBenefitApplicationDetails(benefits, applicatonStatastics);
		} catch (Exception ex) {
			log.info("Exception" + ex.getMessage());
		}
		return result;
	}

	public List<ApplicatonStatastics> getApplicatonStatastics() {
		List<ApplicatonStatastics> response = new ArrayList<>();

		try {
			response = applicationRepository.getApplicationStatsByProgramCode();

		} catch (Exception ex) {
			log.info("Exception" + ex.getMessage());
		}
		return response;
	}

	public List<Benefit> getBenefits() {
		StringBuilder hostName = new StringBuilder(configs.getMdmsHost());
		hostName.append("/mdms-v2/v1/_search");
		String url = hostName.toString();
		// String url = "https://649b-27-107-3-142.ngrok-free.app/mdms-v2/v1/_search";

		// Create request object for the API
		BenefitsRequest request = new BenefitsRequest();

		// Fill in RequestInfo object
		BenefitsRequest.RequestInfo requestInfo = new BenefitsRequest.RequestInfo();
		requestInfo.setApiId("benefits-services");
		requestInfo.setVer("1.0");
		requestInfo.setAction("_search");
		requestInfo.setAuthToken("dfcca143-b5a6-4726-b5cd-c2c949cb0f2b");

		BenefitsRequest.RequestInfo.UserInfo userInfo = new BenefitsRequest.RequestInfo.UserInfo();
		userInfo.setId("1");
		userInfo.setUuid("40dceade-992d-4a8f-8243-19dda76a4171");
		requestInfo.setUserInfo(userInfo);

		request.setRequestInfo(requestInfo);

		// Fill in MdmsCriteria object
		BenefitsRequest.MdmsCriteria mdmsCriteria = new BenefitsRequest.MdmsCriteria();
		mdmsCriteria.setTenantId("Benefits.Benefit1");

		BenefitsRequest.MdmsCriteria.ModuleDetail moduleDetail = new BenefitsRequest.MdmsCriteria.ModuleDetail();
		moduleDetail.setModuleName("Benefits");

		BenefitsRequest.MdmsCriteria.ModuleDetail.MasterDetail masterDetail = new BenefitsRequest.MdmsCriteria.ModuleDetail.MasterDetail();
		masterDetail.setName("BenefitsTable");

		moduleDetail.setMasterDetails(new BenefitsRequest.MdmsCriteria.ModuleDetail.MasterDetail[] { masterDetail });
		mdmsCriteria.setModuleDetails(new BenefitsRequest.MdmsCriteria.ModuleDetail[] { moduleDetail });

		request.setMdmsCriteria(mdmsCriteria);

		// Make the POST request and get the response
		BenefitsResponse response = restTemplate.postForObject(url, request, BenefitsResponse.class);

		if (response != null && response.getMdmsRes() != null) {
			return response.getMdmsRes().getBenefits().getBenefitsTable();
		} else {
			return null;
		}
	}

	public List<ScholarshipDetails> getBenefitApplicationDetails(List<Benefit> benefits,
			List<ApplicatonStatastics> stats) {
		AtomicInteger idGenerator = new AtomicInteger(1);
		// Joining lists and mapping them to ScholarshipDetails
		List<ScholarshipDetails> scholarshipDetailsList = stats.stream()
				.flatMap(stat -> benefits.stream()
						.filter(benefit -> benefit.getBenefitID().equals(stat.getProgramCode())) // Join on programCode
																									// and benefitID
						.map(benefit -> {
							ScholarshipDetails details = new ScholarshipDetails();

							// Set fields from ApplicatonStatastics
							details.setApplicants(stat.getTotalApplications());
							details.setApproved(stat.getApprovedCount());
							details.setRejected(stat.getRejectedCount());

							// Set fields from Benefit
							details.setName(benefit.getBenefitName());
							details.setDeadline(benefit.getApplicationEnd());

							details.setDisbursalPending(stat.getTotalApplications());
							details.setStatus("Draft");
							details.setId(idGenerator.getAndIncrement());

							return details;
						}))
				.collect(Collectors.toList());

		return scholarshipDetailsList;
	}

	public List<ScholarshipStat> getTopXXXBenefitApplicationDetails(List<Benefit> benefits,
			List<ApplicatonStatastics> stats) {
		AtomicInteger idGenerator = new AtomicInteger(1);
		// Joining lists and mapping them to ScholarshipDetails
		List<ScholarshipStat> scholarshipDetailsList = stats.stream()
				.flatMap(stat -> benefits.stream()
						.filter(benefit -> benefit.getBenefitID().equals(stat.getProgramCode())) // Join on programCode
																									// and benefitID
						.map(benefit -> {
							ScholarshipStat details = new ScholarshipStat();

							// Set fields from ApplicatonStatastics
							details.setTotalApplications(stat.getTotalApplications());
							details.setTotalDisbursed("0");

							// Set fields from Benefit
							details.setTitle(benefit.getBenefitName());
							details.setId(idGenerator.getAndIncrement());

							return details;
						}))
				.limit(3).collect(Collectors.toList());

		return scholarshipDetailsList;
	}

	public List<ProviderFundDetails> getFundStatistics(List<Benefit> benefits, List<ApplicatonStatastics> stats) {
		AtomicInteger idGenerator = new AtomicInteger(1);
		// Joining lists and mapping them to ScholarshipDetails
		List<ProviderFundDetails> scholarshipDetailsList = stats.stream()
				.flatMap(stat -> benefits.stream()
						.filter(benefit -> benefit.getBenefitID().equals(stat.getProgramCode())) // Join on programCode
																									// and benefitID
						.map(benefit -> {
							ProviderFundDetails details = new ProviderFundDetails();

							details.setRemainingAmount(benefit.getRemainingAmount());
							details.setUtilizedAmount(benefit.getAmountUtilized());
							// details.setSponserCount(1);

							return details;
						}))
				.collect(Collectors.toList());

		return scholarshipDetailsList;
	}

	public List<ProviderFundStat> calculateProviderFundStats(List<ProviderFundDetails> providerFundDetailsList) {
		long totalRemainingAmount = 0;
		long totalUtilizedAmount = 0;

		// Calculate sums
		for (ProviderFundDetails fundDetails : providerFundDetailsList) {
			totalRemainingAmount += fundDetails.getRemainingAmount();
			totalUtilizedAmount += fundDetails.getUtilizedAmount();
		}
		if (totalRemainingAmount == 0) {
			totalRemainingAmount = 200000; // Assign default value
		}
		if (totalUtilizedAmount == 0) {
			totalUtilizedAmount = 50000; // Assign default value
		}
		// Create the output list
		List<ProviderFundStat> providerFundStats = new ArrayList<>();
		providerFundStats.add(new ProviderFundStat(1, "Remaining", totalRemainingAmount, 1)); // Assuming sponsorCount =
																								// 2
		providerFundStats.add(new ProviderFundStat(2, "Utilised", totalUtilizedAmount, 1)); // Assuming sponsorCount = 1

		return providerFundStats;
	}

	public List<ProviderFundStat> getFundsInfo(List<Benefit> benefits, List<ApplicatonStatastics> stats) {
		List<ProviderFundDetails> result = getFundStatistics(benefits, stats);
		List<ProviderFundStat> response = calculateProviderFundStats(result);
		return response;
	}

	public Application getApplicationById(String applicationId) {
		try {
			Optional<Application> optionalApplication = applicationRepository.getApplicationById(applicationId);

			if (optionalApplication.isPresent()) {
				return optionalApplication.get();
			} else {
				throw new RuntimeException("Application not found with ID: " + applicationId);
			}
		} catch (Exception e) {
			System.err.println("Error occurred while fetching application: " + e.getMessage());
			throw new RuntimeException("Unable to fetch application. Please try again later.", e);
		}
	}
	
	public List<ApplicationIntegrationResponse> getApplicationIntegration() {
		try {
			List<ApplicationIntegrationResponse> optionalApplication = applicationRepository.getApplicationIntegration();

			 if (optionalApplication.isEmpty()) {
		            throw new RuntimeException("Integration not found.");
		        }
			 return optionalApplication;
		} catch (Exception e) {
			System.err.println("Error occurred while fetching application: " + e.getMessage());
			throw new RuntimeException("Unable to fetch application. Please try again later.", e);
		}
	}
	
	
	// priyanka 25Nov2024
	public Application direct_disbursals(String applicationId) {
		try {
			Optional<Application> optionalApplication = applicationRepository.direct_disbursals(applicationId);

			if (optionalApplication.isPresent()) {
				return optionalApplication.get();
			} else {
				throw new RuntimeException("Application not found with ID: " + applicationId);
			}
		} catch (Exception e) {
			System.err.println("Error occurred while fetching application: " + e.getMessage());
			throw new RuntimeException("Unable to fetch application. Please try again later.", e);
		}
	}

	public ApplicationUpdateBatchIDResponse updateApplication_BatchId(ApplicationUpdateBatchIDRequest applicationRequest) {
		String applicationid = applicationRequest.getApplicationId();
		System.out.println("applicationid ID " + applicationid);
		int batch_id = applicationRequest.getBatch_id();
		System.out.println("batch ID " + batch_id);
		boolean isSuccess = false;
		String message = "";

		try {
			// Simulate Kafka push or any other operation
			producer.push(configuration.getKafkaTopicApplicationUpdateBatchID(), applicationRequest);
			isSuccess = true; // Operation succeeded
			message = "Batch ID updated successfully.";
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
			isSuccess = false;
			message = "Failed to update Batch ID.";
		}

		return ApplicationUpdateBatchIDResponse.builder().applicationID(isSuccess ? applicationid : null).success(isSuccess)
				.message(message).build();
	}

	public List<Application> getDisbursementProcessApplications(String disbursalStatus) {
		try {
			List<Application> optionalApplication = applicationRepository
					.getDisbursementProcessApplications(disbursalStatus);

			if (optionalApplication.isEmpty()) {
				throw new RuntimeException("Application not found with ID: " + disbursalStatus);
			}

			return optionalApplication;
		} catch (Exception e) {
			System.err.println("Error occurred while fetching application: " + e.getMessage());
			throw new RuntimeException("Unable to fetch application. Please try again later.", e);
		}
	}
	public ApplicationUpdateBatchIDResponse updateStatusByBatchId(ApplicationUpdateBatchIDRequest applicationRequest) {
		String applicationID = applicationRequest.getApplicationId();
		boolean isSuccess = false;
		String message = "";
		try {
			producer.push(configuration.getKafkaTopicApplicationupdateStatusByBatchId(), applicationRequest);
			isSuccess = true; // Operation succeeded
			message = "Status updated successfully.";
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
			isSuccess = false;
			message = "Failed to update Status.";
		}

		return ApplicationUpdateBatchIDResponse.builder().applicationID(isSuccess ? applicationID : null).success(isSuccess)
				.message(message).build();
	}

	public ApplicationUpdateBatchIDResponse updatestatusByBatchId_ErrorLog(
	     ApplicationUpdateBatchIDRequest applicationRequest) {
		String applicationID = applicationRequest.getApplicationId();
		boolean isSuccess = false;
		String message = "";
		try {
			producer.push(configuration.getKafkaTopicApplicationupdateStatusByBatchIdErrorLog(), applicationRequest);
			isSuccess = true; // Operation succeeded
			message = "Status updated successfully.";
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
			isSuccess = false;
			message = "Failed to update Status.";
		}

		return ApplicationUpdateBatchIDResponse.builder().applicationID(isSuccess ? applicationID : null).success(isSuccess)
				.message(message).build();
	}

	public List<Application> getApplicationByOrderId(String orderId) {
		try {
			List<Application> optionalApplication = applicationRepository.getApplicationByOrderId(orderId);
 
			if (optionalApplication.isEmpty()) {
				throw new RuntimeException("Application not found with ID: " + orderId);
			}
			return optionalApplication;
		} catch (Exception e) {
			System.err.println("Error occurred while fetching application: " + e.getMessage());
			throw new RuntimeException("Unable to fetch application. Please try again later.", e);
		}
	}
	// End by priyanka

	// Main class end
}
