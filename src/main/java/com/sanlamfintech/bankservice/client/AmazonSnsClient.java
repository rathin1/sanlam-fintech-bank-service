package com.sanlamfintech.bankservice.client;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Getter
@Service
public class AmazonSnsClient {

    private AmazonSNS client;

    @Value("${aws.sns.region}")
    private String region;

    @PostConstruct
    private void initializeAmazonSnsClient() {
        this.client =
                AmazonSNSClientBuilder.standard()
                        .withCredentials(getAwsCredentialProvider())
                        .withRegion(region)
                        .build();
    }

    //TODO: accessKey and secretKey should be stored in a secure location such as AWS Secrets Manager
    private AWSCredentialsProvider getAwsCredentialProvider() {
        AWSCredentials awsCredentials =
                new BasicAWSCredentials("accessKey", "secretKey");
        return new AWSStaticCredentialsProvider(awsCredentials);
    }

}
