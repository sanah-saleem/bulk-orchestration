package com.project.bulkorchestration.config;

import com.project.bulkorchestration.model.UserImportItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.batch.infrastructure.item.ItemStreamReader;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.batch.infrastructure.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.infrastructure.item.support.SynchronizedItemStreamReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class UserImportBatchConfig {

    public static final Logger log = LoggerFactory.getLogger(UserImportBatchConfig.class);

    public static final String JOB_NAME = "userImportJob";
    public static final String STEP_NAME = "userImportStep";

    @Bean
    public Job userImportJob(JobRepository jobRepository, Step userImportStep, UserImportJobListener userImportJobListener) {
        return new JobBuilder(JOB_NAME, jobRepository)
                .listener(userImportJobListener)
                .start(userImportStep)
                .build();
    }

    @Bean
    public Step userImportStep(JobRepository jobRepository,
                               ItemStreamReader<UserImportItem> userImportReader,
                               ItemProcessor<UserImportItem, UserImportItem> userImportProcessor,
                               ItemWriter<UserImportItem> userImportWriter,
                               AsyncTaskExecutor userImportTaskExecutor) {
        return new StepBuilder(STEP_NAME, jobRepository)
                .<UserImportItem, UserImportItem>chunk(50)
                .reader(userImportReader)
                .processor(userImportProcessor)
                .writer(userImportWriter)
                .taskExecutor(userImportTaskExecutor)
                .build();
    }

    @Bean
    @StepScope
    public ItemStreamReader<UserImportItem> userImportReader(
            @Value("#{jobParameters['filePath']}") String filePath
    ) {
        log.info("Creating FlatFileItemReader for filePath={}", filePath);
        FlatFileItemReader<UserImportItem> delegate = new FlatFileItemReaderBuilder<UserImportItem>()
                .name("UserImportReader")
                .resource(new FileSystemResource(filePath))
                .delimited()
                .names("email", "firstName", "lastName")
                .fieldSetMapper(fieldSet -> new UserImportItem(
                        fieldSet.readString("email"),
                        fieldSet.readString("firstName"),
                        fieldSet.readString("lastName")
                ))
                .linesToSkip(1)
                .build();
        return new SynchronizedItemStreamReader<>(delegate);
    }

    @Bean
    public ItemProcessor<UserImportItem, UserImportItem> userImportProcessor() {
        return item -> {
            log.info("Processing item: {} on thread: {}", item.email(), Thread.currentThread().getName());
            return item;
        };
    }

    @Bean
    public ItemWriter<UserImportItem> userImportWriter() {
        return items -> {
            for (UserImportItem item : items) {
                log.info("Writing item: {}", item);
            }
        };
    }

    @Bean
    public AsyncTaskExecutor userImportTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(8);
        executor.setQueueCapacity(50);
        executor.setThreadNamePrefix("user-import_");
        executor.initialize();
        return executor;
    }

}
