package digit.repository;

import digit.web.models.Benefit;
import digit.web.models.BenefitSearchResponse;
import digit.web.models.BenefitStatusUpdateRequestModel;
import digit.web.models.Sponsor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.sql.ResultSet;
import java.util.Map;

@Repository
public class BenefitRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BenefitRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Benefit> getAllBenefits() {


        //String sql = "SELECT * FROM benefits"; // Your SQL query
        String sql="SELECT * FROM benefits LEFT JOIN (SELECT  * FROM benefit_extensions ORDER BY id DESC LIMIT 1) AS be ON benefits.benefit_id = be.benefit_id";
        jdbcTemplate.execute(sql);

        return jdbcTemplate.query(sql, new BenefitRowMapper());
    }
    public boolean updateBenefitStatus(String benefitId, String status) {
        String sql = "UPDATE benefits SET status = ? WHERE benefit_id = ?";
        int rowsAffected = jdbcTemplate.update(sql, status, benefitId);
        return rowsAffected > 0; // Returns true if the update was successful
    }
    // Method to get benefits and their associated sponsors
    public List<Benefit> getBenefitsWithSponsors() {
        String sql = """
            SELECT b.*, bs.sponsor_name, bs.entity_type, bs.share_percent, bs.type
            FROM benefits b
            LEFT JOIN benefit_sponsor bs ON b.benefit_id = bs.benefit_id
        """;


        SponsorRowMapper rowMapper = new SponsorRowMapper();
        jdbcTemplate.query(sql, rowMapper);

        // Retrieve the grouped benefits from the mapper
        return new ArrayList<>(rowMapper.getBenefitMap().values());

    }
  public Benefit getById(String benefitId)
    {
        Benefit benefit=new Benefit();
        List<Benefit> benefits=new ArrayList<>();
        List<Sponsor> sponsors=new ArrayList<>();
       // String sql = "SELECT * FROM benefits where benefit_id= ?";// Your SQL query
        String sql="SELECT * FROM benefits LEFT JOIN (SELECT  * FROM benefit_extensions ORDER BY id DESC LIMIT 1) AS be ON benefits.benefit_id = be.benefit_id where benefits.benefit_id= ?";
       // jdbcTemplate.execute(sql);
        benefits= jdbcTemplate.query(sql, new BenefitRowMapper(),benefitId);
        benefit=benefits.get(0);

        String sql1 = "SELECT * FROM benefit_sponsor where benefit_id= ?";// Your SQL query
       // jdbcTemplate.execute(sql1);
        sponsors= jdbcTemplate.query(sql1, new SponsorsRowMapper(),benefitId);
        benefit.setSponsors(sponsors);
        return  benefit;
    }
    public  int updateStatus(String benefitId,String status)
    {
        String sql = "UPDATE benefits SET status = ? WHERE benefit_id = ?";
        return jdbcTemplate.update(sql,status,benefitId);
    }
    public  int discard(String benefitId)
    {
        String sql = "Delete from benefits  WHERE benefit_id = ?";
        return jdbcTemplate.update(sql,benefitId);
    }


    public List<Benefit> searchBenefits(String name, Date validTill, Date createdStart, Date createdEnd,
                                                      String status, int pageNo, int pageSize, String sortBy) {

        StringBuilder query = new StringBuilder("SELECT * FROM benefits WHERE 1=1");
        List<Object> params = new ArrayList<>();

        // Append conditions based on parameters
        if (name != null && !name.isEmpty()) {
            query.append(" AND benefit_name LIKE ?");
            params.add("%" + name + "%");
        }

        if (status != null && !status.isEmpty()) {
            query.append(" AND status = ?");
            params.add(status);
        }
        if (validTill != null) {
            query.append(" AND valid_till_date <= ?");
            params.add(validTill);
        }

        if (createdStart != null) {
            query.append(" AND created_date >= ?");
            params.add(createdStart);
        }

        if (createdEnd != null) {
            query.append(" AND created_date <= ?");
            params.add(createdEnd);
        }

        // sorting
       query.append(" ORDER BY ").append(sortBy != null ? sortBy : "benefit_name");

        //  pagination
        query.append(" LIMIT ? OFFSET ?");
        params.add(pageSize);
        params.add(pageNo * pageSize);

        // Execute query and map results to Benefit model
        return jdbcTemplate.query(query.toString(), params.toArray(), new BenefitSearchRowMapper());
    }
    public String getBenefitStatusById(String benefitId) {
        String sql = "SELECT status FROM benefits WHERE benefit_id = ?";

        try {
            // Use queryForObject to get a single value
            return jdbcTemplate.queryForObject(sql, new Object[]{benefitId}, String.class);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
    public List<Sponsor> getSponsorById(String benefitId)
    {

        List<Sponsor> sponsors=new ArrayList<>();
        String sql1 = "SELECT * FROM benefit_sponsor where benefit_id='BEN001'";// Your SQL query
        jdbcTemplate.execute(sql1);
        sponsors= jdbcTemplate.query(sql1, new SponsorsRowMapper());

        return  sponsors;
    }

}