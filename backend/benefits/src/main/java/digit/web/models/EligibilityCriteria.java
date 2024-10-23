package digit.web.models;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;

import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Builder;

/**
 * EligibilityCriteria
 */
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-10-23T15:21:03.651111+05:30[Asia/Calcutta]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EligibilityCriteria {

    public enum GenderEnum {
        MALE("Male"),

        FEMALE("Female"),

        BOTH("Both");

        private String value;

        GenderEnum(String value) {
            this.value = value;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static GenderEnum fromValue(String text) {
            for (GenderEnum b : GenderEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }
    }

    @JsonProperty("gender")
    private GenderEnum gender = null;

    @JsonProperty("class")
    @Size(max = 128)
    private String academicClass = null;

    @JsonProperty("marks")
    @Size(max = 128)
    private String marks = null;

    @JsonProperty("minQualification")
    @Size(max = 128)
    private String minQualification = null;

    @JsonProperty("fieldOfStudy")
    @Size(max = 128)
    private String fieldOfStudy = null;

    @JsonProperty("attendancePercentage")
    @Size(max = 128)
    private String attendancePercentage = null;

    @JsonProperty("annualIncome")
    @Size(max = 128)
    private String annualIncome = null;


    public enum CasteEnum {
        GENERAL("General"),

        SC("SC"),

        ST("ST"),

        OBC("OBC");

        private String value;

        CasteEnum(String value) {
            this.value = value;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static CasteEnum fromValue(String text) {
            for (CasteEnum b : CasteEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }
    }

    @JsonProperty("caste")
    private List<CasteEnum> caste = null;

    @JsonProperty("disability")
    private Boolean disability = null;

    @JsonProperty("domicile")
    @Size(max = 128)
    private String domicile = null;

    public enum StudentTypeEnum {
        DAYSCHOLAR("dayscholar"),

        HOSTELER("hosteler"),

        BOTH("both");

        private String value;

        StudentTypeEnum(String value) {
            this.value = value;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static StudentTypeEnum fromValue(String text) {
            for (StudentTypeEnum b : StudentTypeEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }
    }

    @JsonProperty("studentType")
    private StudentTypeEnum studentType = null;

    @JsonProperty("age")
    private Integer age = null;

    @JsonProperty("eligibleChildren")
    private Integer eligibleChildren = null;


    public EligibilityCriteria addCasteItem(CasteEnum casteItem) {
        if (this.caste == null) {
            this.caste = new ArrayList<>();
        }
        this.caste.add(casteItem);
        return this;
    }

}
