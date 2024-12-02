package digit.application;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.egov.tracer.config.TracerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@Import({TracerConfiguration.class})
@EnableScheduling
@SpringBootApplication
@ComponentScan(basePackages = {"digit.application", "digit.application.web.controllers", "digit.application.config"})
public class ApplicationMain {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(ApplicationMain.class, args);
    }
}
