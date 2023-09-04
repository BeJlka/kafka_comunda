package com.example.workflow.kafka.consumer;

import com.example.workflow.service.KafkaHandlerOneService;
import com.example.workflow.service.KafkaHandlerTwoService;
import com.example.workflow.util.KafkaUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

import static com.example.workflow.util.Constant.COUNT;
import static com.example.workflow.util.Constant.USER_ID;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaHandlerTwoConsumer {

    private final KafkaHandlerTwoService kafkaHandlerTwoService;

    private final RuntimeService runtimeService;

    @KafkaListener(topics = "exception_queue")
    public void consumer(ConsumerRecord<String, String> event) {
        Map<String, String> headers = KafkaUtils.headersToMap(event.headers());

        String userId = headers.get(USER_ID);
        Integer count = headers.get(COUNT) == null
                ? 0
                : Integer.parseInt(headers.get(COUNT));

        log.info("Получено сообщение из очереди exception_queue, с userId: {} и количеством попыток: {}", userId, count);

        ProcessInstance exceptionQueue = runtimeService.startProcessInstanceByKey("exception_queue");

        log.info("Старт процесса exception_queue с id: {}", exceptionQueue.getId());

        kafkaHandlerTwoService.process(exceptionQueue, userId, count, event.value());
    }
}
