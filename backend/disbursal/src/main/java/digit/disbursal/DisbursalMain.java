package digit.disbursal;


import org.egov.tracer.config.TracerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@Import({TracerConfiguration.class})
@SpringBootApplication
@ComponentScan(basePackages = {"digit.disbursal", "digit.disbursal.web.controllers", "digit.disbursal.config"})
public class DisbursalMain {


    public static void main(String[] args) throws Exception {
        SpringApplication.run(DisbursalMain.class, args);
    }

}
