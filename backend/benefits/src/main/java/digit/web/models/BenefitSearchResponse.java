package digit.web.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import lombok.Builder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BenefitSearchResponse {
    @JsonProperty("benefitId")
    private String benefitId = null;

    @JsonProperty("benefitName")
    @NotNull
    @Size(max = 128)
    private String benefitName = null;

    @JsonProperty("benefitProvider")
    @NotNull
    @Size(max = 128)
    private String benefitProvider = null;

   /* @JsonProperty("sponsors")
    @NotNull
    @Valid
    @Size(min = 1)
    private List<Sponsor> sponsors = new ArrayList<>();*/

    @JsonProperty("benefitDescription")
    @Size(max = 256)
    private String benefitDescription = null;

    @JsonProperty("isDraft")
    @NotNull
    private String status = null;

    @JsonProperty("eligibility")
    @Valid
    private EligibilityCriteria eligibility = null;

    @JsonProperty("financialInformation")
    @Valid
    private FinancialInformation financialInformation = null;

    @JsonProperty("otherTermsAndConditions")
    @Valid
    private OtherTermsAndConditions otherTermsAndConditions = null;

}
