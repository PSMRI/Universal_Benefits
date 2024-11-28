package digit.application.web.models;
 
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.egov.common.contract.response.ResponseInfo;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Builder;
 
/**
* Response for the Application _create, _update and _search api's
*/
@Schema(description = "Response for the Application _create, _update and _search api's")
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-09-09T14:51:43.484446283+05:30[Asia/Calcutta]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
 
public class ApplicationUpdateBatchIDResponse {

	 private Integer batchId;
	    private Boolean success;
	    private String message;
	//private String responseMsg = "Batch ID added successfully";  
 
    // Custom setter for responseMsg if needed for chaining
   // public ApplicationUpdateBatchIDResponse updateResponseMsg(String responseMsg) {
    //    this.responseMsg = responseMsg; 
      //  return this;  // Return this for method chaining
   // }
}