package com.project.bulkorchestration.dto;

public record ServiceTokenRequest(
        String clientId,
        String clientSecret
) {}
