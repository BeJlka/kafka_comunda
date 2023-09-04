package com.example.workflow.controller;

import com.example.workflow.model.ReportEntity;
import com.example.workflow.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RequiredArgsConstructor
@RestController
public class Controller {

    private final ReportRepository repository;

    @GetMapping("/getAll")
    public Collection<ReportEntity> getAll() {
        return repository.getAll();
    }
}
