package digit.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GeneralInformation {
    @JsonProperty("benefitId")
    private String benefitId = null;

    @JsonProperty("benefitName")
    @NotNull
    @Size(max = 128)
    private String benefitName;

    @JsonProperty("benefitProvider")
    @NotNull
    @Size(max = 128)
    private String benefitProvider;

    @JsonProperty("benefitDescription")
    @Size(max = 256)
    private String benefitDescription = null;

    @JsonProperty("sponsors")
    @Valid
    @Size(min = 1)
    private List<Sponsor> sponsors;

    @JsonProperty("status")
    @NotNull
    private Benefit.BenefitsStatusEnum status;
}
