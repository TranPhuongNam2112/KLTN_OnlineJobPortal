package com.datn.onlinejobportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.datn.onlinejobportal.model.Education;

public interface EducationRepository extends JpaRepository<Education, Long> {

	@Query("Select e From Education e LEFT JOIN e.candidate c LEFT JOIN c.user u Where u.id = ?1")
	List<Education> getEducationByUser(Long userId);
}
