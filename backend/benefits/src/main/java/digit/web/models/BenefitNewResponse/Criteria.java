package digit.web.models.BenefitNewResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Criteria {
    private String name;
    private String condition;
    private Object conditionValues;
}
