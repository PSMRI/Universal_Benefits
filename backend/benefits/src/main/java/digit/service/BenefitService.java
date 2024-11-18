package digit.service;

import digit.repository.BenefitRepository;
import digit.web.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.Console;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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

}
