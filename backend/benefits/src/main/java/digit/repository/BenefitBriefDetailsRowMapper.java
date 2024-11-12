package digit.repository;

import digit.web.models.BenefitInfo;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BenefitBriefDetailsRowMapper implements RowMapper<BenefitInfo> {
    @Override
    public BenefitInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
        BenefitInfo benefitResponse = new BenefitInfo();

        benefitResponse.setId(rs.getString("id"));
        //benefitResponse.setPrice(rs.getBigDecimal("price"));


        // Convert SQL Array to List of LocalDate for extended_deadlines
        List<LocalDate> extendedDeadlines = new ArrayList<>();

        Array sqlArray = rs.getArray("extended_deadlines");
        if (sqlArray != null) {
            Object[] dates = (Object[]) sqlArray.getArray();
            for (Object dateObj : dates) {
                if (dateObj instanceof java.sql.Date) {
                    extendedDeadlines.add(((java.sql.Date) dateObj).toLocalDate());
                } else if (dateObj instanceof java.sql.Timestamp) {
                    extendedDeadlines.add(((java.sql.Timestamp) dateObj).toLocalDateTime().toLocalDate());
                } else if (dateObj instanceof java.util.Date) {
                    extendedDeadlines.add(((java.util.Date) dateObj).toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate());
                }
            }
        }
        benefitResponse.setExtendedDeadlines(extendedDeadlines);

        long applicationEndTimestamp = rs.getLong("application_end");
        LocalDate applicationEndDate = Instant.ofEpochMilli(applicationEndTimestamp)
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDate();  // Convert to LocalDate
        benefitResponse.setApplicationDeadline(applicationEndDate);
        return benefitResponse;
    }
}
