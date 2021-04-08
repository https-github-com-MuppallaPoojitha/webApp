package neu.csye6225.webappone.aws;

import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SNSService {
    private AmazonSNS snsClient;
    private final static Logger logger = LoggerFactory.getLogger(SNSService.class);
    String topicArn;

    @Autowired
    public SNSService(){
        this.snsClient =  AmazonSNSClientBuilder.defaultClient();

        //Map<String, String> topicAttributes = new HashMap<>();
        //topicAttributes.put("FifoTopic", "true");
        //topicAttributes.put("ContentBasedDeduplication", "true");
        CreateTopicResult topic = snsClient.createTopic(
                new CreateTopicRequest()
                        .withName("Notification_Email"));
                        //.withAttributes(topicAttributes));
        topicArn = topic.getTopicArn();
    }

    public void postToTopic(String requestType, String recipientEmail, String bookId, String bookName, String author){
        try {
            PublishRequest publishReq = new PublishRequest(topicArn,
                    requestType + "|" + recipientEmail + "|" + bookId  + "|" + bookName + "|" + author +  "|" +
                    "http://prod.poojithamuppalla.me/books/" + bookId);
            PublishResult result = snsClient.publish(publishReq);
            logger.info("Message "+ result.getMessageId() + " is successfully published to SNS Topic 'Notification_Email'.");
        } catch (AmazonSNSException e) {
            logger.error("SNS Exception Warning - " + e.getMessage());
        }
    }
}