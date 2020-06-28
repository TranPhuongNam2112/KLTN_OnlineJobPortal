package com.datn.onlinejobportal.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriUtils;

import com.datn.onlinejobportal.dto.CandidateStats;
import com.datn.onlinejobportal.dto.EmployerSummary;
import com.datn.onlinejobportal.dto.JobPostSummary;
import com.datn.onlinejobportal.event.SaveCandidateEvent;
import com.datn.onlinejobportal.exception.ResourceNotFoundException;
import com.datn.onlinejobportal.model.Candidate;
import com.datn.onlinejobportal.model.CandidateHistory;
import com.datn.onlinejobportal.model.DBFile;
import com.datn.onlinejobportal.model.Education;
import com.datn.onlinejobportal.model.Experience;
import com.datn.onlinejobportal.model.JobLocation;
import com.datn.onlinejobportal.model.JobPost;
import com.datn.onlinejobportal.model.SavedJobPost;
import com.datn.onlinejobportal.model.User;
import com.datn.onlinejobportal.payload.CandidateProfile;
import com.datn.onlinejobportal.payload.CandidateProfileRequest;
import com.datn.onlinejobportal.payload.EducationResponse;
import com.datn.onlinejobportal.payload.EducationsRequest;
import com.datn.onlinejobportal.payload.ExperienceResponse;
import com.datn.onlinejobportal.payload.ExperiencesRequest;
import com.datn.onlinejobportal.payload.JobPostDetails;
import com.datn.onlinejobportal.repository.CandidateHistoryRepository;
import com.datn.onlinejobportal.repository.CandidateRepository;
import com.datn.onlinejobportal.repository.EducationRepository;
import com.datn.onlinejobportal.repository.EmployerRepository;
import com.datn.onlinejobportal.repository.ExperienceRepository;
import com.datn.onlinejobportal.repository.IndustryRepository;
import com.datn.onlinejobportal.repository.JobLocationRepository;
import com.datn.onlinejobportal.repository.JobPostRepository;
import com.datn.onlinejobportal.repository.JobTypeRepository;
import com.datn.onlinejobportal.repository.SavedJobPostRepository;
import com.datn.onlinejobportal.repository.UserRepository;
import com.datn.onlinejobportal.security.CurrentUser;
import com.datn.onlinejobportal.security.UserPrincipal;
import com.datn.onlinejobportal.service.CandidateStatisticService;
import com.datn.onlinejobportal.service.DBFileStorageService;
import com.datn.onlinejobportal.util.AppConstants;
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/candidate")
public class CandidateDashboardController {

	@Autowired
	private CandidateHistoryRepository candidateHistoryRepository;

	@Autowired
	private CandidateRepository candidateRepository;

	@Autowired
	private JobPostRepository jobPostRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private DBFileStorageService dbFileStorageService;

	@Autowired
	private SavedJobPostRepository savedJobPostRepository;

	@Autowired
	private ExperienceRepository experienceRepository;

	@Autowired
	private EducationRepository educationRepository;

	@Autowired
	private JobTypeRepository jobTypeRepository;

	@Autowired
	private JobLocationRepository jobLocationRepository;

	@Autowired
	private IndustryRepository industryRepository;
	
	@Autowired
	private EmployerRepository employerRepository;
	
	@Autowired
	private CandidateStatisticService candidateStatisticService;


	@GetMapping("/myprofile")
	@PreAuthorize("hasRole('CANDIDATE')")
	public CandidateProfile getMyProfile(@CurrentUser UserPrincipal currentUser) {

		Candidate candidate = candidateRepository.getCandidateByUserId(currentUser.getId());
		List<ExperienceResponse> experiences = experienceRepository.getExperienceByUser(currentUser.getId());
		List<EducationResponse> educations = educationRepository.getEducationByUser(currentUser.getId());
		CandidateProfile candidateProfile = new CandidateProfile();
		if (userRepository.getImage(currentUser.getId()) != null) {
			candidateProfile.setImage(userRepository.getImage(currentUser.getId()));
		}
		if (candidate.getFiles() != null) {
			String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
					.path("/downloadFile/")
					.path(candidate.getFiles().getId())
					.toUriString();
			candidateProfile.setCV_Uri(fileDownloadUri);
			candidateProfile.setCV_name(candidate.getFiles().getFileName());
		}
		candidateProfile.setIndustries(industryRepository.getCandidateIndustryNames());
		candidateProfile.setName(currentUser.getFullname());
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
		candidateProfile.setImageUrl(userRepository.getCandidateImageUrl(candidate.getId()));
		candidateProfile.setCity_province(candidate.getCity_province());
		return candidateProfile;
	}



	@PostMapping("/myprofile/addEducation")
	@PreAuthorize("hasRole('CANDIDATE')")
	public ResponseEntity<?> addNewEducation(@CurrentUser UserPrincipal currentUser, 
			@Valid @RequestBody EducationsRequest educationsRequest) {
		Long candidateId = candidateRepository.getCandidateIdByUserId(currentUser.getId());

		Candidate candidate = candidateRepository.findById(candidateId).orElseThrow(
				() -> new ResourceNotFoundException("Candidate", "id", candidateId));
		educationsRequest.getEducations().forEach(educationRequest -> {
			candidate.addEducation(new Education(educationRequest.getUniversity_college(), educationRequest.getMajor(), educationRequest.getStartdate(), educationRequest.getCompletiondate(), educationRequest.getGpa()));
		});
		candidateRepository.save(candidate);
		return ResponseEntity.ok("Thêm thành công");
	}

	@PostMapping("/myprofile/addExperience")
	@PreAuthorize("hasRole('CANDIDATE')")
	public ResponseEntity<?> addNewExperience(@CurrentUser UserPrincipal currentUser, 
			@Valid @RequestBody ExperiencesRequest experiencesRequest) {
		Long candidateId = candidateRepository.getCandidateIdByUserId(currentUser.getId());

		Candidate candidate = candidateRepository.findById(candidateId).orElseThrow(
				() -> new ResourceNotFoundException("Candidate", "id", candidateId));
		experiencesRequest.getExperiences().forEach(experienceRequest -> {
			candidate.addExperience(new Experience(experienceRequest.getCompanyname(), experienceRequest.getJobtitle(), experienceRequest.getStartdate(), experienceRequest.getEnddate(), experienceRequest.getDescription()));
		});
		candidateRepository.save(candidate);
		return ResponseEntity.ok("Thêm thành công");

	}


	@DeleteMapping("/myprofile/experience/remove/{experienceId}")
	@PreAuthorize("hasRole('CANDIDATE')")
	public ResponseEntity<?> removeExperience(@PathVariable("experienceId") Long experienceId ,@CurrentUser UserPrincipal currentUser) {
		Long candidateId = candidateRepository.getCandidateIdByUserId(currentUser.getId());

		Candidate candidate = candidateRepository.findById(candidateId).orElseThrow(
				() -> new ResourceNotFoundException("Candidate", "id", candidateId));
		Experience experience = experienceRepository.getExperienceByUserIdAndExperienceId(currentUser.getId(), experienceId);
		experienceRepository.delete(experience);
		candidate.removeExperience(experience);
		candidateRepository.save(candidate);
		return ResponseEntity.ok("Xóa thành công");

	}

	@DeleteMapping("/myprofile/education/remove/{educationId}")
	@PreAuthorize("hasRole('CANDIDATE')")
	public ResponseEntity<?> removeEducation(@PathVariable("educationId") Long educationId ,@CurrentUser UserPrincipal currentUser) {
		Long candidateId = candidateRepository.getCandidateIdByUserId(currentUser.getId());

		Candidate candidate = candidateRepository.findById(candidateId).orElseThrow(
				() -> new ResourceNotFoundException("Candidate", "id", candidateId));
		Education education = educationRepository.getEducationByUserIdAndEducationId(currentUser.getId(), educationId);
		candidate.removeEducation(education);
		educationRepository.delete(education);
		candidateRepository.save(candidate);
		return ResponseEntity.ok("Xóa thành công");

	}

	@PutMapping("/myprofile")
	@PreAuthorize("hasRole('CANDIDATE')")
	public ResponseEntity<?> updateProfile(@CurrentUser UserPrincipal currentUser,
			@Valid @RequestBody CandidateProfileRequest candidateProfileRequest) {
		Long candidateId = candidateRepository.getCandidateIdByUserId(currentUser.getId());
		Candidate candidate = candidateRepository.findById(candidateId).orElseThrow(
				() -> new ResourceNotFoundException("Candidate", "id", candidateId));
		User user = userRepository.findById(currentUser.getId()).orElseThrow(
				() -> new ResourceNotFoundException("User", "id", candidateId));

		user.setName(candidateProfileRequest.getName());
		candidate.setUpdatedAt(LocalDate.now());
		candidate.setIndustries(industryRepository.getAllIndustriesByNames(candidateProfileRequest.getIndustries()));
		candidate.setDoB(candidateProfileRequest.getDoB());
		candidate.setGender(candidateProfileRequest.getGender());
		candidate.setHomeaddress(candidateProfileRequest.getAddress());
		candidate.setCity_province(candidateProfileRequest.getCity_province());
		candidate.setWork_title(candidateProfileRequest.getWork_title());
		candidate.setPhone_number(candidateProfileRequest.getPhonenumber());
		candidate.setJobtypes(jobTypeRepository.getAllCandidateJobType(candidateProfileRequest.getJobtypes()));
		candidate.setProfile_visible(candidateProfileRequest.getProfile_visible());
		candidate.setYearsofexperience(candidateProfileRequest.getExperiencedyears());
		candidate.setExpectedsalary(candidateProfileRequest.getExpectedsalary());
		candidateRepository.save(candidate);
		return ResponseEntity.ok("Cập nhật thành công!");
	}

	@PostMapping("/uploadProfileImage")
	@PreAuthorize("hasRole('CANDIDATE')")
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

	@PostMapping("/uploadCV")
	@PreAuthorize("hasRole('CANDIDATE')")
	public ResponseEntity<?> uploadCV (@RequestParam("CV") MultipartFile CV, @CurrentUser UserPrincipal currentUser) {
		Long candidateId = candidateRepository.getCandidateIdByUserId(currentUser.getId());
		DBFile CVfile = dbFileStorageService.storeFile(CV);
		if (CVfile.getCreatedAt() == null) {
			CVfile.setCreatedAt(LocalDate.now());
		} else {
			CVfile.setUpdatedAt(LocalDate.now());
		}
		Candidate candidate = candidateRepository.findById(candidateId).orElseThrow(
				() -> new ResourceNotFoundException("Candidate", "id", candidateId));		
		candidate.setFiles(CVfile);
		candidateRepository.save(candidate);
		return ResponseEntity.ok("Uploaded successfully");
	}

	@GetMapping("/savedjobposts")
	@PreAuthorize("hasRole('CANDIDATE')")
	public Page<JobPostSummary> getAllJobPosts(@CurrentUser UserPrincipal currentUser, @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNo,
			@RequestParam(defaultValue = "3")int pageSize,
			@RequestParam(defaultValue = "expirationDate") String sortBy) {
		Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
		Long candidateId = candidateRepository.getCandidateIdByUserId(currentUser.getId());
		return savedJobPostRepository.getJobPostsSavedBy(candidateId, pageable);
	}

	@PostMapping("/savejobpost/{jobpostId}")
	@PreAuthorize("hasRole('CANDIDATE')")
	public ResponseEntity<?> saveJobPost(@PathVariable("jobpostId") Long jobpostId, @CurrentUser UserPrincipal currentUser) {
		Candidate candidate = candidateRepository.getCandidateByUserId(currentUser.getId());
		JobPost jobpost = jobPostRepository.findById(jobpostId).orElseThrow(() -> new ResourceNotFoundException("Job post", "jobpostId", jobpostId));
		SavedJobPost sjp = new SavedJobPost(candidate, jobpost);
		candidate.getSavedJobPosts().add(sjp);
		jobpost.getSavedjobpost().add(sjp);
		savedJobPostRepository.save(sjp);
		candidateRepository.save(candidate);
		jobPostRepository.save(jobpost);
		return ResponseEntity.ok("Đã lưu bài đăng thành công!");
	}

	@DeleteMapping("/savedjobposts/{jobpostId}")
	@PreAuthorize("hasRole('CANDIDATE')")
	public ResponseEntity<?> removedSavedJobPost(@PathVariable("jobpostId") Long jobpostId, @CurrentUser UserPrincipal currentUser) {
		Candidate candidate = candidateRepository.getCandidateByUserId(currentUser.getId());
		JobPost jobpost = jobPostRepository.findById(jobpostId).orElseThrow(() -> new ResourceNotFoundException("Job post", "jobpostId", jobpostId));
		SavedJobPost sjp = savedJobPostRepository.getSavedJobPostByJobPostId(candidate.getId(), jobpostId);
		candidate.removeJobPost(jobpost);
		candidateRepository.save(candidate);
		savedJobPostRepository.delete(sjp);
		return ResponseEntity.ok("Đã xóa bài đăng thành công!");
	}

	@GetMapping("/jobtypes")
//	@PreAuthorize("hasRole('CANDIDATE')")
	public List<String> getAllJobTypes(@CurrentUser UserPrincipal currentUser) {
		return jobTypeRepository.getAllJobTypes();
	}

	@GetMapping("/jobposts/{jobpostId}")
	@PreAuthorize("hasRole('CANDIDATE')")
	public JobPostDetails getJobPostById(@CurrentUser UserPrincipal currentUser, @PathVariable("jobpostId") Long jobpostId) {

		JobPost jobpost = jobPostRepository.findById(jobpostId).orElseThrow(() -> new ResourceNotFoundException("Job post", "jobpostId", jobpostId));
		if (candidateHistoryRepository.getDuplicateViewedJobPost(jobpost.getId(), currentUser.getId()) == null)
		{
			CandidateHistory candidatehistory = new CandidateHistory(candidateRepository.getCandidateByUserId(currentUser.getId()), jobpost, LocalDate.now());
			Candidate candidate = candidateRepository.getCandidateByUserId(currentUser.getId());
			candidate.getCandidatehistories().add(candidatehistory);
			jobpost.getCandidatehistories().add(candidatehistory);
			candidateHistoryRepository.save(candidatehistory);
			candidateRepository.save(candidate);
			jobPostRepository.save(jobpost);
		}
		else {
			CandidateHistory candidatehistory = candidateHistoryRepository.getCandidateHistory(jobpost.getId(), currentUser.getId());
			candidatehistory.setViewDate(LocalDate.now());
			candidateHistoryRepository.save(candidatehistory);
		}
		JobLocation jobLocation = jobLocationRepository.findByJobPostId(jobpostId);
		JobPostDetails jobpostDetails = new JobPostDetails();
		jobpostDetails.setCity_province(jobLocation.getCity_province());
		jobpostDetails.setCreatedDate(jobpost.getCreatedAt());
		jobpostDetails.setDescription(jobpost.getJob_description());
		jobpostDetails.setExpirationDate(jobpost.getExpirationDate());
		jobpostDetails.setIndustry(industryRepository.getAllJobPostIndustries(jobpost.getId()));
		jobpostDetails.setJobtitle(jobpost.getJob_title());
		jobpostDetails.setJobtypes(jobpost.getJobtype().getJob_type_name());
		jobpostDetails.setMaxSalary(jobpost.getMax_salary());
		jobpostDetails.setMinSalary(jobpost.getMin_salary());
		jobpostDetails.setRequiredexperienceyears(jobpost.getRequiredexpreienceyears());
		jobpostDetails.setStreet_address(jobLocation.getStreet_address());

		return jobpostDetails;
	}

	@GetMapping("/recommendedjobposts")
	@PreAuthorize("hasRole('CANDIDATE')")
	public Page<JobPostSummary> getAllRecommendedJobPosts(@CurrentUser UserPrincipal currentUser, @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNo,
			@RequestParam(defaultValue = "20")int pageSize,
			@RequestParam(defaultValue = "expirationDate") String sortBy) {
		Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
		Candidate candidate = candidateRepository.getCandidateByUserId(currentUser.getId());

		return jobPostRepository.getRecommendedJobPostsByUser(candidate.getId(), pageable);

	}

	@Async
	@EventListener
	@PreAuthorize("hasRole('CANDIDATE')")
	public ResponseEntity<?> saveCandiateEventListener(SaveCandidateEvent saveCandidateEvent) throws InterruptedException {
		return ResponseEntity.ok("Nhà tuyển dụng " + saveCandidateEvent.getEmployerId()+" đã lưu hồ sơ của bạn!");
	}

	@GetMapping("/history")
	@PreAuthorize("hasRole('CANDIDATE')")
	public List<JobPostSummary> getAllViewedJobPost(@CurrentUser UserPrincipal currentUser) {
		return candidateHistoryRepository.getAllCandidatHistory(candidateRepository.getCandidateIdByUserId(currentUser.getId()));
	}
	
	@GetMapping("/allemployers")
	@PreAuthorize("hasRole('CANDIDATE')")
	public Page<EmployerSummary> getAllEmployers(@CurrentUser UserPrincipal currentUser,  @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNo,
			@RequestParam(defaultValue = "8")int pageSize,
			@RequestParam(defaultValue = "companyname") String sortBy) {
		Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
		return employerRepository.getAllEmployers(pageable);
	}
	
	@GetMapping("/mystats")
	@PreAuthorize("hasRole('CANDIDATE')")
	public CandidateStats getCandidateStats(@CurrentUser UserPrincipal currentUser) {
		CandidateStats candidate = new CandidateStats();
		candidate.setViewedEmployersCount(candidateStatisticService.countViewedProfileEmployers(currentUser));
		candidate.setSavedEmployersCount(candidateStatisticService.countSavedProfileEmployers(currentUser));
		return candidate;
	}
	

	
	@GetMapping("/employerjobposts/{companyname}")
	@PreAuthorize("hasRole('CANDIDATE')")
	public Page<JobPostSummary> getAllJobPostByEmployers(@CurrentUser UserPrincipal currentUser, @PathVariable("companyname") String companyname, 
			@RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNo,
			@RequestParam(defaultValue = "10")int pageSize,
			@RequestParam(defaultValue = "expirationDate") String sortBy) {
		Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
		return jobPostRepository.getAllJobPostsByEmployer(companyname, pageable);
	}
	
	@GetMapping("/{industry}/{websitename}")
	@PreAuthorize("hasRole('CANDIDATE')")
	public Page<JobPostSummary> getJobPostsByIndustryAndWebsiteName(@PathVariable("industry") String industry, @PathVariable("websitename") String websitename, @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNo,
			@RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_SIZE)int pageSize,
			@RequestParam(defaultValue = "expirationDate") String sortBy) {
		Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
		return jobPostRepository.getAllJobPostsByIndustryAndWebsitename(industry, websitename, pageable);
	}
	
	@GetMapping("/getJobPostby/{industry}")
	@PreAuthorize("hasRole('CANDIDATE')")
	public Page<JobPostSummary> getJobPostsByIndustry(@PathVariable("industry") String industry, 
			@RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNo,
			@RequestParam(defaultValue = "10")int pageSize,
			@RequestParam(defaultValue = "expirationDate") String sortBy) {
		Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
		return jobPostRepository.getAllJobPostsByIndustry(industry, pageable);
	}
	
	@GetMapping("/industries")

	public List<String> getAllIndustries(@CurrentUser UserPrincipal currentUser) {
		return industryRepository.getAllIndustriesName();
	}
	

}
