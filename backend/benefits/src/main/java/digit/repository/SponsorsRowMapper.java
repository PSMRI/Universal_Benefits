package digit.repository;

import digit.web.models.Sponsor;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SponsorsRowMapper implements RowMapper<Sponsor> {
    @Override
    public Sponsor mapRow(ResultSet rs, int rowNum) throws SQLException {
        Sponsor sponsor=new Sponsor();
   //     sponsor.setBenefitId(rs.getString("benefit_id"));
        sponsor.setId(rs.getString("id"));
        sponsor.setSponsorName(rs.getString("sponsor_name"));
       sponsor.setSharePercent(rs.getBigDecimal("share_percent"));

        String entityTypeString = rs.getString("entity_type");
        Sponsor.SponsorEntityEnum sponsorEntityEnum = Sponsor.SponsorEntityEnum.fromValue(entityTypeString);
        sponsor.setEntityType(sponsorEntityEnum);

        String typeString = rs.getString("type");
        Sponsor.SponsorType sponsorType = Sponsor.SponsorType.fromValue(typeString);
        sponsor.setType(sponsorType);


        return sponsor;
    }
}
