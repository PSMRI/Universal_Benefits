package digit.application.service;

import digit.application.config.Configuration;
import digit.application.kafka.Producer;
import digit.application.repository.ApplicationRepository;
import digit.application.util.EnrichmentUtil;
import digit.application.util.ResponseInfoFactory;
import digit.application.util.WorkflowUtil;
import digit.application.web.models.*;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.common.contract.workflow.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class ApplicationService {

    private final EnrichmentUtil enrichmentUtil;
    private final WorkflowUtil workflowUtil;
    private final Configuration configuration;
    private final Producer producer;
    private final ResponseInfoFactory responseInfoFactory;
    private final ApplicationRepository applicationRepository;

    @Autowired
    public ApplicationService(EnrichmentUtil enrichmentUtil, WorkflowUtil workflowUtil, Configuration configuration, Producer producer, ResponseInfoFactory responseInfoFactory, ApplicationRepository applicationRepository) {
        this.enrichmentUtil = enrichmentUtil;
        this.workflowUtil = workflowUtil;
        this.configuration = configuration;
        this.producer = producer;
        this.responseInfoFactory = responseInfoFactory;
        this.applicationRepository = applicationRepository;
    }

    public ApplicationResponse create(ApplicationRequest applicationRequest) {
        Application application = applicationRequest.getApplication();
        ApplicationResponse response = null;

        // TODO: Do custom validations here
        // For example: programCode, individualId, filestoreIds should be valid
        this.enrichmentUtil.enrichApplicationForCreation(applicationRequest);

        log.info("Creating Application: " + application);

        log.info("Application created successfully: " + application);
        if (this.configuration.getIsWorkflowEnabled()) {
            State wfState = workflowUtil.callWorkFlow(workflowUtil.prepareWorkflowRequestForApplication(applicationRequest));
            application.setWfStatus(wfState.getApplicationStatus());
        }
        application.setStatus(Application.StatusEnum.ACTIVE);
        // TODO: Remove after creating the workflow
        application.setWfStatus("PENDING_FOR_VERIFICATION");
        producer.push(configuration.getKafkaTopicApplicationCreate(), applicationRequest);
        response = ApplicationResponse.builder()
                .applications(Arrays.asList(applicationRequest.getApplication()))
                .responseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(applicationRequest.getRequestInfo(), true))
                .build();

        return response;
    }

    /**
     * Updates an existing application
     * @param applicationRequest
     * @return ApplicationResponse with the updated application
     */
    public ApplicationResponse update(ApplicationRequest applicationRequest) {

        Application application = applicationRequest.getApplication();
        ApplicationResponse response = null;
        // TODO: Write business logic to update the application
        producer.push(configuration.getKafkaTopicApplicationUpdate(), applicationRequest);
        response = ApplicationResponse.builder()
                .applications(Arrays.asList(applicationRequest.getApplication()))
                .responseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(applicationRequest.getRequestInfo(), true))
                .build();

        return response;
    }

    /**
     * method to search applications from DB based on application search criteria
     * @param applicationSearchRequest
     * @return
     */
    public ApplicationResponse search(ApplicationSearchRequest applicationSearchRequest) {

        ApplicationSearchCriteria applicationSearchCriteria = applicationSearchRequest.getCriteria();

        // TODO: Add validations here

        log.info("Enrich disbursalCriteria");
        enrichmentUtil.enrichSearchApplicationRequest(applicationSearchRequest);

        log.info("Search repository using applicationCriteria");
        List<Application> applications = applicationRepository.search(applicationSearchRequest);

        ResponseInfo responseInfo = responseInfoFactory.
                createResponseInfoFromRequestInfo(applicationSearchRequest.getRequestInfo(),true);

        return ApplicationResponse.builder()
                .applications(applications)
                .pagination(applicationSearchRequest.getPagination())
                .responseInfo(responseInfo)
                .build();
    }


public ApplicationStatusUpdateResponse updateApplicationStatus(ApplicationStatusUpdateRequest applicationRequest) {

    	String applicationid = applicationRequest.getApplicationId();
    	System.out.println("applicationid is =========>"+applicationid);	
    	String status=applicationRequest.getStatus();  
    	System.out.println("applicationid status is =========>"+status);
    	Application.WFStatusEnum statusEnum = Application.WFStatusEnum.fromValue(status);
        ApplicationStatusUpdateResponse response = null;
//        Application a=getApplicationById(applicationid);
//        if(getApplicationById(applicationid)!=null)		
//        {
//        	a.setStatus(statusEnum);
//        }
        producer.push(configuration.getKafkaTopicApplicationUpdateStatus(), applicationRequest);
        response = ApplicationStatusUpdateResponse.builder().responseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(applicationRequest.getRequestInfo(),true)).build();
        return response;
    }
    
}
