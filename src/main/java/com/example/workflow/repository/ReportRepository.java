package com.example.workflow.repository;

import com.example.workflow.model.ReportEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
@RequiredArgsConstructor
public class ReportRepository {

    private final JpaReportRepository jpaReportRepository;

    public ReportEntity put(ReportEntity report) {
        return jpaReportRepository.saveAndFlush(report);
    }

    public Collection<ReportEntity> getAll() {
        return jpaReportRepository.findAll();
    }
}
