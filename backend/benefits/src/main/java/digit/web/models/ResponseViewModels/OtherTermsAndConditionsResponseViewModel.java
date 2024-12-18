package digit.web.models.ResponseViewModels;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OtherTermsAndConditionsResponseViewModel {
    @JsonProperty("allowWithOtherBenefit")
    private boolean allowWithOtherBenefit;

    @JsonProperty("allowForOneYearIfFailed")
    private boolean allowForOneYearIfFailed;

    @JsonProperty("applicationDeadlineDate")
    private String applicationDeadlineDate;

    @JsonProperty("extendedDeadlineDate")
    private String extendedDeadlineDate;

    @JsonProperty("validTillDate")
    private String validTillDate;

    @JsonProperty("autoRenewalApplicable")
    private boolean autoRenewalApplicable;
}
