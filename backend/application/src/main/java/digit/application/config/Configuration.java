package digit.application.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import org.egov.tracer.config.TracerConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

import java.util.TimeZone;

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

    @Value("${egov.fileStore.host}")
    private String fileStoreHost;

    @Value("${egov.fileStore.path}")
    private String fileStorePath;

    @Value("${egov.idgen.application.format}")
    private String idGenApplicationFormat;

    @Value("${moduleName}")
    private String moduleName;


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

    @Value("${egov.workflow.application.module.name}")
    private String wfApplicationModuleName;
    @Value("${egov.workflow.application.business_service}")
    private String wfApplicationBusinessService;


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
    @Value("${kafka.topic.application.create}")
    private String kafkaTopicApplicationCreate;

    @Value("${kafka.topic.application.update}")
    private String kafkaTopicApplicationUpdate;

     @Value("${kafka.topic.application.updatestatus}")
    private String kafkaTopicApplicationUpdateStatus;
    // application search configs
    @Value("${application.search.default.limit}")
    private Integer defaultLimit;

    @Value("${application.search.default.offset}")
    private Integer defaultOffset;

    @Value("${application.search.max.limit}")
    private Integer maxSearchLimit;
}
