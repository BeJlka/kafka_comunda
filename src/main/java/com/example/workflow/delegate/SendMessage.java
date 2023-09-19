package com.example.workflow.delegate;

import com.example.workflow.kafka.producer.KafkaProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.example.workflow.util.Constant.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class SendMessage implements JavaDelegate {

    private final KafkaProducer kafkaProducer;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String topic = (String) delegateExecution.getVariable(TOPIC);
        String userId = (String) delegateExecution.getVariable(USER_ID);
        Integer count = (Integer) delegateExecution.getVariable(COUNT);
        String message = (String) delegateExecution.getVariable(MESSAGE);

        List<Header> headers = new ArrayList<>();
        headers.add(new RecordHeader(USER_ID, userId.getBytes()));

        if (count != null) {
            headers.add(new RecordHeader(COUNT, count.toString().getBytes()));
        }

        kafkaProducer.send(topic, headers, message);

        log.info("Сообщение с userId: {} отправлено в topic: {}", delegateExecution.getVariable(USER_ID), delegateExecution.getVariable(TOPIC));
    }
}
