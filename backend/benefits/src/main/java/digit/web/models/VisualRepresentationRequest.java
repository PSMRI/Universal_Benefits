package digit.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VisualRepresentationRequest {

    @JsonProperty("benefitId")
    private String benefitId;

    @JsonProperty("monthYear")
    private String monthYear;
}
