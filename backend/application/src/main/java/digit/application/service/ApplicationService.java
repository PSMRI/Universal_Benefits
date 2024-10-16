package digit.application.service;

import digit.application.config.Configuration;
import digit.application.kafka.Producer;
import digit.application.repository.ApplicationRepository;
import digit.application.util.EnrichmentUtil;
import digit.application.util.ResponseInfoFactory;
import digit.application.util.WorkflowUtil;
import digit.application.validators.BPApplicationValidator;
import digit.application.web.models.*;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.common.contract.workflow.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

    @Autowired
    public ApplicationService(EnrichmentUtil enrichmentUtil, WorkflowUtil workflowUtil, Configuration configuration, Producer producer, ResponseInfoFactory responseInfoFactory, ApplicationRepository applicationRepository,BPApplicationValidator applicationValidator) {
        this.enrichmentUtil = enrichmentUtil;
        this.workflowUtil = workflowUtil;
        this.configuration = configuration;
        this.producer = producer;
        this.responseInfoFactory = responseInfoFactory;
        this.applicationRepository = applicationRepository;
        this.applicationValidator = applicationValidator;
    }

    public ApplicationResponse create(ApplicationRequest applicationRequest) {
        Application application = applicationRequest.getApplication();
        ApplicationResponse response = null;

        // TODO: Do custom validations here
        // For example: programCode, individualId, filestoreIds should be valid

        applicationValidator.validateBPApplication(applicationRequest);
        this.enrichmentUtil.enrichApplicationForCreation(applicationRequest);

        log.info("Creating Application: " + application);

        if (this.configuration.getIsWorkflowEnabled()) {
            State wfState = workflowUtil.callWorkFlow(workflowUtil.prepareWorkflowRequestForApplication(applicationRequest));
            application.setWfStatus(Application.WFStatusEnum.APPROVED);
        }
        application.setStatus(Application.StatusEnum.ARCHIVED);
        application.setWfStatus(Application.WFStatusEnum.APPROVED);
        // TODO: Remove after creating the workflow
//        application.setWfStatus("PENDING_FOR_VERIFICATION"); //
        producer.push(configuration.getKafkaTopicApplicationCreate(), applicationRequest);
        log.info("Application pushed successfully: " + application);
        response = ApplicationResponse.builder()
                .applications(Arrays.asList(applicationRequest.getApplication()))
                .responseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(applicationRequest.getRequestInfo(), true))
                .build();

        return response;
    }

    /**
     * Updates an existing application
     * @param applicationRequest
     * @return ApplicationResponse with the updated application
     */
    public ApplicationResponse update(ApplicationRequest applicationRequest) {

        Application application = applicationRequest.getApplication();
        ApplicationResponse response = null;
        // TODO: Write business logic to update the application
        producer.push(configuration.getKafkaTopicApplicationUpdate(), applicationRequest);
        response = ApplicationResponse.builder()
                .applications(Arrays.asList(applicationRequest.getApplication()))
                .responseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(applicationRequest.getRequestInfo(), true))
                .build();

        return response;
    }

    /**
     * method to search applications from DB based on application search criteria
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

        ResponseInfo responseInfo = responseInfoFactory.
                createResponseInfoFromRequestInfo(applicationSearchRequest.getRequestInfo(),true);

        return ApplicationResponse.builder()
                .applications(applications)
                .pagination(applicationSearchRequest.getPagination())
                .responseInfo(responseInfo)
                .build();
    }
    public List<AppStatResponse> GetAppStat()
    {
        List<AppStatResponse> statsResponse=new ArrayList<>();
        try
        {
            statsResponse=applicationRepository.getApplicationStats();
        }
        catch(Exception ex)
        {
            log.info("Exception"+ex.getMessage());
        }
        return statsResponse;
    }
    public List<ProviderFundStat> getProviderFundStats()
    {
        List<ProviderFundStat> response=new ArrayList<>();
        List<Benefit> benefits=new ArrayList<>();
        List<ApplicatonStatastics> applicatonStatastics=new ArrayList<>();
        try
        {
            benefits=getBenefits();
            applicatonStatastics=applicationRepository.getApplicationStatsByProgramCode();
            response=getFundsInfo(benefits,applicatonStatastics);
            if(response==null) {
                response = applicationRepository.getProviderFundStats();
            }

        }
        catch(Exception ex)
        {
            log.info("Exception"+ex.getMessage());
        }
        return response;
    }
    public List<ScholarshipStat> getScholarshipStats()
    {
        //List<ScholarshipStat> response=new ArrayList<>();
        List<ScholarshipStat> result=new ArrayList<>();
        List<Benefit> benefits=new ArrayList<>();
        List<ApplicatonStatastics> applicatonStatastics=new ArrayList<>();
        try
        {
            //response=applicationRepository.getScholarshipStats();
            benefits=getBenefits();
            applicatonStatastics=applicationRepository.getApplicationStatsByProgramCode();
            result=getTopXXXBenefitApplicationDetails(benefits,applicatonStatastics);
        }
        catch(Exception ex)
        {
            log.info("Exception"+ex.getMessage());
        }
        return result;
    }
    public List<ScholarshipDetails> getScholarshipDetails()
    {
        List<ScholarshipDetails> response=new ArrayList<>();
        List<ScholarshipDetails> result=new ArrayList<>();
        List<Benefit> benefits=new ArrayList<>();
        List<ApplicatonStatastics> applicatonStatastics=new ArrayList<>();
        try
        {
            response=applicationRepository.getScholarshipDetails();
            benefits=getBenefits();

            applicatonStatastics=applicationRepository.getApplicationStatsByProgramCode();
            result=getBenefitApplicationDetails(benefits,applicatonStatastics);
        }
        catch(Exception ex)
        {
            log.info("Exception"+ex.getMessage());
        }
        return result;
    }
    public List<ApplicatonStatastics> getApplicatonStatastics()
    {
        List<ApplicatonStatastics> response=new ArrayList<>();

        try
        {
            response=applicationRepository.getApplicationStatsByProgramCode();

        }
        catch(Exception ex)
        {
            log.info("Exception"+ex.getMessage());
        }
        return response;
    }
    public List<Benefit> getBenefits() {
        StringBuilder hostName = new StringBuilder(configs.getMdmsHost());
        hostName.append("/mdms-v2/v1/_search");
        String url = hostName.toString();
        //String url = "https://649b-27-107-3-142.ngrok-free.app/mdms-v2/v1/_search";

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


        moduleDetail.setMasterDetails(new BenefitsRequest.MdmsCriteria.ModuleDetail.MasterDetail[]{masterDetail});
        mdmsCriteria.setModuleDetails(new BenefitsRequest.MdmsCriteria.ModuleDetail[]{moduleDetail});

        request.setMdmsCriteria(mdmsCriteria);

        // Make the POST request and get the response
        BenefitsResponse response = restTemplate.postForObject(url, request, BenefitsResponse.class);


        if (response != null && response.getMdmsRes() != null) {
            return response.getMdmsRes().getBenefits().getBenefitsTable();
        } else {
            return null;
        }
    }


    public List<ScholarshipDetails> getBenefitApplicationDetails(List<Benefit> benefits, List<ApplicatonStatastics> stats) {
        AtomicInteger idGenerator = new AtomicInteger(1);
        // Joining lists and mapping them to ScholarshipDetails
        List<ScholarshipDetails> scholarshipDetailsList = stats.stream()
                .flatMap(stat -> benefits.stream()
                        .filter(benefit -> benefit.getBenefitID().equals(stat.getProgramCode()))  // Join on programCode and benefitID
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
                        })
                )
                .collect(Collectors.toList());

        return scholarshipDetailsList;
    }
    public List<ScholarshipStat> getTopXXXBenefitApplicationDetails(List<Benefit> benefits, List<ApplicatonStatastics> stats) {
        AtomicInteger idGenerator = new AtomicInteger(1);
        // Joining lists and mapping them to ScholarshipDetails
        List<ScholarshipStat> scholarshipDetailsList = stats.stream()
                .flatMap(stat -> benefits.stream()
                        .filter(benefit -> benefit.getBenefitID().equals(stat.getProgramCode()))  // Join on programCode and benefitID
                        .map(benefit -> {
                            ScholarshipStat details = new ScholarshipStat();

                            // Set fields from ApplicatonStatastics
                            details.setTotalApplications(stat.getTotalApplications());
                            details.setTotalDisbursed("0");

                            // Set fields from Benefit
                            details.setTitle(benefit.getBenefitName());
                            details.setId(idGenerator.getAndIncrement());

                            return details;
                        })
                )
                .limit(3)
                .collect(Collectors.toList());

        return scholarshipDetailsList;
    }
    public List<ProviderFundDetails> getFundStatistics(List<Benefit> benefits, List<ApplicatonStatastics> stats) {
        AtomicInteger idGenerator = new AtomicInteger(1);
        // Joining lists and mapping them to ScholarshipDetails
        List<ProviderFundDetails> scholarshipDetailsList = stats.stream()
                .flatMap(stat -> benefits.stream()
                        .filter(benefit -> benefit.getBenefitID().equals(stat.getProgramCode()))  // Join on programCode and benefitID
                        .map(benefit -> {
                            ProviderFundDetails details = new ProviderFundDetails();


                            details.setRemainingAmount(benefit.getRemainingAmount());
                            details.setUtilizedAmount(benefit.getAmountUtilized());
                            //details.setSponserCount(1);

                            return details;
                        })
                )
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
            totalUtilizedAmount =50000; // Assign default value
        }
        // Create the output list
        List<ProviderFundStat> providerFundStats = new ArrayList<>();
        providerFundStats.add(new ProviderFundStat(1, "Remaining", totalRemainingAmount, 1)); // Assuming sponsorCount = 2
        providerFundStats.add(new ProviderFundStat(2, "Utilised", totalUtilizedAmount, 1)); // Assuming sponsorCount = 1

        return providerFundStats;
    }

    public List<ProviderFundStat> getFundsInfo(List<Benefit> benefits, List<ApplicatonStatastics> stats)
    {
        List<ProviderFundDetails> result=getFundStatistics( benefits, stats);
        List<ProviderFundStat> response=calculateProviderFundStats(result);
        return response;
    }
}
