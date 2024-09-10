package digit.disbursal.util;

import digit.disbursal.config.Configuration;
import digit.disbursal.web.models.Disbursal;
import digit.disbursal.web.models.DisbursalRequest;
import digit.disbursal.web.models.DisbursalSearchRequest;
import digit.disbursal.web.models.Pagination;
import org.egov.common.contract.models.AuditDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class EnrichmentUtil {

    private final Configuration configuration;
    private final IdgenUtil idgenUtil;

    @Autowired
    private EnrichmentUtil(Configuration configuration, IdgenUtil idgenUtil) {
        this.configuration = configuration;
        this.idgenUtil = idgenUtil;
    }


    public void enrichDisbursalForCreation(DisbursalRequest disbursalRequest) {
        Disbursal disbursal = disbursalRequest.getDisbursal();
        String createdBy = disbursalRequest.getRequestInfo().getUserInfo().getUuid();
        AuditDetails audit = getAuditDetails(createdBy, true);

        String disbursalNumber = idgenUtil
                .getIdList(disbursalRequest.getRequestInfo(), disbursal.getTenantId(), configuration.getIdGenDisbursalFormat(), null, 1).get(0);
        disbursal.setDisbursalNumber(disbursalNumber);
        disbursal.setAuditDetails(audit);
        disbursal.setId(UUID.randomUUID().toString());

    }


    /**
     * Method to return auditDetails for create/update flows
     *
     * @param by
     * @param isCreate
     * @return AuditDetails
     */
    public AuditDetails getAuditDetails(String by, Boolean isCreate) {

        Long time = System.currentTimeMillis();

        if (Boolean.TRUE.equals(isCreate))
            return AuditDetails.builder()
                    .createdBy(by)
                    .createdTime(time)
                    .lastModifiedBy(by)
                    .lastModifiedTime(time)
                    .build();
        else
            return AuditDetails.builder()
                    .lastModifiedBy(by)
                    .lastModifiedTime(time)
                    .build();
    }


    public void enrichSearchDisbursalRequest(DisbursalSearchRequest disbursalSearchRequest) {

        Pagination pagination = getPagination(disbursalSearchRequest);

        if (pagination.getLimit() == null)
            pagination.setLimit(this.configuration.getDefaultLimit());

        if (pagination.getOffSet() == null)
            pagination.setOffSet(this.configuration.getDefaultOffset());

        if (pagination.getLimit() != null && pagination.getLimit().compareTo(this.configuration.getMaxSearchLimit()) > 0)
            pagination.setLimit(this.configuration.getMaxSearchLimit());
    }

    private Pagination getPagination(DisbursalSearchRequest disbursalSearchRequest) {
        Pagination pagination = disbursalSearchRequest.getPagination();
        if (pagination == null) {
            pagination = Pagination.builder().build();
            disbursalSearchRequest.setPagination(pagination);
        }
        return pagination;
    }





}
