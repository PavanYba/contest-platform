package com.shodh.contestplatform.dto;

import lombok.Data;

@Data
public class SubmissionRequest {
    private Long problemId;
    private Long contestId;
    private String username;
    private String code;
    private String language;
}