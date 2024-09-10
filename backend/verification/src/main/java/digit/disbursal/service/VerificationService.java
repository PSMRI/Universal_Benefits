package digit.disbursal.service;

import digit.disbursal.config.Configuration;
import digit.disbursal.kafka.Producer;
import digit.disbursal.util.ApplicationUtil;
import digit.disbursal.util.EnrichmentUtil;
import digit.disbursal.util.ResponseInfoFactory;
import digit.disbursal.util.WorkflowUtil;
import digit.disbursal.web.models.*;
import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class VerificationService {

    private final EnrichmentUtil enrichmentUtil;
    private final WorkflowUtil workflowUtil;
    private final Configuration configuration;
    private final Producer producer;
    private final ResponseInfoFactory responseInfoFactory;
    private final ApplicationUtil applicationUtil;

    @Autowired
    public VerificationService(EnrichmentUtil enrichmentUtil, WorkflowUtil workflowUtil, Configuration configuration, Producer producer, ResponseInfoFactory responseInfoFactory, ApplicationUtil applicationUtil) {
        this.enrichmentUtil = enrichmentUtil;
        this.workflowUtil = workflowUtil;
        this.configuration = configuration;
        this.producer = producer;
        this.responseInfoFactory = responseInfoFactory;
        this.applicationUtil = applicationUtil;
    }

    public ApplicationResponse verifyApplication(ApplicationRequest applicationRequest) {
        Application application = applicationRequest.getApplication();

        if (application.getWfStatus() != null && application.getWfStatus().equals("PENDING_FOR_VERIFICATION")) {
            ApplicationResponse response = null;

            // TODO: Get the program details
            // Validate the documents

            // TODO: Remove after creating the workflow
            application.setWfStatus("APPROVED");

            return applicationUtil.updateApplication(applicationRequest);
        }
        else {
            throw new CustomException("APPLICATION_ALREADY_VERIFIED", "Application workflow status is not valid for verification");
        }
    }
}