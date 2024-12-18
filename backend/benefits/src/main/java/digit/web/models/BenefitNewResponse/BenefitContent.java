package digit.web.models.BenefitNewResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BenefitContent {
    private String imageUrl;
    private String shortDescription;
    private String shortDescription_md;
    private String longDescription;
    private String longDescription_md;
    private List<Benefit> benefits;
    private List<Exclusion> exclusions;
    private List<Reference> references;
}
