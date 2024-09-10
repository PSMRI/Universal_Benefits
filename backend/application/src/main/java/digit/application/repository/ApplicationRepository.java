package digit.application.repository;

import digit.application.repository.querybuilder.ApplicationQueryBuilder;
import digit.application.repository.rowmapper.SearchApplicationRowMapper;
import digit.application.web.models.Application;
import digit.application.web.models.ApplicationSearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ApplicationRepository {

    private final JdbcTemplate jdbcTemplate;
    private final ApplicationQueryBuilder applicationQueryBuilder;
    private final SearchApplicationRowMapper searchApplicationRowMapper;

    @Autowired
    public ApplicationRepository(JdbcTemplate jdbcTemplate, ApplicationQueryBuilder applicationQueryBuilder, SearchApplicationRowMapper searchApplicationRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.applicationQueryBuilder = applicationQueryBuilder;
        this.searchApplicationRowMapper = searchApplicationRowMapper;
    }

    public List<Application> search(ApplicationSearchRequest applicationSearchRequest){

        List<Object> preparedStatementValues = new ArrayList<>();
        String queryStr = applicationQueryBuilder.getApplicationQuery(applicationSearchRequest, preparedStatementValues);
        return jdbcTemplate.query(queryStr, preparedStatementValues.toArray(), searchApplicationRowMapper);
    }

}
