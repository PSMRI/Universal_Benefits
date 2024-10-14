package digit.application.validators;

import digit.application.repository.ApplicationRepository;
import digit.application.web.models.ApplicationRequest;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
public class BPApplicationValidator {

    @Autowired
    private ApplicationRepository repository;

    public void validateBPApplication(ApplicationRequest applicationRequest) {
        if(ObjectUtils.isEmpty(applicationRequest.getApplication().getTenantId()))
                throw new CustomException("EG_BT_APP_ERR", "tenantId is mandatory for applications");
    }
}
