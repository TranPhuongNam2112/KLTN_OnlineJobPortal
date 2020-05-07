package com.datn.onlinejobportal.controller;

import java.time.LocalDate;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.datn.onlinejobportal.dto.JobPostSummary;
import com.datn.onlinejobportal.exception.ResourceNotFoundException;
import com.datn.onlinejobportal.model.Candidate;
import com.datn.onlinejobportal.model.DBFile;
import com.datn.onlinejobportal.model.Education;
import com.datn.onlinejobportal.model.Experience;
import com.datn.onlinejobportal.model.JobPost;
import com.datn.onlinejobportal.model.User;
import com.datn.onlinejobportal.payload.CandidateProfile;
import com.datn.onlinejobportal.payload.CandidateProfileRequest;
import com.datn.onlinejobportal.repository.CandidateRepository;
import com.datn.onlinejobportal.repository.EducationRepository;
import com.datn.onlinejobportal.repository.ExperienceRepository;
import com.datn.onlinejobportal.repository.JobPostRepository;
import com.datn.onlinejobportal.repository.JobTypeRepository;
import com.datn.onlinejobportal.repository.SavedJobPostRepository;
import com.datn.onlinejobportal.repository.UserRepository;
import com.datn.onlinejobportal.security.CurrentUser;
import com.datn.onlinejobportal.security.UserPrincipal;
import com.datn.onlinejobportal.service.DBFileStorageService;
import com.datn.onlinejobportal.util.AppConstants;

@RestController
@RequestMapping("/candidate")
public class CandidateDashboardController {

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
	



	@GetMapping("/myprofile")
	@PreAuthorize("hasRole('CANDIDATE')")
	public CandidateProfile getMyProfile(@CurrentUser UserPrincipal currentUser) {

		Candidate candidate = candidateRepository.getCandidateByUserId(currentUser.getId());
		List<Experience> experiences = experienceRepository.getExperienceByUser(currentUser.getId());
		List<Education> educations = educationRepository.getEducationByUser(currentUser.getId());
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
		}
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

		return candidateProfile;
	}

	@PostMapping("/myprofile")
	@PreAuthorize("hasRole('CANDIDATE')")
	public Candidate updateProfile(@CurrentUser UserPrincipal currentUser,
			@Valid @RequestBody CandidateProfileRequest candidateProfileRequest) {
		Long candidateId = candidateRepository.getCandidateIdByUserId(currentUser.getId());
		Candidate candidate = candidateRepository.findById(candidateId).orElseThrow(
				() -> new ResourceNotFoundException("Candidate", "id", candidateId));

		User user = userRepository.getOne(currentUser.getId());

		candidateProfileRequest.getEducations().forEach(educationRequest -> {
			candidate.addEducation(new Education(educationRequest.getUniversity_college(), educationRequest.getMajor(), educationRequest.getStartdate(), educationRequest.getCompletiondate(), educationRequest.getGpa()));
		});
		candidateProfileRequest.getExperiences().forEach(experienceRequest -> {
			candidate.addExperience(new Experience(experienceRequest.getCompanyname(), experienceRequest.getJobtitle(), experienceRequest.getStartdate(), experienceRequest.getEnddate(), experienceRequest.getDescription()));
		});
		userRepository.save(user);

		candidate.setUpdatedAt(LocalDate.now());
		candidate.setDoB(candidateProfileRequest.getDoB());
		candidate.setGender(candidateProfileRequest.getGender());
		candidate.setHomeaddress(candidateProfileRequest.getHomeaddress());
		candidate.setCity_province(candidateProfileRequest.getCity_province());
		candidate.setWork_title(candidateProfileRequest.getWork_title());
		candidate.setPhone_number(candidateProfileRequest.getPhone_number());
		candidate.setJobtypes(jobTypeRepository.getAllCandidateJobType(candidateProfileRequest.getJobtypes()));
		candidate.setProfile_visible(candidateProfileRequest.getProfile_visible());
		candidate.setYearsofexperience(candidateProfileRequest.getExperiencedyears());
		Candidate newCandidate = candidateRepository.save(candidate);


		return newCandidate;
	}

	@PostMapping("/uploadProfileImage")
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
	public Page<JobPostSummary> getAllJobPosts(@CurrentUser UserPrincipal currentUser, @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNo,
			@RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_SIZE)int pageSize,
			@RequestParam(defaultValue = "expirationDate") String sortBy) {
		Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
		Long candidateId = candidateRepository.getCandidateIdByUserId(currentUser.getId());
		return savedJobPostRepository.getJobPostsSavedBy(candidateId, pageable);
	}

	@PostMapping("/save/{jobpostId}")
	public ResponseEntity<?> saveJobPost(@RequestParam("jobpostId") Long jobpostId, @CurrentUser UserPrincipal currentUser) {
		Candidate candidate = candidateRepository.getCandidateByUserId(currentUser.getId());
		JobPost jobpost = jobPostRepository.findById(jobpostId).orElseThrow(() -> new ResourceNotFoundException("Job post", "jobpostId", jobpostId));
		candidate.addJobPost(jobpost);
		return ResponseEntity.ok("Đã lưu bài đăng thành công!");
	}
	
	@PostMapping("/savedjobposts/{jobpostId}")
	public ResponseEntity<?> removedSavedJobPost(@RequestParam("jobpostId") Long jobpostId, @CurrentUser UserPrincipal currentUser) {
		Candidate candidate = candidateRepository.getCandidateByUserId(currentUser.getId());
		JobPost jobpost = jobPostRepository.findById(jobpostId).orElseThrow(() -> new ResourceNotFoundException("Job post", "jobpostId", jobpostId));
		candidate.removeJobPost(jobpost);
		return ResponseEntity.ok("Đã xóa bài đăng thành công!");
	}

}
