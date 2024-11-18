package digit.web.models.ResponseViewModels;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SponsorResponseViewModel {
    @JsonProperty("id")
    private String id;

    @JsonProperty("sponsorName")
    private String sponsorName;

    @JsonProperty("entityType")
    private String entityType;

    @JsonProperty("sharePercent")
    private double sharePercent;

    @JsonProperty("type")
    private String type;
}
