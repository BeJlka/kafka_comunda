package com.example.workflow.kafka.consumer;

import com.example.workflow.service.KafkaHandlerThreeService;
import com.example.workflow.util.KafkaUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.example.workflow.util.Constant.USER_ID;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaHandlerThreeConsumer {

    private final RuntimeService runtimeService;

    private final KafkaHandlerThreeService kafkaHandlerThreeService;

    @KafkaListener(topics = "dead_letter_queue")
    public void consumer(ConsumerRecord<String, String> event) {
        Map<String, String> headers = KafkaUtils.headersToMap(event.headers());

        String userId = headers.get(USER_ID);
        log.info("Получено сообщение из очереди dead_letter_queue, с user id: {}", userId);

        ProcessInstance deadLetterQueue = runtimeService.startProcessInstanceByKey("dead_letter_queue");

        log.info("Старт процесса dead_letter_queue с id: {}", deadLetterQueue.getId());

        kafkaHandlerThreeService.process(deadLetterQueue, userId, event.value());
    }
}
