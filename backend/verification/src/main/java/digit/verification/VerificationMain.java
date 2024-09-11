package digit.verification;


import org.egov.tracer.config.TracerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@Import({TracerConfiguration.class})
@SpringBootApplication
@ComponentScan(basePackages = {"digit.verification", "digit.verification.web.controllers", "digit.verification.config"})
public class VerificationMain {


    public static void main(String[] args) throws Exception {
        SpringApplication.run(VerificationMain.class, args);
    }

}
