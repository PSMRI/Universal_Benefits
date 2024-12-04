package digit.web.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.egov.common.contract.models.AuditDetails;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

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
    private String id = null;

    @JsonProperty("tenantId")
    @NotNull
    @NotBlank
    @Size(min = 2, max = 64)
    private String tenantId = null;

    @JsonProperty("order_id")
	private String order_id = null; 
    
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

		ARCHIVED("ARCHIVED"),

		APPROVED("APPROVED"),

		PENDING_FOR_REVIEW("PENDING FOR REVIEW"),

		AMOUNT_TRANSFER_IN_PROGRESS("AMOUNT TRANSFER IN PROGRESS"),

		SUBMITTED_FOR_DISBURSAL("SUBMITTED FOR DISBURSAL"),
    	
    	AMOUNT_RECEIVED("AMOUNT RECEIVED");

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
    private WFStatusEnum wfStatus = null;

    @JsonProperty("documents")
    @NotNull
    @Valid
    private List<Document> documents = new ArrayList<>();

    @JsonProperty("auditDetails")
    @Valid
    private AuditDetails auditDetails = null;

    @JsonProperty("additionalDetails")
    private Object additionalDetails = null;

    @JsonProperty("applicant")
    @NotNull
    @Valid
    private Applicant applicant;

    @JsonProperty("schema")
    public String schema;

    public Application addDocumentsItem(Document documentsItem) {
        this.documents = new ArrayList<>(this.documents);
        this.documents.add(documentsItem);
        return this;
    }

    public enum WFStatusEnum {
    	ACTIVE("ACTIVE"),

		INACTIVE("INACTIVE"),

		ARCHIVED("ARCHIVED"),

		APPROVED("APPROVED"),

		PENDING_FOR_REVIEW("PENDING FOR REVIEW"),

		AMOUNT_TRANSFER_IN_PROGRESS("AMOUNT TRANSFER IN PROGRESS"),

		SUBMITTED_FOR_DISBURSAL("SUBMITTED FOR DISBURSAL"),
    	
    	AMOUNT_RECEIVED("AMOUNT RECEIVED");

        private String value;

        WFStatusEnum(String value) {
            this.value = value;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static WFStatusEnum fromValue(String text) {
            for (WFStatusEnum b : WFStatusEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }
    }

}
