package com.datn.onlinejobportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.datn.onlinejobportal.model.Experience;
import com.datn.onlinejobportal.payload.ExperienceResponse;

public interface ExperienceRepository extends JpaRepository<Experience, Long> {
	
	@Query("Select new com.datn.onlinejobportal.payload.ExperienceResponse(e.id, e.company_name, e.job_title, e.start_date, e.end_date, e.description) From Experience e LEFT JOIN e.candidate c LEFT JOIN c.user u Where u.id = ?1")
	List<ExperienceResponse> getExperienceByUser(Long userId);
	
	@Query("Select new com.datn.onlinejobportal.payload.ExperienceResponse(e.id, e.company_name, e.job_title, e.start_date, e.end_date, e.description) From Experience e "
			+ "LEFT JOIN e.candidate c Where c.id = ?1")
	List<ExperienceResponse> getExperienceByCandidate(Long candidateId);
	
	@Query("Select e From Experience e LEFT JOIN e.candidate c LEFT JOIN c.user u Where u.id = :userId And e.id = :experienceId")
	Experience getExperienceByUserIdAndExperienceId(@Param("userId") Long userId, @Param("experienceId") Long experienceId);

}
