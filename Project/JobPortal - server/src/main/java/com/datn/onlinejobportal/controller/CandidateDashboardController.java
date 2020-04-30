package com.datn.onlinejobportal.controller;

import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
import com.datn.onlinejobportal.model.JobType;
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
@RequestMapping("/candidate/mydashboard")
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

	private Set<JobType> jobtypes;
	
	private List<String> types;
/*
	@GetMapping("/mysavedjobposts/{jobpostId}")
	public PagedResponse<SavedJobPostResponse> getAllSavedJobPosts(@CurrentUser UserPrincipal currentUser) {
		
	}

*/
	
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
		candidate.getJobtypes().forEach(jobtype -> {
			types.add(jobtype.getJob_type_name());
		});;
		candidateProfile.setJobtypes(types);
		candidateProfile.setDoB(candidate.getDoB());
		candidateProfile.setPhonenumber(candidate.getPhone_number());
		candidateProfile.setWork_title(candidate.getWork_title());
		candidateProfile.setEducations(educations);
		candidateProfile.setExperiences(experiences);
		
		return candidateProfile;
	}
	
	
	
	@PutMapping("/myprofile")
	@PreAuthorize("hasRole('CANDIDATE')")
	public Candidate updateProfile(@CurrentUser UserPrincipal currentUser,
			@Valid @RequestBody CandidateProfileRequest candidateProfileRequest, @RequestParam(value="profileimage", required=false) MultipartFile imagefile, @RequestParam(value="CV", required=false) MultipartFile CVfile) {
		Long candidateId = candidateRepository.getCandidateIdByUserId(currentUser.getId());
		Candidate candidate = candidateRepository.findById(candidateId).orElseThrow(
				() -> new ResourceNotFoundException("Candidate", "id", candidateId));
		
		DBFile cv = dbFileStorageService.storeFile(CVfile);
		User user = userRepository.getOne(currentUser.getId());
		DBFile profileimage = dbFileStorageService.storeFile(imagefile);
		user.setFiles(profileimage);
		
		candidateProfileRequest.getEducations().forEach(educationRequest -> {
			 candidate.addEducation(new Education(educationRequest.getUniversity_college(), educationRequest.getMajor(), educationRequest.getStartdate(), educationRequest.getCompletiondate(), educationRequest.getGpa()));
		});
		candidateProfileRequest.getExperiences().forEach(experienceRequest -> {
			 candidate.addExperience(new Experience(experienceRequest.getCompanyname(), experienceRequest.getJobtitle(), experienceRequest.getStartdate(), experienceRequest.getEnddate(), experienceRequest.getDescription()));
		});
		userRepository.save(user);
		
		candidate.setDoB(candidateProfileRequest.getDoB());
		candidate.setGender(candidateProfileRequest.getGender());
		candidate.setHomeaddress(candidateProfileRequest.getHomeaddress());
		candidate.setCity_province(candidateProfileRequest.getCity_province());
		candidate.setWork_title(candidateProfileRequest.getWork_title());
		candidate.setPhone_number(candidateProfileRequest.getPhone_number());
		candidateProfileRequest.getJobtypes().forEach(jobtype -> {
			jobtypes.add(jobTypeRepository.findByJob_type_name(jobtype));
		});
		candidate.setJobtypes(jobtypes);
		candidate.setFiles(cv);
		Candidate newCandidate = candidateRepository.save(candidate);

		 
		return newCandidate;
	}
	
	@PutMapping("/savedjobposts")
	public Page<JobPostSummary> getAllJobPosts(@CurrentUser UserPrincipal currentUser, @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNo,
			@RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_SIZE)int pageSize,
			@RequestParam(defaultValue = "expirationDate") String sortBy) {
		Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
		Long candidateId = candidateRepository.getCandidateIdByUserId(currentUser.getId());
		return savedJobPostRepository.getJobPostsSavedBy(candidateId, pageable);
	}
/*
	@PutMapping("/profile")
	@PreAuthorize("hasRole('EMPLOYER')")
	public Employer updateProfile(@CurrentUser UserPrincipal currentUser, 
			@Valid @RequestBody EmployerRequest employerRequest) {

		Employer employer = employerRepository.findByAccount_id(currentUser.getId());

		employer.setCompanyname(employerRequest.getCompanyname());
		employer.setDescription(employerRequest.getDescription());
		employer.setEstablishmentdate(employerRequest.getEstablishmentdate());
		employer.setIndustry(employerRequest.getIndustry());
		employer.setMain_address(employerRequest.getMainaddress());
		employer.setPhone_number(employerRequest.getPhone_number());
		employer.setWebsiteurl(employerRequest.getWebsite_url());

		Employer updatedEmployer = employerRepository.save(employer);
		return updatedEmployer;
	}
	
*/
}
