package digit.web.models.BenefitNewResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationForm {
    private String type;
    private String name;
    private String label;
    private boolean required;
    private boolean multiple;
    private List<Option> options;
}
