package com.example.workflow.kafka.consumer;

import com.example.workflow.service.KafkaHandlerOneService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaHandlerOneConsumer {

    private final KafkaHandlerOneService kafkaHandlerOneService;

    private final RuntimeService runtimeService;


    @KafkaListener(topics = "input", errorHandler = "kafkaErrorHandler")
    public void consumer(ConsumerRecord<String, String> event) throws JsonProcessingException {
        String userId = UUID.randomUUID().toString();
        log.info("Получено сообщение из очереди input, ему присвоен userId: {}", userId);

        ProcessInstance input = runtimeService.startProcessInstanceByKey("input");

        log.info("Старт процесса input с id: {}", input.getId());

        kafkaHandlerOneService.process(input, userId, event.value());
    }
}
