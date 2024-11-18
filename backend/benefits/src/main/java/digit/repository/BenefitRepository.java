package digit.repository;

import digit.repository.rowmapper.ApplicationResultSetExtractor;
import digit.web.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    public List<Application> getApplicationsByBenefitName(String benefitName) {
        String sql = "SELECT app.id AS application_id, app.tenant_id, app.application_number, " +
                "app.individual_id, app.program_code, app.status, app.wf_status, app.additional_details, " +
                "app.schema, app.created_by, app.last_modified_by, app.created_time, app.last_modified_time, " +
                "applicant.id AS applicant_id, applicant.student_name, applicant.father_name, applicant.samagra_id, " +
                "applicant.school_name AS current_school_name, applicant.school_address AS current_school_address, " +
                "applicant.school_address_district AS current_school_address_district, applicant.current_class, " +
                "applicant.previous_year_marks, applicant.student_type, applicant.aadhar_last_4_digits, applicant.caste, " +
                "applicant.income, applicant.gender, applicant.age, applicant.disability, doc.id AS document_id, " +
                "doc.document_type, doc.filestore_id, doc.document_uid, doc.status AS document_status, doc.additional_details " +
                "AS document_additional_details FROM eg_ubp_application app LEFT JOIN eg_ubp_applicant applicant ON app.id = " +
                "applicant.application_id LEFT JOIN eg_ubp_application_documents doc ON app.id = doc.application_id " +
                "WHERE app.program_code = ?;";

        return jdbcTemplate.query(sql, new Object[]{benefitName}, new ApplicationResultSetExtractor());
    }

    public String findBenefitNameById(String benefitId) {
        String sql = "SELECT benefit_name FROM benefits WHERE benefit_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{benefitId}, String.class);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
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
