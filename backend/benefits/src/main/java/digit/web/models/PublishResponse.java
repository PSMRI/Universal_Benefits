package digit.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@Validated
//@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-10-23T15:21:03.651111+05:30[Asia/Calcutta]")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PublishResponse {
    @JsonProperty("isSuccess")
    private Boolean isSuccess = null;
    @JsonProperty("message")
    private String message = null;
}
