package com.shodh.contestplatform.controller;

import com.shodh.contestplatform.dto.SubmissionRequest;
import com.shodh.contestplatform.dto.SubmissionResponse;
import com.shodh.contestplatform.service.SubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class SubmissionController {

    @Autowired
    private SubmissionService submissionService;

    @PostMapping("/submissions")
    public ResponseEntity<SubmissionResponse> submitCode(@RequestBody SubmissionRequest request) {
        SubmissionResponse response = submissionService.submitCode(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/submissions/{submissionId}")
    public ResponseEntity<SubmissionResponse> getSubmissionStatus(@PathVariable Long submissionId) {
        SubmissionResponse response = submissionService.getSubmissionStatus(submissionId);
        return ResponseEntity.ok(response);
    }
}