package com.datn.onlinejobportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.datn.onlinejobportal.model.Experience;

public interface ExperienceRepository extends JpaRepository<Experience, Long> {
	
	@Query("Select e From Experience e LEFT JOIN e.candidate c LEFT JOIN c.user u Where u.id = ?1")
	List<Experience> getExperienceByUser(Long userId);
	
	@Query("Select e From Experience e LEFT JOIN e.candidate c LEFT JOIN c.user u Where u.id = :userId And e.id = :experienceId")
	Experience getExperienceByUserIdAndExperienceId(@Param("userId") Long userId, @Param("experienceId") Long experienceId);

}
