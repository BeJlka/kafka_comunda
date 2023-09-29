package com.example.workflow.delegate;

import com.example.workflow.dto.MessageDto;
import com.example.workflow.dto.UserDto;
import com.example.workflow.exception.ExternalException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import static com.example.workflow.util.Constant.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class External implements JavaDelegate {

    private final FeignClientExternal feignClientExternal;

    private final ObjectMapper objectMapper;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        MessageDto message = (MessageDto) delegateExecution.getVariable(MESSAGE);

        UserDto user;
        try {
            user = feignClientExternal.getUser(message.getLogin());
        } catch (Exception exc) {
            log.warn("Внешняя система не отвечает");
//            log.warn("Внешняя система не отвечает", exc);
            throw new ExternalException((String) delegateExecution.getVariable(USER_ID), exc);
        }

        delegateExecution.setVariable(USER, user);
    }
}
