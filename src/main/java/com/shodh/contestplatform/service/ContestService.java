package com.shodh.contestplatform.service;

import com.shodh.contestplatform.dto.ContestResponse;
import com.shodh.contestplatform.dto.LeaderboardEntry;
import com.shodh.contestplatform.dto.ProblemDto;
import com.shodh.contestplatform.model.Contest;
import com.shodh.contestplatform.model.Submission;
import com.shodh.contestplatform.repository.ContestRepository;
import com.shodh.contestplatform.repository.SubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ContestService {

    @Autowired
    private ContestRepository contestRepository;

    @Autowired
    private SubmissionRepository submissionRepository;

    public ContestResponse getContestDetails(Long contestId) {
        Contest contest = contestRepository.findById(contestId)
                .orElseThrow(() -> new RuntimeException("Contest not found"));

        ContestResponse response = new ContestResponse();
        response.setId(contest.getId());
        response.setName(contest.getName());
        response.setDescription(contest.getDescription());

        List<ProblemDto> problemDtos = contest.getProblems().stream()
                .map(problem -> {
                    ProblemDto dto = new ProblemDto();
                    dto.setId(problem.getId());
                    dto.setTitle(problem.getTitle());
                    dto.setDescription(problem.getDescription());
                    return dto;
                })
                .collect(Collectors.toList());

        response.setProblems(problemDtos);
        return response;
    }

    public List<LeaderboardEntry> getLeaderboard(Long contestId) {
        List<Submission> acceptedSubmissions = submissionRepository
                .findAcceptedSubmissionsByContestId(contestId);

        Map<String, Set<Long>> userSolvedProblems = new HashMap<>();
        Map<String, LocalDateTime> lastSubmissionTime = new HashMap<>();

        for (Submission submission : acceptedSubmissions) {
            String username = submission.getUser().getUsername();
            userSolvedProblems.computeIfAbsent(username, k -> new HashSet<>())
                    .add(submission.getProblem().getId());

            lastSubmissionTime.merge(username, submission.getSubmittedAt(),
                    (oldTime, newTime) -> newTime.isAfter(oldTime) ? newTime : oldTime);
        }

        List<LeaderboardEntry> leaderboard = userSolvedProblems.entrySet().stream()
                .map(entry -> {
                    LeaderboardEntry leaderboardEntry = new LeaderboardEntry();
                    leaderboardEntry.setUsername(entry.getKey());
                    leaderboardEntry.setSolvedCount(entry.getValue().size());
                    leaderboardEntry.setLastSubmission(lastSubmissionTime.get(entry.getKey()));
                    return leaderboardEntry;
                })
                .sorted((a, b) -> {
                    int solvedCompare = Integer.compare(b.getSolvedCount(), a.getSolvedCount());
                    if (solvedCompare != 0) return solvedCompare;
                    return a.getLastSubmission().compareTo(b.getLastSubmission());
                })
                .collect(Collectors.toList());

        for (int i = 0; i < leaderboard.size(); i++) {
            leaderboard.get(i).setRank(i + 1);
        }

        return leaderboard;
    }
}