package digit.application.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.response.ResponseInfo;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Builder;

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
public class ApplicationResponse {
    @JsonProperty("ResponseInfo")
    @NotNull
    @Valid
    private ResponseInfo responseInfo = null;

    @JsonProperty("Applications")
    @NotNull
    @Valid
    private List<Application> applications = new ArrayList<>();

    @JsonProperty("pagination")
    @Valid
    private Pagination pagination;


    public ApplicationResponse addApplicationsItem(Application applicationsItem) {
        this.applications.add(applicationsItem);
        return this;
    }

}
