package com.project.bulkorchestration.dto;

import com.project.bulkorchestration.model.ImportJobStatus;
import com.project.bulkorchestration.model.ImportJobType;

import java.time.OffsetDateTime;

public record ImportJobResponse(
        Long id,
        ImportJobType type,
        ImportJobStatus status,
        OffsetDateTime createdAt,
        OffsetDateTime startedAt,
        OffsetDateTime finishedAt,
        Integer totalItems,
        Integer processedItems,
        Integer successCount,
        Integer failureCount,
        String sourceFileName,
        String sourceFilePath,
        String errorMessage
) {}
