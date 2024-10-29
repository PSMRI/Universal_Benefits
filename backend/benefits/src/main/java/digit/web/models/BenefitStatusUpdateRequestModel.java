package digit.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class BenefitStatusUpdateRequestModel {
    @JsonProperty("benefitId")
    private String benefitId = null;

    @JsonProperty("status")
    private String status = null;
}
