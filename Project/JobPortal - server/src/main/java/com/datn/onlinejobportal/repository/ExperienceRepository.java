package com.datn.onlinejobportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.datn.onlinejobportal.model.Experience;

public interface ExperienceRepository extends JpaRepository<Experience, Long> {
	
	@Query(value="SELECT * FROM Experience e JOIN e.candidate_id c WHERE c. ", nativeQuery= true)
	List<Experience> findByCandidateName();

}
