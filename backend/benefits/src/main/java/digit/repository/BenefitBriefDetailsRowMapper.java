package digit.repository;

import digit.web.models.BenefitInfo;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BenefitBriefDetailsRowMapper implements RowMapper<BenefitInfo> {
    @Override
    public BenefitInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
        BenefitInfo benefitResponse = new BenefitInfo();

        benefitResponse.setId(rs.getString("benefit_id"));
        benefitResponse.setPrice(rs.getBigDecimal("price"));

        long applicationEndTimestamp = rs.getLong("application_end");
        LocalDate applicationEndDate = Instant.ofEpochMilli(applicationEndTimestamp)
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDate();  // Convert to LocalDate
        benefitResponse.setApplicationDeadline(applicationEndDate);

        // Convert SQL Array to List of LocalDate for extended_deadlines
        List<LocalDate> extendedDeadlines = new ArrayList<>();
        Array sqlArray = rs.getArray("extended_deadlines");
        if (sqlArray != null) {
            LocalDate[] dates = (LocalDate[]) sqlArray.getArray();
            for (LocalDate date : dates) {
                extendedDeadlines.add(date);
            }
        }
        benefitResponse.setExtendedDeadlines(extendedDeadlines);

        return benefitResponse;
    }
}
