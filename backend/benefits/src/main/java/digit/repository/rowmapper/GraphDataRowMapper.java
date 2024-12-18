package digit.repository.rowmapper;

import digit.web.models.GraphData;
import org.springframework.jdbc.core.RowMapper;

public class GraphDataRowMapper implements RowMapper<GraphData> {
    @Override
    public GraphData mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
        return new GraphData(
                rs.getString("label"),
                rs.getInt("count")
        );
    }
}
