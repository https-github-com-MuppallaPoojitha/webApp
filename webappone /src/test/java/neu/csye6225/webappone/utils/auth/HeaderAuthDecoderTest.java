package neu.csye6225.webappone.utils.auth;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {HeaderAuthDecoder.class})
public class HeaderAuthDecoderTest {

    @Autowired
    private HeaderAuthDecoder headerAuthDecoder;

    @Test
    public void isCorrectDecoded() {
        String username = "jane.doe@exmaple.com";
        String password = "12345aA!";
        String base64Encoded = "Basic amFuZS5kb2VAZXhtYXBsZS5jb206MTIzNDVhQSE=";
        String[] decodedUserInfo = headerAuthDecoder.decode(base64Encoded);

        assertEquals(decodedUserInfo[0], username);
        assertEquals(decodedUserInfo[1], password);
    }
}
