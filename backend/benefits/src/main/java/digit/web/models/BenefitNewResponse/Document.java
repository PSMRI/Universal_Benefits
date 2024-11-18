package digit.web.models.BenefitNewResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Document {
    private String documentType;
    private boolean isRequired;
    private List<String> allowedProofs;
}
