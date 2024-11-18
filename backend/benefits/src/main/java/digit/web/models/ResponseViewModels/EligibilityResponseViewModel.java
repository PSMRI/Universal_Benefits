package digit.web.models.ResponseViewModels;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EligibilityResponseViewModel {
    @JsonProperty("gender")
    private String gender;

    @JsonProperty("class")
    private String studentClass;

    @JsonProperty("marks")
    private String marks;

    @JsonProperty("minQualification")
    private String minQualification;

    @JsonProperty("fieldOfStudy")
    private String fieldOfStudy;

    @JsonProperty("attendancePercentage")
    private String attendancePercentage;

    @JsonProperty("annualIncome")
    private String annualIncome;

    @JsonProperty("caste")
    private String caste;

    @JsonProperty("disability")
    private boolean disability;

    @JsonProperty("domicile")
    private String domicile;

    @JsonProperty("studentType")
    private String studentType;

    @JsonProperty("age")
    private String age;

    @JsonProperty("eligibleChildren")
    private String eligibleChildren;
}
