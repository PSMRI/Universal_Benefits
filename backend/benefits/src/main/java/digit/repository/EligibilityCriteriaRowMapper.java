package digit.repository;

import digit.web.models.EligibilityCriteria;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EligibilityCriteriaRowMapper implements RowMapper<EligibilityCriteria> {
    @Override
    public EligibilityCriteria mapRow(ResultSet rs, int rowNum) throws SQLException
    {
        EligibilityCriteria eligibilityCriteria=new EligibilityCriteria();
        eligibilityCriteria.setDisability(rs.getBoolean(""));
        return eligibilityCriteria;
    }
}
