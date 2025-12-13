package com.project.bulkorchestration.service;

import com.project.bulkorchestration.dto.FileMetadataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FileStorageClient {

    @Qualifier("fileStorageWebClient")
    private final WebClient fileStorageWebClient;

    public FileMetadataResponse uploadJobInput(String jobId, MultipartFile file, String createdBy) {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("file", file.getResource())
                .filename(file.getOriginalFilename());
        MultiValueMap<String, HttpEntity<?>> multipartData = builder.build();
        return fileStorageWebClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/files/bulk/job-input/{jobId}")
                        .queryParamIfPresent("createdBy", Optional.ofNullable(createdBy))
                        .build(jobId))
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(multipartData))
                .retrieve().bodyToMono(FileMetadataResponse.class).block();

    }

}
