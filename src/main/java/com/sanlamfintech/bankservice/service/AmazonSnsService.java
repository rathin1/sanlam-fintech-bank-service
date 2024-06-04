package com.sanlamfintech.bankservice.service;

import com.amazonaws.services.sns.model.CreateTopicRequest;
import com.amazonaws.services.sns.model.CreateTopicResult;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sanlamfintech.bankservice.client.AmazonSnsClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AmazonSnsService {

    @Value("${aws.sns.topic.arn}")
    private String topicArn;

    private final AmazonSnsClient amazonSnsClient;
    public void publishMessage(Object event) {
        log.info("Publishing message to SNS topic: {}", topicArn);
        try {
            String message = convertToJsonString(event);
            PublishRequest request = new PublishRequest().withMessage(message).withTopicArn(topicArn);

            PublishResult result = amazonSnsClient.getClient().publish(request);
            log.info("Message published to SNS topic: {}. Status: {}", result.getMessageId(), result.getSdkHttpMetadata().getHttpStatusCode());
        } catch (Exception e) {
            log.error("Error publishing message to SNS topic: {}", e.getMessage());
        }
    }

    private String convertToJsonString(Object object) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            log.info("Converting object to JSON string: {}", object.getClass());
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("Error converting object to JSON string: {}", e.getMessage());
            return "";
        }

    }
}
