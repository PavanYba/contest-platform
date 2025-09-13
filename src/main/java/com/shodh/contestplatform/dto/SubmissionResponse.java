package com.shodh.contestplatform.dto;

import lombok.Data;

@Data
public class SubmissionResponse {
    private Long submissionId;
    private String status;
    private String message;
}