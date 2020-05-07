package com.datn.onlinejobportal.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.datn.onlinejobportal.dto.CandidateSummary;
import com.datn.onlinejobportal.model.SavedCandidate;

public interface SavedCandidateRepository extends JpaRepository<SavedCandidate, Long>{

	@Query("Select new com.datn.onlinejobportal.dto.CandidateSummary(f.data, u.name, c.city_province, c.work_title, c.updatedAt) From Candidate c "
			+ "LEFT JOIN c.savedCandidates sc "
			+ "LEFT JOIN c.user u "
			+ "LEFT JOIN u.files f "
			+ "Where sc.employer.id = :employerId")
	Page<CandidateSummary> findSavedCandidatesByEmployerId(@Param("employerId") Long employerId, Pageable pageable);
	
	
	
}
