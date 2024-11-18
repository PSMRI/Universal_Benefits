package digit.web.models.BenefitNewResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class En {
    private BasicDetails basicDetails;
    private BenefitContent benefitContent;
    private ProvidingEntity providingEntity;
    private List<SponsoringEntity> sponsoringEntities;
    private List<Eligibility> eligibility;
    private List<Document> documents;
    private ApplicationProcess applicationProcess;
    private List<ApplicationForm> applicationForm;
}
