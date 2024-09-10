package digit.disbursal.web.controllers;


import digit.disbursal.service.DisbursalService;
import digit.disbursal.web.models.DisbursalResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import digit.disbursal.web.models.DisbursalRequest;
import digit.disbursal.web.models.DisbursalSearchRequest;
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

import jakarta.validation.Valid;
import jakarta.servlet.http.HttpServletRequest;

@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-09-09T14:51:43.484446283+05:30[Asia/Calcutta]")
@Controller
@RequestMapping("/v1")
public class V1ApiController {

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    private final DisbursalService disbursalService;

    @Autowired
    public V1ApiController(ObjectMapper objectMapper, HttpServletRequest request, DisbursalService disbursalService) {
        this.objectMapper = objectMapper;
        this.request = request;
        this.disbursalService = disbursalService;
    }

    @RequestMapping(value = "/_create", method = RequestMethod.POST)
    public ResponseEntity<DisbursalResponse> v1CreatePost(@Parameter(in = ParameterIn.DEFAULT, description = "Request object to create Application in the system", required = true, schema = @Schema()) @Valid @RequestBody DisbursalRequest body) {
        DisbursalResponse response = disbursalService.create(body);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/_search", method = RequestMethod.POST)
    public ResponseEntity<DisbursalResponse> v1SearchPost(@Parameter(in = ParameterIn.DEFAULT, description = "Request object to search Application in the system", required = true, schema = @Schema()) @Valid @RequestBody DisbursalSearchRequest body) {
        DisbursalResponse applicationResponse = disbursalService.search(body);
        return new ResponseEntity<>(applicationResponse, HttpStatus.ACCEPTED);

    }

    @RequestMapping(value = "/_update", method = RequestMethod.POST)
    public ResponseEntity<DisbursalResponse> v1UpdatePost(@Parameter(in = ParameterIn.DEFAULT, description = "Request object to create Application in the system", required = true, schema = @Schema()) @Valid @RequestBody DisbursalRequest body) {
        DisbursalResponse response = disbursalService.update(body);
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }


}
