package digit.application.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-09-09T14:51:43.484446283+05:30[Asia/Calcutta]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Benefit {
    @JsonProperty("BenefitID")
    private String benefitID;

    @JsonProperty("auto_renew")
    private boolean autoRenew;

    @JsonProperty("benefit_name")
    private String benefitName;

    @JsonProperty("failed_student")
    private boolean failedStudent;

    @JsonProperty("Application_end")
    private String applicationEnd;

    @JsonProperty("eligibility_age")
    private int eligibilityAge;

    @JsonProperty("benefit_provider")
    private String benefitProvider;

    @JsonProperty("Application_start")
    private String applicationStart;

    @JsonProperty("eligibility_caste")
    private String eligibilityCaste;

    @JsonProperty("eligibility_class")
    private String eligibilityClass;

    @JsonProperty("eligibility_marks")
    private String eligibilityMarks;

    @JsonProperty("eligibility_gender")
    private String eligibilityGender;

    @JsonProperty("eligibility_income")
    private String eligibilityIncome;

    @JsonProperty("benefit_description")
    private String benefitDescription;

    @JsonProperty("eligibility_subject")
    private String eligibilitySubject;

    @JsonProperty("beneficiary_count_max")
    private int beneficiaryCountMax;

    @JsonProperty("eligibility_attendance")
    private String eligibilityAttendance;

    @JsonProperty("eligibility_child_count")
    private int eligibilityChildCount;

    @JsonProperty("allow_with_other_benefit")
    private boolean allowWithOtherBenefit;

    @JsonProperty("eligibility_student_type")
    private String eligibilityStudentType;

    @JsonProperty("eligibility_qualification")
    private String eligibilityQualification;

    @JsonProperty("finance_parent_occupation")
    private String financeParentOccupation;

    @JsonProperty("remaining_amount")
    private long remainingAmount;

    @JsonProperty("amount_utilized")
    private long amountUtilized;
}
