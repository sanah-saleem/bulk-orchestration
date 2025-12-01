package com.project.bulkorchestration.dto;

import java.time.OffsetDateTime;

public record ImportJobErrorResponse(
        Long id,
        Integer lineNumber,
        String email,
        String firstName,
        String lastName,
        String errorMessage,
        OffsetDateTime createdAt
) {}
