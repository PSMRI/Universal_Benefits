package digit.web.models;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import digit.web.models.Benefit;
import digit.web.models.ResponseInfo;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;

import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Builder;

/**
 * Contract class to send response.
 */
@Schema(description = "Contract class to send response.")
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-10-23T15:21:03.651111+05:30[Asia/Calcutta]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BenifitDateExtendResponse<T> {
	@JsonProperty("ResponseInfo")
    @NotNull
    @Valid
    private ResponseInfo responseInfo = null;

    private String responseMsg = "Status updated successfully";  

    // Custom setter for responseMsg if needed for chaining
    public BenifitDateExtendResponse BenifitDateExtendResponseResponseMsg(String responseMsg) {
        this.responseMsg = responseMsg; 
        return this;  // Return this for method chaining
    }
}
