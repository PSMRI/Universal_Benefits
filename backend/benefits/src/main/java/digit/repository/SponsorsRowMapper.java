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
        sponsor.setSponsorName(rs.getString("sponsor_name"));
//        sponsor.setSharePercent(rs.getFloat("share_percent"));
//        sponsor.setType(rs.getString("type"));
//
//        String statusString = rs.getString("entity_type");
//        Sponsor.SponsorEntityEnum sponsorEntityEnum= Sponsor.SponsorEntityEnum.fromValue(statusString);
    //    sponsor.setEntityType(sponsorEntityEnum);
        return sponsor;
    }
}
