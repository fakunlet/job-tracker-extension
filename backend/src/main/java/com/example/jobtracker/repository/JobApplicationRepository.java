package com.example.jobtracker.repository;

import com.example.jobtracker.model.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// No implementation is written anywhere. Spring Data reads these method names at
// startup and generates the SQL, which is why the interface can stay empty-ish.
@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

    // Becomes "SELECT EXISTS(... WHERE url = ?)". Cheaper than fetching the row,
    // because we only care whether one exists.
    boolean existsByUrl(String url);

    /**
     * Reads as: find all, order by appliedDate descending, then id descending.
     * The id is a tiebreaker — appliedDate only stores a day, so several
     * applications saved today would otherwise come back in random order.
     */
    List<JobApplication> findAllByOrderByAppliedDateDescIdDesc();
}
