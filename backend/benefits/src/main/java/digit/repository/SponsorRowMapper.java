package digit.repository;

import digit.web.models.Benefit;
import digit.web.models.Sponsor;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class SponsorRowMapper implements  RowMapper<Map<String, Benefit>> {
    private final Map<String, Benefit> benefitMap = new HashMap<>();

    @Override
    public Map<String, Benefit> mapRow(ResultSet rs, int rowNum) throws SQLException {
        String benefitId = rs.getString("benefit_id");

        Benefit benefit = benefitMap.get(benefitId);
        if (benefit == null) {
            // Create a new Benefit if it's the first time we've encountered this benefitId
            benefit = new Benefit();
            benefit.setBenefitId(benefitId);
            benefit.setBenefitName(rs.getString("benefit_name"));
            benefit.setBenefitProvider(rs.getString("benefit_provider"));
            benefit.setBenefitDescription(rs.getString("benefit_description"));

            // Add financial information
            /*benefit.setFinancialInformation(new Benefit.(
                    rs.getString("finance_parent_occupation"),
                    rs.getBoolean("max_beneficiary_limit_allowed"),
                    rs.getInt("max_beneficiary_allowed")
            ));

            // Add other terms and conditions
            benefit.setOtherTermsAndConditions(new Benefit.OtherTermsAndConditions(
                    rs.getBoolean("allow_with_other_benefit"),
                    rs.getBoolean("allow_one_year_if_failed"),
                    rs.getDate("application_end").toLocalDate(),
                    rs.getDate("valid_till_date") != null ? rs.getDate("valid_till_date").toLocalDate() : null,
                    rs.getBoolean("auto_renew_applicable")
            ));*/

            benefitMap.put(benefitId, benefit);
        }

        // Add sponsor information to the benefit
        if (rs.getString("benefit_sponsor") != null) {
            Sponsor sponsor = new Sponsor();
            sponsor.setSponsorName(rs.getString("benefit_sponsor"));
            //sponsor.setSponsorEntity(rs.getString("sponsor_entity"));
            sponsor.setSharePercent(rs.getFloat("sponsor_share"));
            sponsor.setType(rs.getString("sponsor_type"));

            benefit.getSponsors().add(sponsor);
        }

        return benefitMap;
    }
    public Map<String, Benefit> getBenefitMap() {
        return benefitMap;
    }
}
