package com.example.workflow.delegate;

import com.example.workflow.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import static com.example.workflow.util.Constant.USER;

@Slf4j
@Component
public class Work implements JavaDelegate {

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        UserDto user = (UserDto) delegateExecution.getVariable(USER);

        log.info("Поздравляем пользователя {} {} с успешно выполненным процессом", user.getFirstName(), user.getLastName());
    }
}
