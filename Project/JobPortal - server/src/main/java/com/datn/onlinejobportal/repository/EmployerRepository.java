package com.datn.onlinejobportal.repository;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.datn.onlinejobportal.dto.EmployerSummary;
import com.datn.onlinejobportal.model.Employer;

public interface EmployerRepository extends JpaRepository<Employer, Long>, JpaSpecificationExecutor<Employer> {

	@Query("SELECT e FROM Employer e WHERE e.companyname = :companyname")
	Page<Employer> findByCompanyname(@Param("companyname") String companyname, Pageable pageable);
	
	@Query("SELECT COUNT(e) FROM Employer e")
	Long numberofCandidates();

	@Query("Select e.id FROM Employer e LEFT JOIN e.user u WHERE u.id = :account_id")
	Long getEmployerIdByAccount_Id(@Param("account_id") Long accountId);
	
	@Query("Select e FROM Employer e LEFT JOIN e.user u WHERE u.id = :account_id")
	Employer getEmployerByAccount_Id(@Param("account_id") Long accountId);

	Page<Employer> findAll(Pageable pageable);
	
	@Query("Select new com.datn.onlinejobportal.dto.EmployerSummary(e.id, f.data, e.imageUrl, e.companyname, e.industry) From Employer e "
			+ "LEFT JOIN e.user u "
			+ "LEFT JOIN u.files f ")
	Page<EmployerSummary> getAllEmployers(Pageable pageable);
	
	@Query("Select e.id From Employer e Where e.companyname = ?1")
	Long getEmployerIdByName(String companyname);
	
	@Query("Select COUNT(e.id) FROM Employer e "
			+ "Where e.user IS NOT NULL")
	Long getRegisteredEmployerCounts();
	
	@Query("Select COUNT(e.id) FROM Employer e")
	Long getAllEmployerCounts();
	
	@Query("Select COUNT(e.id) FROM Employer e "
			+ "LEFT JOIN e.user u "
			+ "Where u.createdAt = CURRENT_DATE")
	Long getNewRegisteredEmployerCountsToday();
	
	@Modifying
	@Transactional
	@Query("DELETE FROM Employer e Where e.companyname = :companyname")
	void deleteDuplicateEmployer(@Param("companyname") String companyname);
}
