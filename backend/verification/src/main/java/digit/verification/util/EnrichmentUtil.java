package digit.verification.util;

import digit.verification.config.Configuration;
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
