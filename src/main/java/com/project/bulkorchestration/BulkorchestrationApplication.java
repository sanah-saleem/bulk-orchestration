package com.project.bulkorchestration;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class BulkorchestrationApplication {

	public static void main(String[] args) {
		SpringApplication.run(BulkorchestrationApplication.class, args);
	}

}
