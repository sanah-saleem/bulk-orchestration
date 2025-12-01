package com.project.bulkorchestration.controller;

import com.project.bulkorchestration.dto.CreateImportJobRequest;
import com.project.bulkorchestration.dto.ImportJobErrorResponse;
import com.project.bulkorchestration.dto.ImportJobResponse;
import com.project.bulkorchestration.service.FileStorageService;
import com.project.bulkorchestration.service.ImportJobErrorService;
import com.project.bulkorchestration.service.ImportJobService;
import com.project.bulkorchestration.service.UserImportJobLaunchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/imports")
@RequiredArgsConstructor
public class ImportJobController {

    private final ImportJobService importJobService;
    private final FileStorageService fileStorageService;
    private final UserImportJobLaunchService userImportJobLaunchService;
    private final ImportJobErrorService importJobErrorService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ImportJobResponse createImport(@Valid @RequestBody CreateImportJobRequest request) {
        return importJobService.createImportJob(request);
    }

    @GetMapping("/{id}")
    public ImportJobResponse getImport(@PathVariable Long id) {
        return importJobService.getJob(id);
    }

    @PostMapping("/upload")
    @ResponseStatus(HttpStatus.CREATED)
    public ImportJobResponse uploadAndCreate(
            @RequestParam("file")MultipartFile file,
            @RequestParam(name = "sendWelcomeMail", defaultValue = "false") boolean sendWelcomeMail
    ) {
        String storedPath = fileStorageService.store(file);
        String originalName = file.getOriginalFilename();
        return importJobService.createImportJobForUploadedFile(
                originalName, storedPath, sendWelcomeMail );
    }

    @PostMapping("/{id}/run")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String runImportJob(@PathVariable Long id) throws Exception {
        userImportJobLaunchService.launchForImportJob(id);
        return "Import job triggered for id=" + id;
    }

    @GetMapping("/{id}/errors")
    public List<ImportJobErrorResponse> getImportErrors(@PathVariable Long id) {
        return importJobErrorService.getErrors(id);
    }

}
