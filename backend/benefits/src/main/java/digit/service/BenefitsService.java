package digit.service;

import digit.config.Configuration;
import digit.enrichment.BenefitApplicationEnrichment;
import digit.kafka.Producer;
import digit.util.ResponseInfoFactory;
import digit.validators.BenefitsApplicationValidator;
import digit.web.models.*;
import digit.web.models.Error;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

import static digit.config.ServiceConstants.EXTERNAL_SERVICE_EXCEPTION;

@Service
@Slf4j
public class BenefitsService {

    private final BenefitsApplicationValidator benefitsApplicationValidator;

    private final BenefitApplicationEnrichment benefitApplicationEnrichment;

    private final ResponseInfoFactory responseInfoFactory;

    private final Producer producer;

    private final Configuration configuration;

    @Autowired
    public BenefitsService(BenefitsApplicationValidator benefitsApplicationValidator, BenefitApplicationEnrichment benefitApplicationEnrichment, ResponseInfoFactory responseInfoFactory, Producer producer,Configuration configuration) {
        this.benefitsApplicationValidator = benefitsApplicationValidator;
        this.benefitApplicationEnrichment = benefitApplicationEnrichment;
        this.responseInfoFactory = responseInfoFactory;
        this.producer = producer;
        this.configuration = configuration;
    }

    public ResponseEntity<?> createBenefit(BenefitsRegistrationRequest request){
        BenefitsRegistrationResponse<?> response = null;
        HttpStatus returnStatus = HttpStatus.OK;
        try{
            ValidationResult validationResult = benefitsApplicationValidator.validateCreateBenefitsApplication(request.getBenefit());
            if(validationResult.isValid()){
                request = benefitApplicationEnrichment.enrichCreateBenefitApplication(request);

                log.info("Adding Benefit: " + request.getBenefit());

                producer.push(configuration.getCreateTopic(), request);
                log.info("Benefit pushed successfully: " + request.getBenefit());

                response = BenefitsRegistrationResponse.builder()
                        .message("Benefit added successfully.")
                        .data(request.getBenefit())
                        .responseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(request.getRequestInfo(), true))
                        .build();
                returnStatus = HttpStatus.OK;
            }else{
                List<Error> errors = new ArrayList<>();

                for (MessageObj message : validationResult.getMessages()) {
                    errors.add(Error.builder().code(message.getFieldName()).message(message.getMessage()).build());
                }

                ErrorResponse errorResponse = ErrorResponse.builder()
                        .errors(errors)
                        .responseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(request.getRequestInfo(), false))
                        .build();

                returnStatus = HttpStatus.BAD_REQUEST;

                return new ResponseEntity<>(errorResponse, returnStatus);
            }
        } catch (Exception e) {
            log.error(EXTERNAL_SERVICE_EXCEPTION, e);
            List<Error> errors = new ArrayList<>();
            errors.add(Error.builder().code("Internal Error").message("Error while creating benefit scheme").build());

            ErrorResponse errorResponse = ErrorResponse.builder()
                    .errors(errors)
                    .responseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(request.getRequestInfo(), false))
                    .build();

            returnStatus = HttpStatus.INTERNAL_SERVER_ERROR;

            return new ResponseEntity<>(errorResponse, returnStatus);
        }
        return new ResponseEntity<>(response, returnStatus);
    }

    public ResponseEntity<?> updateBenefit(BenefitsRegistrationRequest request){
        BenefitsRegistrationResponse<?> response = null;
        HttpStatus returnStatus = HttpStatus.OK;
        try{
            ValidationResult validationResult = new ValidationResult();

            if(Objects.isNull(request.getBenefit().getEligibility()) && Objects.isNull(request.getBenefit().getFinancialInformation())
            && Objects.isNull(request.getBenefit().getOtherTermsAndConditions())){
                validationResult = benefitsApplicationValidator.validateBenefitsMainDetailsOnly(request.getBenefit());

                if(validationResult.isValid()){
                    request = benefitApplicationEnrichment.enrichUpdateBenefitApplication(request);

                    log.info("Updating Benefit: " + request.getBenefit());

                    producer.push(configuration.getUpdateDetailsTopic(), request);
                    log.info("Benefit pushed for update successfully: " + request.getBenefit());

                    response = BenefitsRegistrationResponse.builder()
                            .message("Benefit updated successfully.")
                            .data(request.getBenefit())
                            .responseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(request.getRequestInfo(), true))
                            .build();
                    returnStatus = HttpStatus.OK;
                }else{
                    List<Error> errors = new ArrayList<>();

                    for (MessageObj message : validationResult.getMessages()) {
                        errors.add(Error.builder().code(message.getFieldName()).message(message.getMessage()).build());
                    }

                    ErrorResponse errorResponse = ErrorResponse.builder()
                            .errors(errors)
                            .responseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(request.getRequestInfo(), false))
                            .build();

                    returnStatus = HttpStatus.BAD_REQUEST;

                    return new ResponseEntity<>(errorResponse, returnStatus);
                }
            }

            if(!Objects.isNull(request.getBenefit().getEligibility())){
                validationResult = benefitsApplicationValidator.validateEligibilityDetails(request.getBenefit());
                if(validationResult.isValid()){
                    request = benefitApplicationEnrichment.enrichUpdateBenefitApplication(request);

                    log.info("Updating Benefit: " + request.getBenefit());

                    producer.push(configuration.getUpdateEligibilityTopic(), request);
                    log.info("Benefit pushed for update successfully: " + request.getBenefit());

                    response = BenefitsRegistrationResponse.builder()
                            .message("Benefit updated successfully.")
                            .data(request.getBenefit())
                            .responseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(request.getRequestInfo(), true))
                            .build();
                    returnStatus = HttpStatus.OK;
                }else{
                    List<Error> errors = new ArrayList<>();

                    for (MessageObj message : validationResult.getMessages()) {
                        errors.add(Error.builder().code(message.getFieldName()).message(message.getMessage()).build());
                    }

                    ErrorResponse errorResponse = ErrorResponse.builder()
                            .errors(errors)
                            .responseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(request.getRequestInfo(), false))
                            .build();

                    returnStatus = HttpStatus.BAD_REQUEST;

                    return new ResponseEntity<>(errorResponse, returnStatus);
                }
            }

            if(!Objects.isNull(request.getBenefit().getFinancialInformation())){
                validationResult = benefitsApplicationValidator.validateFinancialInformationDetails(request.getBenefit());

                if(validationResult.isValid()){
                    request = benefitApplicationEnrichment.enrichUpdateBenefitApplication(request);

                    log.info("Updating Benefit: " + request.getBenefit());

                    producer.push(configuration.getUpdateFinInfoTopic(), request);
                    log.info("Benefit pushed for update successfully: " + request.getBenefit());

                    response = BenefitsRegistrationResponse.builder()
                            .message("Benefit updated successfully.")
                            .data(request.getBenefit())
                            .responseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(request.getRequestInfo(), true))
                            .build();
                    returnStatus = HttpStatus.OK;
                }else{
                    List<Error> errors = new ArrayList<>();

                    for (MessageObj message : validationResult.getMessages()) {
                        errors.add(Error.builder().code(message.getFieldName()).message(message.getMessage()).build());
                    }

                    ErrorResponse errorResponse = ErrorResponse.builder()
                            .errors(errors)
                            .responseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(request.getRequestInfo(), false))
                            .build();

                    returnStatus = HttpStatus.BAD_REQUEST;

                    return new ResponseEntity<>(errorResponse, returnStatus);
                }
            }

            if(!Objects.isNull(request.getBenefit().getOtherTermsAndConditions())){
                validationResult = benefitsApplicationValidator.validateTermsandConditionDetails(request.getBenefit());

                if(validationResult.isValid()){
                    request = benefitApplicationEnrichment.enrichUpdateBenefitApplication(request);

                    log.info("Updating Benefit: " + request.getBenefit());

                    producer.push(configuration.getUpdateTermsTopic(), request);
                    log.info("Benefit pushed for update successfully: " + request.getBenefit());

                    response = BenefitsRegistrationResponse.builder()
                            .message("Benefit updated successfully.")
                            .data(request.getBenefit())
                            .responseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(request.getRequestInfo(), true))
                            .build();
                    returnStatus = HttpStatus.OK;
                }else{
                    List<Error> errors = new ArrayList<>();

                    for (MessageObj message : validationResult.getMessages()) {
                        errors.add(Error.builder().code(message.getFieldName()).message(message.getMessage()).build());
                    }

                    ErrorResponse errorResponse = ErrorResponse.builder()
                            .errors(errors)
                            .responseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(request.getRequestInfo(), false))
                            .build();

                    returnStatus = HttpStatus.BAD_REQUEST;

                    return new ResponseEntity<>(errorResponse, returnStatus);
                }
            }
        } catch (Exception e) {
            List<Error> errors = new ArrayList<>();
            errors.add(Error.builder().code("Internal Error").message("Error while updating benefit scheme").build());

            ErrorResponse errorResponse = ErrorResponse.builder()
                    .errors(errors)
                    .responseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(request.getRequestInfo(), false))
                    .build();

            returnStatus = HttpStatus.INTERNAL_SERVER_ERROR;

            return new ResponseEntity<>(errorResponse, returnStatus);
        }
        return new ResponseEntity<>(response, returnStatus);
    }
}
