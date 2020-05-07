package com.datn.onlinejobportal.repository;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.datn.onlinejobportal.model.*;

public interface EmployerRepository extends JpaRepository<Employer, Long>, JpaSpecificationExecutor<Employer> {

	@Query("SELECT e FROM Employer e WHERE e.companyname = :companyname")
	Page<Employer> findByCompanyname(@Param("companyname") String companyname, Pageable pageable);

	@Query(value ="SELECT e FROM Employer e JOIN e.account_id a WHERE " +
			"LOWER(a.email) LIKE LOWER(CONCAT('%',:email, '%'))", nativeQuery = true)
	Page<Employer> findByEmail(@Param("email") String email, Pageable pageable);
	
	@Query("SELECT COUNT(e) FROM Employer e")
	Long numberofCandidates();

	Page<Candidate> findAllByEstablishmentdateBetween(Date startDate,
			Date endDate, Pageable pageable);

	@Query("Select e.id FROM Employer e LEFT JOIN e.user u WHERE u.id = :account_id")
	Long getEmployerIdByAccount_Id(@Param("account_id") Long accountId);
	
	@Query("Select e FROM Employer e LEFT JOIN e.user u WHERE u.id = :account_id")
	Employer getEmployerByAccount_Id(@Param("account_id") Long accountId);

	Page<Employer> findAll(Pageable pageable);
}
