package com.project.bulkorchestration.repository;

import com.project.bulkorchestration.model.ImportJobError;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImportJobErrorRepository extends JpaRepository<ImportJobError, Long> {
    List<ImportJobError> findByImportJobIdOrderByIdAsc(Long importJobId);
    long countByImportJobId(Long importJobId);
}
