package com.datn.onlinejobportal.controller;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.datn.onlinejobportal.dao.EmployerSpecificationsBuilder;
import com.datn.onlinejobportal.dto.CrawledJobPostSummary;
import com.datn.onlinejobportal.dto.JobPostSummary;
import com.datn.onlinejobportal.exception.ResourceNotFoundException;
import com.datn.onlinejobportal.model.Employer;
import com.datn.onlinejobportal.payload.EmployerProfile;
import com.datn.onlinejobportal.repository.EmployerRepository;
import com.datn.onlinejobportal.repository.JobPostRepository;
import com.datn.onlinejobportal.repository.UserRepository;
import com.datn.onlinejobportal.security.CurrentUser;
import com.datn.onlinejobportal.security.UserPrincipal;
import com.datn.onlinejobportal.service.JobPostService;
import com.datn.onlinejobportal.util.AppConstants;
import com.datn.onlinejobportal.util.SearchOperation;
import com.google.common.base.Joiner;


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
	private JobPostService jobPostService;



	public HomeController(JobPostService jobPostService) {
		super();
		this.jobPostService = jobPostService;
	}
	@GetMapping("/{jobtype}")
	public Page<JobPostSummary> getJobPostsByJobType(@PathVariable("jobtype") String jobtype, @CurrentUser UserPrincipal currentUser, @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNo,
			@RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_SIZE)int pageSize,
			@RequestParam(defaultValue = "expirationDate") String sortBy) {
		Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
		return jobPostRepository.getJobPostsByJobType(jobtype, pageable);
	}
	/*
    @RequestMapping(method = RequestMethod.GET, value = "/jobposts")
    @ResponseBody
    public Page<JobPost> searchJobPosts(@RequestParam(value = "search") String search, @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNo,
			@RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_SIZE)int pageSize,
			@RequestParam(defaultValue = "expirationDate") String sortBy) {
        JobPostSpecificationsBuilder builder = new JobPostSpecificationsBuilder();
        String operationSetExper = Joiner.on("|")
            .join(SearchOperation.SIMPLE_OPERATION_SET);
        Pattern pattern = Pattern.compile("(\\w+?)(" + operationSetExper + ")(\\p{Punct}?)(\\w+?)(\\p{Punct}?),");
        Matcher matcher = pattern.matcher(search + ",");
        while (matcher.find()) {
            builder.with(matcher.group(1), matcher.group(2), matcher.group(4), matcher.group(3), matcher.group(5));
        }
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());

        Specification<JobPost> spec = builder.build();
        return jobPostRepository.findAll(spec, pageable);
    }
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/employers")
	@ResponseBody
	public Page<Employer> searchEmployer(@RequestParam(value = "search") String search, @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNo,
			@RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_SIZE)int pageSize,
			@RequestParam(defaultValue = "id") String sortBy) {
		EmployerSpecificationsBuilder builder = new EmployerSpecificationsBuilder();
		String operationSetExper = Joiner.on("|")
				.join(SearchOperation.SIMPLE_OPERATION_SET);
		Pattern pattern = Pattern.compile("(\\w+?)(" + operationSetExper + ")(\\p{Punct}?)(\\w+?)(\\p{Punct}?),");
		Matcher matcher = pattern.matcher(search + ",");
		while (matcher.find()) {
			builder.with(matcher.group(1), matcher.group(2), matcher.group(4), matcher.group(3), matcher.group(5));
		}

		Specification<Employer> spec = builder.build();
		Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
		return employerRepository.findAll(spec, pageable);
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
	
	@GetMapping("/{industry}/")
	public Page<JobPostSummary> getJobPostsByIndustry(@PathVariable("industry") String industry, @PathVariable("websitename") String websitename, @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNo,
			@RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_SIZE)int pageSize,
			@RequestParam(defaultValue = "expirationDate") String sortBy) {
		Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
		return jobPostRepository.getAllJobPostsByIndustry(industry, pageable);
	}

}
