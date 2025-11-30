package com.project.bulkorchestration.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateImportJobRequest(
   @NotBlank String sourceFileName,
   @NotNull Boolean sendWelcomeEmail
) {}
