package com.shodh.contestplatform.service;

import com.shodh.contestplatform.model.Submission;
import org.springframework.stereotype.Service;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.TimeUnit;

@Service
public class JudgeService {

    private static final String DOCKER_IMAGE = "judge-java:latest";
    private static final int TIMEOUT_SECONDS = 10;
    private static final String TEMP_DIR = "/tmp/judge/";

    public String executeCode(Submission submission) throws Exception {

        File tempDir = new File(TEMP_DIR);
        if (!tempDir.exists()) {
            boolean created = tempDir.mkdirs();
            if (!created) {
                throw new RuntimeException("Failed to create temp directory");
            }
        }

        String uniqueId = System.currentTimeMillis() + "_" + submission.getId();
        Path codeFile = Path.of(TEMP_DIR + "Main_" + uniqueId + ".java");
        Path inputFile = Path.of(TEMP_DIR + "input_" + uniqueId + ".txt");

        try {

            Files.writeString(codeFile, submission.getCode(), StandardOpenOption.CREATE);

            String inputTestCases = submission.getProblem().getInputTestCases();
            Files.writeString(inputFile, inputTestCases, StandardOpenOption.CREATE);

            Process process = executeDockerProcess(codeFile, inputFile);

            return evaluateProcess(process, submission);

        } finally {
            Files.deleteIfExists(codeFile);
            Files.deleteIfExists(inputFile);
        }
    }

    private Process executeDockerProcess(Path codeFile, Path inputFile) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(
                "docker", "run",
                "--rm",
                "-m", "512m",
                "--cpus", "1",
                "-v", codeFile.toAbsolutePath() + ":/code/Main.java",
                "-v", inputFile.toAbsolutePath() + ":/input.txt",
                DOCKER_IMAGE,
                "/bin/bash", "-c",
                "cd /code && javac Main.java && java Main < /input.txt"
        );

        Process process = pb.start();
        boolean finished = process.waitFor(TIMEOUT_SECONDS, TimeUnit.SECONDS);

        if (!finished) {
            process.destroyForcibly();
            throw new RuntimeException("Time Limit Exceeded");
        }

        return process;
    }

    private String evaluateProcess(Process process, Submission submission) throws IOException {
        if (process.exitValue() != 0) {
            String error = readStream(process.getErrorStream());
            if (error.contains("OutOfMemoryError")) {
                return "Memory Limit Exceeded";
            }
            return "Runtime Error";
        }

        String output = readStream(process.getInputStream());

        String expectedOutput = submission.getProblem().getOutputTestCases().trim();
        String actualOutput = output.trim();

        if (expectedOutput.equals(actualOutput)) {
            return "Accepted";
        } else {
            return "Wrong Answer";
        }
    }

    private String readStream(InputStream inputStream) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        }
        return sb.toString();
    }
}