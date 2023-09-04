package com.example.workflow.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.example.workflow.util.Constant.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaHandlerThreeService {

    private final TaskService taskService;

    public void process(ProcessInstance deadLetterQueue, String userId, String message) {
        Map<String, Object> map = Map.of(USER_ID, userId, MESSAGE, message);

        var id = taskService
                .createTaskQuery()
                .processInstanceId(deadLetterQueue.getProcessInstanceId())
                .active()
                .singleResult()
                .getId();

        taskService.resolveTask(id, map);
        taskService.complete(id);
    }
}
