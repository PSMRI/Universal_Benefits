package digit.repository;

import digit.repository.Utility.DateUtils;
import digit.web.models.BenefitStatusCount;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;

public class BenefitStatusCountRowMapper implements RowMapper<BenefitStatusCount> {
    @Override
    public BenefitStatusCount mapRow(ResultSet rs, int rowNum) throws SQLException {
        BenefitStatusCount benefitStatusCount = new BenefitStatusCount();
        benefitStatusCount.setId(rs.getString("benefit_id"));
        benefitStatusCount.setName(rs.getString("benefit_name"));
        /*long applicatioEndTimeStamp = rs.getLong("application_end");
        benefitStatusCount.setApplicationEnd(DateUtils.convertBigintToDate(applicatioEndTimeStamp));*/
        benefitStatusCount.setApplicants((rs.getLong("total_applications")));

        benefitStatusCount.setApproved(rs.getLong("approved_count"));
        benefitStatusCount.setRejected(rs.getLong("rejected_count"));


        // Convert application_end (bigint) to LocalDate
        long applicationEndTimestamp = rs.getLong("application_end");
        LocalDate applicationEndDate = Instant.ofEpochMilli(applicationEndTimestamp)
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDate();  // Convert to LocalDate
        benefitStatusCount.setDeadline(applicationEndDate);
        return benefitStatusCount;
    }

}
