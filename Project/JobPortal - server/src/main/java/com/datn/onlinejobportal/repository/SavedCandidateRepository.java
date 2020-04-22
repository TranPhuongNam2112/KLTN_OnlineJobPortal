package com.datn.onlinejobportal.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.datn.onlinejobportal.model.Candidate;
import com.datn.onlinejobportal.model.SavedCandidate;

public interface SavedCandidateRepository extends JpaRepository<SavedCandidate, Long>{

	@Query(value="Select c from Candidate c JOIN c.savedCandidates sc Where sc.employerId = :employerId", nativeQuery=true)
	Page<Candidate> findSavedCandidatesByEmployerId(@Param("employerId") Long employerId, Pageable pageable);
	
	
	
}
