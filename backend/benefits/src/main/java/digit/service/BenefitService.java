package digit.service;

import digit.repository.BenefitRepository;
import digit.web.models.*;
import digit.web.models.Benefit;
import digit.web.models.BenefitNewResponse.*;
import digit.web.models.BenefitNewResponse.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.Console;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BenefitService
{
    private  final  BenefitRepository benefitRepository;
    private KafkaTemplate<String, BenefitStatusUpdateWrapper> kafkaTemplate;

    private final String topicName = "publish-benefit";

    @Autowired
    public BenefitService(BenefitRepository benefitRepository,KafkaTemplate<String, BenefitStatusUpdateWrapper> kafkaTemplate)
    {
        this.benefitRepository=benefitRepository;
        this.kafkaTemplate=kafkaTemplate;
    }
    public List<Benefit> GetAll()
    {
        List<Benefit> lstBenefits=new ArrayList<>();
        try
        {
            lstBenefits=benefitRepository.getAllBenefits();
        }
        catch (Exception e)
        {

        }
        return  lstBenefits;
    }
    public Benefit GetById(String benefitId)
    {
        Benefit benefit=new Benefit();
        try
        {
            benefit=benefitRepository.getById(benefitId);

        }
        catch (Exception e)
        {

        }
        return  benefit;
    }
    public SuccessResponse UpdateStatus(BenefitStatusUpdateRequestModel inputmodel)
    {
        SuccessResponse successResponse=new SuccessResponse();
        BenefitStatusUpdateWrapper benefitStatusUpdateWrapper=new BenefitStatusUpdateWrapper();
        try
        {
            Benefit.BenefitsStatusEnum statusEnum = Benefit.BenefitsStatusEnum.fromValue(inputmodel.getStatus());
            if (statusEnum == null)
            {
                successResponse.setIsSuccess(false);
                successResponse.setMessage("Invalid status value.");
                return successResponse;
            }
            benefitStatusUpdateWrapper.setBenefitStatusUpdate(inputmodel);
            kafkaTemplate.send(topicName, benefitStatusUpdateWrapper);
           // kafkaTemplate.send(topicName, inputmodel.getBenefitId(), inputmodel);
            successResponse.setIsSuccess(true);
            successResponse.setMessage("Status updated successfully.");
            return successResponse;

        }
        catch (Exception e)
        {
            return  successResponse;
        }
    }
    public SuccessResponse discard(DiscardRequest inputmodel)
    {
        SuccessResponse successResponse=new SuccessResponse();
        try
        {
            String status=benefitRepository.getBenefitStatusById(inputmodel.getBenefitId());
            if("active".equalsIgnoreCase(status))
            {
                successResponse.setIsSuccess(false);
                successResponse.setMessage("The benefit is active, can not to deleted.");
                return successResponse;
            }
            int updateCount=benefitRepository.discard(inputmodel.getBenefitId());
            if(updateCount<=0)
            {
                successResponse.setIsSuccess(false);
                successResponse.setMessage("Invalid benefit Id.");
                return successResponse;
            }
            successResponse.setIsSuccess(true);
            successResponse.setMessage(" deleted successfully.");
            return successResponse;

        }
        catch (Exception e)
        {
            return  successResponse;
        }
    }
    public List<Benefit> search(SearchCriteria searchCriteria)
    {
        List<Benefit> benefits=new ArrayList<>();
        try
        {
            benefits=benefitRepository.searchBenefits(searchCriteria.getName(),searchCriteria.getValidTill(),null,null,searchCriteria.getStatus(), searchCriteria.getPageNo(), searchCriteria.getPageSize(), searchCriteria.getSortBy());
            return  benefits;
        }
        catch (Exception e)
        {
            return  benefits;
        }

    }
    public List<BenefitStatusCount> getApplicantionsStatusCount_Benefitwise(SearchCriteria filter)
    {
        List<BenefitStatusCount> benefits=new ArrayList<>();
        try
        {
            benefits=benefitRepository.getBenefitStatusCount(filter);
            return  benefits;
        }
        catch (Exception e)
        {
            return  benefits;
        }

    }
    public BenefitCollapseResponse getBenefitBriefDetails(String benefitId)
    {
        BenefitCollapseResponse response=new BenefitCollapseResponse();
        ProviderInfo providerInfo=new ProviderInfo();
        BenefitInfo data=new BenefitInfo();
        try
        {
            data=benefitRepository.getBenefitBriefDetails(benefitId);
            if(data!=null)
            {
                //set provider data
                providerInfo.setId(18);
                providerInfo.setDocumentId("jz4r7ot3rq66kzwzk7tbkb8v");
                data.setProvider(providerInfo);

                data.setDocumentId("xxm9upd67fqkxa05flzqlor0");
                data.setPrice(new BigDecimal("1000000"));

                response.setSuccess(true);
                response.setBenefitInfo(data);
            }

        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
        return  response;
    }

    public List<Benefit> GetBenefits()
    {
        List<Benefit> lstBenefits=new ArrayList<>();
        try
        {
            lstBenefits=benefitRepository.getBenefits();
        }
        catch (Exception e)
        {

        }
        return  lstBenefits;
    }
    public List<AmountPerBeneficiaryCategory> GetAmtperBenefitCategory()
    {
        List<AmountPerBeneficiaryCategory> amtpercategory=new ArrayList<>();
        try
        {
            amtpercategory=benefitRepository.getAmtperBenefitCategory();
        }
        catch (Exception e)
        {

        }
        return  amtpercategory;
    }
    public static boolean isValidBenefitStatus(String status) {
        try {
            ResponseInfo.StatusEnum.valueOf(status.toUpperCase()); // Convert to uppercase for case-insensitivity
            return true;
        } catch (IllegalArgumentException e) {
            return false; // Not a valid enum constant
        }
    }

    public ResponseEntity<?> getApplicationsByBenefitId(String benefitId) {
        try{
            String benefitName =benefitRepository.findBenefitNameById(benefitId);
            
            System.out.println("benefitName -"+benefitName);
            
            if(ObjectUtils.isEmpty(benefitName)) {
                return new ResponseEntity<>("BenefitId not found", HttpStatus.BAD_REQUEST);
            }
            List<Application> result = benefitRepository.getApplicationsByBenefitName(benefitName);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error while fetching applications", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public List<ScholarshipDetails> getNewList()
    {
        List<ScholarshipDetails> response=new ArrayList<>();
        List<Benefit> data=new ArrayList<>();
        try
        {
            data=benefitRepository.getBenefits();
            if(data!=null)
            {
                response=mapInputsToScholarshipDetails(data);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return  response;
    }
    public List<ScholarshipDetails> mapInputsToScholarshipDetails(List<Benefit> inputs) {
        return inputs.stream()
                .map(this::mapInputToScholarshipDetails)
                .collect(Collectors.toList());
    }
    public ScholarshipDetails mapInputToScholarshipDetails(Benefit input)
    {
        ScholarshipDetails scholarshipDetails = new ScholarshipDetails();
        En en = new En();

        // Map BasicDetails
        BasicDetails basicDetails = new BasicDetails();
        basicDetails.setTitle(input.getBenefitName());
        basicDetails.setCategory("education-and-learning");
        basicDetails.setSubCategory("scholarship");
        basicDetails.setTags(Arrays.asList("SC", "Financial Assistance", "Scholarship", "Student"));
       // basicDetails.setApplicationOpenDate(formatDate(input.getOtherTermsAndConditions().getApplicationDeadlineDate()));
        //basicDetails.setApplicationCloseDate(formatDate(input.getOtherTermsAndConditions().getExtendedDeadlineDate()));
        en.setBasicDetails(basicDetails);

        //Map References
        List<Reference> references=new ArrayList<>();
        references.add(new Reference("https://scholarships.gov.in/public/schemeGuidelines/Assam_PRE_Matric_Scholarship_Guideline.pdf","Guidelines"));
        references.add(new Reference("https://scholarships.gov.in/public/FAQ/FAQ_ASSAM.pdf","FAQs"));

        // Map BenefitContent
        BenefitContent benefitContent = new BenefitContent();
        benefitContent.setShortDescription(input.getBenefitDescription());
        benefitContent.setShortDescription_md(input.getBenefitDescription()); // Markdown conversion can be applied
        benefitContent.setLongDescription("This is the detailed description for the scholarship."); // Hardcoded for example
        benefitContent.setLongDescription_md("This is the detailed description for the scholarship <br />Day Scholar<br />Boys - 3500<br />Girls - 3500<br />Disabled - 3850<br /><br />Hosteller<br />Boys - 7000<br />Girls - 7000<br />Disabled - 7700"); // Markdown example
        benefitContent.setBenefits(Arrays.asList(
                new digit.web.models.BenefitNewResponse.Benefit("financial", "Scholarship Amount", "Up to $5000", "Up to $5000 in Markdown")
        ));
        benefitContent.setExclusions(Arrays.asList(
                new Exclusion("Not available for students who have already availed similar benefits.", "Markdown for exclusion")
        ));
        benefitContent.setReferences(references);
        en.setBenefitContent(benefitContent);

        // Map ProvidingEntity
        ProvidingEntity providingEntity = new ProvidingEntity();
        providingEntity.setType("government");
        providingEntity.setName(input.getBenefitProvider());
        providingEntity.setAddress(new Address("123 Main St", "CityName", "StateName", "123456"));
        providingEntity.setContactInfo(new ContactInfo("123-456-7890", "info@provider.com"));
        en.setProvidingEntity(providingEntity);

        // Map SponsoringEntities
        List<SponsoringEntity> sponsoringEntities = input.getSponsors().stream().map(sponsor -> {
            SponsoringEntity entity = new SponsoringEntity();
            entity.setName(sponsor.getSponsorName());
            entity.setSponsorShare(String.valueOf(sponsor.getSharePercent()));
            entity.setType("corporate");
            return entity;
        }).collect(Collectors.toList());
        en.setSponsoringEntities(sponsoringEntities);

        // Map Eligibility
        List<Eligibility> eligibilityList = new ArrayList<>();

        Eligibility educationalEligibility=new Eligibility();
        educationalEligibility.setType("educational");
        educationalEligibility.setDescription("The applicant must be a student studying in Class "+input.getEligibility().getAcademicClass());
        educationalEligibility.setCriteria(new Criteria("class", "in", input.getEligibility().getAcademicClass()));
        educationalEligibility.setEvidence("class");

        List<String> allowedProofs = new ArrayList<>();
        allowedProofs.add("academicCertificate");
        allowedProofs.add("bonafideCertificate");
        allowedProofs.add("enrollmentCertificate");
        educationalEligibility.setAllowedProofs(allowedProofs);
        eligibilityList.add(educationalEligibility);

        Eligibility casteEligibility=new Eligibility();
        casteEligibility.setType("personal");
        casteEligibility.setDescription("The applicant must be from "+input.getEligibility().getCaste());
        casteEligibility.setCriteria(new Criteria("caste", "equals", input.getEligibility().getCaste()));
        casteEligibility.setEvidence("caste");
        casteEligibility.setAllowedProofs(Collections.singletonList("casteCertificate"));
        eligibilityList.add(casteEligibility);

        Eligibility incomeEligibility = new Eligibility();
        incomeEligibility.setType("economical");
        incomeEligibility.setDescription("Annual income less than "+input.getEligibility().getAnnualIncome());
        incomeEligibility.setCriteria(new Criteria("Annual Income", "max", input.getEligibility().getAnnualIncome()));
        incomeEligibility.setEvidence("annualIncome");
        incomeEligibility.setAllowedProofs(Collections.singletonList("incomeCertificate"));
        eligibilityList.add(incomeEligibility);

        Eligibility locationEligibility=new Eligibility();
        locationEligibility.setType("geographical");
        locationEligibility.setDescription("The applicant must be from "+input.getEligibility().getDomicile());
        locationEligibility.setCriteria(new Criteria("state", "equals", input.getEligibility().getDomicile()));
        locationEligibility.setEvidence("state");
        locationEligibility.setAllowedProofs(Collections.singletonList("domicileCertificate"));
        eligibilityList.add(locationEligibility);

        en.setEligibility(eligibilityList);

        // Map Documents
        List<digit.web.models.BenefitNewResponse.Document> documents = Arrays.asList(
                new digit.web.models.BenefitNewResponse.Document("Identity Proof", true, Arrays.asList("Aadhaar", "Passport")),
                new Document("Income Proof", true, Arrays.asList("Salary Slip", "Bank Statement"))
        );
        en.setDocuments(documents);

        // Map Application Process
        ApplicationProcess applicationProcess = new ApplicationProcess();
        applicationProcess.setMode("Online");
        applicationProcess.setDescription("Apply online through the portal.");
        applicationProcess.setDescription_md("Markdown description for applying online.");
        en.setApplicationProcess(applicationProcess);

        // Map Application Form
        /*List<ApplicationForm> applicationForms = Arrays.asList(
                new ApplicationForm("text", "firstName", "First Name", true, false, null),
                new ApplicationForm("text", "lastName", "Last Name", true, false, null)
        );*/
        List<ApplicationForm> applicationForms = Arrays.asList(
                new ApplicationForm("text", "firstName", "Enter your First Name", true, false, null),
                new ApplicationForm("text", "middleName", "Enter your Middle Name", true, false, null),
                new ApplicationForm("text", "lastName", "Enter your Last Name", true, false, null),
                new ApplicationForm("radio", "gender", "Choose your Gender", true, false,
                        Arrays.asList(
                                new Option("male", "Male"),
                                new Option("female", "Female")
                        )
                ),
                new ApplicationForm("select", "class", "Enter your Class", true, false,
                        Arrays.asList(
                                new Option("9", "9"),
                                new Option("10", "10")
                        )
                ),
                new ApplicationForm("select", "annualIncome", "Choose your parent's Annual Income", true, false,
                        Arrays.asList(
                                new Option("99999", "Below ₹1,00,000"),
                                new Option("250000", "₹1,00,001 - ₹2,50,000"),
                                new Option("500000", "₹2,50,001 - ₹5,00,000"),
                                new Option("750000", "₹5,00,001 - ₹7,50,000"),
                                new Option("other", "other")
                        )
                ),
                new ApplicationForm("select", "caste", "Choose your Caste", true, false,
                        Arrays.asList(
                                new Option("general", "General"),
                                new Option("sc", "SC"),
                                new Option("st", "ST"),
                                new Option("obc", "OBC"),
                                new Option("na", "NA")
                        )
                ),
                new ApplicationForm("radio", "disabled", "Are you a differently-abled person?", true, false,
                        Arrays.asList(
                                new Option("yes", "Yes"),
                                new Option("no", "No")
                        )
                ),
                new ApplicationForm("select", "domicile", "Choose your Domicile State", true, false,
                        Arrays.asList(
                                new Option("madhya-pradesh", "Madhya Pradesh"),
                                new Option("other", "Other")
                        )
                ),
                new ApplicationForm("radio", "scholarshipType", "Are you a Day Scholar or Hostler?", true, false,
                        Arrays.asList(
                                new Option("dayScholar", "Day Scholar"),
                                new Option("hostler", "Hostler")
                        )
                )
        );

        en.setApplicationForm(applicationForms);



        // Set to ScholarshipDetails
        scholarshipDetails.setEn(en);

        return scholarshipDetails;
    }

    public VisualRepresentationResponse getVisualRepresentation(String benefitId, String monthYear) {
        // Fetch distributions using repository methods
        List<GraphData> applicantsDisbursals = benefitRepository.getWeeklyData(benefitId, monthYear);
        List<GraphData> gender = benefitRepository.getGenderDistribution(benefitId, monthYear);
        List<GraphData> caste = benefitRepository.getCasteDistribution(benefitId, monthYear);
        List<GraphData> age = benefitRepository.getAgeDistribution(benefitId, monthYear);
        List<GraphData> ratio = benefitRepository.getRatioDistribution(benefitId, monthYear);

        // Construct response object
        VisualRepresentationResponse response = new VisualRepresentationResponse();
        response.setApplicantsDisbursals(applicantsDisbursals);
        response.setGender(gender);
        response.setCaste(caste);
        response.setAge(age);
        response.setRatio(ratio);

        return response;
    }
}
