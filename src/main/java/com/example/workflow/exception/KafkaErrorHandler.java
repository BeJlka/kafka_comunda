package com.example.workflow.exception;

import com.example.workflow.configuration.KafkaProperties;
import com.example.workflow.kafka.producer.KafkaProducer;
import com.example.workflow.util.KafkaUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.kafka.listener.ConsumerAwareListenerErrorHandler;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.example.workflow.util.Constant.COUNT;
import static com.example.workflow.util.Constant.USER_ID;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaErrorHandler implements ConsumerAwareListenerErrorHandler {

        private final KafkaProducer kafkaProducer;
    private final KafkaProperties kafkaProperties;

    @Override
    public Object handleError(Message<?> message,
                              ListenerExecutionFailedException exception,
                              Consumer<?, ?> consumer) {
        if (exception.getCause() instanceof ExternalException) {
            ConsumerRecord<String, String> payload = (ConsumerRecord<String, String>) message.getPayload();
            String value = payload.value();

            Map<String, String> headers = KafkaUtils.headersToMap(payload.headers());

            String count;
            if (headers.containsKey(COUNT)) {
                count = String.valueOf(Integer.parseInt(headers.get(COUNT)) + 1);
            } else {
                count = headers.getOrDefault(COUNT, String.valueOf(0L));
            }

            ExternalException cause = (ExternalException) exception.getCause();
            String userId = cause.getUserId();

            List<Header> sendHeaders = new ArrayList<>();
            sendHeaders.add(new RecordHeader(USER_ID, userId.getBytes()));
            sendHeaders.add(new RecordHeader(COUNT, count.getBytes()));

            kafkaProducer.send(kafkaProperties.getExceptionQueue(), sendHeaders, value);
        } else {
            log.warn("Получена неизвестная ошибка", exception.getCause());
        }

        return null;
    }
}