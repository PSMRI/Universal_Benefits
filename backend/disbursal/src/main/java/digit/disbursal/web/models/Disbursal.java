package digit.disbursal.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import digit.disbursal.web.models.enums.PaymentStatus;
import digit.disbursal.web.models.enums.Status;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.egov.common.contract.models.AuditDetails;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;

/**
 * Application
 */
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-09-10T14:51:43.484446283+05:30[Asia/Calcutta]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Disbursal {
    @JsonProperty("id")
    @Valid
    private String id = null;

    @JsonProperty("tenantId")
    @NotNull
    @Size(min = 2, max = 64)
    private String tenantId = null;

    @JsonProperty("referenceId")
    @NotNull
    @Size(min = 4, max = 64)
    private String referenceId = null;

    @JsonProperty("disbursalNumber")
    @Size(min = 4, max = 64)
    private String disbursalNumber = null;

    @JsonProperty("totalAmount")
    @Valid
    @Builder.Default
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @JsonProperty("totalPaidAmount")
    @Valid
    @Builder.Default
    private BigDecimal totalPaidAmount = BigDecimal.ZERO;

    @JsonProperty("paymentStatus")
    private PaymentStatus paymentStatus;

    @JsonProperty("status")
    private Status status = null;

    @JsonProperty("wfStatus")
    @Size(min = 2, max = 64)
    private String wfStatus = null;

    @JsonProperty("auditDetails")
    @Valid
    private AuditDetails auditDetails = null;

    @JsonProperty("additionalDetails")
    private Object additionalDetails = null;

}
