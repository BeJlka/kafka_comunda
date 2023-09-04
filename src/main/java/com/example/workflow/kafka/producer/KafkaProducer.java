package com.example.workflow.kafka.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public ListenableFuture<SendResult<String, String>> send(String topic, List<Header> headers, String message) {
        ProducerRecord<String, String> record = new ProducerRecord <>(topic, null, "message", message, headers);

        return kafkaTemplate.send(record);
    }
}
