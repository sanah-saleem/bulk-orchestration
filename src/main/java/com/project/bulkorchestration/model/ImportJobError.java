package com.project.bulkorchestration.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity
@Table(name = "import_job_errors")
@Getter
@Setter
@NoArgsConstructor
public class ImportJobError {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "import_job_id", nullable = false)
    private ImportJob importJob;

    private Integer lineNumber;

    private String email;
    private String firstName;
    private String lastName;

    @Column(length = 1000)
    private String errorMessage;

    private OffsetDateTime createdAt;

}
