package com.datn.onlinejobportal.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.datn.onlinejobportal.dto.CandidateSummary;
import com.datn.onlinejobportal.model.SavedCandidate;

public interface SavedCandidateRepository extends JpaRepository<SavedCandidate, Long>{

	@Query("Select new com.datn.onlinejobportal.dto.CandidateSummary(c.id, f.data, u.name, c.city_province, c.work_title, c.updatedAt, u.imageUrl) From Candidate c "
			+ "LEFT JOIN c.savedCandidates sc "
			+ "LEFT JOIN c.user u "
			+ "LEFT JOIN u.files f "
			+ "Where sc.employer.id = :employerId")
	Page<CandidateSummary> findSavedCandidatesByEmployerId(@Param("employerId") Long employerId, Pageable pageable);
	
	@Query("Select sc From SavedCandidate sc Where sc.employer.id = :employerId And sc.candidate.id = :candidateId")
	SavedCandidate getSavedCandidateByEmployerId(@Param("employerId") Long employerId, @Param("candidateId") Long candidateId);

	@Query("Select COUNT(sc) FROM Candidate c "
			+ "LEFT JOIN c.savedCandidates sc "
			+ "Where sc.candidate.id = :candidateId")
	Long countSavedProfileEmployer(@Param("candidateId") Long candidateId);
}
