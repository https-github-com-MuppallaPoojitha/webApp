package neu.csye6225.webappone.utils.validation;

import org.springframework.stereotype.Component;

@Component
public class PasswordValidator {

    /**
     * This method checks if a provided password is strong.
     * A strong password is defined as below:
     *      - at least 8 characters long
     *      - at least one digit
     *      - at least one upper case char
     *      - at least one lower case char
     *      - at least one special char from ~`!@#$%^&*()_-+=<,>.?/
     *
     * @param password
     * @return
     */
    public boolean isStrongPass(String password) {
        if (password.isBlank()) return false;
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[~`!@#$%^&*()_\\-+=<,>.?/])(?=\\S+$).{8,}$";
        return password.matches(regex);
    }
}
