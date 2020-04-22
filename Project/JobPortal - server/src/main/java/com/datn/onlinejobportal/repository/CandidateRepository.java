package com.datn.onlinejobportal.repository;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

	@Query(value="Select c from Candidate c where c.savedCandidates.employer_id = :employer_id", nativeQuery=true)
	Page<Candidate> getCandidatesSavedBy(@Param("employer_id") Long employer_id, Pageable pageable);

	@Query(value="Select c from Candidate c where c.user.id = :account_id")
	Candidate getCandidateByUserId(@Param("account_id") Long account_id);
	
	
	@Query(value="Select c.id from Candidate c where c.user.id = :account_id")
	Long getCandidateIdByUserId(@Param("account_id") Long account_id);
	
}
