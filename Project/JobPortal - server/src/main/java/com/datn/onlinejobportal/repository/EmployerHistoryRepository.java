package com.datn.onlinejobportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.datn.onlinejobportal.dto.CandidateSummary;
import com.datn.onlinejobportal.model.Candidate;
import com.datn.onlinejobportal.model.EmployerHistory;

public interface EmployerHistoryRepository extends JpaRepository<EmployerHistory, Long> {

	@Query("Select COUNT(eh) FROM Candidate c "
			+ "LEFT JOIN c.employerhistories eh "
			+ "Where eh.candidate.id = :candidateId")
	Long countViewedProfileEmployer(@Param("candidateId") Long candidateId);

	@Query("Select new com.datn.onlinejobportal.dto.CandidateSummary(c.id, f.data, u.name, c.city_province, c.work_title, c.updatedAt, u.imageUrl) From Candidate c "
			+ "LEFT JOIN c.employerhistories eh "
			+ "LEFT JOIN c.user u "
			+ "LEFT JOIN u.files f "
			+ "Where eh.employer.id = :employerId")
	List<CandidateSummary> getAllViewedCandidateProfiles(@Param("employerId") Long employerId);
	
	@Query("Select c From Employer e "
			+ "LEFT JOIN e.employerhistories eh "
			+ "LEFT JOIN eh.candidate c "
			+ "Where c.id = :candidateId")
	Candidate getDuplicateViewedCandidateProfile(@Param("candidateId") Long candidateId);
	
	@Query("Select eh From Employer e "
			+ "LEFT JOIN e.employerhistories eh "
			+ "Where eh.candidate.id = :candidateId AND eh.employer.id = :employerId ")
	EmployerHistory getEmployerHistory(@Param("employerId") Long employerId, @Param("candidateId") Long candidateId);
	


}
