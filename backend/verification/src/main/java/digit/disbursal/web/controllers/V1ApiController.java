package digit.disbursal.web.controllers;


import digit.disbursal.service.VerificationService;
import digit.disbursal.web.models.ApplicationRequest;
import digit.disbursal.web.models.ApplicationResponse;
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

import jakarta.validation.Valid;
import jakarta.servlet.http.HttpServletRequest;

@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-09-09T14:51:43.484446283+05:30[Asia/Calcutta]")
@Controller
@RequestMapping("/v1")
public class V1ApiController {

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    private final VerificationService verificationService;

    @Autowired
    public V1ApiController(ObjectMapper objectMapper, HttpServletRequest request, VerificationService verificationService) {
        this.objectMapper = objectMapper;
        this.request = request;
        this.verificationService = verificationService;
    }

    @RequestMapping(value = "/_verify", method = RequestMethod.POST)
    public ResponseEntity<ApplicationResponse> v1CreatePost(@Parameter(in = ParameterIn.DEFAULT, description = "Request object to create Application in the system", required = true, schema = @Schema()) @Valid @RequestBody ApplicationRequest body) {
        ApplicationResponse response = verificationService.verifyApplication(body);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
