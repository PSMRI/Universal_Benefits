package digit.disbursal.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.egov.common.contract.response.ResponseInfo;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

/**
 * Response for the Application _create, _update and _search api&#x27;s
 */
@Schema(description = "Response for the Application _create, _update and _search api's")
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-09-09T14:51:43.484446283+05:30[Asia/Calcutta]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DisbursalResponse {
    @JsonProperty("ResponseInfo")
    @NotNull
    @Valid
    private ResponseInfo responseInfo = null;

    @JsonProperty("Disbursal")
    @NotNull
    @Valid
    private List<Disbursal> disbursals = new ArrayList<>();

    @JsonProperty("pagination")
    @Valid
    private Pagination pagination;


    public DisbursalResponse addDisbursalItem(Disbursal disbursal) {
        this.disbursals.add(disbursal);
        return this;
    }

}
