package com.datn.onlinejobportal.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.datn.onlinejobportal.dto.CandidateSummary;
import com.datn.onlinejobportal.model.Candidate;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {

	@Query("Select c from Candidate c where c.user.id = :account_id")
	Candidate getCandidateByUserId(@Param("account_id") Long account_id);
	
	
	@Query("Select c.id from Candidate c LEFT JOIN c.user u Where u.id = :account_id ")
	Long getCandidateIdByUserId(@Param("account_id") Long account_id);
	
	
	@Query("Select DISTINCT new com.datn.onlinejobportal.dto.CandidateSummary(c.id, f.data, u.name, c.city_province, c.work_title, c.updatedAt, u.imageUrl) From Candidate c "
			+ "LEFT JOIN c.savedCandidates sc "
			+ "LEFT JOIN c.user u "
			+ "LEFT JOIN u.files f "
			+ "LEFT JOIN c.jobtypes j "
			+ "WHERE c.id NOT IN (SELECT sc.candidate.id FROM SavedCandidate sc WHERE sc.employer.id = :employerId) "
			+ "AND c.city_province IN (SELECT jl.city_province FROM JobPost jp LEFT JOIN jp.joblocation jl LEFT JOIN jp.employer e Where e.id = :employerId) "
			+ "AND j IN (SELECT jt FROM JobPost jp LEFT JOIN jp.jobtype jt LEFT JOIN jp.employer e WHERE e.id = :employerId) "
			+ "AND c.profile_visible = TRUE")
	Page<CandidateSummary> getRecommendedCandidatesBasedOnJobPostAndEmployerId(@Param("employerId") Long employerId, Pageable pageable);

	@Query("Select new com.datn.onlinejobportal.dto.CandidateSummary(c.id, f.data, u.name, c.city_province, c.work_title, c.updatedAt, u.imageUrl) From Candidate c "
			+ "LEFT JOIN c.savedCandidates sc "
			+ "LEFT JOIN c.user u "
			+ "LEFT JOIN u.files f ")
	Page<CandidateSummary> getAllCandidates(Pageable pageable);

	@Query("Select COUNT(c) From Candidate c ")
	Long getCandidateCounts();
	
	@Query("Select COUNT(c) From Candidate c "
			+ "LEFT JOIN c.user u "
			+ "Where u.createdAt = CURRENT_DATE")
	Long getCurrentDateNewCandidatesCount();
	
}
