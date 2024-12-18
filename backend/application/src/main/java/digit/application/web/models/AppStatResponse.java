package digit.application.web.models;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppStatResponse {
    private int id;
    private String label;
    private int number;
}
