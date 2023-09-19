package com.example.workflow.controller;

import com.example.workflow.configuration.KafkaProperties;
import com.example.workflow.kafka.producer.KafkaProducer;
import com.example.workflow.model.ReportEntity;
import com.example.workflow.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
@RestController
public class Controller {

    private final ReportRepository repository;

    private final KafkaProducer kafkaProducer;

    private final KafkaProperties kafkaProperties;

    @GetMapping("/getAll")
    public Collection<ReportEntity> getAll() {
        return repository.getAll();
    }

    @PostMapping("/send")
    public void sendMessage(@RequestBody String message) {
        log.info("Получено сообщение:\n{}", message);
        kafkaProducer.send(kafkaProperties.getInput(), null, message);
    }
}
