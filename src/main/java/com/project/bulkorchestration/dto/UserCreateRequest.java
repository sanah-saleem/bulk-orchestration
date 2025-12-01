package com.project.bulkorchestration.dto;

public record UserCreateRequest(
        String email,
        String fullName,
        String password,
        boolean enabled
) {}
