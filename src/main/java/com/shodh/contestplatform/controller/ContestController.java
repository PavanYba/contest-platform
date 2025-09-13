package com.shodh.contestplatform.controller;

import com.shodh.contestplatform.dto.ContestResponse;
import com.shodh.contestplatform.dto.LeaderboardEntry;
import com.shodh.contestplatform.service.ContestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ContestController {

    @Autowired
    private ContestService contestService;

    @GetMapping("/contests/{contestId}")
    public ResponseEntity<ContestResponse> getContest(@PathVariable Long contestId) {
        ContestResponse response = contestService.getContestDetails(contestId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/contests/{contestId}/leaderboard")
    public ResponseEntity<List<LeaderboardEntry>> getLeaderboard(@PathVariable Long contestId) {
        List<LeaderboardEntry> leaderboard = contestService.getLeaderboard(contestId);
        return ResponseEntity.ok(leaderboard);
    }
}