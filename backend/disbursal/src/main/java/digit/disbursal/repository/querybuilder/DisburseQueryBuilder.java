package digit.disbursal.repository.querybuilder;


import digit.disbursal.config.Configuration;
import digit.disbursal.config.ServiceConstants;
import digit.disbursal.web.models.DisbursalSearchCriteria;
import digit.disbursal.web.models.DisbursalSearchRequest;
import digit.disbursal.web.models.Pagination;
import digit.disbursal.web.models.enums.Order;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Component
public class DisburseQueryBuilder {

    private final String DISBURSAL_QUERY = "SELECT "

            + " disbursal.id as d_id, disbursal.tenant_id as d_tenantid, disbursal.disbursal_number as d_disbursal_number, disbursal.reference_id as d_reference_id, "
            + " disbursal.total_amount as d_total_amount, disbursal.total_paid_amount as d_total_paid_amount, disbursal.payment_status as d_payment_status, disbursal.status as d_status, disbursal.wf_status as d_wf_status, disbursal.additional_details as d_additional_details, "
            + " disbursal.created_by as d_created_by, disbursal.created_time as created_time, disbursal.last_modified_time as d_last_modified_time, disbursal.last_modified_by as d_last_modified_by "
            + "FROM eg_ubp_disbursal disbursal ";

    private final Configuration configuration;

    @Autowired
    public DisburseQueryBuilder(Configuration configuration) {
        this.configuration = configuration;
    }

    public String getDisburseQuery(DisbursalSearchRequest disbursalSearchRequest, List<Object> preparedStmtList) {

        DisbursalSearchCriteria criteria= disbursalSearchRequest.getCriteria();
        StringBuilder query = new StringBuilder(DISBURSAL_QUERY);

        Set<String> disbursalNumbers = criteria.getDisbursalNumbers();
        if(!CollectionUtils.isEmpty(disbursalNumbers)) {
            addClauseIfRequired(query, preparedStmtList);
            query.append(" disbursal.disbursal_number IN (").append(createQuery(disbursalNumbers)).append(")");
            addToPreparedStatement(preparedStmtList, disbursalNumbers);
        }

        Set<String> ids = criteria.getIds();
        if (!CollectionUtils.isEmpty(ids)) {
            addClauseIfRequired(query, preparedStmtList);
            query.append(" disbursal.id IN (").append(createQuery(ids)).append(")");
            addToPreparedStatement(preparedStmtList, ids);
        }

        if (StringUtils.isNotBlank(criteria.getTenantId())) {
            addClauseIfRequired(query, preparedStmtList);
            query.append(" disbursal.tenant_id = ? ");
            preparedStmtList.add(criteria.getTenantId());
        }

        if (StringUtils.isNotBlank(criteria.getReferenceId())) {
            addClauseIfRequired(query, preparedStmtList);
            query.append(" disbursal.reference_id = ? ");
            preparedStmtList.add(criteria.getReferenceId());
        }

        if (criteria.getStatus() != null) {
            addClauseIfRequired(query, preparedStmtList);
            query.append(" disbursal.status = ? ");
            preparedStmtList.add(criteria.getStatus().toString());
        }
        if (criteria.getIsPaymentStatusNull() != null && criteria.getIsPaymentStatusNull().equals(true)) {
            addClauseIfRequired(query, preparedStmtList);
            query.append(" disbursal.payment_status IS NULL");

        }

        return addPaginationWrapper(query, disbursalSearchRequest.getPagination(), preparedStmtList);
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
