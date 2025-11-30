package com.project.bulkorchestration.service;

import com.project.bulkorchestration.dto.CreateImportJobRequest;
import com.project.bulkorchestration.dto.ImportJobResponse;
import com.project.bulkorchestration.model.ImportJob;
import com.project.bulkorchestration.model.ImportJobStatus;
import com.project.bulkorchestration.model.ImportJobType;
import com.project.bulkorchestration.repository.ImportJobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class ImportJobService {

    private final ImportJobRepository importJobRepository;

    public ImportJobResponse createImportJob(CreateImportJobRequest request) {
        ImportJob job = new ImportJob();
        job.setType(ImportJobType.USER_IMPORT);
        job.setStatus(ImportJobStatus.PENDING);
        job.setCreatedAt(OffsetDateTime.now());
        job.setSourceFileName(request.sourceFileName());
        job.setSendWelcomeEmail(request.sendWelcomeEmail());

        ImportJob saved = importJobRepository.save(job);
        return toResponse(saved);
    }

    public ImportJobResponse getJob(Long id) {
        ImportJob job = importJobRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Import job not found for id: " + id));
        return toResponse(job);
    }

    public ImportJobResponse createImportJobForUploadedFile(
            String originalFileName,
            String storedFilePath,
            boolean sendWelcomeEmail
    ) {
        ImportJob job = new ImportJob();
        job.setType(ImportJobType.USER_IMPORT);
        job.setStatus(ImportJobStatus.PENDING);
        job.setCreatedAt(OffsetDateTime.now());
        job.setSourceFileName(originalFileName);
        job.setSourceFilePath(storedFilePath);
        job.setSendWelcomeEmail(sendWelcomeEmail);

        ImportJob saved = importJobRepository.save(job);
        return toResponse(saved);
    }

    private ImportJobResponse toResponse(ImportJob job) {
        return new ImportJobResponse(
                job.getId(),
                job.getType(),
                job.getStatus(),
                job.getCreatedAt(),
                job.getStartedAt(),
                job.getFinishedAt(),
                job.getTotalItems(),
                job.getProcessedItems(),
                job.getSuccessCount(),
                job.getFailureCount(),
                job.getSourceFileName(),
                job.getSourceFilePath(),
                job.getErrorMessage(),
                job.isSendWelcomeEmail()
        );
    }

}
