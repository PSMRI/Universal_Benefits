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
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("benefitSponsor")
    @NotNull
    private String sponsorName;

    @JsonProperty("sponsorEntity")
    @NotNull
    private SponsorEntityEnum entityType;

    @JsonProperty("sponsorShare")
    @NotNull
    @Valid
    @DecimalMin(value = "0.00", inclusive = true, message = "Share percent must be at least 0")
    @DecimalMax(value = "100.00", inclusive = true, message = "Share percent must not exceed 100")
    @Digits(integer = 3, fraction = 2, message = "Share percent must have at most two digits after the decimal point")
    private BigDecimal sharePercent;

    @JsonProperty("type")
    @NotNull
    @Valid
    private SponsorType type;

    public enum SponsorEntityEnum {
        NON_PROFIT("NON_PROFIT"),

        CORPORATE("CORPORATE"),

        GOVERNMENT("GOVERNMENT"),

        INSTITUTE("INSTITUTE"),

        FOUNDATION("FOUNDATION"),

        INDIVIDUAL("INDIVIDUAL");

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

    public enum SponsorType {
        PRIMARY("PRIMARY"),
        SECONDARY("SECONDARY");

        private final String value;

        SponsorType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static SponsorType fromValue(String value) {
            for (SponsorType type : SponsorType.values()) {
                if (type.value.equalsIgnoreCase(value)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Invalid SponsorType: " + value);
        }
    }
}
