package neu.csye6225.webappone.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import neu.csye6225.webappone.pojo.User;
import neu.csye6225.webappone.service.UserService;
import neu.csye6225.webappone.utils.auth.UserAuthorization;
import neu.csye6225.webappone.utils.validation.UserRequestBodyValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCrypt;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

@Controller
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserAuthorization userAuthorization;
    @Autowired
    private UserRequestBodyValidator userRequestBodyValidator;

    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.000'Z'");

    /**
     * This method handles the GET call to /v1/user/self which gets user information.
     * It checks user's authentication credentials.
     */
    @GetMapping(value = "/v1/user/self", produces = "application/json")
    public @ResponseBody ResponseEntity<?> getUserInfo(HttpServletRequest request) {
        // check for authorization
        String header = request.getHeader("Authorization");
        HashMap<String, String> authResult = userAuthorization.check(header);

        // return request reponse
        if (authResult.get("status").equals("400")) { // return http 400 if authentication format is invalid
            authResult.remove("status");
            return new ResponseEntity<>(authResult,HttpStatus.BAD_REQUEST);
        } else if (authResult.get("status").equals("401")) { // return http 401 if username or password is invalid
            authResult.remove("status");
            return new ResponseEntity<>(authResult,HttpStatus.UNAUTHORIZED);
        } else { // valid authentication, return http 200 and user information as JSON
            authResult.remove("status");
            return new ResponseEntity<>(authResult, HttpStatus.OK);
        }
    }

    /**
     * This method handles the PUT call to /v1/user/self which updates user information.
     * It checks for user's authentication credentials and the request body format.
     */
    @PutMapping(value = "/v1/user/self", produces = "application/json", consumes = "application/json")
    public @ResponseBody ResponseEntity<?> updateUser(HttpServletRequest request, @RequestBody String jsonUser)
            throws JsonProcessingException {
        // check for authorization
        String header = request.getHeader("Authorization");
        HashMap<String, String> authResult = userAuthorization.check(header);
        if (authResult.get("status").equals("400")) { // return http 400 if authentication format is invalid
            authResult.remove("status");
            return new ResponseEntity<>(authResult,HttpStatus.BAD_REQUEST);
        } else if (authResult.get("status").equals("401")) { // return http 401 if username or password is invalid
            authResult.remove("status");
            return new ResponseEntity<>(authResult,HttpStatus.UNAUTHORIZED);
        }

        // valid authentication, get the logged in User object
        User registeredUser = userService.findByUsernameIgnoreCase(authResult.get("username"));
        // new user information as hashmap
        HashMap<String, String> newUserInfo = new ObjectMapper().readValue(jsonUser, new TypeReference<>(){});

        // check request body validity
        HashMap<String, String> reqBodyCheckResult =
                userRequestBodyValidator.checkForPut(newUserInfo, registeredUser.getUsername());
        if (reqBodyCheckResult.containsKey("error")) { // return http 400 if request body is invalid
            return new ResponseEntity<>(reqBodyCheckResult, HttpStatus.BAD_REQUEST);
        }

        // update changed fields after request body is valid
        if (newUserInfo.containsKey("first_name")) { // update first name if user intends to
            registeredUser.setFirst_name(newUserInfo.get("first_name"));
        }
        if (newUserInfo.containsKey("last_name")) { // update last name if user intends to
            registeredUser.setLast_name(newUserInfo.get("last_name"));
        }
        if (newUserInfo.containsKey("password")) { // update password name if user intends to
            String pw_hash = BCrypt.hashpw(newUserInfo.get("password"), BCrypt.gensalt());
            registeredUser.setPassword(pw_hash);
        }

        // save the updated user information with updated timestamp and return response http 204
        String currTimestamp = formatter.format(new Date());
        registeredUser.setAccount_updated(currTimestamp);
        userService.save(registeredUser);
        HashMap<String, String> response = new HashMap<>(); // response
        response.put("message", "You have successfully updated your information!");
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }

    /**
     * This method handles the POST call to /v1/user which register a new user.
     * It checks for user's request body format.
     */
    @PostMapping(value = "/v1/user", produces = "application/json", consumes = "application/json")
    public @ResponseBody ResponseEntity<?> registerUser(@RequestBody String jsonUser) throws JsonProcessingException {
        // converts inputted user information from JSON to HashMap
        HashMap<String, String> mapUser = new ObjectMapper().readValue(jsonUser, new TypeReference<>(){});

        // check request body validity
        HashMap<String, String> reqBodyCheckResult = userRequestBodyValidator.checkForPost(mapUser);
        if (reqBodyCheckResult.containsKey("error")) { // return http 400 if request body is invalid
            return new ResponseEntity<>(reqBodyCheckResult, HttpStatus.BAD_REQUEST);
        }

        // create User object if all request body is valid
        String bcryptPass = BCrypt.hashpw(mapUser.get("password"), BCrypt.gensalt());
        User tmpUser = new User(mapUser.get("first_name"), mapUser.get("last_name"),
                mapUser.get("username"), bcryptPass);
        String currTimestamp = formatter.format(new Date());
        tmpUser.setAccount_created(currTimestamp);
        tmpUser.setAccount_updated(currTimestamp);

        // register the user and return response http 201
        userService.save(tmpUser);
        HashMap<String, String> response = tmpUser.serializeToMap();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}