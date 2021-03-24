package neu.csye6225.webappone.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.timgroup.statsd.StatsDClient;
import neu.csye6225.webappone.pojo.User;
import neu.csye6225.webappone.service.UserService;
import neu.csye6225.webappone.utils.auth.UserAuthorization;
import neu.csye6225.webappone.utils.validation.UserRequestBodyValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.HashMap;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;
    @MockBean
    private UserAuthorization userAuthorization;
    @MockBean
    private UserRequestBodyValidator userRequestBodyValidator;
    @MockBean
    private StatsDClient statsd;

    private User user = new User("Jane", "Doe", "janeDoe@example.com", "12345aA!");
    HashMap<String, String> authRes = new HashMap<>();
    HashMap<String, String> reqBodyRes = new HashMap<>();
    HashMap<String, String> emptyMap = new HashMap<>();


    @Before
    public void setup() {
        authRes.put("username", user.getUsername());
        authRes.put("status", "200");
        given(userAuthorization.check(null)).willReturn(authRes);
        given(userService.findByUsernameIgnoreCase(user.getUsername())).willReturn(user);
        given(userRequestBodyValidator.checkForPut(reqBodyRes, user.getUsername())).willReturn(reqBodyRes);
        given(userRequestBodyValidator.checkForPut(reqBodyRes, user.getUsername())).willReturn(reqBodyRes);
        given(userRequestBodyValidator.checkForPost(emptyMap)).willReturn(reqBodyRes);
    }

    @Test
    public void getUserInfoTest_valid() throws Exception {
        this.mvc.perform(MockMvcRequestBuilders.get("/v1/user/self")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{'username': 'janeDoe@example.com'}"));
    }

    @Test
    public void updateUserInfoTest_valid() throws Exception {
        MockHttpServletRequestBuilder mockHttpReq = MockMvcRequestBuilders
                .put("/v1/user/self")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{}");
        this.mvc.perform(mockHttpReq)
                .andExpect(status().isNoContent());
    }

    @Test
    public void registerUserTest_valid() throws Exception {
        MockHttpServletRequestBuilder mockHttpReq = MockMvcRequestBuilders
                .post("/v1/user")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"first_name\": \"Jane\", \"last_name\": \"Doe\", " +
                        "\"username\": \"janeDoe@example.com\", \"password\": \"12345aA.\"}");
        this.mvc.perform(mockHttpReq)
                .andExpect(status().isCreated());
    }


}
