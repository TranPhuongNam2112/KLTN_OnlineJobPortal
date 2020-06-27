package com.datn.onlinejobportal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.datn.onlinejobportal.dto.CrawledJobPostSummary;
import com.datn.onlinejobportal.dto.JobPostSummary;
import com.datn.onlinejobportal.exception.ResourceNotFoundException;
import com.datn.onlinejobportal.model.Employer;
import com.datn.onlinejobportal.payload.EmployerProfile;
import com.datn.onlinejobportal.repository.EmployerRepository;
import com.datn.onlinejobportal.repository.IndustryRepository;
import com.datn.onlinejobportal.repository.JobPostRepository;
import com.datn.onlinejobportal.repository.UserRepository;
import com.datn.onlinejobportal.security.CurrentUser;
import com.datn.onlinejobportal.security.UserPrincipal;
import com.datn.onlinejobportal.util.AppConstants;


@RestController
@RequestMapping("/home")
public class HomeController {

	@Autowired
	private JobPostRepository jobPostRepository;

	@Autowired
	private EmployerRepository employerRepository;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private IndustryRepository industryRepository;

	@GetMapping("/{jobtype}")
	public Page<JobPostSummary> getJobPostsByJobType(@PathVariable("jobtype") String jobtype, @CurrentUser UserPrincipal currentUser, @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNo,
			@RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_SIZE)int pageSize,
			@RequestParam(defaultValue = "expirationDate") String sortBy) {
		Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
		return jobPostRepository.getJobPostsByJobType(jobtype, pageable);
	}

	@GetMapping("/alljobposts")
	public Page<JobPostSummary> getAllJobPosts(@RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNo,
			@RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_SIZE)int pageSize,
			@RequestParam(defaultValue = "expirationDate") String sortBy) {
		Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
		return jobPostRepository.getAllJobPosts(pageable);
	}

	@GetMapping("/employers/{employerId}")
	public EmployerProfile getEmployerProfile(@PathVariable("employerId") Long employerId) {

		Employer employer = employerRepository.findById(employerId).orElseThrow(() -> new ResourceNotFoundException("Employer", "employerId", employerId));    	
		EmployerProfile employerProfile = new EmployerProfile();

		employerProfile.setCompanyname(employer.getCompanyname());
		employerProfile.setDescription(employer.getDescription());
		employerProfile.setEstablishmentdate(employer.getEstablishmentdate());
		employerProfile.setId(employer.getId());
		employerProfile.setImage(userRepository.getEmployerImage(employerId));
		employerProfile.setIndustry(employer.getIndustry());
		employerProfile.setMain_address(employer.getMain_address());
		employerProfile.setPhone_number(employer.getPhone_number());
		employerProfile.setWebsiteUrl(employer.getWebsiteurl());
		employerProfile.setImage_url(employer.getImageUrl());
		return employerProfile;

	}
	
	@GetMapping("/otherwebsites/alljobposts/{websitename}")
	public Page<CrawledJobPostSummary> getAllCrawledJobPosts(@PathVariable("websitename") String websitename, 
			@RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNo,
			@RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_SIZE)int pageSize,
			@RequestParam(defaultValue = "expirationDate") String sortBy) {
		Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
		return jobPostRepository.getCrawledJobPostByWebsiteName(websitename, pageable);
	}
	

	@GetMapping("/search/jobposts")
	public Page<JobPostSummary> getFilterJobPosts(@RequestParam(value = "jobtitle",required=false) String jobtitle, 
			@RequestParam(value = "industry",required = false) String industry, 
			@RequestParam(value = "jobtype",required = false) String jobtype, 
			@RequestParam(value = "joblocation",required = false) String joblocation,
			@RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNo,
			@RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_SIZE)int pageSize,
			@RequestParam(defaultValue = "expirationDate") String sortBy) {
		Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
		return jobPostRepository.getJobPostsByJobTitleAndIndustryAndJobTypeAndJobLocation(jobtitle, industry, jobtype, joblocation, pageable);
	}
	
	@GetMapping("/otherwebsites")
	public Page<CrawledJobPostSummary> getOtherWebsitesJobPosts(@RequestParam(value="websitename") List<String> websitename,
			@RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNo,
			@RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_SIZE)int pageSize,
			@RequestParam(defaultValue = "expirationDate") String sortBy) {
		Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
		return jobPostRepository.getCrawledJobPostByWebsiteNames(websitename, pageable);
	}

	@GetMapping("/topviewedjobposts")
	public List<JobPostSummary> getTopViewedJobPost() {
		Pageable pageable = PageRequest.of(0, 10);
		return jobPostRepository.getTopViewedJobPost(jobPostRepository.getTop10ViewedJobPost(), pageable);
	}
	
	@GetMapping("/{industry}/{websitename}")
	public Page<JobPostSummary> getJobPostsByIndustryAndWebsiteName(@PathVariable("industry") String industry, @PathVariable("websitename") String websitename, @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNo,
			@RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_SIZE)int pageSize,
			@RequestParam(defaultValue = "expirationDate") String sortBy) {
		Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
		return jobPostRepository.getAllJobPostsByIndustryAndWebsitename(industry, websitename, pageable);
	}
	
	@GetMapping("/{industry}")
	public Page<JobPostSummary> getJobPostsByIndustry(@PathVariable("industry") String industry, @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNo,
			@RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_SIZE)int pageSize,
			@RequestParam(defaultValue = "expirationDate") String sortBy) {
		Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
		return jobPostRepository.getAllJobPostsByIndustry(industry, pageable);
	}
	
	@GetMapping("/hotjobposts")
	public Page<JobPostSummary> getHottestJobPostsByIndustryId(@RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNo,
			@RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_SIZE)int pageSize,
			@RequestParam(defaultValue = "expirationDate") String sortBy) {
		Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
		return jobPostRepository.getAllJobPostsByIndustryId(industryRepository.getIndustryJobPostCounts().get(0).getId(), pageable);
	}

}
