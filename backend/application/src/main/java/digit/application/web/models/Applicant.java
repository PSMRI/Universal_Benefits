package digit.application.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@Validated
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Applicant {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("applicationId")
    private String applicationId;

    @JsonProperty("studentName")
    @NotNull
    private String studentName;

    @JsonProperty("fatherName")
    private String fatherName = null;

    @NotNull
    @JsonProperty("samagraId")
    private String samagraId;

    @JsonProperty("currentSchoolName")
    private String currentSchoolName = null;

    @JsonProperty("currentSchoolAddress")
    private String currentSchoolAddress = null;

    @JsonProperty("currentSchoolAddressDistrict")
    private String currentSchoolAddressDistrict = null;

    @JsonProperty("currentClass")
    private String currentClass = null;

    @JsonProperty("previousYearMarks")
    private String previousYearMarks = null;

    @JsonProperty("studentType")
    private String studentType = null;

    @JsonProperty("aadharLast4Digits")
    @Size(min = 4, max = 4, message = "aadharLast4Digits must be exactly 4 characters long")
    private String aadharLast4Digits = null;

    @JsonProperty("caste")
    private String caste = null;

    @JsonProperty("income")
    private String income = null;

    @JsonProperty("gender")
    private String gender = null;

    @JsonProperty("age")
    private int age;

    @JsonProperty("disability")
    private boolean disability;
    
 
}
