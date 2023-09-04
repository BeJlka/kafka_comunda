package com.example.workflow.delegate;

import com.example.workflow.dto.MessageDto;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import static com.example.workflow.util.Constant.MESSAGE;

@Slf4j
@Component
public class Work implements JavaDelegate {

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        MessageDto message = (MessageDto) delegateExecution.getVariable(MESSAGE);

        log.info("Поздравляем пользователя {} {} с успешно выполненным процессом", message.getFirstName(), message.getLastName());
    }
}
