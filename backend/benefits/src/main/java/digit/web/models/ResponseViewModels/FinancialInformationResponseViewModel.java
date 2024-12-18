package digit.web.models.ResponseViewModels;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FinancialInformationResponseViewModel {
    @JsonProperty("parentOccupation")
    private String parentOccupation;

    @JsonProperty("amountPerBeneficiaryCategory")
    private List<AmountPerBeneficiaryCategoryResponseViewModel> amountPerBeneficiaryCategory;

    @JsonProperty("maxBeneficiaryLimit")
    private boolean maxBeneficiaryLimit;

    @JsonProperty("maxBeneficiaryAllowed")
    private int maxBeneficiaryAllowed;
}
