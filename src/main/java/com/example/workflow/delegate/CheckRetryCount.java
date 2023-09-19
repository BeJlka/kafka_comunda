package com.example.workflow.delegate;

import com.example.workflow.configuration.KafkaProperties;
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
public class CheckRetryCount implements JavaDelegate {

    private final KafkaProperties kafkaProperties;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        Integer count = (Integer) delegateExecution.getVariable(COUNT);
        String maxCount = (String) delegateExecution.getVariable(MAX_COUNT);

        log.info("Проверка кол-во попыток для сообщения с userId: {}", delegateExecution.getVariable(USER_ID));
        if (count < Integer.parseInt(maxCount)) {
            delegateExecution.setVariable(COUNT, count + 1);
            return;
        }

        delegateExecution.setVariable(TOPIC, kafkaProperties.getDeadLetterQueue());
        throw new BpmnError(COUNT_ERROR);
    }
}
