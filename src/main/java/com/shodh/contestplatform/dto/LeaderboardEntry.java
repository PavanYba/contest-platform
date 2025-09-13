package com.shodh.contestplatform.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class LeaderboardEntry {
    private String username;
    private int solvedCount;
    private LocalDateTime lastSubmission;
    private int rank;
}