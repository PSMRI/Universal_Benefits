package digit.web.models;

import java.util.Date;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Builder;

/**
 * OtherTermsAndConditions
 */
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-10-23T15:21:03.651111+05:30[Asia/Calcutta]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OtherTermsAndConditions {
    @JsonProperty("allowWithOtherBenefit")
    private Boolean allowWithOtherBenefit = null;

    @JsonProperty("allowForOneYearIfFailed")
    private Boolean allowOneYearIfFailed = null;

    @JsonProperty("applicationDeadlineDate")
    @Valid
    private Date applicationDeadlineDate = null;

    @JsonProperty("extendedDeadlineDate")
    @Valid
    private Date extendedDeadlineDate = null;

    @JsonProperty("validTillDate")
    @Valid
    private Date validTillDate = null;

    @JsonProperty("autoRenewalApplicable")
    private Boolean autoRenewalApplicable = null;


}
