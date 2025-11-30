package com.project.bulkorchestration.service;

import com.project.bulkorchestration.config.UserImportBatchConfig;
import com.project.bulkorchestration.model.ImportJob;
import com.project.bulkorchestration.model.ImportJobStatus;
import com.project.bulkorchestration.repository.ImportJobRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class UserImportJobLaunchService {

    private static final Logger log = LoggerFactory.getLogger(UserImportJobLaunchService.class);

    private final JobOperator jobOperator;
    private final Job userImportJob;
    private final ImportJobRepository importJobRepository;

    public JobExecution launchForImportJob(Long importJobId) throws Exception {
        ImportJob importJob = importJobRepository.findById(importJobId)
                .orElseThrow(() -> new IllegalArgumentException("ImportJob not found: " + importJobId));
        if (importJob.getSourceFilePath() == null) {
            throw new IllegalStateException("ImportJob " + importJobId + " has no sourceFilePath");
        }
        importJob.setStatus(ImportJobStatus.RUNNING);
        importJob.setStartedAt(OffsetDateTime.now());
        importJobRepository.save(importJob);

        JobParameters parameters = new JobParametersBuilder()
                .addString("filePath", importJob.getSourceFilePath())
                .addLong("run.id", System.currentTimeMillis())
                .addLong("importJobId", importJobId)
                .toJobParameters();

        log.info("Launching job {} for importJobId={} with file={}",
                UserImportBatchConfig.JOB_NAME, importJobId, importJob.getSourceFilePath());

        JobExecution execution = jobOperator.start(userImportJob, parameters);
        log.info("Job finished with status {}", execution.getStatus());
        return execution;
    }

}
