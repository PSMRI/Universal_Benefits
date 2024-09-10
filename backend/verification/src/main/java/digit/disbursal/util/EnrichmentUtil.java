package digit.disbursal.util;

import digit.disbursal.config.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EnrichmentUtil {

    private final Configuration configuration;

    @Autowired
    private EnrichmentUtil(Configuration configuration) {
        this.configuration = configuration;

    }




}
