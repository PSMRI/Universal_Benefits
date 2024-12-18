package digit.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Validated
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AmountPerBeneficiaryCategory {

    @JsonProperty("id")
    private String id;

    @JsonProperty("beneficiaryType")
    @NotNull
    private EligibilityCriteria.StudentTypeEnum beneficiaryType;

    @JsonProperty("beneficiaryCaste")
    @NotNull
    private EligibilityCriteria.CasteEnum beneficiaryCaste;

    @JsonProperty("beneficiaryCategory")
    @NotNull
    private String beneficiaryCategory;

    @JsonProperty("beneficiaryAmount")
    @NotNull
    private double beneficiaryAmount;
}

