package com.project.bulkorchestration.service;

import com.project.bulkorchestration.config.ServiceTokenProperties;
import com.project.bulkorchestration.dto.ServiceTokenRequest;
import com.project.bulkorchestration.dto.ServiceTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class ServiceTokenProvider {

    @Qualifier("userManagementWebClient")
    private final WebClient userManagementWebClient;

    private final ServiceTokenProperties props;
    private volatile String cachedToken;
    private volatile Instant tokenExpiry;

    public synchronized String getToken() {
        if (cachedToken != null && tokenExpiry != null && Instant.now().isBefore(tokenExpiry.minusSeconds(30))) {
            return cachedToken;
        }
        ServiceTokenResponse response = userManagementWebClient
                .post().uri(props.ServiceTokenPath())
                .bodyValue(new ServiceTokenRequest(props.serviceClientId(), props.serviceClientSecret()))
                .retrieve()
                .bodyToMono(ServiceTokenResponse.class)
                .block();
        this.cachedToken = response.jwt();
        this.tokenExpiry = Instant.now().plusSeconds(600);
        return cachedToken;
    }

}
