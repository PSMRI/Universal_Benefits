package digit.application.validators;

import digit.application.repository.ApplicationRepository;
import digit.application.web.models.ApplicationRequest;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
public class BPApplicationValidator {

    @Autowired
    private ApplicationRepository repository;

    public void validateBPApplication(ApplicationRequest applicationRequest, List<MultipartFile> files) {
        if(ObjectUtils.isEmpty(applicationRequest.getApplication().getTenantId()))
                throw new RuntimeException("tenantId is mandatory for applications");

    }

    public void validateBPUpdateApplication(ApplicationRequest applicationRequest) {
        if(ObjectUtils.isEmpty(applicationRequest.getApplication().getId()))
            throw new RuntimeException("Id is mandatory for updating applications");

        boolean appExists = repository.doesApplicationExist(applicationRequest.getApplication().getId());

        if(!appExists)
            throw new RuntimeException("Application for Id not found");
    }
}
