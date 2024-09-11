package digit.verification.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.models.AuditDetails;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Builder;

/**
 * Application
 */
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-09-09T14:51:43.484446283+05:30[Asia/Calcutta]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Application {
    @JsonProperty("id")

    @Valid
    private String id = null;

    @JsonProperty("tenantId")
    @NotNull

    @Size(min = 2, max = 64)
    private String tenantId = null;

    @JsonProperty("applicationNumber")
    @Size(min = 4, max = 64)
    private String applicationNumber = null;

    @JsonProperty("individualId")
    @NotNull
    @Size(min = 4, max = 64)
    private String individualId = null;

    @JsonProperty("programCode")
    @NotNull
    @Size(min = 2, max = 64)
    private String programCode = null;

    /**
     * Flag to soft delete
     */
    public enum StatusEnum {
        ACTIVE("ACTIVE"),

        INACTIVE("INACTIVE"),

        ARCHIVED("ARCHIVED");

        private String value;

        StatusEnum(String value) {
            this.value = value;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static StatusEnum fromValue(String text) {
            for (StatusEnum b : StatusEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }
    }

    @JsonProperty("status")
    private StatusEnum status = null;

    @JsonProperty("wfStatus")
    @Size(min = 2, max = 64)
    private String wfStatus = null;

    @JsonProperty("documents")
    @NotNull
    @Valid
    private List<Document> documents = new ArrayList<>();

    @JsonProperty("auditDetails")

    @Valid
    private AuditDetails auditDetails = null;

    @JsonProperty("additionalDetails")

    private Object additionalDetails = null;


    public Application addDocumentsItem(Document documentsItem) {
        this.documents.add(documentsItem);
        return this;
    }

}
