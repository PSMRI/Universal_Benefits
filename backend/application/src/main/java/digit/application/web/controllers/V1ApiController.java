package digit.application.web.controllers;


import digit.application.service.ApplicationService;
import digit.application.web.models.ApplicationRequest;
import digit.application.web.models.ApplicationResponse;
import digit.application.web.models.ApplicationSearchRequest;
import digit.application.web.models.ErrorRes;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.*;

import jakarta.validation.constraints.*;
import jakarta.validation.Valid;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;

@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-09-09T14:51:43.484446283+05:30[Asia/Calcutta]")
@Controller
@RequestMapping("/v1")
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

    @RequestMapping(value = "/_create", method = RequestMethod.POST)
    public ResponseEntity<ApplicationResponse> v1CreatePost(@Parameter(in = ParameterIn.DEFAULT, description = "Request object to create Application in the system", required = true, schema = @Schema()) @Valid @RequestBody ApplicationRequest body) {
        ApplicationResponse response = applicationService.create(body);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
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

}
