package com.project.bulkorchestration.controller;

import com.project.bulkorchestration.dto.CreateImportJobRequest;
import com.project.bulkorchestration.dto.ImportJobResponse;
import com.project.bulkorchestration.service.ImportJobService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/imports")
@RequiredArgsConstructor
public class ImportJobController {

    private final ImportJobService importJobService;

    @PostMapping
    public ImportJobResponse createImport(@Valid @RequestBody CreateImportJobRequest request) {
        return importJobService.createImportJob(request);
    }

    @GetMapping("/{id}")
    public ImportJobResponse getImport(@PathVariable Long id) {
        return importJobService.getJob(id);
    }

}
