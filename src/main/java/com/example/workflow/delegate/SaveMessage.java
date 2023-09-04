package com.example.workflow.delegate;

import com.example.workflow.model.ReportEntity;
import com.example.workflow.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.example.workflow.util.Constant.MESSAGE;
import static com.example.workflow.util.Constant.USER_ID;

@Slf4j
@Component
@RequiredArgsConstructor
public class SaveMessage implements JavaDelegate {

    private final ReportRepository repository;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        ReportEntity reportEntity = new ReportEntity()
                .setCreateDate(LocalDateTime.now())
                .setRequest((String) delegateExecution.getVariable(MESSAGE))
                .setUserId((String) delegateExecution.getVariable(USER_ID))
                .setDescription("Не валидное сообщение");

        repository.put(reportEntity);

        log.info("Сообщение с userId: {} об ошибке сохранено", delegateExecution.getVariable(USER_ID));
    }
}
