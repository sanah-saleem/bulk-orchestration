package com.project.bulkorchestration.service;

import com.project.bulkorchestration.dto.ImportJobErrorResponse;
import com.project.bulkorchestration.model.ImportJob;
import com.project.bulkorchestration.model.ImportJobError;
import com.project.bulkorchestration.model.UserImportItem;
import com.project.bulkorchestration.repository.ImportJobErrorRepository;
import com.project.bulkorchestration.repository.ImportJobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImportJobErrorService {

    private final ImportJobRepository importJobRepository;
    private final ImportJobErrorRepository errorRepository;

    public void recordError(Long importJobId, UserImportItem item, String errorMessage) {
        ImportJob importJob = importJobRepository.findById(importJobId)
                .orElseThrow(() -> new IllegalArgumentException("ImportJob not found: " + importJobId));

        ImportJobError error = new ImportJobError();
        error.setImportJob(importJob);
        error.setLineNumber(null); // can be filled later if we track it
        error.setEmail(item.email());
        error.setFirstName(item.firstName());
        error.setLastName(item.lastName());
        error.setErrorMessage(errorMessage);
        error.setCreatedAt(OffsetDateTime.now());

        errorRepository.save(error);

    }

    public List<ImportJobErrorResponse> getErrors(Long importJobId) {
        List<ImportJobError> errors = errorRepository.findByImportJobIdOrderByIdAsc(importJobId);
        return errors.stream()
                .map(err -> new ImportJobErrorResponse(
                        err.getId(),
                        err.getLineNumber(),
                        err.getEmail(),
                        err.getFirstName(),
                        err.getLastName(),
                        err.getErrorMessage(),
                        err.getCreatedAt()
                ))
                .toList();
    }

    public long countErrors(Long importJobId) {
        return errorRepository.countByImportJobId(importJobId);
    }

}
