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

    @JsonProperty("caste")
    private String caste = null;

    @JsonProperty("disability")
    private Boolean disability = null;

    @JsonProperty("domicile")
    @Size(max = 128)
    private String domicile = null;

    @JsonProperty("studentType")
    private StudentTypeEnum studentType = null;

    @JsonProperty("age")
    @Size(max = 128)
    private String age = null;

    @JsonProperty("eligibleChildren")
    @Size(max = 128)
    private String eligibleChildren = null;

    public enum GenderEnum {
        MALE("M"),

        FEMALE("F"),

        BOTH("B");

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

    public enum StudentTypeEnum {
        DAYSCHOLAR("DAYSCHOLAR"),

        HOSTELER("HOSTELER"),

        BOTH("BOTH");

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

    public enum CasteEnum {
        GENERAL("GENERAL"),

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

}
