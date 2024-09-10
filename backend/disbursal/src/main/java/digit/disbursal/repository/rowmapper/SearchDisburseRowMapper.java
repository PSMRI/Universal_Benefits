package digit.disbursal.repository.rowmapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import digit.disbursal.web.models.Disbursal;
import digit.disbursal.web.models.enums.PaymentStatus;
import digit.disbursal.web.models.enums.Status;
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
public class SearchDisburseRowMapper implements ResultSetExtractor<List<Disbursal>> {

    private final ObjectMapper mapper;

    public SearchDisburseRowMapper(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public List<Disbursal> extractData(ResultSet rs) throws SQLException {

        Map<String, Disbursal> disbursalMap = new LinkedHashMap<>();

        while (rs.next()) {
            String disbursalId = rs.getString("d_id");
            Disbursal disbursal = disbursalMap.get(disbursalId);

            if (disbursal == null) {
                AuditDetails disbursalAuditDetails = getAuditDetailsForKey(rs,
                        "d_created_by",
                        "created_time",
                        "d_last_modified_by",
                        "d_last_modified_time");

                disbursal = Disbursal.builder()
                        .status(Status.fromValue(rs.getString("d_status")))
                        .wfStatus(rs.getString("d_wf_status"))
                        .additionalDetails(getadditionalDetail(rs, "d_additional_details"))
                        .tenantId(rs.getString("d_tenantid"))
                        .referenceId(rs.getString("d_reference_id"))
                        .totalAmount(rs.getBigDecimal("d_total_amount"))
                        .totalPaidAmount(rs.getBigDecimal("d_total_paid_amount"))
                        .paymentStatus(PaymentStatus.fromValue(rs.getString("d_payment_status")))
                        .disbursalNumber(rs.getString("d_disbursal_number"))
                        .auditDetails(disbursalAuditDetails)
                        .id(rs.getString("d_id"))
                        .build();

                disbursalMap.put(disbursal.getId(), disbursal);
            }


        }
        log.debug("converting map to list object ::: " + disbursalMap.values());
        return new ArrayList<>(disbursalMap.values());
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
