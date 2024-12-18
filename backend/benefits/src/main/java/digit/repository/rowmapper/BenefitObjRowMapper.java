package digit.repository.rowmapper;

import digit.web.models.*;

import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BenefitObjRowMapper implements RowMapper<Benefit> {

    @Override
    public Benefit mapRow(ResultSet rs, int rowNum) throws SQLException {
        Benefit benefit = new Benefit();

        benefit.setBenefitId(rs.getString("benefit_id"));
        benefit.setBenefitName(rs.getString("benefit_name"));
        benefit.setBenefitDescription(rs.getString("benefit_description"));
        benefit.setBenefitProvider(rs.getString("benefit_provider"));
        benefit.setStatus(
                Benefit.BenefitsStatusEnum.valueOf(rs.getString("status"))
        );

        benefit.setAuditDetails(new AuditDetails());

        benefit.getAuditDetails().setCreatedBy(rs.getString("created_by"));
        benefit.getAuditDetails().setLastModifiedBy(rs.getString("last_modified_by"));
        benefit.getAuditDetails().setCreatedTime(rs.getLong("created_time"));
        benefit.getAuditDetails().setLastModifiedTime(rs.getLong("last_modified_time"));

        return benefit;
    }
}

