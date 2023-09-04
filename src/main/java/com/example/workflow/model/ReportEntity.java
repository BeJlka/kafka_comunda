package com.example.workflow.model;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Accessors(chain = true)
public class ReportEntity {

    @Id
    @GeneratedValue(generator = "UUID_GENERATOR")
    private UUID id;

    private String description;

    private String userId;

    private String request;

    private LocalDateTime createDate;
}
