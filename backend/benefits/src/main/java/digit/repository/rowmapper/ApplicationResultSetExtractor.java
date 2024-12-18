package digit.repository.rowmapper;

import digit.web.models.Applicant;
import digit.web.models.Application;
import digit.web.models.Document;
import org.egov.common.contract.models.AuditDetails;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ApplicationResultSetExtractor implements ResultSetExtractor<List<Application>> {
    @Override
    public List<Application> extractData(ResultSet rs) throws SQLException {
        Map<String, Application> applicationMap = new LinkedHashMap<>();

        while (rs.next()) {
            String applicationId = rs.getString("application_id");
            Application application = applicationMap.get(applicationId);

            if (application == null) {
                // Build Application object
                application = Application.builder()
                        .id(applicationId)
                        .tenantId(rs.getString("tenant_id"))
                        .order_id(rs.getString("order_id"))
                        .applicationNumber(rs.getString("application_number"))
                        .individualId(rs.getString("individual_id"))
                        .programCode(rs.getString("program_code"))
                        .status(Application.StatusEnum.fromValue(rs.getString("status")))
                        .wfStatus(Application.WFStatusEnum.fromValue(rs.getString("wf_status")))
                        .schema(rs.getString("schema"))
                        .additionalDetails(rs.getObject("additional_details"))
                        .auditDetails(AuditDetails.builder()
                                .createdBy(rs.getString("created_by"))
                                .lastModifiedBy(rs.getString("last_modified_by"))
                                .createdTime(rs.getLong("created_time"))
                                .lastModifiedTime(rs.getLong("last_modified_time"))
                                .build())
                        .applicant(Applicant.builder()
                                .id(rs.getString("applicant_id"))
                                .applicationId(applicationId)
                                .studentName(rs.getString("student_name"))
                                .fatherName(rs.getString("father_name"))
                                .samagraId(rs.getString("samagra_id"))
                                .currentSchoolName(rs.getString("current_school_name"))
                                .currentSchoolAddress(rs.getString("current_school_address"))
                                .currentSchoolAddressDistrict(rs.getString("current_school_address_district"))
                                .currentClass(rs.getString("current_class"))
                                .previousYearMarks(rs.getString("previous_year_marks"))
                                .studentType(rs.getString("student_type"))
                                .aadharLast4Digits(rs.getString("aadhar_last_4_digits"))
                                .caste(rs.getString("caste"))
                                .income(rs.getString("income"))
                                .gender(rs.getString("gender"))
                                .age(rs.getInt("age"))
                                .disability(rs.getBoolean("disability"))
                                .build())
                        .documents(new ArrayList<>())
                        .build();

                applicationMap.put(applicationId, application);
            }

            // Add Document to Application
            String documentId = rs.getString("document_id");
            if (documentId != null) {
                Document document = Document.builder()
                        .id(documentId)
                        .documentType(rs.getString("document_type"))
                        .fileStoreId(rs.getString("filestore_id"))
                        .documentUid(rs.getString("document_uid"))
                        .status(Document.StatusEnum.fromValue(rs.getString("document_status")))
                        .additionalDetails(rs.getObject("document_additional_details"))
                        .build();
                application.getDocuments().add(document);
            }
        }

        return new ArrayList<>(applicationMap.values());
    }
}
