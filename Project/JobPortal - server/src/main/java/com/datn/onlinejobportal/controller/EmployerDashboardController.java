package com.datn.onlinejobportal.controller;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.datn.onlinejobportal.dto.CandidateSummary;
import com.datn.onlinejobportal.dto.MyJobPostSummary;
import com.datn.onlinejobportal.event.SaveCandidateEvent;
import com.datn.onlinejobportal.exception.ResourceNotFoundException;
import com.datn.onlinejobportal.model.Candidate;
import com.datn.onlinejobportal.model.DBFile;
import com.datn.onlinejobportal.model.Employer;
import com.datn.onlinejobportal.model.EmployerHistory;
import com.datn.onlinejobportal.model.JobLocation;
import com.datn.onlinejobportal.model.JobPost;
import com.datn.onlinejobportal.model.SavedCandidate;
import com.datn.onlinejobportal.model.User;
import com.datn.onlinejobportal.payload.ApiResponse;
import com.datn.onlinejobportal.payload.CandidateProfile;
import com.datn.onlinejobportal.payload.EducationResponse;
import com.datn.onlinejobportal.payload.EmployerProfile;
import com.datn.onlinejobportal.payload.EmployerRequest;
import com.datn.onlinejobportal.payload.ExperienceResponse;
import com.datn.onlinejobportal.payload.JobPostRequest;
import com.datn.onlinejobportal.repository.CandidateRepository;
import com.datn.onlinejobportal.repository.EducationRepository;
import com.datn.onlinejobportal.repository.EmployerHistoryRepository;
import com.datn.onlinejobportal.repository.EmployerRepository;
import com.datn.onlinejobportal.repository.ExperienceRepository;
import com.datn.onlinejobportal.repository.IndustryRepository;
import com.datn.onlinejobportal.repository.JobLocationRepository;
import com.datn.onlinejobportal.repository.JobPostRepository;
import com.datn.onlinejobportal.repository.JobTypeRepository;
import com.datn.onlinejobportal.repository.SavedCandidateRepository;
import com.datn.onlinejobportal.repository.UserRepository;
import com.datn.onlinejobportal.security.CurrentUser;
import com.datn.onlinejobportal.security.UserPrincipal;
import com.datn.onlinejobportal.service.DBFileStorageService;
import com.datn.onlinejobportal.service.JobPostService;
import com.datn.onlinejobportal.util.AppConstants;

@RestController
@RequestMapping("/employer")
public class EmployerDashboardController {

	@Autowired
    ApplicationEventPublisher applicationEventPublisher;
	
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

	@Autowired
	private CandidateRepository candidateRepository;

	@Autowired
	private JobTypeRepository jobTypeRepository;
	
	@Autowired
	private ExperienceRepository experienceRepository;

	@Autowired
	private EducationRepository educationRepository;
	
	@Autowired
	private IndustryRepository industryRepository;
	
	@Autowired
	private EmployerHistoryRepository employerHistoryRepository;

	@PostMapping("/createpost")
	@PreAuthorize("hasRole('EMPLOYER')")
	public ResponseEntity<?> createJobPost(@Valid @RequestBody JobPostRequest jobPostRequest, @CurrentUser UserPrincipal currentUser) {
		JobPost jobpost = jobPostService.createJobPost(jobPostRequest, currentUser);

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
		jobpost.setJobtype(jobTypeRepository.findByJob_type_name(jobPostRequest.getJobType()));
		jobpost.setJoblocation(joblocation);
		jobpost.setIndustries(industryRepository.getAllIndustries(jobPostRequest.getIndustry()));
		jobpost.setJob_description(jobPostRequest.getJobdescription());
		jobpost.setCreatedAt(LocalDate.now());
		jobpost.setExpirationDate(jobPostRequest.getExpiredDate());    
		jobpost.setRequiredexpreienceyears(jobPostRequest.getRequiredexperience());

		JobPost updatedJobPost = jobPostRepository.save(jobpost);
		return updatedJobPost;
	}

	@DeleteMapping("/deleteJobPost/{jobpostId}")
	@PreAuthorize("hasRole('EMPLOYER')")
	public ResponseEntity<?> deleteJobPost(@PathVariable(value = "jobpostId") Long jobpostId) {
		JobPost jobpost = jobPostRepository.findById(jobpostId)
				.orElseThrow(() -> new ResourceNotFoundException("Job post", "id", jobpostId));

		jobPostRepository.delete(jobpost);

		return ResponseEntity.ok().build();
	}

	@GetMapping("/profile")
	@PreAuthorize("hasRole('EMPLOYER')")
	public EmployerProfile getProfile(@CurrentUser UserPrincipal currentUser) {
		Employer employer = employerRepository.getEmployerByAccount_Id(currentUser.getId());
		
		EmployerProfile employerProfile = new EmployerProfile();
		if (userRepository.getImage(currentUser.getId()) != null) {
		employerProfile.setImage(userRepository.getImage(currentUser.getId()));
		}
		employerProfile.setId(employer.getId());
		employerProfile.setCompanyname(employer.getCompanyname());
		employerProfile.setEstablishmentdate(employer.getEstablishmentdate());
		employerProfile.setIndustry(employer.getIndustry());
		employerProfile.setDescription(employer.getDescription());
		employerProfile.setMain_address(employer.getMain_address());
		employerProfile.setPhone_number(employer.getPhone_number());
		employerProfile.setWebsiteUrl(employer.getWebsiteurl());
		
		return employerProfile;
	}


	@PutMapping("/profile")
	@PreAuthorize("hasRole('EMPLOYER')")
	public Employer updateProfile(@CurrentUser UserPrincipal currentUser, 
			@Valid @RequestBody EmployerRequest employerRequest) {
		Employer employer = employerRepository.getEmployerByAccount_Id(currentUser.getId());

		User user = userRepository.getOne(currentUser.getId());
//		DBFile dbFile = dbFileStorageService.storeFile(file);
//
//		user.setFiles(dbFile);
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
	@PreAuthorize("hasRole('EMPLOYER')")
	public Page<CandidateSummary> getAllSavedCandidates(@CurrentUser UserPrincipal currentUser, @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNo,
			@RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_SIZE)int pageSize,
			@RequestParam(defaultValue = "id") String sortBy) {
		Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
		Long employerId = employerRepository.getEmployerIdByAccount_Id(currentUser.getId());
		return savedCandidateRepository.findSavedCandidatesByEmployerId(employerId, pageable);
	}

	@GetMapping("/myjobposts")
	@PreAuthorize("hasRole('EMPLOYER')")
	public Page<MyJobPostSummary> getAllJobPosts(@CurrentUser UserPrincipal currentUser, @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNo,
			@RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_SIZE)int pageSize,
			@RequestParam(defaultValue = "expirationDate") String sortBy) {
		Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
		Long employerId = employerRepository.getEmployerIdByAccount_Id(currentUser.getId());
		return jobPostRepository.getAllJobPostByEmployerId(employerId, pageable);
	}

	@PostMapping("/uploadProfileImage")
	@PreAuthorize("hasRole('EMPLOYER')")
	public ResponseEntity<?> uploadProfileImage (@RequestParam("profileimage") MultipartFile profileimage, @CurrentUser UserPrincipal currentUser) {
		User user = userRepository.getOne(currentUser.getId());
		DBFile profile = dbFileStorageService.storeFile(profileimage);
		if (profile.getCreatedAt() == null) {
			profile.setCreatedAt(LocalDate.now());
		} else {
			profile.setUpdatedAt(LocalDate.now());
		}
		user.setFiles(profile);	
		userRepository.save(user);

		return ResponseEntity.ok("Uploaded successfully");
	}

	@PostMapping("/save/{candidateId}")
	@PreAuthorize("hasRole('EMPLOYER')")
	public ResponseEntity<?> saveCandidate(@PathVariable("candidateId") Long candidateId, @CurrentUser UserPrincipal currentUser) {
		Employer employer = employerRepository.getEmployerByAccount_Id(currentUser.getId());
		Candidate candidate = candidateRepository.findById(candidateId).orElseThrow(() -> new ResourceNotFoundException("Candidate", "candidateId", candidateId));
		SavedCandidate sc = new SavedCandidate(employer, candidate);
		employer.getSavedCandidates().add(sc);
		candidate.getSavedCandidates().add(sc);
		savedCandidateRepository.save(sc);
		candidateRepository.save(candidate);
		employerRepository.save(employer);
		applicationEventPublisher.publishEvent(new SaveCandidateEvent(this, currentUser.getId()));
		return ResponseEntity.ok("Đã lưu hồ sơ ứng viên thành công!");
	}

	@DeleteMapping("/savedcandidates/{candidateId}")
	@PreAuthorize("hasRole('EMPLOYER')")
	public ResponseEntity<?> removedSavedJobPost(@PathVariable("candidateId") Long candidateId, @CurrentUser UserPrincipal currentUser) {
		Employer employer = employerRepository.getEmployerByAccount_Id(currentUser.getId());
		Candidate candidate = candidateRepository.findById(candidateId).orElseThrow(() -> new ResourceNotFoundException("Candidate", "candidateId", candidateId));
		SavedCandidate sc = savedCandidateRepository.getSavedCandidateByEmployerId(employer.getId(), candidateId);
		employer.removeCandidate(candidate);
		candidateRepository.save(candidate);
		savedCandidateRepository.delete(sc);
		return ResponseEntity.ok("Đã xóa hồ sơ ứng viên thành công!");
	}

	@GetMapping("/recommendedcandidates")
	@PreAuthorize("hasRole('EMPLOYER')")
	public Page<CandidateSummary> getAllRecommendedCandidates(@CurrentUser UserPrincipal currentUser, @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNo,
			@RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_SIZE)int pageSize,
			@RequestParam(defaultValue = "id") String sortBy) {
		Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
		Long employerId = employerRepository.getEmployerIdByAccount_Id(currentUser.getId());
		return candidateRepository.getRecommendedCandidatesBasedOnJobPostAndEmployerId(employerId, pageable);
	}

	@GetMapping("/candidates")
	@PreAuthorize("hasRole('EMPLOYER')")
	public Page<CandidateSummary> getAllCandidates(@CurrentUser UserPrincipal currentUser, @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNo,
			@RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_SIZE)int pageSize,
			@RequestParam(defaultValue = "id") String sortBy) {
		Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
		return candidateRepository.getAllCandidates(pageable);
	}
	
	@GetMapping("/candidates/{candidateId}")
	@PreAuthorize("hasRole('EMPLOYER')")
	public CandidateProfile getCandidateById(@CurrentUser UserPrincipal currentUser, @PathVariable("candidateId") Long candidateId) {
		Candidate candidate = candidateRepository.findById(candidateId).orElseThrow(() -> new ResourceNotFoundException("Candidate", "candidateId", candidateId));
		if (employerHistoryRepository.getDuplicateViewedCandidateProfile(candidateId) == null) {
			EmployerHistory employerhistory = new EmployerHistory(candidate, employerRepository.getEmployerByAccount_Id(currentUser.getId()), LocalDate.now());
			candidate.getEmployerhistories().add(employerhistory);
			Employer employer = employerRepository.getEmployerByAccount_Id(currentUser.getId());
			employer.getEmployerhistories().add(employerhistory);
			employerHistoryRepository.save(employerhistory);
			candidateRepository.save(candidate);
			employerRepository.save(employer);
		}
		else {
			Employer employer = employerRepository.getEmployerByAccount_Id(currentUser.getId());
			EmployerHistory employerhistory = employerHistoryRepository.getEmployerHistory(employer.getId(), candidateId);
			employerhistory.setViewDate(LocalDate.now());
			employerHistoryRepository.save(employerhistory);
		}
		List<ExperienceResponse> experiences = experienceRepository.getExperienceByCandidate(candidateId);
		List<EducationResponse> educations = educationRepository.getEducationByCandidate(candidateId);
		CandidateProfile candidateProfile = new CandidateProfile();
		if (userRepository.getCandidateImage(candidateId) != null) {
			candidateProfile.setImage(userRepository.getCandidateImage(candidateId));
		}
		if (candidate.getFiles() != null) {
			String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
					.path("/downloadFile/")
					.path(candidate.getFiles().getId())
					.toUriString();
			candidateProfile.setCV_Uri(fileDownloadUri);
		}
		candidateProfile.setName(userRepository.getNameByCandidateId(candidateId));
		candidateProfile.setAddress(candidate.getHomeaddress());
		candidateProfile.setGender(candidate.getGender());
		List<String> types = jobTypeRepository.getAllCandidateJobTypeName(candidate.getId());
		candidateProfile.setJobtypes(types);
		candidateProfile.setDoB(candidate.getDoB());
		candidateProfile.setPhonenumber(candidate.getPhone_number());
		candidateProfile.setWork_title(candidate.getWork_title());
		candidateProfile.setEducations(educations);
		candidateProfile.setExperiences(experiences);
		candidateProfile.setExpectedsalary(candidate.getExpectedsalary());

		return candidateProfile;
	}
	
	@GetMapping("/history")
	public List<CandidateSummary> getAllViewedCandidateProfiles(@CurrentUser UserPrincipal currentUser) {
		return employerHistoryRepository.getAllViewedCandidateProfiles(employerRepository.getEmployerIdByAccount_Id(currentUser.getId()));
	}

}
