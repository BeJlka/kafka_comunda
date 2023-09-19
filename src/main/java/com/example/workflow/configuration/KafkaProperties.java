package com.example.workflow.configuration;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("application.kafka")
@Slf4j
public class KafkaProperties {
    private String input;
    private String exceptionQueue;
    private String deadLetterQueue;
}
