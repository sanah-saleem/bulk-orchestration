package com.project.bulkorchestration.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties
public record ServiceTokenProperties (
        String baseUrl,
        String serviceClientId,
        String serviceClientSecret,
        String ServiceTokenPath
){}
