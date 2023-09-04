package com.example.workflow.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

import static com.example.workflow.util.Constant.MESSAGE;
import static com.example.workflow.util.Constant.USER_ID;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaHandlerOneService {

    private final TaskService taskService;

    public void process(ProcessInstance input, String userId, String message) {
        Map<String, Object> map = Map.of(USER_ID, userId, MESSAGE, message);

        var id = taskService
                .createTaskQuery()
                .processInstanceId(input.getProcessInstanceId())
                .active()
                .singleResult()
                .getId();

        taskService.resolveTask(id, map);
        taskService.complete(id);
    }
}
