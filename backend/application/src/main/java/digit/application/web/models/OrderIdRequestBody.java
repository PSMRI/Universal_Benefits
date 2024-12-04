package digit.application.web.models;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;
 
@Validated
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderIdRequestBody {
	@NotNull
    @NotBlank
    @JsonProperty("orderId")
    private String orderId;
}