package com.project.bulkorchestration.repository;

import com.project.bulkorchestration.model.ImportJob;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImportJobRepository extends JpaRepository<ImportJob, Long> {
}
