package com.shodh.contestplatform.config;

import com.shodh.contestplatform.model.*;
import com.shodh.contestplatform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private ContestRepository contestRepository;

    @Autowired
    private ProblemRepository problemRepository;

    @Override
    public void run(String... args) {
        if (contestRepository.count() > 0) {
            return;
        }

        Contest contest = new Contest();
        contest.setName("Weekly Contest 1");
        contest.setDescription("Practice your coding skills");
        Contest savedContest = contestRepository.save(contest);

        Problem problem1 = new Problem();
        problem1.setTitle("Two Sum");
        problem1.setDescription("Given an array of integers, return indices of two numbers that add up to target.");
        problem1.setInputTestCases("4\n2 7 11 15\n9");
        problem1.setOutputTestCases("0 1");
        problem1.setContest(savedContest);
        problemRepository.save(problem1);

        Problem problem2 = new Problem();
        problem2.setTitle("Reverse String");
        problem2.setDescription("Reverse the given string.");
        problem2.setInputTestCases("hello");
        problem2.setOutputTestCases("olleh");
        problem2.setContest(savedContest);
        problemRepository.save(problem2);

        Problem problem3 = new Problem();
        problem3.setTitle("Palindrome Check");
        problem3.setDescription("Check if given string is palindrome. Print 'true' or 'false'.");
        problem3.setInputTestCases("racecar");
        problem3.setOutputTestCases("true");
        problem3.setContest(savedContest);
        problemRepository.save(problem3);

        System.out.println("Sample data initialized successfully!");
    }
}