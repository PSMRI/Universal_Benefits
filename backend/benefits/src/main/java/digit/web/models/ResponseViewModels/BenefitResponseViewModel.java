package digit.web.models.ResponseViewModels;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BenefitResponseViewModel {

    @JsonProperty("benefitId")
    private String benefitId;

    @JsonProperty("benefitName")
    private String benefitName;

    @JsonProperty("benefitProvider")
    private String benefitProvider;

    @JsonProperty("benefitDescription")
    private String benefitDescription;

    @JsonProperty("sponsors")
    private List<SponsorResponseViewModel> sponsors;

    @JsonProperty("status")
    private String status;

    @JsonProperty("eligibility")
    private EligibilityResponseViewModel eligibility;

    @JsonProperty("financialInformation")
    private FinancialInformationResponseViewModel financialInformation;

    @JsonProperty("otherTermsAndConditions")
    private OtherTermsAndConditionsResponseViewModel otherTermsAndConditions;

    @JsonProperty("auditDetails")
    private AuditDetailsResponseViewModel auditDetails;
}
