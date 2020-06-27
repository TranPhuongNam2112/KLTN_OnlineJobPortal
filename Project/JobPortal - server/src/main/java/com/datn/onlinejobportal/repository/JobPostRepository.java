package com.datn.onlinejobportal.repository;


import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.datn.onlinejobportal.dto.CrawledJobPostSummary;
import com.datn.onlinejobportal.dto.JobPostSummary;
import com.datn.onlinejobportal.dto.MyJobPostSummary;
import com.datn.onlinejobportal.model.JobPost;

@Repository
public interface JobPostRepository extends JpaRepository<JobPost, Long>, JpaSpecificationExecutor<JobPost> {
	

	@Query("Select new com.datn.onlinejobportal.dto.MyJobPostSummary(jp.id, jp.job_title, jl.city_province, jt.job_type_name, jp.requiredexperienceyears, jp.expirationDate, jp.min_salary, jp.max_salary) "
			+ "From JobPost jp "
			+ "LEFT JOIN jp.savedjobpost sjp "
			+ "LEFT JOIN jp.joblocation jl "
			+ "LEFT JOIN jp.employer e "
			+ "LEFT JOIN e.user u "
			+ "LEFT JOIN u.files f "
			+ "LEFT JOIN jp.jobtype jt "
			+ "Where e.id = :employerId")
	Page<MyJobPostSummary> getAllJobPostByEmployerId(@Param("employerId") Long employerId, Pageable pageable);
	
	@Query("Select new com.datn.onlinejobportal.dto.JobPostSummary(j.id, f.data, e.imageUrl, e.companyname, j.job_title, j.requiredexperienceyears, jl.city_province, jt.job_type_name, j.expirationDate, j.min_salary, j.max_salary, j.sourceUrl) "
			+ "From JobPost j "
			+ "LEFT JOIN j.employer e "
			+ "LEFT JOIN e.user u "
			+ "LEFT JOIN u.files f "
			+ "LEFT JOIN j.joblocation jl "
			+ "LEFT JOIN j.jobtype jt "
			+ "Where jt.job_type_name = :jobtypename AND j.expirationDate >= CURRENT_DATE")
	Page<JobPostSummary> getJobPostsByJobType(@Param("jobtypename") String jobtypename, Pageable pageable); 
	
	@Query("Select new com.datn.onlinejobportal.dto.JobPostSummary(j.id, f.data, e.imageUrl, e.companyname, j.job_title, j.requiredexperienceyears, jl.city_province, jt.job_type_name, j.expirationDate, j.min_salary, j.max_salary, j.sourceUrl) "
			+ "From JobPost j "
			+ "LEFT JOIN j.employer e "
			+ "LEFT JOIN e.user u "
			+ "LEFT JOIN u.files f "
			+ "LEFT JOIN j.joblocation jl "
			+ "LEFT JOIN j.jobtype jt "
			+ "Where jt IN (SELECT jt FROM Candidate c LEFT JOIN c.jobtypes jt WHERE c.id = :candidateId ) AND "
			+ "j.max_salary >= (SELECT c.expectedsalary FROM Candidate c WHERE c.id = :candidateId) AND j.min_salary >= (SELECT c.expectedsalary FROM Candidate c WHERE c.id = :candidateId) "
			+ "AND jl.city_province = (SELECT c.city_province FROM Candidate c WHERE c.id = :candidateId) AND j.expirationDate >= CURRENT_DATE")
	Page<JobPostSummary> getRecommendedJobPostsByUser(@Param("candidateId") Long candidateId, Pageable pageable); 
	
	@Query("Select j from JobPost j Where j.id = :jobpostId")
	JobPost findByJobPostId(@Param("jobpostId") Long jobpostId);
	
	@Query("Select new com.datn.onlinejobportal.dto.JobPostSummary(j.id, f.data, e.imageUrl, e.companyname, j.job_title, j.requiredexperienceyears, jl.city_province, jt.job_type_name, j.expirationDate, j.min_salary, j.max_salary, j.sourceUrl) "
			+ "From JobPost j "
			+ "LEFT JOIN j.employer e "
			+ "LEFT JOIN e.user u "
			+ "LEFT JOIN u.files f "
			+ "LEFT JOIN j.joblocation jl "
			+ "LEFT JOIN j.jobtype jt "
			+ "Where j.expirationDate >= CURRENT_DATE AND j.sourceUrl IS NULL")
	Page<JobPostSummary> getAllJobPosts(Pageable pageable); 
	
	@Query("Select new com.datn.onlinejobportal.dto.CrawledJobPostSummary(j.id, e.imageUrl, e.companyname, j.job_title, j.requiredexperienceyears, jl.city_province, jt.job_type_name, j.expirationDate, j.min_salary, j.max_salary, j.sourceUrl) "
			+ "From JobPost j "
			+ "LEFT JOIN j.employer e "
			+ "LEFT JOIN j.joblocation jl "
			+ "LEFT JOIN j.jobtype jt "
			+ "Where j.expirationDate >= CURRENT_DATE AND j.sourceUrl IS NOT NULL AND lower(j.sourceUrl) Like lower(concat('%', :pagename,'%'))")
	Page<CrawledJobPostSummary> getCrawledJobPostByWebsiteName(@Param("pagename") String pagename, Pageable pageable);
	
	@Query("Select new com.datn.onlinejobportal.dto.CrawledJobPostSummary(j.id, e.imageUrl, e.companyname, j.job_title, j.requiredexperienceyears, jl.city_province, jt.job_type_name, j.expirationDate, j.min_salary, j.max_salary, j.sourceUrl) "
			+ "From JobPost j "
			+ "LEFT JOIN j.employer e "
			+ "LEFT JOIN j.joblocation jl "
			+ "LEFT JOIN j.jobtype jt "
			+ "Where j.expirationDate >= CURRENT_DATE AND j.sourceUrl IS NOT NULL "
			+ "AND j.sourceWebsite IN :pagenames")
	Page<CrawledJobPostSummary> getCrawledJobPostByWebsiteNames(@Param("pagenames") List<String> pagenames, Pageable pageable);
	
	@Query("Select new com.datn.onlinejobportal.dto.JobPostSummary(j.id, f.data, e.imageUrl, e.companyname, j.job_title, j.requiredexperienceyears, jl.city_province, jt.job_type_name, j.expirationDate, j.min_salary, j.max_salary, j.sourceUrl) "
			+ "From JobPost j "
			+ "LEFT JOIN j.employer e "
			+ "LEFT JOIN e.user u "
			+ "LEFT JOIN u.files f "
			+ "LEFT JOIN j.joblocation jl "
			+ "LEFT JOIN j.jobtype jt "
			+ "LEFT JOIN j.industries i "
			+ "Where j.expirationDate >= CURRENT_DATE AND lower(j.job_title) LIKE lower(concat('%', ?1,'%')) OR (i IN (Select i From Industry i Where i.industryname = ?2) "
			+ "OR jt.job_type_name = ?3 OR jl.city_province = ?4)")
	Page<JobPostSummary> getJobPostsByJobTitleAndIndustryAndJobTypeAndJobLocation(String jobtitle, String industry, String job_type_name, String city_province, Pageable pageable);
	
	@Query("Select jp.id FROM JobPost jp "
			+ "LEFT JOIN jp.candidatehistories ch "
			+ "GROUP BY jp.id "
			+ "ORDER BY COUNT(ch.candidate.id)")
	List<Long> getTop10ViewedJobPost();
	
	@Query("Select new com.datn.onlinejobportal.dto.JobPostSummary(j.id, f.data, e.imageUrl, e.companyname, j.job_title, j.requiredexperienceyears, jl.city_province, jt.job_type_name, j.expirationDate, j.min_salary, j.max_salary, j.sourceUrl) "
			+ "From JobPost j "
			+ "LEFT JOIN j.employer e "
			+ "LEFT JOIN e.user u "
			+ "LEFT JOIN u.files f "
			+ "LEFT JOIN j.joblocation jl "
			+ "LEFT JOIN j.jobtype jt "
			+ "Where j.expirationDate >= CURRENT_DATE AND j.id IN :top AND j.sourceUrl IS NULL")
	List<JobPostSummary> getTopViewedJobPost(@Param("top") List<Long> top, Pageable pageable); 
	
	@Query("Select new com.datn.onlinejobportal.dto.JobPostSummary(j.id, f.data, e.imageUrl, e.companyname, j.job_title, j.requiredexperienceyears, jl.city_province, jt.job_type_name, j.expirationDate, j.min_salary, j.max_salary, j.sourceUrl) "
			+ "From JobPost j "
			+ "LEFT JOIN j.employer e "
			+ "LEFT JOIN e.user u "
			+ "LEFT JOIN u.files f "
			+ "LEFT JOIN j.joblocation jl "
			+ "LEFT JOIN j.jobtype jt "
			+ "Where j.expirationDate >= CURRENT_DATE AND e.companyname = :companyname")
	Page<JobPostSummary> getAllJobPostsByEmployer(@Param("companyname") String companyname, Pageable pageable);
	
	@Query("Select COUNT(j.id) From JobPost j "
			+ "Where j.createdAt = CURENT_DATE")
	Long getNewJobPostsCountToday();
	
	@Modifying
	@Transactional
	@Query("DELETE FROM JobPost jp Where jp.employer.companyname = :companyname")
	void deleteJobPostsByEmployer(@Param("companyname") String companyname);
	
	@Query("Select new com.datn.onlinejobportal.dto.JobPostSummary(j.id, f.data, e.imageUrl, e.companyname, j.job_title, j.requiredexperienceyears, jl.city_province, jt.job_type_name, j.expirationDate, j.min_salary, j.max_salary, j.sourceUrl) "
			+ "From JobPost j "
			+ "LEFT JOIN j.employer e "
			+ "LEFT JOIN e.user u "
			+ "LEFT JOIN u.files f "
			+ "LEFT JOIN j.joblocation jl "
			+ "LEFT JOIN j.jobtype jt "
			+ "LEFT JOIN j.industries i "
			+ "Where j.expirationDate >= CURRENT_DATE AND j.sourceUrl IS NOT NULL AND i.industryname = ?1 AND j.sourceWebsite = ?2")
	Page<JobPostSummary> getAllJobPostsByIndustryAndWebsitename(String industryname, String sourceWebsite, Pageable pageable); 
	
	
	@Query("Select new com.datn.onlinejobportal.dto.JobPostSummary(j.id, f.data, e.imageUrl, e.companyname, j.job_title, j.requiredexperienceyears, jl.city_province, jt.job_type_name, j.expirationDate, j.min_salary, j.max_salary, j.sourceUrl) "
			+ "From JobPost j "
			+ "LEFT JOIN j.employer e "
			+ "LEFT JOIN e.user u "
			+ "LEFT JOIN u.files f "
			+ "LEFT JOIN j.joblocation jl "
			+ "LEFT JOIN j.jobtype jt "
			+ "LEFT JOIN j.industries i "
			+ "Where j.expirationDate >= CURRENT_DATE AND i.industryname = ?1")
	Page<JobPostSummary> getAllJobPostsByIndustry(String industryname, Pageable pageable); 
	
	@Query("Select new com.datn.onlinejobportal.dto.JobPostSummary(j.id, f.data, e.imageUrl, e.companyname, j.job_title, j.requiredexperienceyears, jl.city_province, jt.job_type_name, j.expirationDate, j.min_salary, j.max_salary, j.sourceUrl) "
			+ "From JobPost j "
			+ "LEFT JOIN j.employer e "
			+ "LEFT JOIN e.user u "
			+ "LEFT JOIN u.files f "
			+ "LEFT JOIN j.joblocation jl "
			+ "LEFT JOIN j.jobtype jt "
			+ "LEFT JOIN j.industries i "
			+ "Where j.expirationDate >= CURRENT_DATE AND i.id = ?1")
	Page<JobPostSummary> getAllJobPostsByIndustryId(Long industryid, Pageable pageable); 
	
}

