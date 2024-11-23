package digit.application.web.controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import digit.application.service.ApplicationService;
import digit.application.web.models.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import jakarta.validation.Valid;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-09-09T14:51:43.484446283+05:30[Asia/Calcutta]")
@Controller
@RequestMapping("/v1")
@CrossOrigin(origins = "*")
@Slf4j
public class V1ApiController {

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    private final ApplicationService applicationService;


    @Autowired
    public V1ApiController(ObjectMapper objectMapper, HttpServletRequest request, ApplicationService applicationService) {
        this.objectMapper = objectMapper;
        this.request = request;
        this.applicationService = applicationService;

    }

    @RequestMapping(value = "/_create", method = RequestMethod.POST, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public ResponseEntity<?> v1CreatePost(@Parameter(in = ParameterIn.DEFAULT, description = "Request object to create Application in the system", required = true, schema = @Schema())
                                                                @Valid @RequestPart("application") ApplicationRequest application,
                                                            @RequestPart(value ="files", required = false) List<MultipartFile> files) {
        try{
            ApplicationResponse response = applicationService.create(application, files);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/_search", method = RequestMethod.POST)
    public ResponseEntity<ApplicationResponse> v1SearchPost(@Parameter(in = ParameterIn.DEFAULT, description = "Request object to search Application in the system", required = true, schema = @Schema()) @Valid @RequestBody ApplicationSearchRequest body) {
        ApplicationResponse applicationResponse = applicationService.search(body);
        return new ResponseEntity<>(applicationResponse, HttpStatus.ACCEPTED);

    }

    @RequestMapping(value = "/_update", method = RequestMethod.POST)
    public ResponseEntity<?> v1UpdatePost(@Parameter(in = ParameterIn.DEFAULT, description = "Request object to update Application in the system", required = true, schema = @Schema()) @Valid @RequestBody ApplicationRequest body) {
        try{
            ApplicationResponse response = applicationService.update(body);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/_appstat", method = RequestMethod.POST)
    public ResponseEntity<List<AppStatResponse>> getAppStat()
    {
        List<AppStatResponse> response= applicationService.GetAppStat();
        return  new ResponseEntity<>(response,HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/_fundsstat", method = RequestMethod.POST)
    public ResponseEntity<List<ProviderFundStat>> getFundsStat()
    {
        List<ProviderFundStat> response=applicationService.getProviderFundStats();
        return  new ResponseEntity<>(response,HttpStatus.ACCEPTED);
    }
    @RequestMapping(value = "/scholarships/top-3", method = RequestMethod.POST)
    public ResponseEntity<List<ScholarshipStat>> getScholarshipStat()
    {
        List<ScholarshipStat> response= applicationService.getScholarshipStats();
        return  new ResponseEntity<>(response,HttpStatus.ACCEPTED);
    }
    @RequestMapping(value = "/scholarships/details", method = RequestMethod.POST)
    public ResponseEntity<List<ScholarshipDetails>> getScholarshipDetails()
    {
        List<ScholarshipDetails> response=applicationService.getScholarshipDetails();
        return  new ResponseEntity<>(response,HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "getByApplicationId", method = RequestMethod.POST)
    public ResponseEntity<Object> getApplicationById(@RequestBody IdRequestBody body) {
        try {
            Application application = applicationService.getApplicationById(body.getApplicationId());
            return new ResponseEntity<>(application,HttpStatus.ACCEPTED);
        } catch (Exception e) {
            System.err.println("Error while fetching application: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred: " + e.getMessage());
        }
    }
    /*@RequestMapping(value = "/applicationstat", method = RequestMethod.GET)
    public ResponseEntity<List<ApplicatonStatastics>> getApplicatonStatastics()
    {
        List<ApplicatonStatastics> response=applicationService.getApplicatonStatastics();
        return  new ResponseEntity<>(response,HttpStatus.ACCEPTED);
    }
    @RequestMapping(value = "/benefits", method = RequestMethod.GET)
    public ResponseEntity<List<Benefit>> getBenefits()
    {
        List<Benefit> response=applicationService.getBenefits();
        return  new ResponseEntity<>(response,HttpStatus.ACCEPTED);
    }*/

    @RequestMapping(value = "/_updatestatus", method = RequestMethod.POST)
    public ResponseEntity<ApplicationStatusUpdateResponse> v1UpdatePostApplicayionStatus(@Parameter(in = ParameterIn.DEFAULT, description = "Request object to create Application in the system", required = true, schema = @Schema()) @Valid @RequestBody ApplicationStatusUpdateRequest body) {
//        ApplicationResponse response = applicationService.update(body);
    	System.out.println("inside update status");
    	  ApplicationStatusUpdateResponse response = applicationService.updateApplicationStatus(body);
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }
    
}
