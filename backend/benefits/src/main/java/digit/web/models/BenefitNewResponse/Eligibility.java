package digit.web.models.BenefitNewResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Eligibility {
    private String type;
    private String description;
    private String evidence;
    private Criteria criteria;
    private List<String> allowedProofs;
}
