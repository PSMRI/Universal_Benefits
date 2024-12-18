package digit.web.models.BenefitNewResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BasicDetails {
    private String title;
    private String category;
    private String subCategory;
    private List<String> tags;
    private String applicationOpenDate;
    private String applicationCloseDate;
}
