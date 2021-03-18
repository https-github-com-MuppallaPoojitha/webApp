package neu.csye6225.webappone.utils.auth;

import org.springframework.stereotype.Component;

import java.util.Base64;

/**
 * This class decodes the 'Authorization' field in the request header.
 */
@Component
public class HeaderAuthDecoder {
    public String[] decode(String encodedAuth) {
        // remove the 'Basic ' at the beginning
        String encodedNamePass = encodedAuth.substring(6);
        // decode authorization information to 'username:password(BCrypted)'
        String decodedNamePass = new String(Base64.getDecoder().decode(encodedNamePass.getBytes()));

        return decodedNamePass.split(":", 2);
    }
}
