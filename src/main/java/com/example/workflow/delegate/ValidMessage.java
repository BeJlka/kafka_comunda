package com.example.workflow.delegate;

import com.example.workflow.dto.MessageDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.common.header.Headers;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
            delegateExecution.setVariable(VALID, false);
            return;
        }

        if (StringUtils.isBlank(messageDto.getFirstName())
                || StringUtils.isBlank(messageDto.getLastName())) {
            log.error("Поля firstName и lastName не должны быть пустыми");
            delegateExecution.setVariable(VALID, false);
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
            delegateExecution.setVariable(VALID, false);
            return;
        }

        delegateExecution.setVariable(MESSAGE, messageDto);
        delegateExecution.setVariable(VALID, true);
    }
}
