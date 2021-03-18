package neu.csye6225.webappone.utils.auth;

import neu.csye6225.webappone.pojo.User;
import neu.csye6225.webappone.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * This class handles user authorization status.
 */
@Component
public class UserAuthorization {
    @Autowired
    private UserService userService;
    @Autowired
    private HeaderAuthDecoder headerAuthDecoder;

    /**
     * This method confirm that the log in credentials from user
     * input matches the user information database.
     *
     * @param authHeader 'Authorization' field in the API request header
     */
    public HashMap<String, String> check(String authHeader) {
        HashMap<String, String> response = new HashMap<>(); // status code, json response key, json response value

        if(authHeader != null && authHeader.startsWith("Basic")) { // check format of provided credentials
            // decode Base64 to {username, password}
            String[] userAuthInfo = headerAuthDecoder.decode(authHeader);
            // find username in database
            User registeredUser = userService.findByUsernameIgnoreCase(userAuthInfo[0]);
            if(registeredUser == null ) { // username not registered
                response.put("status", "401");
                response.put("error", "Please enter a valid username.");
            } else { // username registered
                BCryptPasswordEncoder bEncoder = new BCryptPasswordEncoder();
                if(!bEncoder.matches(userAuthInfo[1], registeredUser.getPassword())) { // password is incorrect
                    response.put("status", "401");
                    response.put("error", "Password is incorrect.");
                } else { //password is correct
                    response = registeredUser.serializeToMap();
                    response.put("status", "200");
                }
            }
        } else { //authentication credentials not provided
            response.put("status", "400");
            response.put("error", "Please log in before viewing your account information.");
        }

        return response;
    }


}
