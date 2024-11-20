package digit.repository;

import digit.repository.rowmapper.ApplicationResultSetExtractor;
import digit.repository.rowmapper.GraphDataRowMapper;
import digit.web.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
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


        String amtperBenefitCategoryQuery = "SELECT * FROM benefits_category WHERE benefit_id = ?";
        List<AmountPerBeneficiaryCategory> lstAmtperBenefitCategory = jdbcTemplate.query(
                amtperBenefitCategoryQuery,
                new Object[]{benefit.getBenefitId()}, // Pass benefitId as a parameter
                new AmountPerBeneficiaryCategoryRowMapper()
        );
        benefit.getFinancialInformation().setAmountPerBeneficiaryCategory(lstAmtperBenefitCategory);
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
   /* public List<BenefitStatusCount> getBenefitStatusCounts() {
        String sql = "SELECT b.benefit_id, b.benefit_name, b.application_end, " +
                "COUNT(CASE WHEN a.status = 'Approved' THEN 1 END) AS approved_count, " +
                "COUNT(CASE WHEN a.status = 'Rejected' THEN 1 END) AS rejected_count, " +
                "COUNT(*) AS total_applications " +  // Fixed spacing here
                "FROM public.benefits b " +
                "LEFT JOIN public.eg_ubp_application a ON b.benefit_id = a.program_code " +
                "GROUP BY b.benefit_id, b.benefit_name, b.application_end";

        // Use the BenefitStatusCountRowMapper
        return jdbcTemplate.query(sql, new BenefitStatusCountRowMapper());
    }*/
    public List<BenefitStatusCount> getBenefitStatusCount(SearchCriteria filter) {
        StringBuilder sql = new StringBuilder(
                "SELECT b.benefit_id, b.benefit_name, b.application_end, " +
                        "COUNT(CASE WHEN a.status = 'Approved' THEN 1 END) AS approved_count, " +
                        "COUNT(CASE WHEN a.status = 'Rejected' THEN 1 END) AS rejected_count, " +
                        "COUNT(*) AS total_applications " +
                        "FROM public.benefits b " +
                        "LEFT JOIN public.eg_ubp_application a ON b.benefit_id = a.program_code " +
                        "WHERE 1 = 1 "
        );

        // Apply filters dynamically
        if (filter.getName() != null) {
            sql.append("AND b.benefit_name ILIKE ? ");
        }
        if (filter.getValidTill() != null) {
            sql.append("AND b.valid_till_date <= ? ");
        }
        if (filter.getCreatedStart() != null) {
            sql.append("AND b.created_time >= ? ");
        }
        if (filter.getCreatedEnd() != null) {
            sql.append("AND b.created_time <= ? ");
        }
        if (filter.getStatus() != null) {
            sql.append("AND b.status = ? ");
        }

        sql.append("GROUP BY b.benefit_id, b.benefit_name, b.application_end ");

        // Add sorting logic
        if ("benefit_name".equals(filter.getSortBy())) {
            sql.append("ORDER BY b.benefit_name ");
        } else {
            sql.append("ORDER BY b.benefit_id "); // Default sorting
        }

        // Apply pagination
        sql.append("LIMIT ? OFFSET ?");

        // Prepare the parameters
        List<Object> params = new ArrayList<>();

        if (filter.getName() != null) {
            params.add("%" + filter.getName() + "%");
        }
        if (filter.getValidTill() != null) {
            params.add(Long.parseLong(String.valueOf(filter.getValidTill())));
        }
        if (filter.getCreatedStart() != null) {
            params.add(Long.parseLong(String.valueOf(filter.getCreatedStart())));
        }
        if (filter.getCreatedEnd() != null) {
            params.add(Long.parseLong(String.valueOf(filter.getCreatedEnd())));
        }
        if (filter.getStatus() != null) {
            params.add(filter.getStatus());
        }

        // Pagination params
        params.add(filter.getPageSize());
        params.add(filter.getPageNo() * filter.getPageSize()); // offset calculation

        // Execute the query with the dynamically built SQL
        return jdbcTemplate.query(sql.toString(), params.toArray(), new BenefitStatusCountRowMapper());
    }

    public BenefitInfo getBenefitBriefDetails(String benefitId)
    {
        Benefit benefit=new Benefit();
        List<BenefitInfo> benefitResponse=new ArrayList<>();
        BenefitInfo response=new BenefitInfo();
        List<Sponsor> sponsors=new ArrayList<>();
        String sql = "SELECT " +
                "b.benefit_id AS id, " +
                "b.application_end, " +
                "array_agg(be.new_deadline) AS extended_deadlines " +
                "FROM public.benefits b " +
                "LEFT JOIN public.benefit_extensions be ON b.benefit_id = be.benefit_id " +
                "WHERE b.benefit_id = ? " +
                "GROUP BY b.benefit_id, b.application_end";

        // jdbcTemplate.execute(sql);
        benefitResponse = jdbcTemplate.query(sql, new Object[]{benefitId}, new BenefitBriefDetailsRowMapper());
        response=benefitResponse.get(0);
        String sql1 = "SELECT * FROM benefit_sponsor where benefit_id= ?";// Your SQL query
        // jdbcTemplate.execute(sql1);
        sponsors= jdbcTemplate.query(sql1, new SponsorsRowMapper(),benefitId);
        response.setSponsors(sponsors);
        return  response;
    }


    public List<Benefit> getBenefits()
    {
        String benefitQuery = "SELECT * FROM benefits LEFT JOIN (SELECT  * FROM benefit_extensions ORDER BY id DESC LIMIT 1) AS be ON benefits.benefit_id = be.benefit_id"; // Adjust the query as needed
        jdbcTemplate.execute(benefitQuery);
        List<Benefit> benefits = jdbcTemplate.query(benefitQuery, new BenefitRowMapper());

        for (Benefit benefit : benefits) {
            String sponsorQuery = "SELECT * FROM public.benefit_sponsor WHERE benefit_id = ?";
            List<Sponsor> sponsors = jdbcTemplate.query(sponsorQuery, new Object[]{benefit.getBenefitId()}, new SponsorsRowMapper());
            benefit.setSponsors(sponsors);
        }
        for (Benefit benefit : benefits) {
            // Query to fetch categories related to the specific benefit_id
            String amtperBenefitCategoryQuery = "SELECT * FROM benefits_category WHERE benefit_id = ?";
            List<AmountPerBeneficiaryCategory> lstAmtperBenefitCategory = jdbcTemplate.query(
                    amtperBenefitCategoryQuery,
                    new Object[]{benefit.getBenefitId()}, // Pass benefitId as a parameter
                    new AmountPerBeneficiaryCategoryRowMapper()
            );
            benefit.getFinancialInformation().setAmountPerBeneficiaryCategory(lstAmtperBenefitCategory);
        }

        return benefits;
    }
    public List<AmountPerBeneficiaryCategory> getAmtperBenefitCategory()
    {
        String amtperBenefitCategory="Select * FROM benefits_category";
        jdbcTemplate.execute(amtperBenefitCategory);
        List<AmountPerBeneficiaryCategory> amountPerBeneficiaryCategories=jdbcTemplate.query(amtperBenefitCategory,new AmountPerBeneficiaryCategoryRowMapper());
        return  amountPerBeneficiaryCategories;
    }

    private ResultSetExtractor<List<GraphData>> extractGraphData() {
        return rs -> {
            List<GraphData> graphDataList = new ArrayList<>();
            while (rs.next()) {
                GraphData graphData = new GraphData();
                graphData.setLabel(rs.getString("label"));
                graphData.setCount(rs.getInt("count"));
                graphDataList.add(graphData);
            }
            return graphDataList;
        };
    }

    public List<GraphData> getWeeklyData(String benefitId, String monthYear) {
        String sql = """
                WITH date_range AS (
                    SELECT
                        DATE_TRUNC('month', TO_DATE(?, 'Month YYYY')) AS start_date,  -- Start of the month
                        (DATE_TRUNC('month', TO_DATE(?, 'Month YYYY')) + INTERVAL '1 month - 1 day')::DATE AS end_date -- End of the month
                ),
                week_ranges AS (
                    SELECT
                        generate_series(
                            (SELECT start_date FROM date_range),
                            (SELECT end_date FROM date_range),
                            '7 days'::interval
                        )::DATE AS week_start,
                        generate_series(
                            (SELECT start_date FROM date_range),
                            (SELECT end_date FROM date_range),
                            '7 days'::interval
                        )::DATE + INTERVAL '6 days' AS raw_week_end
                    FROM date_range
                ),
                final_week_ranges AS (
                    SELECT
                        week_start,
                        LEAST(raw_week_end, (SELECT end_date FROM date_range)) AS week_end -- Ensure no overlap and end date is within the month
                    FROM
                        week_ranges
                ),
                weekly_data AS (
                    SELECT
                        fwr.week_start,
                        fwr.week_end,
                        COUNT(id) AS count
                    FROM
                        final_week_ranges fwr
                    LEFT JOIN LATERAL (
                        SELECT
                            app.id,
                            DATE_TRUNC('day', TO_TIMESTAMP(app.created_time / 1000))::DATE AS created_date
                        FROM
                            eg_ubp_application app
                        JOIN
                            benefits ben
                        ON
                            app.program_code = ben.benefit_name
                        WHERE
                            ben.benefit_id = ?
                        ) AS app_data
                    ON app_data.created_date BETWEEN fwr.week_start AND fwr.week_end
                    GROUP BY
                        fwr.week_start, fwr.week_end
                )
                SELECT
                    'Week ' || ROW_NUMBER() OVER (ORDER BY week_start ASC) AS label,
                    COALESCE(weekly_data.count, 0) AS count
                FROM
                    weekly_data
                ORDER BY
                    week_start;
        """;

        return jdbcTemplate.query(sql, new Object[]{monthYear, monthYear, benefitId}, extractGraphData());
    }

    public List<GraphData> getCasteDistribution(String benefitId, String monthYear) {
        String sql = """
                WITH date_range AS (
                    SELECT
                        DATE_TRUNC('month', TO_DATE(?, 'Month YYYY')) AS start_date, -- Start of the month
                        (DATE_TRUNC('month', TO_DATE(?, 'Month YYYY')) + INTERVAL '1 month - 1 day')::DATE AS end_date -- End of the month
                ),
                caste_data AS (
                    SELECT
                        appl.caste AS label,
                        COUNT(*) AS count
                    FROM
                        eg_ubp_application app
                    JOIN
                        benefits ben ON app.program_code = ben.benefit_name
                    JOIN
                        eg_ubp_applicant appl ON app.id = appl.application_id
                    WHERE
                        ben.benefit_id = ?
                        AND TO_TIMESTAMP(app.created_time / 1000)::DATE BETWEEN
                            (SELECT start_date FROM date_range) AND
                            (SELECT end_date FROM date_range)
                    GROUP BY
                        appl.caste
                )
                SELECT
                    label,
                    COALESCE(count, 0) AS count
                FROM
                    caste_data
                ORDER BY
                    count DESC;
        """;

        return jdbcTemplate.query(sql, new Object[]{monthYear, monthYear, benefitId}, extractGraphData());
    }

    public List<GraphData> getAgeDistribution(String benefitId, String monthYear) {
        String sql = """
                WITH date_range AS (
                    SELECT
                        DATE_TRUNC('month', TO_DATE(?, 'Month YYYY')) AS start_date, -- Start of the month
                        (DATE_TRUNC('month', TO_DATE(?, 'Month YYYY')) + INTERVAL '1 month - 1 day')::DATE AS end_date -- End of the month
                ),
                age_data AS (
                    SELECT
                        CASE
                            WHEN appl.age BETWEEN 15 AND 25 THEN '15-25'
                            WHEN appl.age BETWEEN 25 AND 35 THEN '25-35'
                            WHEN appl.age BETWEEN 45 AND 55 THEN '45-55'
                            ELSE 'Others'
                        END AS label,
                        COUNT(*) AS count
                    FROM
                        eg_ubp_application app
                    JOIN
                        benefits ben ON app.program_code = ben.benefit_name
                    JOIN
                        eg_ubp_applicant appl ON app.id = appl.application_id
                    WHERE
                        ben.benefit_id = ?
                        AND TO_TIMESTAMP(app.created_time / 1000)::DATE BETWEEN
                            (SELECT start_date FROM date_range) AND
                            (SELECT end_date FROM date_range)
                    GROUP BY
                        label
                )
                SELECT
                    label,
                    COALESCE(count, 0) AS count
                FROM
                    age_data
                ORDER BY
                    count DESC;
    """;

        return jdbcTemplate.query(sql, new Object[]{monthYear, monthYear, benefitId}, extractGraphData());
    }

    public List<GraphData> getRatioDistribution(String benefitId, String monthYear) {
        String sql = """
                WITH date_range AS (
                    SELECT
                        DATE_TRUNC('month', TO_DATE(?, 'Month YYYY')) AS start_date, -- Start of the month
                        (DATE_TRUNC('month', TO_DATE(?, 'Month YYYY')) + INTERVAL '1 month - 1 day')::DATE AS end_date -- End of the month
                ),
                student_type_data AS (
                    SELECT
                        appl.student_type AS label,
                        COUNT(*) AS count
                    FROM
                        eg_ubp_application app
                    JOIN
                        benefits ben
                    ON
                        app.program_code = ben.benefit_name
                    JOIN
                        eg_ubp_applicant appl
                    ON
                        app.id = appl.application_id
                    WHERE
                        ben.benefit_id = ?
                        AND TO_TIMESTAMP(app.created_time / 1000)::DATE BETWEEN
                            (SELECT start_date FROM date_range) AND
                            (SELECT end_date FROM date_range)
                    GROUP BY
                        appl.student_type
                )
                SELECT
                    label,
                    COALESCE(count, 0) AS count
                FROM
                    student_type_data
                ORDER BY
                    count DESC;
        """;

        return jdbcTemplate.query(sql, new Object[]{monthYear, monthYear, benefitId}, extractGraphData());
    }

    public List<GraphData> getGenderDistribution(String benefitId, String monthYear) {
        String sql = """
                WITH date_range AS (
                    SELECT
                        DATE_TRUNC('month', TO_DATE(?, 'Month YYYY')) AS start_date, -- Start of the month
                        (DATE_TRUNC('month', TO_DATE(?, 'Month YYYY')) + INTERVAL '1 month - 1 day')::DATE AS end_date -- End of the month
                ),
                gender_data AS (
                    SELECT
                        appl.gender AS label,
                        COUNT(*) AS count
                    FROM
                        eg_ubp_application app
                    JOIN
                        benefits ben
                        ON app.program_code = ben.benefit_name
                    JOIN
                        eg_ubp_applicant appl
                        ON app.id = appl.application_id
                    WHERE
                        ben.benefit_id = ?
                        AND TO_TIMESTAMP(app.created_time / 1000)::DATE BETWEEN
                            (SELECT start_date FROM date_range) AND
                            (SELECT end_date FROM date_range)
                    GROUP BY
                        appl.gender
                )
                SELECT
                    label,
                    COALESCE(count, 0) AS count
                FROM
                    gender_data
                ORDER BY
                    count DESC;
        """;

        return jdbcTemplate.query(sql, new Object[]{monthYear, monthYear, benefitId}, extractGraphData());
    }
}
