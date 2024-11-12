package digit.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BenefitCollapseResponse {
    private boolean success;

    @JsonProperty("benefit")
    private BenefitInfo benefitInfo;
}
