package digit.application.repository;

import digit.application.repository.querybuilder.ApplicationQueryBuilder;
import digit.application.repository.rowmapper.SearchApplicationRowMapper;
import digit.application.web.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    public List<AppStatResponse> getApplicationStats() {
        String sql = "SELECT COUNT(*) AS totalApplications, " +
                "COUNT(CASE WHEN status = 'APPROVED' THEN 1 END) AS approvedCount, " +
                "COUNT(CASE WHEN status = 'REJECTED' THEN 1 END) AS rejectedCount " +
                "FROM eg_ubp_application";

        ApplicationStatsResponse stats = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            ApplicationStatsResponse statResponse = new ApplicationStatsResponse();
            statResponse.setTotalApplications(rs.getInt("totalApplications"));
            statResponse.setApprovedCount(rs.getInt("approvedCount"));
            statResponse.setRejectedCount(rs.getInt("rejectedCount"));
            return statResponse;
        });

        // Transform to the required format
        List<AppStatResponse> applicationStats = new ArrayList<>();
        applicationStats.add(new AppStatResponse(1, "Total Applicants", stats.getTotalApplications()));
        applicationStats.add(new AppStatResponse(2, "New Applicants", 0)); // Assuming you need to set this value appropriately
        applicationStats.add(new AppStatResponse(3, "Accepted Applicants", stats.getApprovedCount()));
        applicationStats.add(new AppStatResponse(4, "Rejected Applicants", stats.getRejectedCount()));

        return applicationStats;
    }

    public List<ProviderFundStat> getProviderFundStats() {
                List<ProviderFundStat> providerFundStats = Arrays.asList(
                new ProviderFundStat(1, "Remaining", 30000, 1),
                new ProviderFundStat(2, "Utilised", 250000, 1)
        );
        return providerFundStats;
    }
    public List<ScholarshipStat> getScholarshipStats() {
        List<ScholarshipStat> scholarshipStats = Arrays.asList(
                new ScholarshipStat(1, "Pre-Matric Scholarship-General", 4325, "1,00,000"),
                new ScholarshipStat(2, "Pre-Matric Scholarship-ST", 4325, "1,00,000"),
                new ScholarshipStat(3, "Pre-Matric Scholarship-SC", 4325, "1,00,000")
        );
        return scholarshipStats;
    }
    public List<ScholarshipDetails> getScholarshipDetails() {
        List<ScholarshipDetails> scholarshipDetailsList = Arrays.asList(
                new ScholarshipDetails(836, "Pre-Matric Scholarship-SC Drafts 722", 266, 97, 209, 77, "10-04-2021", "Drafts"),
                new ScholarshipDetails(184, "Pre-Matric Scholarship-SC Drafts 904", 525, 40, 145, 279, "13-08-2022", "Drafts"),
                new ScholarshipDetails(884, "Pre-Matric Scholarship-SC Drafts 266", 160, 85, 7, 806, "15-12-2022", "Drafts"),
                new ScholarshipDetails(567, "Pre-Matric Scholarship-SC Drafts 89", 98, 60, 457, 831, "19-03-2023", "Drafts"),
                new ScholarshipDetails(572, "Pre-Matric Scholarship-SC Drafts 628", 601, 117, 143, 207, "26-06-2022", "Drafts"),
                new ScholarshipDetails(571, "Pre-Matric Scholarship-SC Drafts 830", 298, 230, 86, 961, "27-11-2021", "Drafts"),
                new ScholarshipDetails(678, "Pre-Matric Scholarship-SC Drafts 299", 252, 570, 494, 528, "25-01-2022", "Drafts"),
                new ScholarshipDetails(733, "Pre-Matric Scholarship-SC Drafts 650", 851, 265, 778, 32, "16-10-2022", "Drafts"),
                new ScholarshipDetails(38, "Pre-Matric Scholarship-SC Drafts 901", 771, 841, 401, 892, "02-02-2021", "Drafts"),
                new ScholarshipDetails(494, "Pre-Matric Scholarship-SC Drafts 579", 74, 425, 897, 488, "16-03-2021", "Drafts")
        );
        return scholarshipDetailsList;
    }
    public List<ApplicatonStatastics> getApplicationStatsByProgramCode() {
        String sql = "SELECT program_code, " +
                "COUNT(*) AS total_applications, " +
                "COUNT(CASE WHEN status = 'APPROVED' THEN 1 END) AS approved_count, " +
                "COUNT(CASE WHEN status = 'REJECTED' THEN 1 END) AS rejected_count " +
                "FROM public.eg_ubp_application " +
                "GROUP BY program_code " +
                "ORDER BY program_code";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            ApplicatonStatastics statsResponse = new ApplicatonStatastics();
            statsResponse.setProgramCode(rs.getString("program_code"));
            statsResponse.setTotalApplications(rs.getInt("total_applications"));
            statsResponse.setApprovedCount(rs.getInt("approved_count"));
            statsResponse.setRejectedCount(rs.getInt("rejected_count"));
            return statsResponse;
        });
    }

    public Optional<Application> getApplicationById(String applicationId) {
        String query = "SELECT * FROM eg_ubp_application WHERE id = ?";

        return jdbcTemplate.query(query,
                new Object[]{applicationId},
                applicationRowMapper()).stream().findFirst();
    }

    public boolean doesApplicationExist(String id) {
        String sql = "SELECT COUNT(*) FROM eg_ubp_application WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, new Object[]{id}, Integer.class);
        return count != null && count > 0;
    }

    private RowMapper<Application> applicationRowMapper() {
        return (rs, rowNum) -> {
            Application application = Application.builder()
                    .id(rs.getString("id"))
                    .tenantId(rs.getString("tenant_id"))
                    .applicationNumber(rs.getString("application_number"))
                    .individualId(rs.getString("individual_id"))
                    .programCode(rs.getString("program_code"))
                    .status(Application.StatusEnum.fromValue(rs.getString("status")))
                    .wfStatus(Application.WFStatusEnum.fromValue(rs.getString("wf_status")))
                    .additionalDetails(rs.getString("additional_details"))
                    .schema(rs.getString("schema"))
                    .orderId(rs.getString("order_id"))
                    .transactionId(rs.getString("transaction_id"))
                    .submissionId(rs.getString("submission_id"))
                    .contentId(rs.getString("content_id"))
                    .auditDetails(null)
                    .batch_id(rs.getInt("batch_id")) 
                    .build();

            // Fetch and attach related Applicant
            application.setApplicant(getApplicantByApplicationId(application.getId()));

            // Fetch and attach related Documents
            application.setDocuments(getDocumentsByApplicationId(application.getId()));

            return application;
        };
    }

    private Applicant getApplicantByApplicationId(String applicationId) {
        String query = "SELECT * FROM eg_ubp_applicant WHERE application_id = ?";
        return jdbcTemplate.query(query, new Object[]{applicationId}, searchApplicationRowMapper.applicantRowMapper()).stream().findFirst().orElse(null);
    }



    private List<Document> getDocumentsByApplicationId(String applicationId) {
        String query = "SELECT * FROM eg_ubp_application_documents WHERE application_id = ?";
        return jdbcTemplate.query(query, new Object[]{applicationId}, searchApplicationRowMapper.documentRowMapper());
    }

    //priyanka 25Nov2024
    public Optional<Application> direct_disbursals(String applicationId) {
        String query = "SELECT * FROM eg_ubp_application WHERE id = ?";

        return jdbcTemplate.query(query,
                new Object[]{applicationId},
                applicationRowMapper()).stream().findFirst();
    }
    
   //public Optional<Application> getDisbursementProcessApplications(Map<String, Object> requestPayload) {
   public List<Application> getDisbursementProcessApplications(String disbursalStatus) {
            String query = "SELECT * FROM eg_ubp_application WHERE status = ?";
            List<Application> applications = jdbcTemplate.query(query, 
                    new Object[]{disbursalStatus}, 
                    applicationRowMapper());
            return applications;
    }

   
    //End by priyanka

}
