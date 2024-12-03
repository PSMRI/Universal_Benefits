package digit.application.web.controllers;


import digit.application.config.Configuration;
import com.fasterxml.jackson.core.JsonProcessingException;
import digit.application.service.ApplicationService;
import digit.application.web.models.*;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;


 
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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpEntity;
import java.util.*;

import java.util.regex.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
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
	public V1ApiController(ObjectMapper objectMapper, HttpServletRequest request, ApplicationService applicationService,
			Configuration configuration, RestTemplate restTemplate) {
		this.objectMapper = objectMapper;
		this.request = request;
		this.applicationService = applicationService;
		this.configuration = configuration;
		this.restTemplate = restTemplate;
	}

	@RequestMapping(value = "/_create", method = RequestMethod.POST, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE,
			MediaType.APPLICATION_OCTET_STREAM_VALUE })
	public ResponseEntity<?> v1CreatePost(
			@Parameter(in = ParameterIn.DEFAULT, description = "Request object to create Application in the system", required = true, schema = @Schema()) @Valid @RequestPart("application") ApplicationRequest application,
			@RequestPart(value = "files", required = false) List<MultipartFile> files) {
		try {
			ApplicationResponse response = applicationService.create(application, files);

			 try {
		            Thread.sleep(2000); // Wait for 3000 milliseconds (3 seconds)
		            
		            for (int i = 0; i < response.getApplications().size(); i++) {
				String applicationId = response.getApplications().get(i).getId();
				boolean isApproved = configuration.isAuto_Approve_Applications();

				ApplicationStatusUpdateRequest applicationStatusUpdateRequest = new ApplicationStatusUpdateRequest();

				applicationStatusUpdateRequest.setApplicationId(applicationId);
				applicationStatusUpdateRequest.setStatus("APPROVED");
				if (isApproved) {
					System.out.println("Inside Approved");
					v1UpdatePostApplicayionStatus(applicationStatusUpdateRequest);
				}
			}
		            
		        } catch (InterruptedException e) {
		            e.printStackTrace();
		        }
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
	public ResponseEntity<ApplicationResponse> v1SearchPost(
			@Parameter(in = ParameterIn.DEFAULT, description = "Request object to search Application in the system", required = true, schema = @Schema()) @Valid @RequestBody ApplicationSearchRequest body) {
		ApplicationResponse applicationResponse = applicationService.search(body);
		return new ResponseEntity<>(applicationResponse, HttpStatus.ACCEPTED);

	}

	@RequestMapping(value = "/_update", method = RequestMethod.POST)
	public ResponseEntity<ApplicationResponse> v1UpdatePost(
			@Parameter(in = ParameterIn.DEFAULT, description = "Request object to create Application in the system", required = true, schema = @Schema()) @Valid @RequestBody ApplicationRequest body) {
		ApplicationResponse response = applicationService.update(body);
		return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
	}

	
	@RequestMapping(value = "/_updatestatus", method = RequestMethod.POST)
	public ResponseEntity<ApplicationStatusUpdateResponse> v1UpdatePostApplicayionStatus(
			@Parameter(in = ParameterIn.DEFAULT, description = "Request object to create Application in the system", required = true, schema = @Schema()) @Valid @RequestBody ApplicationStatusUpdateRequest body) {
		try {
			if (body.getStatus().toLowerCase().equals("approved")) {
				IdRequestBody idRequestBody = new IdRequestBody();
				idRequestBody.setApplicationId(body.getApplicationId()); // Example, adjust according to actual fields
				ResponseEntity<Object> disbursalsResponse = direct_disbursals(idRequestBody);
				System.out.println("direct disbursal response is "+disbursalsResponse);
				
				String errorMsg = null;
				Integer batchId = null;
				if (disbursalsResponse != null && disbursalsResponse.getBody() != null) {
					Object responseBody = disbursalsResponse.getBody();
				
					// Directly parse the body to Integer
					if (responseBody instanceof Integer) {
						batchId = (Integer) responseBody;
					} else if (responseBody instanceof String) {
						try {
							batchId = Integer.parseInt((String) responseBody);
						} catch (NumberFormatException e) {
							//System.out.println("Response body could not be parsed to Integer: " + responseBody);
							   String responseBodyString = responseBody.toString();
								System.out.println("responseBody 113 : " + responseBody);
								
								// Regex pattern to extract msg fields
						        Pattern pattern = Pattern.compile("msg=([^,}]+)");
						        Matcher matcher = pattern.matcher(responseBodyString);

						        // List to store all matched msg fields
						        List<String> messages = new ArrayList<>();

						        while (matcher.find()) {
						            messages.add(matcher.group(1));
						        }

						        // Join all messages with a comma separator
						        String result = String.join(", ", messages);
						        System.out.println(result);
						        errorMsg = result;
								
						}
					} else {
						System.out.println("Unexpected response type: " + responseBody.getClass());
					}
				}
 
				System.out.println("Response batch ID - " + batchId);
 
				ApplicationUpdateBatchIDResponse BatchIDresponse = null;
				if(batchId != null) {
					System.out.println("Inside if batchId != null");
					ApplicationUpdateBatchIDRequest applicationRequest = new ApplicationUpdateBatchIDRequest();
					applicationRequest.setApplicationId(body.getApplicationId());
					applicationRequest.setBatch_id(batchId); // You can set the status as per the incoming body
					System.out.println("batch id inside if "+batchId);
					 BatchIDresponse = applicationService.updateApplication_BatchId(applicationRequest);
				}
				// update Status
				
				try {
					if (BatchIDresponse.getSuccess() != null && BatchIDresponse.getSuccess()) {
						body.setStatus("AMOUNT TRANSFER IN PROGRESS");
						ApplicationStatusUpdateResponse response = applicationService.updateApplicationStatus(body);
						return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
					} else {
						System.out.println("Inside else 166");
						body.setStatus(errorMsg);
						ApplicationStatusUpdateResponse response = applicationService.updateApplicationLog(body);
						return new ResponseEntity<>(response.updateResponseMsgforfailure(errorMsg),HttpStatus.BAD_REQUEST); 
					}
				} catch (Exception e) {
					System.out.println("Inside else 166");
					body.setStatus(errorMsg);
					ApplicationStatusUpdateResponse response = applicationService.updateApplicationLog(body);
					return new ResponseEntity<>(response.updateResponseMsgforfailure(errorMsg),HttpStatus.BAD_REQUEST); 
				}
 
			} else {
				ApplicationStatusUpdateResponse response = applicationService.updateApplicationStatus(body);
				return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
			}
 
		} catch (Exception e) {
			// Handle any exception that occurs
			System.err.println("Error occurred: " + e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
 
	}

	@RequestMapping(value = "/_appstat", method = RequestMethod.POST)
	public ResponseEntity<List<AppStatResponse>> getAppStat() {
		List<AppStatResponse> response = applicationService.GetAppStat();
		return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
	}

	@RequestMapping(value = "/_fundsstat", method = RequestMethod.POST)
	public ResponseEntity<List<ProviderFundStat>> getFundsStat() {
		List<ProviderFundStat> response = applicationService.getProviderFundStats();
		return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
	}

	@RequestMapping(value = "/scholarships/top-3", method = RequestMethod.POST)
	public ResponseEntity<List<ScholarshipStat>> getScholarshipStat() {
		List<ScholarshipStat> response = applicationService.getScholarshipStats();
		return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
	}

	@RequestMapping(value = "/scholarships/details", method = RequestMethod.POST)
	public ResponseEntity<List<ScholarshipDetails>> getScholarshipDetails() {
		List<ScholarshipDetails> response = applicationService.getScholarshipDetails();
		return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
	}

	@RequestMapping(value = "/getByApplicationId", method = RequestMethod.POST)
	public ResponseEntity<Object> getApplicationById(@RequestBody IdRequestBody body) {
		System.out.println("Received req");
		try {
			Application application = applicationService.getApplicationById(body.getApplicationId());
			// getDisbursementProcessApplications();
			return new ResponseEntity<>(application, HttpStatus.ACCEPTED);
		} catch (Exception e) {
			System.err.println("Error while fetching application: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred: " + e.getMessage());
		}
	}
	

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
			String amount = "0"; // annualIncome need to change in discussion
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
					if (node.get("name").asText().equals("mobile")) {
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
					
					if (node.get("name").asText().equals("benefit_id")) {
						scheme = node.get("value").asText();
					}
					/*if (node.get("name").asText().equals("disability")) {
						isDisabled = node.get("value").asText();
					}*/
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
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Mobile Number is missing in schema");
				}
				if (account_number == null || account_number.isBlank()) {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Account number is missing in schema");
				}
				if (scheme == null || scheme.isBlank()) {
					scheme = "";
				}
			/*	if (isDisabled == null || isDisabled.isBlank()) {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Disability field is missing in schema");
				}*/

				// currently not in use
				/*if (isDisabled != null && isDisabled.toLowerCase().equals("true")) {
					System.out.println("isDisabled inside");
					amount = "1650"; // Set amount if the condition is true
				}*/

			} catch (JsonProcessingException e) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body("Error parsing schema field to JSON: " + e.getMessage());
			}

			if(scheme.equals("PB-BTR-2024-12-02-000726")) {
				amount = "1000";
			}else if(scheme.equals("PB-BTR-2024-12-02-000725")) {
				amount = "1500";
			}
			
			// Make the POST request
			Map<String, Object> dataEntry = new HashMap<>();
			dataEntry.put("first_name", firstname);
			dataEntry.put("last_name", lastName);
			dataEntry.put("amount", amount);
			dataEntry.put("ifsc_code", ifsc_code);  //ifsc_code
			dataEntry.put("account_number", account_number);
			dataEntry.put("mobile", mobile);
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
			} catch (HttpClientErrorException e) {
			    System.out.println("Disbursal Response - catch block");

			    // Extract the response body (error details) from the exception
			    String responseBody = e.getResponseBodyAsString();

			    // Use ObjectMapper to parse the JSON response
			    ObjectMapper objectMapper1 = new ObjectMapper();
			    try {
			        // Parse the JSON response and convert it to a Map or a custom class as needed
			        Map<String, Object> errorDetails = objectMapper1.readValue(responseBody, Map.class);
			        
			        // Extract the "detail" field from the response
			        Object details = errorDetails.get("detail");
			        
			        System.out.println("details : "+ details);
			        
			        // Return a detailed error message in the response body
			       // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to call external API: " + details.toString());
			        return new ResponseEntity<>(details.toString(), HttpStatus.ACCEPTED);
			    } catch (Exception parseException) {
			        // Handle any parsing exceptions
			        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to parse error details: " + parseException.getMessage());
			    }
			} catch (Exception e) {
			    // Handle other exceptions
			    System.out.println("Disbursal Response - general catch block");
			    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to call external API: " + e.getMessage());
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

			/*
			 * ApplicationUpdateBatchIDRequest applicationRequest = new
			 * ApplicationUpdateBatchIDRequest();
			 * applicationRequest.setApplicationId(body.getApplicationId());
			 * applicationRequest.setBatch_id(batchId); // You can set the status as per the
			 * incoming body
			 * 
			 * // Call the updateApplication_BatchId method ApplicationUpdateBatchIDResponse
			 * response = applicationService .updateApplication_BatchId(applicationRequest);
			 */

			// update status api currently not in use
			/*
			 * try { if (response.getSuccess().equals(true)) {
			 * ApplicationStatusUpdateRequest updateRequest = new
			 * ApplicationStatusUpdateRequest();
			 * updateRequest.setApplicationId(body.getApplicationId());
			 * updateRequest.setStatus("SUBMITTED FOR DISBURSAL"); // Example status
			 * ResponseEntity<ApplicationStatusUpdateResponse> updateResponse =
			 * v1UpdatePostApplicayionStatus(updateRequest);
			 * System.out.println("Status Update Response: " + updateResponse.getBody()); }
			 * } catch (Exception e) { return
			 * ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
			 * body("Error occurred: " + e.getMessage()); }
			 */

			return new ResponseEntity<>(batchId, HttpStatus.ACCEPTED);

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
	//@GetMapping("/UpdateDisbursals") 
	public ResponseEntity<?> getDisbursementProcessApplications(@RequestParam(value = "disbursalStatus", defaultValue = "Disbursement is under Process") String disbursalStatus) {
		try {
			List<Application> applications = applicationService.getDisbursementProcessApplications(disbursalStatus);

			if (applications == null || applications.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No applications found.");
			}
			
			System.out.println("Application response : " + applications);

			// Convert the applications list to JSON
			ResponseEntity<ApplicationUpdateBatchIDResponse> updateResponse = null;
			StringBuilder Disbursals_batchIds = new StringBuilder();
			StringBuilder Disbursals_batchIds_Empty = new StringBuilder();
			ObjectMapper objectMapper = new ObjectMapper();
			String jsonResponse = objectMapper.writeValueAsString(applications);

			ObjectMapper objectMapper1 = new ObjectMapper();
			try {
				// Convert JSON string back to a List of Application objects
				List<Application> applicationList = objectMapper1.readValue(jsonResponse,
						objectMapper1.getTypeFactory().constructCollectionType(List.class, Application.class));
				
				System.out.println("application List : " + applicationList);
				// Loop through the list
				for (Application app : applicationList) {
					System.out.println("Batch ID: " + app.getBatch_id());
					if(app.getBatch_id() > 0) {
						String xapikey = configuration.getDirect_disbursals_x_api_key();
						HttpHeaders headers = new HttpHeaders();
						headers.set("x-api-key", xapikey);
						headers.setContentType(MediaType.APPLICATION_JSON); // Optional, depending on your API's
																			// requirements
						HttpEntity<Void> entity = new HttpEntity<>(headers);
	
						String externalApiUrl = configuration.getCheck_disbursals_status() + app.getBatch_id();
						ResponseEntity<Object> externalResponse;
						System.out.println("----------extrenal api url " + externalApiUrl);
						try {
							externalResponse = restTemplate.exchange(externalApiUrl, HttpMethod.GET, entity, Object.class);
							
							System.out.println("----------externalResponse" + externalResponse);
							String disbursal_status = null;
	
							if (externalResponse.getBody() instanceof Map) {
								Map<String, Object> responseBody = (Map<String, Object>) externalResponse.getBody();
	
								// Check if 'data' exists and is a Map
								if (responseBody.containsKey("data") && responseBody.get("data") instanceof Map) {
									Map<String, Object> dataMap = (Map<String, Object>) responseBody.get("data");
	
									// Check if 'disbursal_status' exists and is a list
									if (dataMap.containsKey("disbursal_status")&& dataMap.get("disbursal_status") instanceof List) {
										List<Map<String, Object>> disbursalStatusList = (List<Map<String, Object>>) dataMap
												.get("disbursal_status");
	
										// Check if the list is not empty and extract the first item (or process all items)
										if (!disbursalStatusList.isEmpty()) {
											Map<String, Object> firstItem = disbursalStatusList.get(0);
											disbursal_status = (String) firstItem.get("disbursal_status");
											System.out.println("----------disbursal_status: " + disbursal_status);
	
											//verified
											String configStatus = configuration.getDisbursal_status();
											if(configStatus.toLowerCase().equals("verified")) {
												if (disbursal_status.toLowerCase().equals("verified")) {
													System.out.println("----------disbursal_status Verified");
													if (Disbursals_batchIds.length() > 0) {
														Disbursals_batchIds.append(","); // Add a comma only if this is not the
													}
													Disbursals_batchIds.append(app.getBatch_id());
												}
											}else {
												if (disbursal_status.toLowerCase().equals("disbursal done") || disbursal_status.toLowerCase().equals("payment acknowledged")) {
													System.out.println("----------disbursal_status disbursal done");
													if (Disbursals_batchIds.length() > 0) {
														Disbursals_batchIds.append(","); // Add a comma only if this is not the
													}
													Disbursals_batchIds.append(app.getBatch_id());
												}
											}
										} else {
											System.out.println("----------disbursal_status empty-----call here error log ");
											if (Disbursals_batchIds_Empty.length() > 0) {
												Disbursals_batchIds_Empty.append(","); // Add a comma only if this is not the
											}
											Disbursals_batchIds_Empty.append(app.getBatch_id());
										}
									} else {
										System.out.println("'disbursal_status' field not found or is not a list");
									}
								} else {
									System.out.println("'data' field not found or not in the expected format");
								}
							} else {
								System.out.println("Response is not in expected Map format.");
							}
						} catch (Exception e) {
							System.err.println("Error response: " + e.getMessage());
							return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to call external API: " + e.getMessage());
						}//check disbursals call 
					}
				} // For Loop End

			} catch (Exception e) {
				System.err.println("Error while parsing JSON: " + e.getMessage());
			}

			// Update status by id with success
			if (!(Disbursals_batchIds == null || Disbursals_batchIds.length() == 0)) {
				String[] batchIdArray = Disbursals_batchIds.toString().split(",");
				System.out.println("----------Batch ID array " + batchIdArray);
				// Print the array to check its content
				for (String id : batchIdArray) {
					System.out.println("----Batch -- "+id);
					try {
						ApplicationUpdateBatchIDRequest updateStatusByBatchIdReq = new ApplicationUpdateBatchIDRequest();
						int batchID = Integer.parseInt(id);
						updateStatusByBatchIdReq.setBatch_id(batchID);
						updateResponse = updateStatusByBatchId(updateStatusByBatchIdReq);
						System.out.println("----------updateResponse " + updateResponse);
					} catch (Exception e) {
						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while fetching applications: " + e.getMessage());
					}
				} // for loop
			} else {
			//	return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No batch Id found with status 'Disbursal Done' and 'Payment Acknowledged'");
			}
			
			
			//update status with error log 
			if (!(Disbursals_batchIds_Empty == null || Disbursals_batchIds_Empty.length() == 0)) {
				String[] batchIdArray = Disbursals_batchIds_Empty.toString().split(",");
				System.out.println("-------Error Log Batch ID array " + batchIdArray);
				
				for (String id : batchIdArray) {
					System.out.println("----Error Log Batch -- "+id);
					try {
						ApplicationUpdateBatchIDRequest updateStatusByBatchIdReq = new ApplicationUpdateBatchIDRequest();
						int batchID = Integer.parseInt(id);
						updateStatusByBatchIdReq.setBatch_id(batchID);
						updateResponse = updatestatusByBatchId_ErrorLog(updateStatusByBatchIdReq);
						System.out.println("----------Error Log updateResponse " + updateResponse);
					} catch (Exception e) {
						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while fetching applications: " + e.getMessage());
					}
				} // for loop

			} else {
				//return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No batch Id found with status 'Disbursal Done' and 'Payment Acknowledged'");
			}
			
			// Return the applications as JSON
			System.out.println("\n\n-------------Status Update Response: " + updateResponse.getBody());

			return ResponseEntity.ok(updateResponse);

		} catch (Exception e) {
			System.err.println("Error while fetching application: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error occurred while fetching applications: " + e.getMessage());
		}
	}
	
	public ResponseEntity<?> callDisbursementProcessInternally() {
	    String disbursalStatus = "Disbursement is under Process";
	    return getDisbursementProcessApplications(disbursalStatus);
	}
	
	@GetMapping("/UpdateDisbursals")
	public ResponseEntity<?> internalDisbursalCall() {
	    return callDisbursementProcessInternally();
	}

	@RequestMapping(value = "/_updatestatusByBatchId", method = RequestMethod.POST)
	public ResponseEntity<ApplicationUpdateBatchIDResponse> updateStatusByBatchId(
			@Parameter(in = ParameterIn.DEFAULT, description = "Request to update status by batch id", required = true, schema = @Schema()) @Valid @RequestBody ApplicationUpdateBatchIDRequest body) {
		ApplicationUpdateBatchIDResponse response = applicationService.updateStatusByBatchId(body);
		return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
	}
	
	//If update status by id with error log 
	@RequestMapping(value = "/_updatestatusByBatchId_ErrorLog", method = RequestMethod.POST)
	public ResponseEntity<ApplicationUpdateBatchIDResponse> updatestatusByBatchId_ErrorLog(
			@Parameter(in = ParameterIn.DEFAULT, description = "Request to update status by batch id with error log ", required = true, schema = @Schema()) @Valid @RequestBody ApplicationUpdateBatchIDRequest body) {
		ApplicationUpdateBatchIDResponse response = applicationService.updatestatusByBatchId_ErrorLog(body);
		return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
	}

	/*@Scheduled(cron = "0 0/15 * * * ?") // run every 1 mins
	public void runEvery15Minutes() {
		// System.out.println("\n-------------Task running every 15 minutes");
		String disbursalStatus = "Disbursement is under Process";
		// getDisbursementProcessApplications(disbursalStatus);

	}*/
	// End by priyanka 26Nov2024

}
