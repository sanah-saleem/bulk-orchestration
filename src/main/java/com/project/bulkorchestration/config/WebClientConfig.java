package com.project.bulkorchestration.config;

import com.project.bulkorchestration.service.ServiceTokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient userManagementWebClient(
            @Value("${user-management.base-url}") String baseUrl
    ) {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    @Bean
    public WebClient fileStorageWebClient(
            @Value("${file-storage.base-url}") String baseUrl,
            ServiceTokenProvider serviceTokenProvider
    ) {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .filter(((request, next) -> {
                    String token = serviceTokenProvider.getToken();
                    ClientRequest newRequest = ClientRequest.from(request)
                            .header(HttpHeaders.AUTHORIZATION, "Bearer "+token)
                            .build();
                    return next.exchange(newRequest);
                })).build();
    }
}
