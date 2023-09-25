package com.example.workflow.delegate;

import com.example.workflow.dto.MessageDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import static com.example.workflow.util.Constant.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class ValidMessage implements JavaDelegate {

    private final ObjectMapper objectMapper;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String message = (String) delegateExecution.getVariable(MESSAGE);

        MessageDto messageDto;

        try {
            messageDto = objectMapper.readValue(message, MessageDto.class);
        } catch (Exception e) {
            log.error("Не получилось преобразовать сообщение в MessageDto", e);
            throw new BpmnError(VALIDATE_ERROR);
        }

        if (StringUtils.isBlank(messageDto.getLogin())) {
            log.error("Поле login не должны быть пустым");
            throw new BpmnError(VALIDATE_ERROR);
        }

        delegateExecution.setVariable(MESSAGE, messageDto);
    }
}
