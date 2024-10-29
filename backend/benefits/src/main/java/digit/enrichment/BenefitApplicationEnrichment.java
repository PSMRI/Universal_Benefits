package digit.enrichment;

import digit.util.IdgenUtil;
import digit.web.models.AmountPerBeneficiaryCategory;
import digit.web.models.AuditDetails;
import digit.web.models.Benefit.BenefitsStatusEnum;
import digit.web.models.BenefitsRegistrationRequest;
import digit.web.models.Sponsor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import digit.config.Configuration;

import java.util.Objects;
import java.util.UUID;

@Component
public class BenefitApplicationEnrichment {

    private final Configuration configuration;

    private final IdgenUtil idgenUtil;

    @Autowired
    private BenefitApplicationEnrichment(Configuration configuration, IdgenUtil idgenUtil) {
        this.configuration = configuration;
        this.idgenUtil = idgenUtil;

    }


    public BenefitsRegistrationRequest enrichCreateBenefitApplication(BenefitsRegistrationRequest request) {
        String createdBy = request.getRequestInfo().getUserInfo().getUuid();

        AuditDetails auditDetails = getAuditDetails(createdBy, true);

        String benefitId = idgenUtil
                .getIdList(request.getRequestInfo(), "pb", configuration.getIdGenApplicationFormat(), null ,1).get(0);

        request.getBenefit().setBenefitId(benefitId);
        request.getBenefit().setAuditDetails(auditDetails);
        request.getBenefit().setStatus(BenefitsStatusEnum.DRAFT);

        for (Sponsor sponsor : request.getBenefit().getSponsors()) {
            sponsor.setId(UUID.randomUUID().toString());
        }

        return request;
    }

    public BenefitsRegistrationRequest enrichUpdateBenefitApplication(BenefitsRegistrationRequest request) {
        String updatedBy = request.getRequestInfo().getUserInfo().getUuid();
        AuditDetails auditDetails = getAuditDetails(updatedBy, true);
        request.getBenefit().setAuditDetails(auditDetails);

        for (Sponsor sponsor : request.getBenefit().getSponsors()) {
            if(Objects.isNull(sponsor.getId())){
                sponsor.setId(UUID.randomUUID().toString());
            }
        }

        if(!Objects.isNull(request.getBenefit().getFinancialInformation()) && !Objects.isNull(request.getBenefit().getFinancialInformation().getAmountPerBeneficiaryCategory())){
            for (AmountPerBeneficiaryCategory amount : request.getBenefit().getFinancialInformation().getAmountPerBeneficiaryCategory()) {
                if(Objects.isNull(amount.getId())){
                    amount.setId(UUID.randomUUID().toString());
                }
            }
        }

        return request;
    }

    public AuditDetails getAuditDetails(String by , boolean isCreate){
        Long time = System.currentTimeMillis();

        if (isCreate){
            return AuditDetails.builder()
                    .createdBy(by)
                    .createdTime(time)
                    .lastModifiedBy(by)
                    .lastModifiedTime(time)
                    .build();
        }else{
            return AuditDetails.builder()
                    .lastModifiedBy(by)
                    .lastModifiedTime(time)
                    .build();
        }
    }



}
