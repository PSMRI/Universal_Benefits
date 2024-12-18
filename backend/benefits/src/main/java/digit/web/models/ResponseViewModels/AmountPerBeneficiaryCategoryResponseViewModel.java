package digit.web.models.ResponseViewModels;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AmountPerBeneficiaryCategoryResponseViewModel {
    @JsonProperty("id")
    private String id;

    @JsonProperty("beneficiaryType")
    private String beneficiaryType;

    @JsonProperty("beneficiaryCaste")
    private String beneficiaryCaste;

    @JsonProperty("beneficiaryCategory")
    private String beneficiaryCategory;

    @JsonProperty("beneficiaryAmount")
    private double beneficiaryAmount;
}
