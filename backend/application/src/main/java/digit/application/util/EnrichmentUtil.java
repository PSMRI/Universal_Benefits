package digit.application.util;

import digit.application.config.Configuration;
import digit.application.web.models.*;
import org.apache.commons.lang3.ObjectUtils;
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

    public void enrichApplicationForCreation(ApplicationRequest applicationRequest) {
        Application application = applicationRequest.getApplication();
        String createdBy = applicationRequest.getRequestInfo().getUserInfo().getUuid();
        AuditDetails audit = getAuditDetails(createdBy, true);

        String applicationNumber = idgenUtil
                .getIdList(applicationRequest.getRequestInfo(), application.getTenantId(), configuration.getIdGenApplicationFormat(), null, 1).get(0);
        application.setApplicationNumber(applicationNumber);
        application.setAuditDetails(audit);
        application.setId(UUID.randomUUID().toString());

        for (Document document : application.getDocuments()) {
            document.setId(UUID.randomUUID().toString());
        }
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


    public void enrichSearchApplicationRequest(ApplicationSearchRequest applicationSearchRequest) {

        Pagination pagination = getPagination(applicationSearchRequest);

        if (pagination.getLimit() == null)
            pagination.setLimit(this.configuration.getDefaultLimit());

        if (pagination.getOffSet() == null)
            pagination.setOffSet(this.configuration.getDefaultOffset());

        if (pagination.getLimit() != null && pagination.getLimit().compareTo(this.configuration.getMaxSearchLimit()) > 0)
            pagination.setLimit(this.configuration.getMaxSearchLimit());
    }

    private Pagination getPagination(ApplicationSearchRequest applicationSearchRequest) {
        Pagination pagination = applicationSearchRequest.getPagination();
        if (pagination == null) {
            pagination = Pagination.builder().build();
            applicationSearchRequest.setPagination(pagination);
        }
        return pagination;
    }


}
