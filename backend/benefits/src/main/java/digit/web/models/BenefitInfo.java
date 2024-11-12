package digit.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BenefitInfo {
    private String id;
    private String documentId;
    private BigDecimal price;

    @JsonProperty("application_deadline")
    private LocalDate applicationDeadline;

    @JsonProperty("provider")
    private ProviderInfo provider;
    private List<Sponsor> sponsors;
    private List<LocalDate> extendedDeadlines;
}
