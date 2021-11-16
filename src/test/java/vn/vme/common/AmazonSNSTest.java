package vn.vme.common;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.CreateTopicRequest;
import com.amazonaws.services.sns.model.CreateTopicResult;
import com.amazonaws.services.sns.model.DeleteTopicRequest;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.amazonaws.services.sns.model.SubscribeRequest;
import com.amazonaws.services.sns.model.SubscribeResult;

/**
 * @author Ozkan Can
 */
public class AmazonSNSTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(AmazonSNS.class);


    public static void main(String[] args) throws InterruptedException {

		String accessKeyId = "AKIAJAUMDDWSGXUEPATQ";

		String secretAccessKey = "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";
    	
        if(args.length == 0)
        {
            System.out.println("Enter a valid email address as argument");
            //System.exit(-1);
        }

        final String email = "m-tech@gmail.com";

        AWSCredentials credentials = new BasicAWSCredentials(accessKeyId, secretAccessKey);

        AmazonSNS snsClient = AmazonSNSClientBuilder.standard()
				.withCredentials(new AWSStaticCredentialsProvider(credentials))
				.withRegion("ap-southeast-1")
				.build();

        //AmazonSNSClient snsClient = new AmazonSNSClient(new DefaultAWSCredentialsProviderChain());

        
        CreateTopicRequest createTopicRequest = new CreateTopicRequest().withName("MyTopic");
        CreateTopicResult createTopicResult = snsClient.createTopic(createTopicRequest);
        final String topicArn = createTopicResult.getTopicArn();
        LOGGER.info("Topic ARN : "+topicArn);

        SubscribeRequest subscribeRequest =
                new SubscribeRequest()
                .withTopicArn(topicArn)
                .withProtocol("email")
                .withEndpoint(email);

        SubscribeResult subscribeResult = snsClient.subscribe(subscribeRequest);
        String subscriptionArn = subscribeResult.getSubscriptionArn();
        LOGGER.info("Subscription ARN : {}, for Endpoint {}",subscriptionArn, email);
        if(subscriptionArn.equals("confirmation pending")) {
            LOGGER.info("Subscription confirmation pending. Waiting 60s for confirmation ...");
            Thread.sleep(TimeUnit.SECONDS.toMillis(600));
        }


        String msg = "My text published to SNS topic with email endpoint";
        PublishRequest publishRequest = new PublishRequest().withMessage(msg).withTopicArn(topicArn);
        PublishResult publishResult = snsClient.publish(publishRequest);
        LOGGER.info("Message send with id {}."+publishResult.getMessageId());

        DeleteTopicRequest deleteTopicRequest = new DeleteTopicRequest().withTopicArn(topicArn);
        snsClient.deleteTopic(deleteTopicRequest);
    }
}