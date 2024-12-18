package digit.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import digit.repository.rowmapper.BenefitObjRowMapper;
import digit.repository.rowmapper.SponsorRowMapper;
import digit.web.models.Benefit;
import digit.web.models.Sponsor;
import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.model.ServiceCallException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

import static digit.config.ServiceConstants.*;

@Repository
@Slf4j
public class ServiceRequestRepository {

    private ObjectMapper mapper;

    private RestTemplate restTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String CHECK_BENEFIT_NAME_EXISTS_SQL =
            "SELECT EXISTS (SELECT 1 FROM benefits WHERE LOWER(benefit_name) = LOWER(?) AND LOWER(benefit_provider) = LOWER(?))";

    private static final String GET_BENEFIT_DATA_USING_ID =
            "SELECT * FROM benefits WHERE benefit_id = ?";

    private static final String GET_SPONSORS_BY_BENEFIT_ID = "SELECT * FROM benefit_sponsor WHERE benefit_id = ?";

    @Autowired
    public ServiceRequestRepository(ObjectMapper mapper, RestTemplate restTemplate) {
        this.mapper = mapper;
        this.restTemplate = restTemplate;
    }


    public Object fetchResult(StringBuilder uri, Object request) {
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        Object response = null;
        try {
            response = restTemplate.postForObject(uri.toString(), request, Map.class);
        }catch(HttpClientErrorException e) {
            log.error(EXTERNAL_SERVICE_EXCEPTION,e);
            throw new ServiceCallException(e.getResponseBodyAsString());
        }catch(Exception e) {
            log.error(SEARCHER_SERVICE_EXCEPTION,e);
        }

        return response;
    }

    public boolean checkBenefitNameExists(String benefitName, String benefitProvider) {
        try {
            Boolean exists = jdbcTemplate.queryForObject(CHECK_BENEFIT_NAME_EXISTS_SQL, Boolean.class, benefitName,benefitProvider);
            return exists != null && exists;
        } catch (DataAccessException e) {
            log.error("Error checking if benefit name exists", e);
            throw e;
        }
    }

    public Benefit getBenefitData(String benefitId) {
        try {
            List<Benefit> benefits = jdbcTemplate.query(GET_BENEFIT_DATA_USING_ID, new BenefitObjRowMapper() , benefitId);

            if (benefits.isEmpty()) {
                return null;
            } else {
                return benefits.get(0);
            }
        } catch (DataAccessException e) {
            log.error("Error checking if benefit name exists", e);
            throw e;
        }
    }

//    public List<Sponsor> getAllSponsors(String benefitId){
//        return  jdbcTemplate.query(GET_SPONSORS_BY_BENEFIT_ID, new SponsorRowMapper(), benefitId);
//    }

    public boolean isProviderValid(String providerName){
        return true;
    }

}