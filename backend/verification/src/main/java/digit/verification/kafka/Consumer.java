package digit.verification.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import digit.verification.service.VerificationService;
import digit.verification.web.models.ApplicationRequest;
import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Consumer {

    private final ObjectMapper mapper;

    private final VerificationService verificationService;

    @Autowired
    public Consumer(ObjectMapper mapper, VerificationService verificationService) {
        this.mapper = mapper;
        this.verificationService = verificationService;
    }

    /*
     * Uncomment the below line to start consuming record from kafka.topics.consumer
     * Value of the variable kafka.topics.consumer should be overwritten in application.properties
     */
    @KafkaListener(topics = {"${kafka.topic.application.create}"})
    public void listen(final String message, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {

        log.info("Consumer::listen");
        try {
            ApplicationRequest applicationRequest = mapper.readValue(message, ApplicationRequest.class);
            if (applicationRequest != null && applicationRequest.getApplication() != null && applicationRequest.getRequestInfo() != null) {
                verificationService.verifyApplication(applicationRequest);
            } else {
                log.info("No application request found for the received message : " + message);
            }
        } catch (Exception e) {
            log.error("Error occurred while processing the consumed save estimate record from topic : " + topic, e);
            throw new CustomException("CONSUMER_ERROR", "Error occurred while processing the consumed save estimate record from topic : " + topic);
        }

    }
}
