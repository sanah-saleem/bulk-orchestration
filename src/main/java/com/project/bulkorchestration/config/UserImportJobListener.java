package com.project.bulkorchestration.config;

import com.project.bulkorchestration.model.ImportJob;
import com.project.bulkorchestration.model.ImportJobStatus;
import com.project.bulkorchestration.repository.ImportJobRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListener;
import org.springframework.batch.core.step.StepExecution;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserImportJobListener implements JobExecutionListener {

    private static final Logger log = LoggerFactory.getLogger(UserImportJobListener.class);

    private final ImportJobRepository importJobRepository;

    @Override
    public void afterJob(JobExecution jobExecution) {
        Long importJobId = jobExecution.getJobParameters().getLong("importJobId");
        if (importJobId == null ) {
            log.warn("Job finished without importJobId parameter, skipping ImportJob update");
            return;
        }
        ImportJob importJob = importJobRepository.findById(importJobId).orElse(null);
        if (importJob == null) {
            log.warn("ImportJob with id={} not found, cannot update status", importJobId);
            return;
        }
        long totalRead = jobExecution.getStepExecutions().stream()
                .mapToLong(StepExecution::getReadCount)
                .sum();
        long totalWritten = jobExecution.getStepExecutions().stream()
                .mapToLong(StepExecution::getWriteCount)
                .sum();
        long failureCount = Math.max(0, totalRead - totalWritten);
        importJob.setFinishedAt(OffsetDateTime.now());
        importJob.setTotalItems((int)totalRead);
        importJob.setProcessedItems((int)totalWritten);
        importJob.setSuccessCount((int)totalWritten);
        importJob.setFailureCount((int)failureCount);
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            importJob.setStatus(ImportJobStatus.COMPLETED);
            importJob.setErrorMessage(null);
        }
         else {
             importJob.setStatus(ImportJobStatus.FAILED);
             String failures = jobExecution.getAllFailureExceptions()
                     .stream().map(Throwable::getMessage)
                     .collect(Collectors.joining(" | "));
             importJob.setErrorMessage(failures);
        }
         importJobRepository.save(importJob);
         log.info("Updated ImportJob id={} -> status={}, totalRead={}, written={}, failureCount={}",
                importJobId, importJob.getStatus(), importJob.getTotalItems(),
                 importJob.getSuccessCount(), importJob.getFailureCount());

    }

}
