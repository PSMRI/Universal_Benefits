package digit.disbursal.repository;

import digit.disbursal.repository.querybuilder.DisburseQueryBuilder;
import digit.disbursal.repository.rowmapper.SearchDisburseRowMapper;
import digit.disbursal.web.models.Disbursal;
import digit.disbursal.web.models.DisbursalSearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class DisbursalRepository {

    private final JdbcTemplate jdbcTemplate;
    private final DisburseQueryBuilder disburseQueryBuilder;
    private final SearchDisburseRowMapper searchDisburseRowMapper;

    @Autowired
    public DisbursalRepository(JdbcTemplate jdbcTemplate, DisburseQueryBuilder disburseQueryBuilder, SearchDisburseRowMapper searchDisburseRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.disburseQueryBuilder = disburseQueryBuilder;
        this.searchDisburseRowMapper = searchDisburseRowMapper;
    }

    public List<Disbursal> search(DisbursalSearchRequest disbursalSearchRequest){

        List<Object> preparedStatementValues = new ArrayList<>();
        String queryStr = disburseQueryBuilder.getDisburseQuery(disbursalSearchRequest, preparedStatementValues);
        return jdbcTemplate.query(queryStr, preparedStatementValues.toArray(), searchDisburseRowMapper);
    }

}
