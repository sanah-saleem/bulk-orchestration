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
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.batch.infrastructure.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

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
                               FlatFileItemReader<UserImportItem> userImportReader,
                               ItemProcessor<UserImportItem, UserImportItem> userImportProcessor,
                               ItemWriter<UserImportItem> userImportWriter) {
        return new StepBuilder(STEP_NAME, jobRepository)
                .<UserImportItem, UserImportItem>chunk(10)
                .reader(userImportReader)
                .processor(userImportProcessor)
                .writer(userImportWriter)
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemReader<UserImportItem> userImportReader(
            @Value("#{jobParameters['filePath']}") String filePath
    ) {
        log.info("Creating FlatFileItemReader for filePath={}", filePath);
        return new FlatFileItemReaderBuilder<UserImportItem>()
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
    }

    @Bean
    public ItemProcessor<UserImportItem, UserImportItem> userImportProcessor() {
        return item -> {
            log.info("Processing item: {}", item);
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

}
