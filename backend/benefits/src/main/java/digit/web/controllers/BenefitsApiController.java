package digit.web.controllers;


import digit.web.models.BenefitsDiscardRequest;
import digit.web.models.BenefitsDiscardResponse;
import digit.web.models.BenefitsRegistrationRequest;
import digit.web.models.BenefitsRegistrationResponse;
import digit.web.models.BenefitsSearchCriteria;
import digit.web.models.ErrorResponse;
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
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-10-23T15:21:03.651111+05:30[Asia/Calcutta]")
@Controller
    @RequestMapping("")
    public class BenefitsApiController{

        private final ObjectMapper objectMapper;

        private final HttpServletRequest request;

        @Autowired
        public BenefitsApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
        }

                @RequestMapping(value="/benefits/v1/_create", method = RequestMethod.POST)
                public ResponseEntity<BenefitsRegistrationResponse> benefitsV1CreatePost(@Parameter(in = ParameterIn.DEFAULT, description = "Details for the new Benefits Registration Application(s) + RequestInfo metadata.", required=true, schema=@Schema()) @Valid @RequestBody BenefitsRegistrationRequest body) {
                        String accept = request.getHeader("Accept");
                            if (accept != null && accept.contains("application/json")) {
                            try {
                            return new ResponseEntity<BenefitsRegistrationResponse>(objectMapper.readValue("{  \"ResponseInfo\" : {    \"ver\" : \"ver\",    \"resMsgId\" : \"resMsgId\",    \"msgId\" : \"msgId\",    \"apiId\" : \"apiId\",    \"ts\" : 0,    \"status\" : \"SUCCESSFUL\"  },  \"Benefit\" : [ {    \"benefitDescription\" : \"benefitDescription\",    \"financialInformation\" : {      \"maxBeneficiaryLimit\" : true,      \"maxBeneficiaryAllowed\" : 7,      \"amountPerBeneficiaryCategory\" : {        \"beneficiaryCaste\" : \"General\",        \"beneficiaryType\" : \"dayscholar\",        \"beneficiaryCategory\" : \"beneficiaryCategory\",        \"beneficiaryAmount\" : 4.145608029883936      },      \"parentOccupation\" : \"parentOccupation\"    },    \"benefitName\" : \"benefitName\",    \"sponsors\" : [ {      \"sponsorShare\" : 9.301444243932576,      \"sponsorEntity\" : \"Non-Profit\",      \"benefitSponsor\" : \"benefitSponsor\"    }, {      \"sponsorShare\" : 9.301444243932576,      \"sponsorEntity\" : \"Non-Profit\",      \"benefitSponsor\" : \"benefitSponsor\"    } ],    \"isDraft\" : true,    \"benefitProvider\" : \"benefitProvider\",    \"eligibility\" : {      \"eligibleChildren\" : 2,      \"studentType\" : \"dayscholar\",      \"annualIncome\" : \"annualIncome\",      \"gender\" : \"Male\",      \"caste\" : [ \"General\", \"General\" ],      \"disability\" : true,      \"attendancePercentage\" : \"attendancePercentage\",      \"marks\" : \"marks\",      \"minQualification\" : \"minQualification\",      \"domicile\" : \"domicile\",      \"class\" : \"class\",      \"fieldOfStudy\" : \"fieldOfStudy\",      \"age\" : 3    },    \"otherTermsAndConditions\" : {      \"autoRenewalApplicable\" : true,      \"applicationDeadlineDate\" : \"2000-01-23\",      \"validTillDate\" : \"2000-01-23\",      \"allowWithOtherBenefit\" : true,      \"allowForOneYearIfFailed\" : true,      \"extendedDeadlineDate\" : \"2000-01-23\"    },    \"benefitId\" : \"benefitId\"  }, {    \"benefitDescription\" : \"benefitDescription\",    \"financialInformation\" : {      \"maxBeneficiaryLimit\" : true,      \"maxBeneficiaryAllowed\" : 7,      \"amountPerBeneficiaryCategory\" : {        \"beneficiaryCaste\" : \"General\",        \"beneficiaryType\" : \"dayscholar\",        \"beneficiaryCategory\" : \"beneficiaryCategory\",        \"beneficiaryAmount\" : 4.145608029883936      },      \"parentOccupation\" : \"parentOccupation\"    },    \"benefitName\" : \"benefitName\",    \"sponsors\" : [ {      \"sponsorShare\" : 9.301444243932576,      \"sponsorEntity\" : \"Non-Profit\",      \"benefitSponsor\" : \"benefitSponsor\"    }, {      \"sponsorShare\" : 9.301444243932576,      \"sponsorEntity\" : \"Non-Profit\",      \"benefitSponsor\" : \"benefitSponsor\"    } ],    \"isDraft\" : true,    \"benefitProvider\" : \"benefitProvider\",    \"eligibility\" : {      \"eligibleChildren\" : 2,      \"studentType\" : \"dayscholar\",      \"annualIncome\" : \"annualIncome\",      \"gender\" : \"Male\",      \"caste\" : [ \"General\", \"General\" ],      \"disability\" : true,      \"attendancePercentage\" : \"attendancePercentage\",      \"marks\" : \"marks\",      \"minQualification\" : \"minQualification\",      \"domicile\" : \"domicile\",      \"class\" : \"class\",      \"fieldOfStudy\" : \"fieldOfStudy\",      \"age\" : 3    },    \"otherTermsAndConditions\" : {      \"autoRenewalApplicable\" : true,      \"applicationDeadlineDate\" : \"2000-01-23\",      \"validTillDate\" : \"2000-01-23\",      \"allowWithOtherBenefit\" : true,      \"allowForOneYearIfFailed\" : true,      \"extendedDeadlineDate\" : \"2000-01-23\"    },    \"benefitId\" : \"benefitId\"  } ]}", BenefitsRegistrationResponse.class), HttpStatus.NOT_IMPLEMENTED);
                            } catch (IOException e) {
                            return new ResponseEntity<BenefitsRegistrationResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
                            }
                            }

                        return new ResponseEntity<BenefitsRegistrationResponse>(HttpStatus.NOT_IMPLEMENTED);
                }

                @RequestMapping(value="/benefits/v1/_discard", method = RequestMethod.POST)
                public ResponseEntity<BenefitsDiscardResponse> benefitsV1DiscardPost(@Parameter(in = ParameterIn.DEFAULT, description = "Details for the Benefits Discard Application(s) + RequestInfo metadata.", required=true, schema=@Schema()) @Valid @RequestBody BenefitsDiscardRequest body) {
                        String accept = request.getHeader("Accept");
                            if (accept != null && accept.contains("application/json")) {
                            try {
                            return new ResponseEntity<BenefitsDiscardResponse>(objectMapper.readValue("{  \"ResponseInfo\" : {    \"ver\" : \"ver\",    \"resMsgId\" : \"resMsgId\",    \"msgId\" : \"msgId\",    \"apiId\" : \"apiId\",    \"ts\" : 0,    \"status\" : \"SUCCESSFUL\"  },  \"message\" : \"message\"}", BenefitsDiscardResponse.class), HttpStatus.NOT_IMPLEMENTED);
                            } catch (IOException e) {
                            return new ResponseEntity<BenefitsDiscardResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
                            }
                            }

                        return new ResponseEntity<BenefitsDiscardResponse>(HttpStatus.NOT_IMPLEMENTED);
                }

                @RequestMapping(value="/benefits/v1/_search", method = RequestMethod.POST)
                public ResponseEntity<BenefitsRegistrationRequest> benefitsV1SearchPost(@Parameter(in = ParameterIn.DEFAULT, description = "Parameter to carry Request metadata in the request body", schema=@Schema()) @Valid @RequestBody BenefitsSearchCriteria body) {
                        String accept = request.getHeader("Accept");
                            if (accept != null && accept.contains("application/json")) {
                            try {
                            return new ResponseEntity<BenefitsRegistrationRequest>(objectMapper.readValue("{  \"Benefit\" : [ {    \"benefitDescription\" : \"benefitDescription\",    \"financialInformation\" : {      \"maxBeneficiaryLimit\" : true,      \"maxBeneficiaryAllowed\" : 7,      \"amountPerBeneficiaryCategory\" : {        \"beneficiaryCaste\" : \"General\",        \"beneficiaryType\" : \"dayscholar\",        \"beneficiaryCategory\" : \"beneficiaryCategory\",        \"beneficiaryAmount\" : 4.145608029883936      },      \"parentOccupation\" : \"parentOccupation\"    },    \"benefitName\" : \"benefitName\",    \"sponsors\" : [ {      \"sponsorShare\" : 9.301444243932576,      \"sponsorEntity\" : \"Non-Profit\",      \"benefitSponsor\" : \"benefitSponsor\"    }, {      \"sponsorShare\" : 9.301444243932576,      \"sponsorEntity\" : \"Non-Profit\",      \"benefitSponsor\" : \"benefitSponsor\"    } ],    \"isDraft\" : true,    \"benefitProvider\" : \"benefitProvider\",    \"eligibility\" : {      \"eligibleChildren\" : 2,      \"studentType\" : \"dayscholar\",      \"annualIncome\" : \"annualIncome\",      \"gender\" : \"Male\",      \"caste\" : [ \"General\", \"General\" ],      \"disability\" : true,      \"attendancePercentage\" : \"attendancePercentage\",      \"marks\" : \"marks\",      \"minQualification\" : \"minQualification\",      \"domicile\" : \"domicile\",      \"class\" : \"class\",      \"fieldOfStudy\" : \"fieldOfStudy\",      \"age\" : 3    },    \"otherTermsAndConditions\" : {      \"autoRenewalApplicable\" : true,      \"applicationDeadlineDate\" : \"2000-01-23\",      \"validTillDate\" : \"2000-01-23\",      \"allowWithOtherBenefit\" : true,      \"allowForOneYearIfFailed\" : true,      \"extendedDeadlineDate\" : \"2000-01-23\"    },    \"benefitId\" : \"benefitId\"  }, {    \"benefitDescription\" : \"benefitDescription\",    \"financialInformation\" : {      \"maxBeneficiaryLimit\" : true,      \"maxBeneficiaryAllowed\" : 7,      \"amountPerBeneficiaryCategory\" : {        \"beneficiaryCaste\" : \"General\",        \"beneficiaryType\" : \"dayscholar\",        \"beneficiaryCategory\" : \"beneficiaryCategory\",        \"beneficiaryAmount\" : 4.145608029883936      },      \"parentOccupation\" : \"parentOccupation\"    },    \"benefitName\" : \"benefitName\",    \"sponsors\" : [ {      \"sponsorShare\" : 9.301444243932576,      \"sponsorEntity\" : \"Non-Profit\",      \"benefitSponsor\" : \"benefitSponsor\"    }, {      \"sponsorShare\" : 9.301444243932576,      \"sponsorEntity\" : \"Non-Profit\",      \"benefitSponsor\" : \"benefitSponsor\"    } ],    \"isDraft\" : true,    \"benefitProvider\" : \"benefitProvider\",    \"eligibility\" : {      \"eligibleChildren\" : 2,      \"studentType\" : \"dayscholar\",      \"annualIncome\" : \"annualIncome\",      \"gender\" : \"Male\",      \"caste\" : [ \"General\", \"General\" ],      \"disability\" : true,      \"attendancePercentage\" : \"attendancePercentage\",      \"marks\" : \"marks\",      \"minQualification\" : \"minQualification\",      \"domicile\" : \"domicile\",      \"class\" : \"class\",      \"fieldOfStudy\" : \"fieldOfStudy\",      \"age\" : 3    },    \"otherTermsAndConditions\" : {      \"autoRenewalApplicable\" : true,      \"applicationDeadlineDate\" : \"2000-01-23\",      \"validTillDate\" : \"2000-01-23\",      \"allowWithOtherBenefit\" : true,      \"allowForOneYearIfFailed\" : true,      \"extendedDeadlineDate\" : \"2000-01-23\"    },    \"benefitId\" : \"benefitId\"  } ],  \"RequestInfo\" : {    \"userInfo\" : {      \"pwdExpiryDate\" : 7,      \"correspondenceCity\" : \"correspondenceCity\",      \"accountLockedDate\" : 1,      \"gender\" : \"gender\",      \"signature\" : \"signature\",      \"mobileNumber\" : \"mobileNumber\",      \"roles\" : [ {        \"tenantId\" : \"tenantId\",        \"name\" : \"name\",        \"description\" : \"description\",        \"id\" : \"id\"      }, {        \"tenantId\" : \"tenantId\",        \"name\" : \"name\",        \"description\" : \"description\",        \"id\" : \"id\"      } ],      \"correspondencePincode\" : \"correspondencePincode\",      \"emailId\" : \"emailId\",      \"locale\" : \"locale\",      \"type\" : \"type\",      \"uuid\" : \"uuid\",      \"correspondenceAddress\" : \"correspondenceAddress\",      \"bloodGroup\" : \"bloodGroup\",      \"password\" : \"password\",      \"alternateMobileNumber\" : \"alternateMobileNumber\",      \"id\" : 6,      \"permanentAddress\" : \"permanentAddress\",      \"pan\" : \"pan\",      \"relationship\" : \"relationship\",      \"accountLocked\" : true,      \"altContactNumber\" : \"altContactNumber\",      \"identificationMark\" : \"identificationMark\",      \"lastModifiedDate\" : \"2000-01-23\",      \"lastModifiedBy\" : 5,      \"fatherOrHusbandName\" : \"fatherOrHusbandName\",      \"active\" : true,      \"photo\" : \"photo\",      \"userName\" : \"userName\",      \"aadhaarNumber\" : \"aadhaarNumber\",      \"createdDate\" : \"2000-01-23\",      \"createdBy\" : 5,      \"otpReference\" : \"otpReference\",      \"dob\" : 2,      \"tenantId\" : \"tenantId\",      \"name\" : \"name\",      \"salutation\" : \"salutation\",      \"permanentCity\" : \"permanentCity\",      \"permanentPincode\" : \"permanentPincode\"    },    \"ver\" : \"ver\",    \"requesterId\" : \"requesterId\",    \"authToken\" : \"authToken\",    \"action\" : \"action\",    \"msgId\" : \"msgId\",    \"correlationId\" : \"correlationId\",    \"apiId\" : \"apiId\",    \"did\" : \"did\",    \"key\" : \"key\",    \"ts\" : 0  }}", BenefitsRegistrationRequest.class), HttpStatus.NOT_IMPLEMENTED);
                            } catch (IOException e) {
                            return new ResponseEntity<BenefitsRegistrationRequest>(HttpStatus.INTERNAL_SERVER_ERROR);
                            }
                            }

                        return new ResponseEntity<BenefitsRegistrationRequest>(HttpStatus.NOT_IMPLEMENTED);
                }

                @RequestMapping(value="/benefits/v1/_update", method = RequestMethod.POST)
                public ResponseEntity<BenefitsRegistrationResponse> benefitsV1UpdatePost(@Parameter(in = ParameterIn.DEFAULT, description = "Details for the updated Benefits Registration Applications + RequestInfo metadata.", required=true, schema=@Schema()) @Valid @RequestBody BenefitsRegistrationRequest body) {
                        String accept = request.getHeader("Accept");
                            if (accept != null && accept.contains("application/json")) {
                            try {
                            return new ResponseEntity<BenefitsRegistrationResponse>(objectMapper.readValue("{  \"ResponseInfo\" : {    \"ver\" : \"ver\",    \"resMsgId\" : \"resMsgId\",    \"msgId\" : \"msgId\",    \"apiId\" : \"apiId\",    \"ts\" : 0,    \"status\" : \"SUCCESSFUL\"  },  \"Benefit\" : [ {    \"benefitDescription\" : \"benefitDescription\",    \"financialInformation\" : {      \"maxBeneficiaryLimit\" : true,      \"maxBeneficiaryAllowed\" : 7,      \"amountPerBeneficiaryCategory\" : {        \"beneficiaryCaste\" : \"General\",        \"beneficiaryType\" : \"dayscholar\",        \"beneficiaryCategory\" : \"beneficiaryCategory\",        \"beneficiaryAmount\" : 4.145608029883936      },      \"parentOccupation\" : \"parentOccupation\"    },    \"benefitName\" : \"benefitName\",    \"sponsors\" : [ {      \"sponsorShare\" : 9.301444243932576,      \"sponsorEntity\" : \"Non-Profit\",      \"benefitSponsor\" : \"benefitSponsor\"    }, {      \"sponsorShare\" : 9.301444243932576,      \"sponsorEntity\" : \"Non-Profit\",      \"benefitSponsor\" : \"benefitSponsor\"    } ],    \"isDraft\" : true,    \"benefitProvider\" : \"benefitProvider\",    \"eligibility\" : {      \"eligibleChildren\" : 2,      \"studentType\" : \"dayscholar\",      \"annualIncome\" : \"annualIncome\",      \"gender\" : \"Male\",      \"caste\" : [ \"General\", \"General\" ],      \"disability\" : true,      \"attendancePercentage\" : \"attendancePercentage\",      \"marks\" : \"marks\",      \"minQualification\" : \"minQualification\",      \"domicile\" : \"domicile\",      \"class\" : \"class\",      \"fieldOfStudy\" : \"fieldOfStudy\",      \"age\" : 3    },    \"otherTermsAndConditions\" : {      \"autoRenewalApplicable\" : true,      \"applicationDeadlineDate\" : \"2000-01-23\",      \"validTillDate\" : \"2000-01-23\",      \"allowWithOtherBenefit\" : true,      \"allowForOneYearIfFailed\" : true,      \"extendedDeadlineDate\" : \"2000-01-23\"    },    \"benefitId\" : \"benefitId\"  }, {    \"benefitDescription\" : \"benefitDescription\",    \"financialInformation\" : {      \"maxBeneficiaryLimit\" : true,      \"maxBeneficiaryAllowed\" : 7,      \"amountPerBeneficiaryCategory\" : {        \"beneficiaryCaste\" : \"General\",        \"beneficiaryType\" : \"dayscholar\",        \"beneficiaryCategory\" : \"beneficiaryCategory\",        \"beneficiaryAmount\" : 4.145608029883936      },      \"parentOccupation\" : \"parentOccupation\"    },    \"benefitName\" : \"benefitName\",    \"sponsors\" : [ {      \"sponsorShare\" : 9.301444243932576,      \"sponsorEntity\" : \"Non-Profit\",      \"benefitSponsor\" : \"benefitSponsor\"    }, {      \"sponsorShare\" : 9.301444243932576,      \"sponsorEntity\" : \"Non-Profit\",      \"benefitSponsor\" : \"benefitSponsor\"    } ],    \"isDraft\" : true,    \"benefitProvider\" : \"benefitProvider\",    \"eligibility\" : {      \"eligibleChildren\" : 2,      \"studentType\" : \"dayscholar\",      \"annualIncome\" : \"annualIncome\",      \"gender\" : \"Male\",      \"caste\" : [ \"General\", \"General\" ],      \"disability\" : true,      \"attendancePercentage\" : \"attendancePercentage\",      \"marks\" : \"marks\",      \"minQualification\" : \"minQualification\",      \"domicile\" : \"domicile\",      \"class\" : \"class\",      \"fieldOfStudy\" : \"fieldOfStudy\",      \"age\" : 3    },    \"otherTermsAndConditions\" : {      \"autoRenewalApplicable\" : true,      \"applicationDeadlineDate\" : \"2000-01-23\",      \"validTillDate\" : \"2000-01-23\",      \"allowWithOtherBenefit\" : true,      \"allowForOneYearIfFailed\" : true,      \"extendedDeadlineDate\" : \"2000-01-23\"    },    \"benefitId\" : \"benefitId\"  } ]}", BenefitsRegistrationResponse.class), HttpStatus.NOT_IMPLEMENTED);
                            } catch (IOException e) {
                            return new ResponseEntity<BenefitsRegistrationResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
                            }
                            }

                        return new ResponseEntity<BenefitsRegistrationResponse>(HttpStatus.NOT_IMPLEMENTED);
                }

        }
