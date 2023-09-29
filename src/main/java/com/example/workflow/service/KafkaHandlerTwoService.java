package com.example.workflow.service;

import com.example.workflow.configuration.KafkaProperties;
import com.example.workflow.kafka.producer.KafkaProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.example.workflow.util.Constant.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaHandlerTwoService {

    private final TaskService taskService;

    private final KafkaProperties kafkaProperties;

    private final KafkaProducer kafkaProducer;

    @Value("${application.max-count-retry}")
    private Integer maxCount;

    public void process(ProcessInstance exceptionQueue, String userId, Integer count, String message) {
        if (count >= maxCount) {
            kafkaProducer.send(
                    kafkaProperties.getDeadLetterQueue(),
                    List.of(new RecordHeader(USER_ID, userId.getBytes())),
                    message);
            return;
        }

        Map<String, Object> map = Map.of(USER_ID, userId, COUNT, count, MESSAGE, message);

        var id = taskService
                .createTaskQuery()
                .processInstanceId(exceptionQueue.getProcessInstanceId())
                .active()
                .singleResult()
                .getId();

        taskService.resolveTask(id, map);
        taskService.complete(id);
    }
}
