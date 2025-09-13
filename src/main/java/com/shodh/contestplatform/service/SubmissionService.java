package com.shodh.contestplatform.service;

import com.shodh.contestplatform.dto.SubmissionRequest;
import com.shodh.contestplatform.dto.SubmissionResponse;
import com.shodh.contestplatform.model.Problem;
import com.shodh.contestplatform.model.Submission;
import com.shodh.contestplatform.model.User;
import com.shodh.contestplatform.repository.ProblemRepository;
import com.shodh.contestplatform.repository.SubmissionRepository;
import com.shodh.contestplatform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class SubmissionService {

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProblemRepository problemRepository;

    @Autowired
    private JudgeService judgeService;

    @Transactional
    public SubmissionResponse submitCode(SubmissionRequest request) {

        User user = userRepository.findByUsername(request.getUsername())
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setUsername(request.getUsername());
                    return userRepository.save(newUser);
                });

        Problem problem = problemRepository.findById(request.getProblemId())
                .orElseThrow(() -> new RuntimeException("Problem not found"));

        Submission submission = new Submission();
        submission.setCode(request.getCode());
        submission.setStatus("Pending");
        submission.setSubmittedAt(LocalDateTime.now());
        submission.setUser(user);
        submission.setProblem(problem);

        Submission saved = submissionRepository.save(submission);

        processSubmission(saved.getId());

        SubmissionResponse response = new SubmissionResponse();
        response.setSubmissionId(saved.getId());
        response.setStatus("Pending");
        response.setMessage("Submission received and queued for processing");

        return response;
    }

    @Async
    public void processSubmission(Long submissionId) {
        try {
            Submission submission = submissionRepository.findById(submissionId).orElseThrow();
            submission.setStatus("Running");
            submissionRepository.save(submission);

            String result = judgeService.executeCode(submission);

            submission.setStatus(result);
            submissionRepository.save(submission);

        } catch (Exception e) {
            Submission submission = submissionRepository.findById(submissionId).orElseThrow();
            submission.setStatus("Error");
            submissionRepository.save(submission);
        }
    }

    public SubmissionResponse getSubmissionStatus(Long submissionId) {
        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("Submission not found"));

        SubmissionResponse response = new SubmissionResponse();
        response.setSubmissionId(submission.getId());
        response.setStatus(submission.getStatus());
        response.setMessage("Current status: " + submission.getStatus());

        return response;
    }
}