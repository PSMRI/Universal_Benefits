package digit.web.models;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import digit.web.models.EligibilityCriteria;
import digit.web.models.FinancialInformation;
import digit.web.models.OtherTermsAndConditions;
import digit.web.models.Sponsor;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;

import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Builder;

/**
 * Benefit
 */
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-10-23T15:21:03.651111+05:30[Asia/Calcutta]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Benefit {
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

    @JsonProperty("sponsors")
    @NotNull
    @Valid
    @Size(min = 1)
    private List<Sponsor> sponsors = new ArrayList<>();

    @JsonProperty("benefitDescription")
    @Size(max = 256)
    private String benefitDescription = null;

    @JsonProperty("isDraft")
    @NotNull
    private BenefitsStatusEnum status = null;

    @JsonProperty("eligibility")
    @Valid
    private EligibilityCriteria eligibility = null;

    @JsonProperty("financialInformation")
    @Valid
    private FinancialInformation financialInformation = null;

    @JsonProperty("otherTermsAndConditions")
    @Valid
    private OtherTermsAndConditions otherTermsAndConditions = null;

    public enum BenefitsStatusEnum {
        NON_PROFIT("Draft"),

        CORPORATE("Active"),

        GOVERNMENT("Closed");

        private String value;

        BenefitsStatusEnum(String value) {
            this.value = value;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static BenefitsStatusEnum fromValue(String text) {
            for (BenefitsStatusEnum b : BenefitsStatusEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }
    }


    public Benefit addSponsorsItem(Sponsor sponsorsItem) {
        this.sponsors.add(sponsorsItem);
        return this;
    }

}
