package com.shodh.contestplatform.repository;

import com.shodh.contestplatform.model.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {

    @Query("SELECT s FROM Submission s WHERE s.problem.contest.id = :contestId AND s.status = 'Accepted'")
    List<Submission> findAcceptedSubmissionsByContestId(Long contestId);

}