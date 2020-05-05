package com.datn.onlinejobportal.repository;

import java.util.Date;
import java.util.List;

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

	@Query(value="Select c From Candidate c Join c.user u where c.id in :candidateIds", nativeQuery=true)
	List<Candidate> getByCandidateId(@Param("candidateIds") List<Long> candidateIds);

	@Query("Select c from Candidate c where c.user.id = :account_id")
	Candidate getCandidateByUserId(@Param("account_id") Long account_id);
	
	
	@Query("Select c.id from Candidate c LEFT JOIN c.user u Where u.id = :account_id ")
	Long getCandidateIdByUserId(@Param("account_id") Long account_id);
	
	@Query(value="Select c.name, c.city_province, c.work_title, c.updatedAt From Candidate c Join c.user u Where u.id = :userId and c.id in (Select sc.id From SavedCandidate sc)", nativeQuery = true)
	Page<CandidateSummary> getCandidatesSavedBy(@Param("userId") Long userId, Pageable pageable);
	
	@Query(value="Select c from Candidate c"
			+ "Join c.user u"
			+ "Where u.name = :candidateName", nativeQuery=true)
	Candidate getCandidateByName(@Param("candidateName") String candidateName);
}
