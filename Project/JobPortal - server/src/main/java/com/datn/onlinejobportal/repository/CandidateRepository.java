package com.datn.onlinejobportal.repository;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.datn.onlinejobportal.dto.CandidateSummary;
import com.datn.onlinejobportal.model.Candidate;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {

	Page<Candidate> findCandidateByGender(String gender, Pageable paging);
	
	

	@Query(value ="SELECT c FROM Candidate c JOIN c.account_id a WHERE " +
			"LOWER(a.email) LIKE LOWER(CONCAT('%',:email, '%'))", nativeQuery = true)
	Page<Candidate> findByEmail(@Param("email") String email, Pageable pageable);

	@Query("SELECT c FROM Candidate c WHERE " +
			"LOWER(c.phone_number) LIKE LOWER(CONCAT('%',:phone_number, '%'))")
	Page<Candidate> findByPhoneNumber(@Param("phone_number") String phone_number, Pageable pageable);

	@Query("SELECT COUNT(c) FROM Candidate c")
	Long numberofCandidates();

	Page<Candidate> findAllByDoBBetween(Date startDate,
			Date endDate, Pageable pageable);

	@Query("Select c from Candidate c where c.user.id = :account_id")
	Candidate getCandidateByUserId(@Param("account_id") Long account_id);
	
	
	@Query("Select c.id from Candidate c LEFT JOIN c.user u Where u.id = :account_id ")
	Long getCandidateIdByUserId(@Param("account_id") Long account_id);
	
	
	@Query("Select new com.datn.onlinejobportal.dto.CandidateSummary(f.data, u.name, c.city_province, c.work_title, c.updatedAt) From Candidate c "
			+ "LEFT JOIN c.savedCandidates sc "
			+ "LEFT JOIN c.user u "
			+ "LEFT JOIN u.files f "
			+ "WHERE c.id NOT IN (SELECT sc.candidate FROM SavedCandidate sc WHERE sc.employer = :employerId) "
			+ "AND c.city_province IN (SELECT jl.city_province FROM JobPost jp LEFT JOIN jp.joblocation jl LEFT JOIN jp.employer e Where e.id = :employerId) "
			+ "AND (SELECT jt.job_type_name FROM JobPost jp LEFT JOIN jp.jobtype jt LEFT JOIN jp.employer e WHERE e.id = :employerId AND jp.id = :jobpostId) IN c.jobtypes "
			+ "AND c.profile_visible = TRUE")
	Page<CandidateSummary> getRecommendedCandidatesBasedOnJobPostAndEmployerId(@Param("employerId") Long employerId, @Param("jobpostId") Long jobpostId, Pageable pageable);

	

	
	
}
