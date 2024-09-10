package digit.disbursal.config;

import lombok.*;
import org.egov.tracer.config.TracerConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

@Component
@Data
@Import({TracerConfiguration.class})
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Configuration {


    //Idgen Config
    @Value("${egov.idgen.host}")
    private String idGenHost;

    @Value("${egov.idgen.path}")
    private String idGenPath;

    @Value("${egov.idgen.disbursal.format}")
    private String idGenDisbursalFormat;


    //Workflow Config
    @Value("${egov.workflow.host}")
    private String wfHost;

    @Value("${is.workflow.enabled}")
    private Boolean isWorkflowEnabled;

    @Value("${egov.workflow.transition.path}")
    private String wfTransitionPath;

    @Value("${egov.workflow.businessservice.search.path}")
    private String wfBusinessServiceSearchPath;

    @Value("${egov.workflow.processinstance.search.path}")
    private String wfProcessInstanceSearchPath;

    //MDMS
    @Value("${egov.mdms.host}")
    private String mdmsHost;

    @Value("${egov.mdms.search.endpoint}")
    private String mdmsEndPoint;

    //URLShortening
    @Value("${egov.url.shortner.host}")
    private String urlShortnerHost;

    @Value("${egov.url.shortner.endpoint}")
    private String urlShortnerEndpoint;


    //SMSNotification
    @Value("${egov.sms.notification.topic}")
    private String smsNotificationTopic;

    // KAFKA Topics
    @Value("${kafka.topic.disbursal.create}")
    private String kafkaTopicDisbursalCreate;

    @Value("${kafka.topic.disbursal.update}")
    private String kafkaTopicDisbursalUpdate;

    @Value("${kafka.topic.application.update}")
    private String kafkaTopicApplicationUpdate;

    @Value("${egov.ubp.application.host}")
    private String ubpApplicationHost;

    @Value("${egov.ubp.application.search.path}")
    private String ubpApplicationSearchPath;


    // application search configs
    @Value("${disbursal.search.default.limit}")
    private Integer defaultLimit;

    @Value("${disbursal.search.default.offset}")
    private Integer defaultOffset;

    @Value("${disbursal.search.max.limit}")
    private Integer maxSearchLimit;
}
