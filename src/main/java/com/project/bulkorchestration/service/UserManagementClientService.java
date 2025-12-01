package com.project.bulkorchestration.service;

import com.project.bulkorchestration.dto.UserCreateRequest;
import com.project.bulkorchestration.model.UserImportItem;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class UserManagementClientService {

    private static final Logger log = LoggerFactory.getLogger(UserManagementClientService.class);

    private final WebClient userManagementWebClient;

    public void createUser(UserImportItem item) {
        UserCreateRequest request = new UserCreateRequest(
                item.email(),
                item.firstName() + " " + item.lastName(),
                "Temp@123",
                true
        );
        log.info("Calling User Management to create user: {}", item.email());
        userManagementWebClient.post()
                .uri("/api/auth/register")
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                        response.bodyToMono(String.class)
                        .defaultIfEmpty("")
                        .flatMap(body -> Mono.error(
                                new RuntimeException("Failed to create user " + item.email()
                                        + " status=" + response.statusCode()
                                        + " body=" + body)
                        ))).toBodilessEntity()
                .timeout(Duration.ofSeconds(5))
                .block();
    }

}
