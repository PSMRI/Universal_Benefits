package digit.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VisualRepresentationResponse {
    @JsonProperty("applicantsDisbursals")
    private List<GraphData> applicantsDisbursals;
    @JsonProperty("gender")
    private List<GraphData> gender;
    @JsonProperty("caste")
    private List<GraphData> caste;
    @JsonProperty("age")
    private List<GraphData> age;
    @JsonProperty("ratio")
    private List<GraphData> ratio;
}
