package digit.repository;
import digit.repository.Utility.DateUtils;
import digit.web.models.*;
import digit.web.models.Benefit.BenefitsStatusEnum;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import digit.web.models.Benefit;
import org.springframework.jdbc.core.RowMapper;

public class BenefitRowMapper implements RowMapper<Benefit> {
    @Override
    public Benefit mapRow(ResultSet rs, int rowNum) throws SQLException {
        Benefit benefit = new Benefit();
        EligibilityCriteria eligibilityCriteria=new EligibilityCriteria();
        FinancialInformation financialInformation=new FinancialInformation();
        OtherTermsAndConditions otherTermsAndConditions=new OtherTermsAndConditions();
        AuditDetails auditDetails=new AuditDetails();
        List<Sponsor> lstSponsors=new ArrayList<>();

        benefit.setBenefitId(rs.getString("benefit_id"));
        benefit.setBenefitName(rs.getString("benefit_name"));
        benefit.setBenefitProvider(rs.getString("benefit_provider"));
        benefit.setBenefitDescription(rs.getString("benefit_description"));

        // Convert status (String) to BenefitsStatusEnum
        String statusString = rs.getString("status");
        BenefitsStatusEnum statusEnum = BenefitsStatusEnum.fromValue(statusString);
        benefit.setStatus(statusEnum);

        // Map other fields as needed...
        //Set Eligibility criteria
        eligibilityCriteria.setAcademicClass(rs.getString("eligibility_academic_class"));
        eligibilityCriteria.setMarks(rs.getString("eligibility_marks"));
        eligibilityCriteria.setMinQualification(rs.getString("eligibility_min_qualification"));
        eligibilityCriteria.setFieldOfStudy(rs.getString("eligibility_field_of_study"));
        eligibilityCriteria.setAttendancePercentage(rs.getString("eligibility_attendance_percentage"));
        eligibilityCriteria.setAnnualIncome(rs.getString("eligibility_annual_income"));
        eligibilityCriteria.setDisability(rs.getBoolean("eligibility_disability"));
        eligibilityCriteria.setDomicile(rs.getString("eligibility_domicile"));
        eligibilityCriteria.setAge(rs.getString("eligibility_age"));
        eligibilityCriteria.setEligibleChildren(rs.getString("eligibility_eligible_children"));


        String genderString=rs.getString("eligibility_gender");
        EligibilityCriteria.GenderEnum genderEnum= EligibilityCriteria.GenderEnum.fromValue(genderString);
        eligibilityCriteria.setGender(genderEnum);

       /* String casteString=rs.getString("eligibility_caste");
        EligibilityCriteria.CasteEnum casteEnum= EligibilityCriteria.CasteEnum.fromValue(casteString);*/
        eligibilityCriteria.setCaste(rs.getString("eligibility_caste"));

        String studenTypeString=rs.getString("eligibility_student_type");
        EligibilityCriteria.StudentTypeEnum studentTypeEnum= EligibilityCriteria.StudentTypeEnum.fromValue(studenTypeString);
        eligibilityCriteria.setStudentType(studentTypeEnum);
        benefit.setEligibility(eligibilityCriteria);
        //Elgibility criteria ends

        //Finacial Information starts
        financialInformation.setParentOccupation(rs.getString("finance_parent_occupation"));
        financialInformation.setMaxBeneficiaryLimit(rs.getBoolean("finance_max_beneficiary_limit_allowed"));
        financialInformation.setMaxBeneficiaryAllowed(rs.getInt("finance_max_beneficiary_allowed"));
        //financialInformation.setAmountPerBeneficiaryCategory();
        benefit.setFinancialInformation(financialInformation);
        //......................Finacial Information Ends

        //OtherTermsAndConditions starts
        otherTermsAndConditions.setAllowWithOtherBenefit(rs.getBoolean("allow_with_other_benefit"));
        otherTermsAndConditions.setAllowOneYearIfFailed(rs.getBoolean("allow_one_year_if_failed"));
        otherTermsAndConditions.setExtendedDeadlineDate(rs.getDate("new_deadline"));
        otherTermsAndConditions.setAutoRenewalApplicable(rs.getBoolean("auto_renew_applicable"));

        long validTillDateTimestamp = rs.getLong("valid_till_date");
        otherTermsAndConditions.setValidTillDate(DateUtils.convertBigintToDate(validTillDateTimestamp));

        long applicationEndTimestamp = rs.getLong("application_end");
        otherTermsAndConditions.setApplicationDeadlineDate(DateUtils.convertBigintToDate(applicationEndTimestamp));

        long applicationEndExtendedTimestamp = rs.getLong("application_end_extended");
        otherTermsAndConditions.setExtendedDeadlineDate(DateUtils.convertBigintToDate(applicationEndExtendedTimestamp));

        benefit.setOtherTermsAndConditions(otherTermsAndConditions);
        //...................OtherTermsAndConditions ends


        //Audit Details
        auditDetails.setCreatedBy(rs.getString("created_by"));
        auditDetails.setCreatedTime(rs.getLong("created_time"));
        auditDetails.setLastModifiedBy(rs.getString("last_modified_by"));
        auditDetails.setLastModifiedTime(rs.getLong("last_modified_time"));
        benefit.setAuditDetails(auditDetails);
        //ends
        benefit.setSponsors(lstSponsors);

        /*benefit.setEligibilityGender(rs.getString("eligibility_gender"));
        benefit.setEligibilityMinQualification(rs.getString("eligibility_min_qualification"));
        benefit.setEligibilityAttendancePercentage(rs.getString("eligibility_attendance_percentage"));
        benefit.setEligibilityAcademicClass(rs.getString("eligibility_academic_class"));
        benefit.setEligibilityMarks(rs.getString("eligibility_marks"));
        benefit.setEligibilityFieldOfStudy(rs.getString("eligibility_field_of_study"));
        benefit.setEligibilityAnnualIncome(rs.getString("eligibility_annual_income"));
        benefit.setEligibilityCaste(rs.getString("eligibility_caste"));
        benefit.setEligibilityStudentType(rs.getString("eligibility_student_type"));
        benefit.setEligibilityDisability(rs.getBoolean("eligibility_disability"));
        benefit.setEligibilityDomicile(rs.getString("eligibility_domicile"));
        benefit.setEligibilityAge(rs.getInt("eligibility_age"));
        benefit.setEligibilityEligibleChildren(rs.getInt("eligibility_eligible_children"));
        benefit.setFinanceParentOccupation(rs.getString("finance_parent_occupation"));
        benefit.setBeneficiaryCountMax(rs.getInt("beneficiary_count_max"));
        benefit.setAllowWithOtherBenefit(rs.getBoolean("allow_with_other_benefit"));
        benefit.setAllowOneYearIfFailed(rs.getBoolean("allow_one_year_if_failed"));
        benefit.setApplicationStart(rs.getDate("application_start"));
        benefit.setApplicationEnd(rs.getDate("application_end"));
        benefit.setAutoRenewApplicable(rs.getBoolean("auto_renew_applicable"));
        benefit.setMaxBeneficiaryLimitAllowed(rs.getBoolean("max_beneficiary_limit_allowed"));
        benefit.setMaxBeneficiaryAllowed(rs.getInt("max_beneficiary_allowed"));
        benefit.setValidTillDate(rs.getDate("valid_till_date"));*/

        return benefit;
    }
}
