package com.datn.onlinejobportal.controller;

import java.net.URI;
import java.time.Instant;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.datn.onlinejobportal.exception.ResourceNotFoundException;
import com.datn.onlinejobportal.model.Candidate;
import com.datn.onlinejobportal.model.Education;
import com.datn.onlinejobportal.model.Employer;
import com.datn.onlinejobportal.model.Experience;
import com.datn.onlinejobportal.model.JobLocation;
import com.datn.onlinejobportal.model.JobPost;
import com.datn.onlinejobportal.payload.ApiResponse;
import com.datn.onlinejobportal.payload.CandidateProfileRequest;
import com.datn.onlinejobportal.payload.EmployerRequest;
import com.datn.onlinejobportal.payload.JobPostRequest;
import com.datn.onlinejobportal.payload.PagedResponse;
import com.datn.onlinejobportal.repository.CandidateRepository;
import com.datn.onlinejobportal.repository.JobPostRepository;
import com.datn.onlinejobportal.repository.UserRepository;
import com.datn.onlinejobportal.security.CurrentUser;
import com.datn.onlinejobportal.security.UserPrincipal;

@RestController
@RequestMapping("/candidate/mydashboard")
public class CandidateDashboardController {
	
	@Autowired
	private CandidateRepository candidateRepository;
	
	@Autowired
	private JobPostRepository jobPostRepository;
	
	@Autowired
	private UserRepository userRepository;
	
/*
	@GetMapping("/mysavedjobposts/{jobpostId}")
	public PagedResponse<SavedJobPostResponse> getAllSavedJobPosts(@CurrentUser UserPrincipal currentUser) {
		
	}

*/
	@PutMapping("/myprofile")
	@PreAuthorize("hasRole('CANDIDATE')")
	public Candidate updateProfile(@CurrentUser UserPrincipal currentUser,
			@Valid @RequestBody CandidateProfileRequest candidateProfileRequest) {
		Long candidateId = candidateRepository.getCandidateIdByUserId(currentUser.getId());
		Candidate candidate = candidateRepository.findById(candidateId).orElseThrow(
				() -> new ResourceNotFoundException("Candidate", "id", candidateId));
		
		candidate.setDoB(candidateProfileRequest.getDoB());
		candidate.setGender(candidateProfileRequest.getGender());
		candidate.setHomeaddress(candidateProfileRequest.getHomeaddress());
		candidate.setPhone_number(candidateProfileRequest.getPhone_number());
		candidateProfileRequest.getEducations().forEach(educationRequest -> {
			 candidate.addEducation(new Education(educationRequest.getUniversity_college(), educationRequest.getMajor(), educationRequest.getStartdate(), educationRequest.getCompletiondate(), educationRequest.getGpa()));
		});
		candidateProfileRequest.getExperiences().forEach(experienceRequest -> {
			 candidate.addExperience(new Experience(experienceRequest.getCompanyname(), experienceRequest.getJobtitle(), experienceRequest.getStartdate(), experienceRequest.getEnddate()));
		});
		
		Candidate newCandidate = candidateRepository.save(candidate);
		   
		return newCandidate;
	}
	
	@DeleteMapping("/jobposts/{jobpostId}")
	@PreAuthorize("hasRole('EMPLOYER')")
	public ResponseEntity<?> deleteJobPost(@PathVariable(value = "jobpostId") Long jobpostId) {
	    JobPost jobpost = jobPostRepository.findById(jobpostId)
	            .orElseThrow(() -> new ResourceNotFoundException("Job post", "id", jobpostId));

	    jobPostRepository.delete(jobpost);

	    return ResponseEntity.ok().build();
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
