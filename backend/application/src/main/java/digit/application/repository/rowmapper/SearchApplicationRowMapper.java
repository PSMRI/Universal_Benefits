package digit.application.repository.rowmapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import digit.application.web.models.Application;
import digit.application.web.models.Document;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.AuditDetails;
import org.egov.tracer.model.CustomException;
import org.postgresql.util.PGobject;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
@Slf4j
public class SearchApplicationRowMapper implements ResultSetExtractor<List<Application>> {

    private final ObjectMapper mapper;

    public SearchApplicationRowMapper(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public List<Application> extractData(java.sql.ResultSet rs) throws SQLException {

        Map<String, Application> applicationMap = new LinkedHashMap<>();

        while (rs.next()) {
            String applicationId = rs.getString("a_id");
            Application application = applicationMap.get(applicationId);

            if (application == null) {

                Document document = getDocument(rs);

                AuditDetails applicationAuditDetails = getAuditDetailsForKey(rs,
                        "a_created_by",
                        "created_time",
                        "a_last_modified_by",
                        "a_last_modified_time");

                application = Application.builder()
                        .status(Application.StatusEnum.fromValue(rs.getString("a_status")))
                        .wfStatus(rs.getString("a_wf_status"))
                        .additionalDetails(getadditionalDetail(rs, "a_additional_details"))
                        .programCode(rs.getString("a_program_code"))
                        .applicationNumber(rs.getString("a_application_number"))
                        .individualId(rs.getString("a_individual_id"))
                        .tenantId(rs.getString("a_tenantid"))
                        .auditDetails(applicationAuditDetails)
                        .documents(Arrays.asList(document))
                        .id(rs.getString("a_id"))
                        .build();

                applicationMap.put(application.getId(), application);
            } else {
                Document document = getDocument(rs);
                application.addDocumentsItem(document);
            }


        }
        log.debug("converting map to list object ::: " + applicationMap.values());
        return new ArrayList<>(applicationMap.values());
    }

    /**
     * Fetch audit details from result set for the given keys
     *
     * @param rs
     * @param createdBy
     * @param createdTime
     * @param modifiedBy
     * @param modifiedTime
     * @return
     * @throws SQLException
     */
    private AuditDetails getAuditDetailsForKey (ResultSet rs, String createdBy, String createdTime, String modifiedBy, String modifiedTime) throws SQLException {

        return AuditDetails.builder()
                .lastModifiedTime(rs.getLong(modifiedTime))
                .createdTime((Long) rs.getObject(createdTime))
                .lastModifiedBy(rs.getString(modifiedBy))
                .createdBy(rs.getString(createdBy))
                .build();
    }

    /**
     * Fetch payer details from result set
     * @param rs
     * @return
     * @throws SQLException
     */
    private Document getDocument(ResultSet rs) throws SQLException {

        return Document.builder()
                .status(Document.StatusEnum.fromValue(rs.getString("d_status")))
                .fileStore(rs.getString("d_filestore_id"))
                .documentType(rs.getString("d_document_type"))
                .documentUid(rs.getString("d_document_uid"))
                .additionalDetails(getadditionalDetail(rs, "d_additional_details"))
                .id(rs.getString("d_id"))
                .build();
    }

    /**
     * method parses the PGobject and returns the JSON node
     *
     * @param rs
     * @param key
     * @return
     * @throws SQLException
     */
    private JsonNode getadditionalDetail(ResultSet rs, String key) throws SQLException {

        JsonNode additionalDetails = null;

        try {

            PGobject obj = (PGobject) rs.getObject(key);
            if (obj != null) {
                additionalDetails = mapper.readTree(obj.getValue());
            }

        } catch (IOException e) {
            throw new CustomException("PARSING ERROR", "The propertyAdditionalDetail json cannot be parsed");
        }

        if(additionalDetails != null && additionalDetails.isEmpty())
            additionalDetails = null;

        return additionalDetails;

    }


}
