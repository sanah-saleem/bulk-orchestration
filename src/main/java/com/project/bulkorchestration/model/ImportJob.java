package com.project.bulkorchestration.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity
@Table(name = "import_jobs")
@Getter
@Setter
@NoArgsConstructor
public class ImportJob {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ImportJobType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ImportJobStatus status;

    @Column(nullable = false)
    private OffsetDateTime createdAt;

    private OffsetDateTime startedAt;
    private OffsetDateTime finishedAt;

    private Integer totalItems;
    private Integer processedItems;
    private Integer successCount;
    private Integer failureCount;

    private String sourceFileName;
    private String sourceFilePath;

    @Column(length = 500)
    private String errorMessage;

    private boolean sendWelcomeEmail;

}
