package digit.web.controllers;

import digit.web.models.BenefitsDiscardRequest;
import digit.web.models.BenefitsDiscardResponse;
import digit.web.models.BenefitsRegistrationRequest;
import digit.web.models.BenefitsRegistrationResponse;
import digit.web.models.BenefitsSearchCriteria;
import digit.web.models.ErrorResponse;
import org.junit.Test;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import digit.TestConfiguration;

    import java.util.ArrayList;
    import java.util.HashMap;
    import java.util.List;
    import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
* API tests for BenefitsApiController
*/
@Ignore
@RunWith(SpringRunner.class)
@WebMvcTest(BenefitsApiController.class)
@Import(TestConfiguration.class)
public class BenefitsApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void benefitsV1CreatePostSuccess() throws Exception {
        mockMvc.perform(post("/benefits/v1/_create").contentType(MediaType
        .APPLICATION_JSON_UTF8))
        .andExpect(status().isOk());
    }

    @Test
    public void benefitsV1CreatePostFailure() throws Exception {
        mockMvc.perform(post("/benefits/v1/_create").contentType(MediaType
        .APPLICATION_JSON_UTF8))
        .andExpect(status().isBadRequest());
    }

    @Test
    public void benefitsV1DiscardPostSuccess() throws Exception {
        mockMvc.perform(post("/benefits/v1/_discard").contentType(MediaType
        .APPLICATION_JSON_UTF8))
        .andExpect(status().isOk());
    }

    @Test
    public void benefitsV1DiscardPostFailure() throws Exception {
        mockMvc.perform(post("/benefits/v1/_discard").contentType(MediaType
        .APPLICATION_JSON_UTF8))
        .andExpect(status().isBadRequest());
    }

    @Test
    public void benefitsV1SearchPostSuccess() throws Exception {
        mockMvc.perform(post("/benefits/v1/_search").contentType(MediaType
        .APPLICATION_JSON_UTF8))
        .andExpect(status().isOk());
    }

    @Test
    public void benefitsV1SearchPostFailure() throws Exception {
        mockMvc.perform(post("/benefits/v1/_search").contentType(MediaType
        .APPLICATION_JSON_UTF8))
        .andExpect(status().isBadRequest());
    }

    @Test
    public void benefitsV1UpdatePostSuccess() throws Exception {
        mockMvc.perform(post("/benefits/v1/_update").contentType(MediaType
        .APPLICATION_JSON_UTF8))
        .andExpect(status().isOk());
    }

    @Test
    public void benefitsV1UpdatePostFailure() throws Exception {
        mockMvc.perform(post("/benefits/v1/_update").contentType(MediaType
        .APPLICATION_JSON_UTF8))
        .andExpect(status().isBadRequest());
    }

}
