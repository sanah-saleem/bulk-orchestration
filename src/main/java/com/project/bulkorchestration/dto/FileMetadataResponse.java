package com.project.bulkorchestration.dto;

import java.time.Instant;
import java.util.UUID;

public record FileMetadataResponse(
        UUID id,
        String s3Key,
        String originalFileName,
        String contentType,
        long size,
        String domain,
        String referenceId,
        Instant createdAt,
        String createdBy,
        String status
) {}
