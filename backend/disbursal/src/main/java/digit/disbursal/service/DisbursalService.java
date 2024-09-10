package digit.disbursal.service;

import digit.disbursal.config.Configuration;
import digit.disbursal.kafka.Producer;
import digit.disbursal.repository.DisbursalRepository;
import digit.disbursal.util.DisbursalUtil;
import digit.disbursal.util.EnrichmentUtil;
import digit.disbursal.util.ResponseInfoFactory;
import digit.disbursal.util.WorkflowUtil;
import digit.disbursal.web.models.*;
import digit.disbursal.web.models.enums.Status;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.response.ResponseInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class DisbursalService {

    private final EnrichmentUtil enrichmentUtil;
    private final WorkflowUtil workflowUtil;
    private final Configuration configuration;
    private final Producer producer;
    private final ResponseInfoFactory responseInfoFactory;
    private final DisbursalUtil disbursalUtil;
    private final DisbursalRepository disbursalRepository;

    @Autowired
    public DisbursalService(EnrichmentUtil enrichmentUtil, WorkflowUtil workflowUtil, Configuration configuration, Producer producer, ResponseInfoFactory responseInfoFactory, DisbursalUtil disbursalUtil, DisbursalRepository disbursalRepository) {
        this.enrichmentUtil = enrichmentUtil;
        this.workflowUtil = workflowUtil;
        this.configuration = configuration;
        this.producer = producer;
        this.responseInfoFactory = responseInfoFactory;
        this.disbursalUtil = disbursalUtil;
        this.disbursalRepository = disbursalRepository;
    }


    public DisbursalResponse create(DisbursalRequest disbursalRequest) {
        Disbursal disbursal = disbursalRequest.getDisbursal();
        DisbursalResponse response = null;

        // TODO: Do custom validations here
        // For example: programCode, individualId, filestoreIds should be valid
        // TODO: Get application details
        // TODO: Get program details based
        // TODO: Set total amount same as programs has
        disbursal.setTotalAmount(BigDecimal.valueOf(5000));
        this.enrichmentUtil.enrichDisbursalForCreation(disbursalRequest);

        log.info("Creating disbursal: " + disbursal);

//        if (this.configuration.getIsWorkflowEnabled()) {
//            State wfState = workflowUtil.callWorkFlow(workflowUtil.prepareWorkflowRequestForApplication(disbursalRequest));
//            disbursal.setWfStatus(wfState.getApplicationStatus());
//        }
        disbursal.setStatus(Status.ACTIVE);
        // TODO: Remove after creating the workflow
        disbursal.setWfStatus("APPROVED");
        producer.push(configuration.getKafkaTopicDisbursalCreate(), disbursalRequest);
        response = DisbursalResponse.builder()
                .disbursals(Arrays.asList(disbursalRequest.getDisbursal()))
                .responseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(disbursalRequest.getRequestInfo(), true))
                .build();

        return response;
    }

    /**
     * Updates an existing application
     * @param disbursalRequest
     * @return ApplicationResponse with the updated application
     */
    public DisbursalResponse update(DisbursalRequest disbursalRequest) {

        Disbursal disbursal = disbursalRequest.getDisbursal();
        DisbursalResponse response = null;
        // TODO: Write business logic to update the application
        producer.push(configuration.getKafkaTopicDisbursalUpdate(), disbursalRequest);
        response = DisbursalResponse.builder()
                .disbursals(Arrays.asList(disbursalRequest.getDisbursal()))
                .responseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(disbursalRequest.getRequestInfo(), true))
                .build();

        return response;
    }

    /**
     * method to search applications from DB based on disbursal search criteria
     * @param disbursalSearchRequest
     * @return
     */
    public DisbursalResponse search(DisbursalSearchRequest disbursalSearchRequest) {

        DisbursalSearchCriteria disbursalCriteria = disbursalSearchRequest.getCriteria();

        // TODO: Add validations here

        log.info("Enrich disbursalCriteria");
        enrichmentUtil.enrichSearchDisbursalRequest(disbursalSearchRequest);

        log.info("Search repository using disbursalCriteria");
        List<Disbursal> disbursals = disbursalRepository.search(disbursalSearchRequest);

        ResponseInfo responseInfo = responseInfoFactory.
                createResponseInfoFromRequestInfo(disbursalSearchRequest.getRequestInfo(),true);

        return DisbursalResponse.builder()
                .disbursals(disbursals)
                .pagination(disbursalSearchRequest.getPagination())
                .responseInfo(responseInfo)
                .build();
    }
}