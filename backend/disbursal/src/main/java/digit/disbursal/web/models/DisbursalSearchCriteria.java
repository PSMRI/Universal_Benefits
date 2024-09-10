package digit.disbursal.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import digit.disbursal.web.models.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.util.HashSet;
import java.util.Set;

/**
 * Request for Application _searcrh
 */
@Schema(description = "Request for Application _searcrh")
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-09-09T14:51:43.484446283+05:30[Asia/Calcutta]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DisbursalSearchCriteria {
    @JsonProperty("ids")
    private Set<String> ids = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("referenceId")
    private String referenceId = null;

    @JsonProperty("disbursalNumbers")
    private Set<String> disbursalNumbers = null;

    @JsonProperty("status")
    private Status status = null;

    @JsonProperty("isPaymentStatusNull")
    private Boolean isPaymentStatusNull;

    public DisbursalSearchCriteria addIdsItem(String idsItem) {
        if (this.ids == null) {
            this.ids = new HashSet<>();
        }
        this.ids.add(idsItem);
        return this;
    }

    public DisbursalSearchCriteria addApplicationNumbersItem(String disbursalNumberItem) {
        if (this.disbursalNumbers == null) {
            this.disbursalNumbers = new HashSet<>();
        }
        this.disbursalNumbers.add(disbursalNumberItem);
        return this;
    }

}
