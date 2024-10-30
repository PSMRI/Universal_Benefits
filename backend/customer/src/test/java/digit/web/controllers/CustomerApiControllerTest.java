package digit.web.controllers;

import digit.web.models.CustomerRequest;
import digit.web.models.CustomerResponse;
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
* API tests for CustomerApiController
*/
@Ignore
@RunWith(SpringRunner.class)
@WebMvcTest(CustomerApiController.class)
@Import(TestConfiguration.class)
public class CustomerApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void customerV1CreatePostSuccess() throws Exception {
        mockMvc.perform(post("/customer/v1/_create").contentType(MediaType
        .APPLICATION_JSON_UTF8))
        .andExpect(status().isOk());
    }

    @Test
    public void customerV1CreatePostFailure() throws Exception {
        mockMvc.perform(post("/customer/v1/_create").contentType(MediaType
        .APPLICATION_JSON_UTF8))
        .andExpect(status().isBadRequest());
    }

    @Test
    public void customerV1UpdatePostSuccess() throws Exception {
        mockMvc.perform(post("/customer/v1/_update").contentType(MediaType
        .APPLICATION_JSON_UTF8))
        .andExpect(status().isOk());
    }

    @Test
    public void customerV1UpdatePostFailure() throws Exception {
        mockMvc.perform(post("/customer/v1/_update").contentType(MediaType
        .APPLICATION_JSON_UTF8))
        .andExpect(status().isBadRequest());
    }

}
