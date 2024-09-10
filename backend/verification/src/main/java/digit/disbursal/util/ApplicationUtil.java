package digit.disbursal.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import digit.disbursal.config.Configuration;
import digit.disbursal.repository.ServiceRequestRepository;
import digit.disbursal.web.models.ApplicationRequest;
import digit.disbursal.web.models.ApplicationResponse;
import org.springframework.stereotype.Component;

@Component
public class ApplicationUtil {

    private final Configuration configs;
    private final ServiceRequestRepository restRepo;
    private final ObjectMapper mapper;


    public ApplicationUtil(Configuration configs, ServiceRequestRepository restRepo, ObjectMapper mapper) {
        this.configs = configs;
        this.restRepo = restRepo;
        this.mapper = mapper;
    }

    public ApplicationResponse updateApplication(ApplicationRequest request) {
        StringBuilder uri = new StringBuilder(configs.getUbpApplicationHost()).append(configs.getUbpApplicationUpdatePath());
        ApplicationResponse response = mapper.convertValue(restRepo.fetchResult(uri, request), ApplicationResponse.class);
        return response;
    }

}
