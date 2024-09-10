package digit.application.web.models;

import java.util.*;

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
 * Request for Application _searcrh
 */
@Schema(description = "Request for Application _searcrh")
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-09-09T14:51:43.484446283+05:30[Asia/Calcutta]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplicationSearchCriteria {
    @JsonProperty("ids")
    private Set<String> ids = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("individualId")
    private String individualId = null;

    @JsonProperty("applicationNumbers")
    private Set<String> applicationNumbers = null;

    @JsonProperty("programCode")
    private String programCode = null;

    @JsonProperty("status")
    private Application.StatusEnum status = null;


    public ApplicationSearchCriteria addIdsItem(String idsItem) {
        if (this.ids == null) {
            this.ids = new HashSet<>();
        }
        this.ids.add(idsItem);
        return this;
    }

    public ApplicationSearchCriteria addApplicationNumbersItem(String applicationNumbersItem) {
        if (this.applicationNumbers == null) {
            this.applicationNumbers = new HashSet<>();
        }
        this.applicationNumbers.add(applicationNumbersItem);
        return this;
    }

}
