package digit.repository.rowmapper;

import digit.web.models.Sponsor;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SponsorRowMapper implements RowMapper<Sponsor> {
    @Override
    public Sponsor mapRow(ResultSet rs, int rowNum) throws SQLException {
        Sponsor sponsor = new Sponsor();
        sponsor.setId(rs.getString("id"));
        sponsor.setSponsorName(rs.getString("sponsor_name"));
        sponsor.setEntityType(Sponsor.SponsorEntityEnum.valueOf(rs.getString("entity_type")));
        sponsor.setSharePercent(rs.getBigDecimal("share_percent"));
        sponsor.setType(Sponsor.SponsorType.valueOf(rs.getString("type")));

        return sponsor;
    }
}
