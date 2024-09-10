package digit.application.repository.querybuilder;

import digit.application.config.Configuration;
import digit.application.config.ServiceConstants;
import digit.application.web.models.ApplicationSearchCriteria;
import digit.application.web.models.ApplicationSearchRequest;
import digit.application.web.models.Order;
import digit.application.web.models.Pagination;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Component
public class ApplicationQueryBuilder {

    private final String APPLICATION_QUERY = "SELECT "

            + " application.id as a_id, application.tenant_id as a_tenantid, application.application_number as a_application_number, application.individual_id as a_individual_id, "
            + " application.program_code as a_program_code, application.status as a_status, application.wf_status as a_wf_status, application.additional_details as a_additional_details, "
            + " application.created_by as a_created_by, application.created_time as created_time, application.last_modified_time as a_last_modified_time, application.last_modified_by as a_last_modified_by, "
            + " document.id as d_id, document.filestore_id as d_filestore_id, document.document_type as d_document_type, document.document_uid as d_document_uid, document.status as d_status, "
            + " document.application_id as d_application_id, document.additional_details as d_additional_details "

            + "FROM eg_ubp_application application "
            + "INNER JOIN eg_ubp_application_documents document ON application.id = document.application_id ";

    private final Configuration configuration;

    @Autowired
    public ApplicationQueryBuilder(Configuration configuration) {
        this.configuration = configuration;
    }

    public String getApplicationQuery(ApplicationSearchRequest applicationSearchRequest, List<Object> preparedStmtList) {

        ApplicationSearchCriteria criteria= applicationSearchRequest.getCriteria();
        StringBuilder query = new StringBuilder(APPLICATION_QUERY);

        Set<String> applicationNumbers = criteria.getApplicationNumbers();
        if(!CollectionUtils.isEmpty(applicationNumbers)) {
            addClauseIfRequired(query, preparedStmtList);
            query.append(" application.application_number IN (").append(createQuery(applicationNumbers)).append(")");
            addToPreparedStatement(preparedStmtList, applicationNumbers);
        }

        Set<String> ids = criteria.getIds();
        if (!CollectionUtils.isEmpty(ids)) {
            addClauseIfRequired(query, preparedStmtList);
            query.append(" application.id IN (").append(createQuery(ids)).append(")");
            addToPreparedStatement(preparedStmtList, ids);
        }

        if (StringUtils.isNotBlank(criteria.getTenantId())) {
            addClauseIfRequired(query, preparedStmtList);
            query.append(" application.tenant_id = ? ");
            preparedStmtList.add(criteria.getTenantId());
        }

        if (StringUtils.isNotBlank(criteria.getProgramCode())) {
            addClauseIfRequired(query, preparedStmtList);
            query.append(" application.program_code = ? ");
            preparedStmtList.add(criteria.getProgramCode());
        }

        if (criteria.getStatus() != null) {
            addClauseIfRequired(query, preparedStmtList);
            query.append(" application.status = ? ");
            preparedStmtList.add(criteria.getStatus().toString());
        }

        return addPaginationWrapper(query, applicationSearchRequest.getPagination(), preparedStmtList);
    }

    private String addOrderByClause(Pagination pagination) {

        String paginationWrapper = "SELECT * FROM " +
                "(SELECT *, DENSE_RANK() OVER (ORDER BY {sortBy} {orderBy}) offset_ FROM " +
                "({})" +
                " result) result_offset " +
                "WHERE offset_ > ? AND offset_ <= ?";

        if ( !StringUtils.isEmpty(pagination.getSortBy()) && ServiceConstants.SORTABLE_APPLICATION_COLUMNS.contains(pagination.getSortBy())) {
            paginationWrapper=paginationWrapper.replace("{sortBy}", pagination.getSortBy());
        }
        else{
            paginationWrapper=paginationWrapper.replace("{sortBy}", "created_time");
        }

        if (pagination.getOrder() != null && Order.fromValue(pagination.getOrder().toString()) != null) {
            paginationWrapper=paginationWrapper.replace("{orderBy}", pagination.getOrder().name());
        }
        else{
            paginationWrapper=paginationWrapper.replace("{orderBy}", Order.DESC.name());
        }

        return paginationWrapper;
    }

    private String addPaginationWrapper(StringBuilder query, Pagination pagination, List<Object> preparedStmtList){

        String paginatedQuery = addOrderByClause(pagination);

        int limit = null != pagination.getLimit() ? pagination.getLimit() : configuration.getDefaultLimit();
        int offset = null != pagination.getOffSet() ? pagination.getOffSet() : configuration.getDefaultOffset();

        String finalQuery = paginatedQuery.replace("{}", query);

        preparedStmtList.add(offset);
        preparedStmtList.add(limit + offset);

        return finalQuery;
    }

    private String createQuery(Collection<String> ids) {
        StringBuilder builder = new StringBuilder();
        int length = ids.size();
        for (int i = 0; i < length; i++) {
            builder.append(" ? ");
            if (i != length - 1) builder.append(",");
        }
        return builder.toString();
    }

    private void addClauseIfRequired(StringBuilder query, List<Object> preparedStmtList) {
        if (preparedStmtList.isEmpty()) {
            query.append(" WHERE ");
        } else {
            query.append(" AND ");
        }
    }

    private void addToPreparedStatement(List<Object> preparedStmtList, Collection<String> ids) {
        preparedStmtList.addAll(ids);
    }
}
