package neu.csye6225.webappone.utils.validation;

import org.apache.commons.validator.EmailValidator;
import org.springframework.stereotype.Component;

@Component
public class UsernameValidator {

    /**
     * This method checks if a provided email has valid address.
     *
     * The EmailValidator library is last updated in Aug 2020, complies with majority
     * of the current RFC protocol.
     *
     * @param email
     * @return
     */
    public boolean isEmail(String email) {
        if (email.isBlank()) return false;
        return EmailValidator.getInstance().isValid(email);
    }
}