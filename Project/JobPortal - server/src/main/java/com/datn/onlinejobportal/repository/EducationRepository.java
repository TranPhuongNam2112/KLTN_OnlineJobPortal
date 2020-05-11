package com.datn.onlinejobportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.datn.onlinejobportal.model.Education;
import com.datn.onlinejobportal.payload.EducationResponse;

public interface EducationRepository extends JpaRepository<Education, Long> {

	@Query("Select e From Education e LEFT JOIN e.candidate c LEFT JOIN c.user u Where u.id = :userId And e.id = :educationId")
	Education getEducationByUserIdAndEducationId(@Param("userId") Long userId, @Param("educationId") Long educationId);
	
	@Query("Select e From Education e LEFT JOIN e.candidate c LEFT JOIN c.user u Where u.id = :userId")
	List<EducationResponse> getEducationByUser(@Param("userId") Long userId);
	

}
