package digit.web.models;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Builder;

/**
 * FinancialInformation
 */
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-10-23T15:21:03.651111+05:30[Asia/Calcutta]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FinancialInformation {

    @JsonProperty("parentOccupation")
    @Size(max = 128)
    private String parentOccupation = null;

    @JsonProperty("amountPerBeneficiaryCategory")
    @Valid
    private List<AmountPerBeneficiaryCategory> amountPerBeneficiaryCategory = null;

    @JsonProperty("maxBeneficiaryLimit")
    @NotNull
    private Boolean maxBeneficiaryLimit = null;

    @JsonProperty("maxBeneficiaryAllowed")
    private Integer maxBeneficiaryAllowed = null;


}
