package digit.validators;

import digit.repository.ServiceRequestRepository;
import digit.web.models.Benefit;
import digit.web.models.MessageObj;
import digit.web.models.Sponsor;
import digit.web.models.ValidationResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@Slf4j
public class BenefitsApplicationValidator {

    @Autowired
    private ServiceRequestRepository repository;

    public ValidationResult validateCreateBenefitsApplication(Benefit benefitsCreateRequest) {
        ValidationResult result = new ValidationResult();
        try{
            boolean benefitWithSameNameExists = repository.checkBenefitNameExists(benefitsCreateRequest.getBenefitName(), benefitsCreateRequest.getBenefitProvider());

            if(benefitWithSameNameExists) {
                result.getMessages().add(MessageObj.builder().fieldName("benefitName").message("Benefit with this name already exists.").build());
            }

            boolean validProvider = repository.isProviderValid(benefitsCreateRequest.getBenefitProvider());

            if(!validProvider) {
                result.getMessages().add(MessageObj.builder().fieldName("benefitProvider").message("Provider is not present.").build());
            }

            boolean hasPrimarySponsor = hasPrimarySponsor(benefitsCreateRequest.getSponsors());

            if(!hasPrimarySponsor) {
                result.getMessages().add(MessageObj.builder().fieldName("sponsors").message("Atleast one primary sponsor should be present").build());
            }

            if(!isSponsorSharesSum100(benefitsCreateRequest.getSponsors())){
                result.getMessages().add(MessageObj.builder().fieldName("sponsors").message("Please check the share percentages for sponsor. Must be equal to 100%").build());
            }

            if((long) result.getMessages().size() > 0){
                result.setValid(false);
            }

            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ValidationResult validateBenefitsMainDetailsOnly(Benefit benefitsUpdateRequest){
        ValidationResult result = new ValidationResult();
        try{
            Benefit benefitData = repository.getBenefitData(benefitsUpdateRequest.getBenefitId());

            if(benefitData != null) {
//                if(benefitData.getStatus() == Benefit.BenefitsStatusEnum.Active){
//                    result.setValid(false);
//                    result.getMessages().add(MessageObj.builder().fieldName("status").message("Benefits scheme in active status cannot be updated.").build());
//                    return result;
//                }

                if(!Objects.equals(benefitData.getBenefitName(), benefitsUpdateRequest.getBenefitName()) || !Objects.equals(benefitData.getBenefitProvider(), benefitsUpdateRequest.getBenefitProvider())){
                    boolean benefitWithSameNameExists = repository.checkBenefitNameExists(benefitsUpdateRequest.getBenefitName(), benefitsUpdateRequest.getBenefitProvider());

                    if(benefitWithSameNameExists) {
                        result.getMessages().add(MessageObj.builder().fieldName("benefitName").message("Benefit with this name already exists.").build());
                    }
                }

                boolean hasPrimarySponsor = hasPrimarySponsor(benefitsUpdateRequest.getSponsors());

                if(!hasPrimarySponsor) {
                    result.getMessages().add(MessageObj.builder().fieldName("sponsors").message("Atleast one primary sponsor should be present").build());
                }

                if(!isSponsorSharesSum100(benefitsUpdateRequest.getSponsors())){
                    result.getMessages().add(MessageObj.builder().fieldName("sponsors").message("Please check the share percentages for sponsor. Must be equal to 100%").build());
                }

            }else{
                result.getMessages().add(MessageObj.builder().fieldName("benefitId").message("Benefit with this Id not found.").build());
            }

            if((long) result.getMessages().size() > 0){
                result.setValid(false);
            }
            return result;

        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    public ValidationResult validateEligibilityDetails(Benefit benefitsUpdateRequest){
        ValidationResult result = new ValidationResult();
        try{
            Benefit benefitData = repository.getBenefitData(benefitsUpdateRequest.getBenefitId());

            if(benefitData != null) {
//                if(benefitData.getStatus() == Benefit.BenefitsStatusEnum.Active){
//                    result.setValid(false);
//                    result.getMessages().add(MessageObj.builder().fieldName("status").message("Benefits scheme in active status cannot be updated.").build());
//                    return result;
//                }
                return result;
            }else{
                result.getMessages().add(MessageObj.builder().fieldName("benefitId").message("Benefit with this Id not found.").build());
            }

            if((long) result.getMessages().size() > 0){
                result.setValid(false);
            }
            return result;

        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    public ValidationResult validateFinancialInformationDetails(Benefit benefitsUpdateRequest){
        ValidationResult result = new ValidationResult();
        try{
            Benefit benefitData = repository.getBenefitData(benefitsUpdateRequest.getBenefitId());

            if(benefitData != null) {
//                if(benefitData.getStatus() == Benefit.BenefitsStatusEnum.Active){
//                    result.setValid(false);
//                    result.getMessages().add(MessageObj.builder().fieldName("status").message("Benefits scheme in active status cannot be updated.").build());
//                    return result;
//                }

                if(benefitsUpdateRequest.getFinancialInformation().getMaxBeneficiaryLimit() && Objects.isNull(benefitsUpdateRequest.getFinancialInformation().getMaxBeneficiaryAllowed())){
                    result.getMessages().add(MessageObj.builder().fieldName("benefitId").message("Max Beneficiary Count is required when limit condition is true").build());
                }

                if(Objects.isNull(benefitsUpdateRequest.getFinancialInformation().getAmountPerBeneficiaryCategory())){
                    benefitsUpdateRequest.getFinancialInformation().setAmountPerBeneficiaryCategory(new ArrayList<>());
                }

                return result;
            }else{
                result.getMessages().add(MessageObj.builder().fieldName("benefitId").message("Benefit with this Id not found.").build());
            }

            if((long) result.getMessages().size() > 0){
                result.setValid(false);
            }
            return result;

        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    public ValidationResult validateTermsandConditionDetails(Benefit benefitsUpdateRequest){
        ValidationResult result = new ValidationResult();
        try{
            Benefit benefitData = repository.getBenefitData(benefitsUpdateRequest.getBenefitId());

            if(benefitData != null) {
//                if(benefitData.getStatus() == Benefit.BenefitsStatusEnum.Active){
//                    result.setValid(false);
//                    result.getMessages().add(MessageObj.builder().fieldName("status").message("Benefits scheme in active status cannot be updated.").build());
//                    return result;
//                }
                return result;
            }else{
                result.getMessages().add(MessageObj.builder().fieldName("benefitId").message("Benefit with this Id not found.").build());
            }

            if((long) result.getMessages().size() > 0){
                result.setValid(false);
            }
            return result;

        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    public boolean isSponsorSharesSum100(List<Sponsor> sponsors){
        BigDecimal totalSharePercent = sponsors.stream()
                .filter(Objects::nonNull)
                .map(Sponsor::getSharePercent)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return totalSharePercent.compareTo(new BigDecimal("100.00")) == 0;
    }

    public boolean hasPrimarySponsor(List<Sponsor> sponsors) {
        return sponsors != null && sponsors.stream()
                .filter(Objects::nonNull)
                .anyMatch(sponsor -> Sponsor.SponsorType.PRIMARY.equals(sponsor.getType()));
    }


}
