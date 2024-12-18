package digit.web.controllers;


import digit.config.Configuration;
import digit.kafka.Producer;
import digit.service.CustomerService;
import digit.web.models.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.*;

    import jakarta.validation.constraints.*;
    import jakarta.validation.Valid;
    import jakarta.servlet.http.HttpServletRequest;
        import java.util.Optional;
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-10-30T00:31:03.321266700+05:30[Asia/Calcutta]")
@Controller
    @RequestMapping("")
    public class CustomerApiController{

        private final ObjectMapper objectMapper;

        private final HttpServletRequest request;

        private final Producer producer;

        private final Configuration configuration;

        private final CustomerService customerService;


    @Autowired
        public CustomerApiController(ObjectMapper objectMapper, HttpServletRequest request,Producer producer, Configuration configuration, CustomerService customerService) {
        this.objectMapper = objectMapper;
        this.request = request;
        this.producer =  producer;
        this.configuration=configuration;
        this.customerService=customerService;

        }


//Get Customer by Id

    @RequestMapping(value="v1/_get", method = RequestMethod.POST)
    public ResponseEntity<CustomerResponse> getCustomerById(
            @Parameter(in = ParameterIn.DEFAULT, description = "Request metadata + Customer ID to fetch.", required=true, schema=@Schema())
            @RequestBody CustomerRequest body) {

        String accept = request.getHeader("Accept");
        System.out.println("Received Get Customer Request: " + body);

        if (accept != null && accept.contains("application/json")) {
            try {
                // Assuming there’s a service or repository method to fetch customer by ID
                String customerId = body.getCustomer().getId();
                Customer customer = customerService.getCustomerById(customerId);  // Use your service/repository layer to fetch data

                if (customer == null) {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);  // If customer is not found, return 404
                }

                // Build a dynamic CustomerResponse based on fetched customer data
                CustomerResponse response = new CustomerResponse();

                // Populate ResponseInfo
                ResponseInfo responseInfo = new ResponseInfo();
                responseInfo.setApiId("customer-get-api");
                responseInfo.setVer("1.0");
                responseInfo.setTs(System.currentTimeMillis());
                responseInfo.setMsgId("customer-get-success");
                responseInfo.setStatus(ResponseInfo.StatusEnum.SUCCESSFUL);
                response.setResponseInfo(responseInfo);

                // Set fetched Customer data in the response
                response.setCustomer(customer);

                // Return the response with the fetched Customer
                return new ResponseEntity<>(response, HttpStatus.OK);

            } catch (Exception e) {
                // Log error details
                System.err.println("Error fetching customer by ID: " + e.getMessage());
                e.printStackTrace();
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        // Default response if JSON is not accepted
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }






    //End







    @RequestMapping(value="v1/_upsert", method = RequestMethod.POST)
    public ResponseEntity<CustomerResponse> customerV1CreatePost(
            @Parameter(in = ParameterIn.DEFAULT, description = "Details for the new customer record + RequestInfo metadata.", required=true, schema=@Schema())
            @Valid @RequestBody CustomerRequest body) {

        // Check the "Accept" header to determine response content
        String accept = request.getHeader("Accept");
        System.out.println("Received Request: " + body);

        if (accept != null && accept.contains("application/json")) {
            try {

                if(Objects.isNull(body.getCustomer().getId())){
                    body.getCustomer().setId(UUID.randomUUID().toString());
                }
                // Send the request data to Kafka topic
                producer.push(configuration.getCreateCustomerTopic(), body);

                // Create a dynamic CustomerResponse based on the request data
                CustomerResponse response = new CustomerResponse();

                // Create ResponseInfo based on request data
                ResponseInfo responseInfo = new ResponseInfo();
                responseInfo.setApiId("customer-api");
                responseInfo.setVer("1.0");
                responseInfo.setTs(System.currentTimeMillis());
                responseInfo.setMsgId("customer-create-success");
                responseInfo.setStatus(ResponseInfo.StatusEnum.SUCCESSFUL);
                response.setResponseInfo(responseInfo);

                // Set Customer data in the response dynamically based on the request body
                Customer customer = body.getCustomer();  // Assuming body has a Customer object
                response.setCustomer(customer);

                // Return dynamic CustomerResponse
                return new ResponseEntity<>(response, HttpStatus.CREATED);

            } catch (Exception e) {
                // Log error details
                System.err.println("Error processing customer creation: " + e.getMessage());
                e.printStackTrace();
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        // Default response if JSON is not accepted
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }











//                @RequestMapping(value="v1/_create", method = RequestMethod.POST)
//                public ResponseEntity<CustomerResponse> customerV1CreatePost(@Parameter(in = ParameterIn.DEFAULT, description = "Details for the new customer record + RequestInfo metadata.", required=true, schema=@Schema()) @Valid @RequestBody CustomerRequest body) {
//                        String accept = request.getHeader("Accept");
//                    System.out.println("JSON "+body);
//                            if (accept != null && accept.contains("application/json")) {
//                            try {
//                                producer.push(configuration.getCreateCustomerTopic(),body);
//                            return new ResponseEntity<CustomerResponse>(objectMapper.readValue("{  \"ResponseInfo\" : {    \"ver\" : \"ver\",    \"resMsgId\" : \"resMsgId\",    \"msgId\" : \"msgId\",    \"apiId\" : \"apiId\",    \"ts\" : 0,    \"status\" : \"SUCCESSFUL\"  },  \"Customer\" : {    \"transaction_id\" : \"transaction_id\",    \"submission_id\" : \"submission_id\",    \"gender\" : \"Male\",    \"updated_at\" : \"2000-01-23T04:56:07\",    \"phone\" : \"phone\",    \"content_id\" : \"content_id\",    \"name\" : \"name\",    \"created_at\" : \"2000-01-23T04:56:07\",    \"id\" : 6,    \"order_id\" : \"order_id\",    \"email\" : \"\"  }}", CustomerResponse.class), HttpStatus.CREATED);
//                            } catch (IOException e) {
//                            return new ResponseEntity<CustomerResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
//                            }
//                            }
//
//                        return new ResponseEntity<CustomerResponse>(HttpStatus.NOT_IMPLEMENTED);
//                }

//                @RequestMapping(value="v1/_update", method = RequestMethod.POST)
//                public ResponseEntity<CustomerResponse> customerV1UpdatePost(@Parameter(in = ParameterIn.DEFAULT, description = "Details for updating the customer record + RequestInfo metadata.", required=true, schema=@Schema()) @Valid @RequestBody CustomerRequest body) {
//                        String accept = request.getHeader("Accept");
//                            if (accept != null && accept.contains("application/json")) {
//                            try {
//                                producer.push(configuration.getUpdateCustomerTopic(),body);
//
//                            return new ResponseEntity<CustomerResponse>(objectMapper.readValue("{  \"ResponseInfo\" : {    \"ver\" : \"ver\",    \"resMsgId\" : \"resMsgId\",    \"msgId\" : \"msgId\",    \"apiId\" : \"apiId\",    \"ts\" : 0,    \"status\" : \"SUCCESSFUL\"  },  \"Customer\" : {    \"transaction_id\" : \"transaction_id\",    \"submission_id\" : \"submission_id\",    \"gender\" : \"Male\",    \"updated_at\" : \"2000-01-23T04:56:07.000+00:00\",    \"phone\" : \"phone\",    \"content_id\" : \"content_id\",    \"name\" : \"name\",    \"created_at\" : \"2000-01-23T04:56:07.000+00:00\",    \"id\" : 6,    \"order_id\" : \"order_id\",    \"email\" : \"\"  }}", CustomerResponse.class), HttpStatus.OK);
//                            } catch (IOException e) {
//                            return new ResponseEntity<CustomerResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
//                            }
//                            }
//
//                        return new ResponseEntity<CustomerResponse>(HttpStatus.NOT_IMPLEMENTED);
//                }




    @RequestMapping(value="v1/_update", method = RequestMethod.POST)
    public ResponseEntity<CustomerResponse> customerV1UpdatePost(
            @Parameter(in = ParameterIn.DEFAULT, description = "Details for updating the customer record + RequestInfo metadata.", required=true, schema=@Schema())
            @Valid @RequestBody CustomerRequest body) {

        String accept = request.getHeader("Accept");
        System.out.println("Received Update Request: " + body);

        if (accept != null && accept.contains("application/json")) {
            try {
                // Push update data to the Kafka topic for customer updates
                producer.push(configuration.getUpdateCustomerTopic(), body);

                // Build dynamic CustomerResponse based on request data
                CustomerResponse response = new CustomerResponse();

                // Populate ResponseInfo dynamically
                ResponseInfo responseInfo = new ResponseInfo();
                responseInfo.setApiId("customer-update-api");
                responseInfo.setVer("1.0");
                responseInfo.setTs(System.currentTimeMillis());
                responseInfo.setMsgId("customer-update-success");
                responseInfo.setStatus(ResponseInfo.StatusEnum.SUCCESSFUL);
                response.setResponseInfo(responseInfo);

                // Get Customer object from request and update dynamic fields
                Customer customer = body.getCustomer();  // Assuming body contains a Customer object
                response.setCustomer(customer);

                // Return a dynamic response
                return new ResponseEntity<>(response, HttpStatus.OK);

            } catch (Exception e) {
                // Log error details
                System.err.println("Error processing customer update: " + e.getMessage());
                e.printStackTrace();
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        // Default response if JSON is not accepted
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }


}
