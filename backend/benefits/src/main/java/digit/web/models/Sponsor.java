package digit.web.models;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Builder;

/**
 * Additional sponsor details.
 */
@Schema(description = "Additional sponsor details.")
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-10-23T15:21:03.651111+05:30[Asia/Calcutta]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Sponsor {
    @JsonProperty("benefitId")
    private String benefitId = null;

    @JsonProperty("benefitSponsor")
    @NotNull
    private String sponsorName = null;

    public enum SponsorEntityEnum {
        NON_PROFIT("Non-Profit"),

        CORPORATE("Corporate"),

        GOVERNMENT("Government"),

        INSTITUTE("Institute"),

        FOUNDATION("Foundation"),

        INDIVIDUAL("Individual");

        private String value;

        SponsorEntityEnum(String value) {
            this.value = value;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static SponsorEntityEnum fromValue(String text) {
            for (SponsorEntityEnum b : SponsorEntityEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }
    }

    @JsonProperty("sponsorEntity")
    @NotNull
    private SponsorEntityEnum entityType = null;

    @JsonProperty("sponsorShare")
    @NotNull
    @Valid
    private Float sharePercent = null;

    @JsonProperty("type")
    @NotNull
    @Valid
    private String type;

}
