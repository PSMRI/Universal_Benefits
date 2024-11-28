package digit.service;

import digit.config.Configuration;
import digit.enrichment.BenefitApplicationEnrichment;
import digit.kafka.Producer;
import digit.repository.BenefitRepository;
import digit.util.ResponseInfoFactory;
import digit.validators.BenefitsApplicationValidator;
import digit.web.models.*;
import digit.web.models.Error;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    
    private  final  BenefitRepository benefitRepository;
//    private  ExtensionDateObjets Edo;

    @Autowired
    public BenefitsService(BenefitsApplicationValidator benefitsApplicationValidator, BenefitApplicationEnrichment benefitApplicationEnrichment, ResponseInfoFactory responseInfoFactory, Producer producer,Configuration configuration,BenefitRepository benefitRepository) {
        this.benefitsApplicationValidator = benefitsApplicationValidator;
        this.benefitApplicationEnrichment = benefitApplicationEnrichment;
        this.responseInfoFactory = responseInfoFactory;
        this.producer = producer;
        this.configuration = configuration;
		this.benefitRepository = benefitRepository;
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
        System.out.println("Req body 1 in service "+request);
        System.out.println("application end deadline date "+request.getBenefit().getOtherTermsAndConditions().getApplicationDeadlineDate());
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
    
    public ResponseEntity<?> extendBenifitDate(BenifitDateExtendRequest request){
    	BenifitDateExtendResponse<?> response = null;
        HttpStatus returnStatus = HttpStatus.OK;
        String BenefitId=request.getBenefitId();
        System.out.println("Benifit id is "+BenefitId);
        System.out.println("New deadline is "+request.getExtensionDate());
        System.out.println("body is "+request);
        
        Benefit benefitbyid=  benefitRepository.getById(BenefitId);
        Date currentDeadline= benefitbyid.getOtherTermsAndConditions().getApplicationDeadlineDate();
        Date newDeadline=request.getExtensionDate();
        String benefitId=request.getBenefitId();
        ExtensionDateObjets Edo = new ExtensionDateObjets();
        try {
        		
        	Edo.setCurrentDeadline(currentDeadline);
        	Edo.setNewDeadline(newDeadline);
        	Edo.setBenefitId(benefitId);
        	
		} catch (Exception e) {
			e.getMessage();
		}
        System.out.println("edo is "+Edo);
        System.out.println("Current deadline is "+currentDeadline);
        System.out.println("New deadline is "+request.getExtensionDate());
        System.out.println("body is "+request);
//        Map<String, Object> deadlineInfo = new HashMap<>();
//        deadlineInfo.put("benefitId", benefitId);
//        deadlineInfo.put("currentDate", currnetDeadline);
//        deadlineInfo.put("extensionDate", newDeadline);
        
//        System.out.println("Map object for deadline info "+Edo);
        
        /*
        String SimpleNewDate=request.getExtensionDate();
        LocalDate date = LocalDate.parse(SimpleNewDate);

        // Convert LocalDate to LocalDateTime with time set to 00:00:00
        LocalDateTime dateTime = date.atStartOfDay();

        // Format the LocalDateTime to the desired string format
        DateTimeFormatter formattedDateToStore = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");   
        */  
//        request.setExtensionDate(formattedDateToStore.toString());
        producer.push(configuration.getExtendbenefitdateTopic(), request);
        producer.push(configuration.getExtendbenefitdateTopicInExtensionTable(), Edo);
//        log.info("Benefit pushed for update successfully: " + request.getBenefit());

        response = BenifitDateExtendResponse.builder()
                .
            responseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(request.getRequestInfo(),true)).build();
                
        returnStatus = HttpStatus.OK;
        
        return new ResponseEntity<>(response, returnStatus);
        /*
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
                }
                else{
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
        */
        
    }
    
}
