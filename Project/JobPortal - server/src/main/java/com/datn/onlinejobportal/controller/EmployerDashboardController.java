package com.datn.onlinejobportal.controller;

import java.net.URI;
import java.time.LocalDate;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.datn.onlinejobportal.dto.CandidateSummary;
import com.datn.onlinejobportal.dto.MyJobPostSummary;
import com.datn.onlinejobportal.exception.ResourceNotFoundException;
import com.datn.onlinejobportal.model.DBFile;
import com.datn.onlinejobportal.model.Employer;
import com.datn.onlinejobportal.model.JobLocation;
import com.datn.onlinejobportal.model.JobPost;
import com.datn.onlinejobportal.model.User;
import com.datn.onlinejobportal.payload.ApiResponse;
import com.datn.onlinejobportal.payload.EmployerRequest;
import com.datn.onlinejobportal.payload.JobPostRequest;
import com.datn.onlinejobportal.repository.EmployerRepository;
import com.datn.onlinejobportal.repository.JobLocationRepository;
import com.datn.onlinejobportal.repository.JobPostRepository;
import com.datn.onlinejobportal.repository.SavedCandidateRepository;
import com.datn.onlinejobportal.repository.UserRepository;
import com.datn.onlinejobportal.security.CurrentUser;
import com.datn.onlinejobportal.security.UserPrincipal;
import com.datn.onlinejobportal.service.DBFileStorageService;
import com.datn.onlinejobportal.service.JobPostService;
import com.datn.onlinejobportal.util.AppConstants;

@RestController
public class EmployerDashboardController {

	@Autowired
	private EmployerRepository employerRepository;

	@Autowired
	private JobPostRepository jobPostRepository;

	@Autowired
	private JobLocationRepository jobLocationRepository;

	@Autowired
	private JobPostService jobPostService;
	
    @Autowired
    private DBFileStorageService dbFileStorageService;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private SavedCandidateRepository savedCandidateRepository;
    
	@PostMapping("/createpost")
	@PreAuthorize("hasRole('EMPLOYER')")
	public ResponseEntity<?> createJobPost(@Valid @RequestBody JobPostRequest jobPostRequest) {
		JobPost jobpost = jobPostService.createJobPost(jobPostRequest);

		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest().path("/{jobpostId}")
				.buildAndExpand(jobpost.getId()).toUri();

		return ResponseEntity.created(location)
				.body(new ApiResponse(true, "Đã tạo bài post thành công!"));
	}

	@GetMapping("/{jobpostId}")
	public JobPost getJobPostById(@CurrentUser UserPrincipal currentUser,
			@PathVariable Long jobpostId) {
		return jobPostService.getJobPostById(jobpostId, currentUser);
	}

	@PutMapping("/myjobposts/{jobpostId}")
	@PreAuthorize("hasRole('EMPLOYER')")
	public JobPost updateJobPost(@CurrentUser UserPrincipal currentUser, @PathVariable(value = "id") Long jobpostId,
			@Valid @RequestBody JobPostRequest jobPostRequest) {
		JobPost jobpost = jobPostRepository.findById(jobpostId).orElseThrow(
				() -> new ResourceNotFoundException("Job post", "id", jobpostId));
		JobLocation joblocation = jobLocationRepository.findByJobPostId(jobpostId);

		joblocation.setStreet_address(jobPostRequest.getStreet_address());
		joblocation.setCity_province(jobPostRequest.getCity_province());
		jobpost.setJob_title(jobPostRequest.getJobtitle());
		jobpost.setJobtype(jobPostRequest.getJobType());
		jobpost.setJoblocation(joblocation);
		jobpost.setIndustry(jobPostRequest.getIndustry());
		jobpost.setJob_description(jobPostRequest.getJobdescription());
		jobpost.setCreatedAt(LocalDate.now());
		jobpost.setExpirationDate(jobPostRequest.getExpiredDate());    
		jobpost.setRequiredexpreienceyears(jobPostRequest.getRequiredexperience());

		JobPost updatedJobPost = jobPostRepository.save(jobpost);
		return updatedJobPost;
	}

	@DeleteMapping("/myjobposts/{jobpostId}")
	@PreAuthorize("hasRole('EMPLOYER')")
	public ResponseEntity<?> deleteJobPost(@PathVariable(value = "jobpostId") Long jobpostId) {
		JobPost jobpost = jobPostRepository.findById(jobpostId)
				.orElseThrow(() -> new ResourceNotFoundException("Job post", "id", jobpostId));

		jobPostRepository.delete(jobpost);

		return ResponseEntity.ok().build();
	}

	@PutMapping("/profile")
	@PreAuthorize("hasRole('EMPLOYER')")
	public Employer updateProfile(@CurrentUser UserPrincipal currentUser, 
			@Valid @RequestBody EmployerRequest employerRequest, @RequestParam("file") MultipartFile file) {
		Employer employer = employerRepository.getEmployerByAccount_Id(currentUser.getId());
		
		User user = userRepository.getOne(currentUser.getId());
		DBFile dbFile = dbFileStorageService.storeFile(file);
		
		user.setFiles(dbFile);
		employer.setCompanyname(employerRequest.getCompanyname());
		employer.setDescription(employerRequest.getDescription());
		employer.setEstablishmentdate(employerRequest.getEstablishmentdate());
		employer.setIndustry(employerRequest.getIndustry());
		employer.setMain_address(employerRequest.getMainaddress());
		employer.setPhone_number(employerRequest.getPhone_number());
		employer.setWebsiteurl(employerRequest.getWebsite_url());

		userRepository.save(user);
		Employer updatedEmployer = employerRepository.save(employer);
		return updatedEmployer;
	}
	
	@GetMapping("/savedcandidates")
	public Page<CandidateSummary> getAllSavedCandidates(@CurrentUser UserPrincipal currentUser, @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNo,
			@RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_SIZE)int pageSize,
			@RequestParam(defaultValue = "created_at") String sortBy) {
		Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
		Long employerId = employerRepository.getEmployerIdByAccount_Id(currentUser.getId());
		return savedCandidateRepository.findSavedCandidatesByEmployerId(employerId, pageable);
	}
	
	@GetMapping("/myjobposts")
	public Page<MyJobPostSummary> getAllJobPosts(@CurrentUser UserPrincipal currentUser, @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNo,
			@RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_SIZE)int pageSize,
			@RequestParam(defaultValue = "expirationDate") String sortBy) {
		Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
		Long employerId = employerRepository.getEmployerIdByAccount_Id(currentUser.getId());
		return jobPostRepository.getAllJobPostByEmployerId(employerId, pageable);
	}
	
	
}
