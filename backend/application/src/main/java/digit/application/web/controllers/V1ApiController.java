package digit.application.web.controllers;


import digit.application.config.Configuration;
import com.fasterxml.jackson.core.JsonProcessingException;
import digit.application.service.ApplicationService;
import digit.application.web.models.*;
 
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
 
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
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
    private final Configuration configuration;
	private final RestTemplate restTemplate;

    @Autowired
    public V1ApiController(ObjectMapper objectMapper, HttpServletRequest request, ApplicationService applicationService,Configuration configuration, RestTemplate restTemplate) {
        this.objectMapper = objectMapper;
        this.request = request;
        this.applicationService = applicationService;
        this.configuration = configuration;
		this.restTemplate = restTemplate;
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
    public ResponseEntity<ApplicationResponse> v1UpdatePost(@Parameter(in = ParameterIn.DEFAULT, description = "Request object to create Application in the system", required = true, schema = @Schema()) @Valid @RequestBody ApplicationRequest body) {
        ApplicationResponse response = applicationService.update(body);
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/_updatestatus", method = RequestMethod.POST)
    public ResponseEntity<ApplicationStatusUpdateResponse> v1UpdatePostApplicayionStatus(@Parameter(in = ParameterIn.DEFAULT, description = "Request object to create Application in the system", required = true, schema = @Schema()) @Valid @RequestBody ApplicationStatusUpdateRequest body) {
//        ApplicationResponse response = applicationService.update(body);
    	System.out.println("inside update status");
    	  ApplicationStatusUpdateResponse response = applicationService.updateApplicationStatus(body);
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
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

    @RequestMapping(value = "/getByApplicationId", method = RequestMethod.POST)
    public ResponseEntity<Object> getApplicationById(@RequestBody IdRequestBody body) {
    	System.out.println("Received req");
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
    
 // Priyanka 25Nov2024
 	@RequestMapping(value = "/direct_disbursals", method = RequestMethod.POST)
 	public ResponseEntity<Object> direct_disbursals(@RequestBody IdRequestBody body) {
 		try {
 			Application application = applicationService.direct_disbursals(body.getApplicationId());
 			// Log the application data
 			System.out.println("Fetched application: " + application);
  
 			if (application == null) {
 				return ResponseEntity.status(HttpStatus.NOT_FOUND)
 						.body("Application not found for ID: " + body.getApplicationId());
 			}
  
 			// Extract the schema field
 			String schema = application.getSchema();
 			if (schema == null || schema.isBlank()) {
 				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Schema is missing in the application");
 			}
 			// Convert schema field to JSON format
 			ObjectMapper objectMapper = new ObjectMapper();
 			JsonNode jsonSchema;
 			String firstname = null; // firstName
 			String lastName = null; // lastName
 			String amount = "1500"; // annualIncome need to change in discussion
 			String ifsc_code = null; // bankIfscCode
 			String account_number = null; // bankAccountNumber
 			String mobile = null; // not present
 			String email = ""; // not present
 			String scheme = null; // as program code
 			String uid = body.getApplicationId();
 			String isDisabled = null;
  
 			try {
 				jsonSchema = objectMapper.readTree(schema); // Convert string to JSON object
 				System.out.println("Fetched jsonSchema: " + jsonSchema);
  
 				for (JsonNode node : jsonSchema) {
 					if (node.get("name").asText().equals("firstName")) {
 						firstname = node.get("value").asText();
 					}
 					if (node.get("name").asText().equals("lastName")) {
 						lastName = node.get("value").asText();
 					}
 					if (node.get("name").asText().equals("mobileNumber")) {
 						mobile = node.get("value").asText();
 					}
 					/*
 					 * if (node.get("name").asText().equals("income")) { amount =
 					 * node.get("value").asText(); }
 					 */
 					if (node.get("name").asText().equals("bankIfscCode")) {
 						ifsc_code = node.get("value").asText();
 					}
 					if (node.get("name").asText().equals("bankAccountNumber")) {
 						account_number = node.get("value").asText();
 					}
 					if (node.get("name").asText().equals("programCode")) {
 						scheme = node.get("value").asText();
 					}
 					if (node.get("name").asText().equals("disability")) {
 						isDisabled = node.get("value").asText();
 					}
 				}
  
 				// Validate extracted fields
 				if (firstname == null || firstname.isBlank()) {
 					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("First Name is missing in schema");
 				}
 				if (lastName == null || lastName.isBlank()) {
 					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Last Name is missing in schema");
 				}
 				if (ifsc_code == null || ifsc_code.isBlank()) {
 					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("IFSC Code is missing in schema");
 				}
 				if (mobile == null || mobile.isBlank()) {
 					// return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Mobile Number is
 					// missing in schema");
 				}
 				if (account_number == null || account_number.isBlank()) {
 					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Account number is missing in schema");
 				}
 				if (scheme == null || scheme.isBlank()) {
 					scheme = "";
 				}
 				if (isDisabled == null || isDisabled.isBlank()) {
 					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Disability field is missing in schema");
 				}
  
 				if (isDisabled != null && isDisabled.toLowerCase().equals("true")) {
 					System.out.println("isDisabled inside");
 					amount = "1650"; // Set amount if the condition is true
 				}
  
 			} catch (JsonProcessingException e) {
 				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
 						.body("Error parsing schema field to JSON: " + e.getMessage());
 			}
  
 			// Make the POST request
 			Map<String, Object> dataEntry = new HashMap<>();
 			dataEntry.put("first_name", firstname);
 			dataEntry.put("last_name", lastName);
 			dataEntry.put("amount", amount);
 			dataEntry.put("ifsc_code", ifsc_code);
 			dataEntry.put("account_number", account_number);
 			dataEntry.put("mobile", "8600775434");
 			dataEntry.put("email", "");
 			dataEntry.put("scheme", scheme);
 			dataEntry.put("uid", uid);
  
 			// Wrap the data map in a list
 			List<Map<String, Object>> dataList = new ArrayList<>();
 			dataList.add(dataEntry);
  
 			System.out.println("dataList : " + dataList);
  
 			// Create the final payload
 			Map<String, Object> requestPayload = new HashMap<>();
 			requestPayload.put("data", dataList);
  
 			HttpHeaders headers = new HttpHeaders();
 			String xapikey = configuration.getDirect_disbursals_x_api_key();
 			headers.set("x-api-key", xapikey);
 			HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestPayload, headers);
  
 			String externalApiUrl = configuration.getDirect_dibursals_APIURL();
 			ResponseEntity<Object> externalResponse;
  
 			try {
 				externalResponse = restTemplate.postForEntity(externalApiUrl, entity, Object.class);
 			} catch (Exception e) {
 				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
 						.body("Failed to call external API: " + e.getMessage());
 			}
  
 			// Extract the response body
 			Integer batchId = null;
 			if (externalResponse.getBody() instanceof Map) {
 				Map<String, Object> responseBody = (Map<String, Object>) externalResponse.getBody();
 				Map<String, Object> dataMap = (Map<String, Object>) responseBody.get("data");
 				batchId = (Integer) dataMap.get("batch_id");
 			} else {
 				System.out.println("Response is not in expected Map format.");
 			}
  
 			ApplicationUpdateBatchIDRequest applicationRequest = new ApplicationUpdateBatchIDRequest();
 			applicationRequest.setApplicationId(body.getApplicationId());
 			applicationRequest.setBatch_id(batchId); // You can set the status as per the incoming body
  
 			// Call the updateApplication_BatchId method
 			ApplicationUpdateBatchIDResponse response = applicationService
 					.updateApplication_BatchId(applicationRequest);
 			
 			//update status api
 			try {
 				if (response.getSuccess().equals(true)) {
 	                ApplicationStatusUpdateRequest updateRequest = new ApplicationStatusUpdateRequest();
 	                
 	                updateRequest.setApplicationId(body.getApplicationId());
 	                updateRequest.setStatus("SUBMITTED FOR DISBURSAL"); // Example status
 	                
 	                ResponseEntity<ApplicationStatusUpdateResponse> updateResponse = 
 	                    v1UpdatePostApplicayionStatus(updateRequest);
 	                System.out.println("Status Update Response: " + updateResponse.getBody());
 	            }
 	 			
			} catch (Exception e) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred: " + e.getMessage());
			}
 			
 			return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
  
 		} catch (Exception e) {
 			System.err.println("Error while fetching application: " + e.getMessage());
 			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred: " + e.getMessage());
 		}
 	}
  
 	public ResponseEntity<ApplicationUpdateBatchIDResponse> updateApplication_BatchId(
 			@Parameter(in = ParameterIn.DEFAULT, description = "Request object to update batch id in Application", required = true, schema = @Schema()) @Valid @RequestBody ApplicationUpdateBatchIDRequest body) {
 		ApplicationUpdateBatchIDResponse response = applicationService.updateApplication_BatchId(body);
 		return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
 	}
  
 	// Create function to check disbursal status
 	//String disbursalStatus = "APPROVED";
 	public List<Application> getDisbursementProcessApplications(String disbursalStatus) {
 		try {
 			System.out.println("Wf status req- " + disbursalStatus);
 			List<Application> applications = applicationService.getDisbursementProcessApplications(disbursalStatus);
 	        
 	        System.out.println("Wf status response- " + applications);
  
 	        // Convert the list of applications to a JSON string
 	        ObjectMapper objectMapper = new ObjectMapper();
 	        String jsonResponse = objectMapper.writeValueAsString(applications);
  
 	        System.out.println("JSON Response: " + jsonResponse);
 	        
 	        if(jsonResponse.isBlank()) {
 	        	return null;
 	        }
 	        
 	       try {
 	            // Convert JSON string to an ArrayList of objects (Application class in this case)
 	            List<Application> applicationList = objectMapper.readValue(jsonResponse,
 	                    objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, Application.class));
 	            
 	            // Print out the list to verify
 	            for (Application app : applicationList) {
 	                System.out.println(app.getBatch_id());
 	                
 	                //Get Disbursal Status by Batch Id
 	                HttpHeaders headers = new HttpHeaders();
 	                String xapikey = configuration.getDirect_disbursals_x_api_key();
 	                headers.set("x-api-key", xapikey);
  
 	                // Create the HTTP entity with headers
 	                HttpEntity<Map<String, Object>> entity = new HttpEntity<>(headers);
  
 	                String externalApiUrl = configuration.getCheck_disbursals_status();
 	                ResponseEntity<Object> externalResponse;
  
 	                try {
 	                    // Make the GET request to the external API
 	                    externalResponse = restTemplate.getForEntity(externalApiUrl, Object.class, entity);
 	                } catch (Exception e) {
 	                    // Handle the exception and return an error response
 	                    return (List<Application>) ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
 	                            .body("Failed to call external API: " + e.getMessage());
 	                }
 	                System.out.println("externalResponse for batch id"+ externalResponse);           
 	            }
 	            
 	        } catch (Exception e) {
 	            System.err.println("Error while converting JSON to ArrayList: " + e.getMessage());
 	        }
 			return null;
 		} catch (Exception e) {
 			System.err.println("Error while fetching application: " + e.getMessage());
 			throw new RuntimeException("Error occurred: " + e.getMessage());
 		}
 	}
  
 	/*@Scheduled(cron = "0 0/1 * * * ?") // run every 1 mins
 	public void runEvery15Minutes() {
 		System.out.println("\n-------------Task running every 15 minutes");
 		String disbursalStatus = "Disbursement is under Process";
 	}*/
 	// End by priyanka 26Nov2024
 
}
