package digit.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

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
    private String benefitName;

    @JsonProperty("benefitProvider")
    @NotNull
    @Size(max = 128)
    private String benefitProvider;

    @JsonProperty("benefitDescription")
    @Size(max = 256)
    private String benefitDescription = null;

    @JsonProperty("image_url")
    @Valid
    private String imageUrl = null;

    @JsonProperty("short_description")
    @Valid
    private String shortDescription = null;

    @JsonProperty("short_description_md")
    @Valid
    private String shortDescriptionMd = null;

    @JsonProperty("long_description")
    @Valid
    private String longDescription = null;

    @JsonProperty("long_description_md")
    @Valid
    private String longDescriptionMd = null;


    @JsonProperty("sponsors")
    @Valid
    @NotNull
    @Size(min = 1)
    private List<Sponsor> sponsors;

    @JsonProperty("status")
    @NotNull
    private BenefitsStatusEnum status;

    @JsonProperty("eligibility")
    @Valid
    private EligibilityCriteria eligibility = null;

    @JsonProperty("financialInformation")
    @Valid
    private FinancialInformation financialInformation = null;

    @JsonProperty("otherTermsAndConditions")
    @Valid
    private OtherTermsAndConditions otherTermsAndConditions = null;

    @JsonProperty("auditDetails")
    @Valid
    private AuditDetails auditDetails = null;

    @JsonProperty("schema")
    @Valid
    private Object schema = null;

    public enum BenefitsStatusEnum {
        DRAFT("DRAFT"),

        ACTIVE("ACTIVE"),

        CLOSED("CLOSED");

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
