package neu.csye6225.webappone.utils.validation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes={PasswordValidator.class})
public class PasswordValidatorTest {

    @Autowired
    PasswordValidator passwordValidator;

    @Test
    public void isStrongPasswordTest() {

        List<String> strongPasswordList = new ArrayList<>();
        strongPasswordList.add("aaaAAA111!");
        strongPasswordList.add("aA1******!!..");
        strongPasswordList.add("12Ncs89*&111!");

        List<String> weakPasswordList = new ArrayList<>();
        weakPasswordList.add("123aA!"); // too short
        weakPasswordList.add("aaaaaaaa"); // missing digit, upper case, special char
        weakPasswordList.add("AAAAAAAA"); // missing digit, lower case, special char
        weakPasswordList.add("aaaaAAAA"); // missing digit, special char
        weakPasswordList.add("aaaAAA111"); // missing special char
        //weakPasswordList.add("aaaAAA111!|"); // invalid special char

        for (String password : strongPasswordList)
            assertTrue(passwordValidator.isStrongPass(password));

        for (String password : weakPasswordList){
            assertFalse(passwordValidator.isStrongPass(password));
        }
    }
}
