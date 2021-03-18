package neu.csye6225.webappone.utils.validation;

import neu.csye6225.webappone.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class UserRequestBodyValidator {

    @Autowired
    private UserService userService;
    @Autowired
    private UsernameValidator usernameValidator;
    @Autowired
    private PasswordValidator passwordValidator;

    /**
     * This method checks for the validity of request body for POST method (/v1/user).
     * Note:
     *      - User must enter first name, last name, username and password.
     *      - First and last names must not be blank.
     *      - Username must be a valid email address and unregistered.
     *      - Password must be strong (specific result is listed in the PasswordValidator class).
     *      - Attempt to set any other fields would be ignored.
     *
     * @param userInput
     * @return
     */
    public HashMap<String, String> checkForPost(HashMap<String, String> userInput) {
        HashMap<String, String> response = new HashMap<>();

        if (!userInput.containsKey("first_name") || userInput.get("first_name").isBlank()) {
        // check if first name is not null, not empty and not blank
            response.put("error", "Please do not leave your 'first_name' empty!");
        } else if (!userInput.containsKey("last_name") || userInput.get("last_name").isBlank()) {
        // check if last name is not null, not empty and not blank
            response.put("error", "Please do not leave your 'last_name' empty!");
        } else if (!userInput.containsKey("username") || !usernameValidator.isEmail(userInput.get("username"))) {
        // check if username is a valid email address
            response.put("error", "Please enter a valid email as your username!");
        } else if (!userInput.containsKey("password") || !passwordValidator.isStrongPass(userInput.get("password"))) {
        // check if password is a strong password
            response.put("error", "Please enter a valid password that is at least 8 characters long, " +
                    "has at least one upper character, one lower character, one digit, " +
                    "and one special symbol from ~`!@#$%^&*()_-+=<,>.?/");
        } else if (userService.findByUsernameIgnoreCase(userInput.get("username")) != null) {
        // check if the username is unregistered
            response.put("error", "This username already exists!");
        } else {
        // if request body passes all requirements
            response.put("ok", "");
        }
        return response;
    }

    /**
     * This method checks for the validity of request body for PUT method (/v1/user/self).
     * Note (each field has the same rule as above):
     *      - User is only allowed to change first_name, last_name or password.
     *      - User can enter username, however it must match with authenticated username.
     *      - User may only enter the field that he/she wants to change.
     *      - Attempt to change any other fields would result in an error response.
     *
     */
    public HashMap<String, String> checkForPut(HashMap<String, String> userInput, String currUser) {
        HashMap<String, String> response = new HashMap<>();
        int inputFieldCnt = userInput.size();

        if (userInput.containsKey("username")){
            if (!userInput.get("username").equals(currUser)) {
            // check if user wants to change username
                response.put("error", "Username cannot be changed. " +
                        "You can only update the information in your logged in account.");
            }
            inputFieldCnt--;
        }

        if (userInput.containsKey("first_name")) {
            if (userInput.get("first_name").isBlank()) {
            // check if first name is null, empty or blank
                response.put("error", "Please enter a valid first name for update!");
            }
            inputFieldCnt--;
        }

        if (userInput.containsKey("last_name")) {
            if (userInput.get("last_name").isBlank()) {
            // check if last name is null, empty or blank
                response.put("error", "Please enter a valid last name for update!");
            }
            inputFieldCnt--;
        }

        if (userInput.containsKey("password")) {
            if (!passwordValidator.isStrongPass(userInput.get("password"))) {
            // check if password is strong
                response.put("error", "Please enter a valid password for update that is at least 8 characters long, " +
                        "has at least one upper character, one lower character, one digit, " +
                        "and one special symbol from ~`!@#$%^&*()_-+=<,>.?/");
            }
            inputFieldCnt--;
        }

        if (inputFieldCnt > 0) {
        // check if user intends to change information other than first name, last name or password
            response.put("error", "User is only allowed to update the first name, the last name or the password.");
        }

        if (response.isEmpty()) {
        // if request body passes all requirements
            response.put("ok", "");
        }

        return response;
    }
}
