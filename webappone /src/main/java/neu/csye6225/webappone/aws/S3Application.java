package neu.csye6225.webappone.aws;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.AmazonS3;

@Configuration
public class S3Application {

    @Bean
    public AmazonS3 s3client() {
        return AmazonS3ClientBuilder.defaultClient();
    }
}
