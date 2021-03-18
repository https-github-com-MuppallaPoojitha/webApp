package neu.csye6225.webappone.utils.validation;

import neu.csye6225.webappone.pojo.User;
import neu.csye6225.webappone.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {UserRequestBodyValidator.class})
public class UserRequestBodyValidatorTest {

    @Autowired
    private UserRequestBodyValidator userRequestBodyValidator;
    @MockBean
    private UserService userService;
    @MockBean
    private UsernameValidator usernameValidator;
    @MockBean
    private PasswordValidator passwordValidator;

    private User user = new User("Jane", "Doe", "janeDoe@example.com", "12345aA!");
    private String invalidPassword = "123";
    private String invalidUsername = "jane doe";

    @Before
    public void setUp() {
        Mockito.when(usernameValidator.isEmail(user.getUsername())).thenReturn(true);
        Mockito.when(passwordValidator.isStrongPass(user.getPassword())).thenReturn(true);
        Mockito.when(usernameValidator.isEmail("newUser@example.com")).thenReturn(true);
        Mockito.when(userService.findByUsernameIgnoreCase(user.getUsername())).thenReturn(user);
        Mockito.when(userService.findByUsernameIgnoreCase("newUser@example.com")).thenReturn(null);
    }

    @Test
    public void checkForPostTest() {
        HashMap<String, String> userInput = new HashMap<>();
        // invalid first name
        userInput.put("first_name", "");
        assertEquals(userRequestBodyValidator.checkForPost(userInput).get("error"),
                "Please do not leave your 'first_name' empty!");
        userInput.put("first_name", user.getFirst_name());
        // invalid last name
        userInput.put("last_name", "");
        assertEquals(userRequestBodyValidator.checkForPost(userInput).get("error"),
                "Please do not leave your 'last_name' empty!");
        userInput.put("last_name", user.getLast_name());
        // invalid username
        userInput.put("username", "jane doe");
        assertEquals(userRequestBodyValidator.checkForPost(userInput).get("error"),
                "Please enter a valid email as your username!");
        userInput.put("username", user.getUsername());
        // invalid password
        userInput.put("password", "");
        assertEquals(userRequestBodyValidator.checkForPost(userInput).get("error"),
                "Please enter a valid password that is at least 8 characters long, " +
                        "has at least one upper character, one lower character, one digit, " +
                        "and one special symbol from ~`!@#$%^&*()_-+=<,>.?/");
        userInput.put("password", user.getPassword());
        // username already registered
        assertEquals(userRequestBodyValidator.checkForPost(userInput).get("error"),
                "This username already exists!");
        userInput.put("username", "newUser@example.com");
        // successful post
        assertTrue(userRequestBodyValidator.checkForPost(userInput).containsKey("ok"));
    }

    // @Test
    public void checkForPutTest() {
        HashMap<String, String> userInput = new HashMap<>();
        // non matching username of current logged in
        userInput.put("username", "newUser@example.com");
        assertEquals(userRequestBodyValidator.checkForPut(userInput, user.getUsername()).get("error"),
                "Username cannot be changed. You can only update the information in your logged in account.");
        userInput.put("username", user.getUsername());
        // invalid first name
        userInput.put("first_name", "");
        assertEquals(userRequestBodyValidator.checkForPut(userInput, user.getUsername()).get("error"),
                "Please enter a valid first name for update!");
        userInput.put("first_name", user.getFirst_name());
        // invalid last name
        userInput.put("last_name", "");
        assertEquals(userRequestBodyValidator.checkForPut(userInput, user.getUsername()).get("error"),
                "Please enter a valid last name for update!");
        userInput.put("last_name", user.getLast_name());
        // invalid password
        userInput.put("password", "");
        assertEquals(userRequestBodyValidator.checkForPut(userInput, user.getUsername()).get("error"),
                "Please enter a valid password for update that is at least 8 characters long, " +
                        "has at least one upper character, one lower character, one digit, " +
                        "and one special symbol from ~`!@#$%^&*()_-+=<,>.?/");
        userInput.put("password", user.getPassword());
        // attempt to set read_only fields
        userInput.put("id", "1");
        assertEquals(userRequestBodyValidator.checkForPut(userInput, user.getUsername()).get("error"),
                "User is only allowed to update the first name, the last name or the password.");
        userInput.remove("id");
        // successful put
        assertTrue(userRequestBodyValidator.checkForPut(userInput, user.getUsername()).containsKey("ok"));
    }


}
