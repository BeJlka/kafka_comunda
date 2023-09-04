package com.example.workflow.repository;

import com.example.workflow.model.ReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaReportRepository extends JpaRepository<ReportEntity, UUID> {
}
