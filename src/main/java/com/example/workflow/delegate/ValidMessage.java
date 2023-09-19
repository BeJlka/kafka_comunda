package com.example.workflow.delegate;

import com.example.workflow.configuration.KafkaProperties;
import com.example.workflow.dto.MessageDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.example.workflow.util.Constant.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class ValidMessage implements JavaDelegate {

    private final ObjectMapper objectMapper;

    private final KafkaProperties kafkaProperties;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String message = (String) delegateExecution.getVariable(MESSAGE);

        MessageDto messageDto;

        try {
            messageDto = objectMapper.readValue(message, MessageDto.class);
        } catch (Exception e) {
            log.error("Не получилось преобразовать сообщение в MessageDto", e);
            checkTopic(delegateExecution);
            throw new BpmnError(VALIDATE_ERROR);
        }

        if (StringUtils.isBlank(messageDto.getFirstName())
                || StringUtils.isBlank(messageDto.getLastName())) {
            log.error("Поля firstName и lastName не должны быть пустыми");
            checkTopic(delegateExecution);
            throw new BpmnError(VALIDATE_ERROR);
        }

        String pattern = (String) delegateExecution.getVariable(PATTERN);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
                StringUtils.isBlank(pattern)
                        ? "yyyy-MM-dd"
                        : pattern);

        try {
            LocalDate.parse(messageDto.getBirthDay(), formatter);
        } catch (Exception exc) {
            log.error("Значение даты рождения {} не соответствует формату yyyy-MM-dd", messageDto.getBirthDay());
            checkTopic(delegateExecution);
            throw new BpmnError(VALIDATE_ERROR);
        }

        delegateExecution.setVariable(MESSAGE, messageDto);
    }

    private void checkTopic(DelegateExecution delegateExecution) {
        if (StringUtils.isBlank((String) delegateExecution.getVariable(TOPIC))) {
            delegateExecution.setVariable(TOPIC, kafkaProperties.getExceptionQueue());
        }
    }
}
