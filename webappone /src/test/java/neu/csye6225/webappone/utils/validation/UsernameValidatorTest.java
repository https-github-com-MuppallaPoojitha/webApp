package neu.csye6225.webappone.utils.validation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {UsernameValidator.class})
public class UsernameValidatorTest {

    @Autowired
    UsernameValidator usernameValidator;

    @Test
    public void isEmailTest() {
        List<String> validEmailList = new ArrayList<>();
        validEmailList.add("jane.doe@example.com");
        validEmailList.add("1@husky.NEU.edu");
        validEmailList.add("\" \"@example.org");
        validEmailList.add("user%example.com@example.org");

        List<String> invalidEmailList = new ArrayList<>();
        invalidEmailList.add("Abc.example.com");
        invalidEmailList.add("A@b@c@example.com");
        invalidEmailList.add("just\"not\"right@example.com");
        invalidEmailList.add("i_like_underscore@not_allowed_here.example.com");

        for (String email : validEmailList)
            assertTrue(usernameValidator.isEmail(email));

        for (String email : invalidEmailList){
            assertFalse(usernameValidator.isEmail(email));
        }
    }

}
