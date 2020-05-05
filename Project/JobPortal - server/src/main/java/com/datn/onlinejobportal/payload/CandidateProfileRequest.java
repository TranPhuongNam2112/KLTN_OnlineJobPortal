package com.datn.onlinejobportal.payload;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.datn.onlinejobportal.model.JobType;

public class CandidateProfileRequest {
	
	@NotBlank
	@Size(max=100)
	private String fullname;
	
	private Date DoB;
	
	private String gender;
	
	private String homeaddress;
	
	private String city_province;
	
	private String work_title;
	
	@Size(max=11)
	private String phone_number;
	
	private List<String> jobtypes;
	
	private Boolean profile_visible;
	
	private Long experiencedyears;
	
	@Valid
	private Set<EducationRequest> educations;
	
	@Valid
	private Set<ExperienceRequest> experiences;

	public Boolean getProfile_visible() {
		return profile_visible;
	}

	public void setProfile_visible(Boolean profile_visible) {
		this.profile_visible = profile_visible;
	}

	public Long getExperiencedyears() {
		return experiencedyears;
	}

	public void setExperiencedyears(Long experiencedyears) {
		this.experiencedyears = experiencedyears;
	}

	public String getCity_province() {
		return city_province;
	}

	public void setCity_province(String city_province) {
		this.city_province = city_province;
	}

	public String getWork_title() {
		return work_title;
	}

	public void setWork_title(String work_title) {
		this.work_title = work_title;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public Date getDoB() {
		return DoB;
	}

	public void setDoB(Date doB) {
		DoB = doB;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getHomeaddress() {
		return homeaddress;
	}

	public void setHomeaddress(String homeaddress) {
		this.homeaddress = homeaddress;
	}

	public String getPhone_number() {
		return phone_number;
	}

	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}

	public Set<EducationRequest> getEducations() {
		return educations;
	}

	public void setEducations(Set<EducationRequest> educations) {
		this.educations = educations;
	}

	public Set<ExperienceRequest> getExperiences() {
		return experiences;
	}

	public void setExperiences(Set<ExperienceRequest> experiences) {
		this.experiences = experiences;
	}

	public List<String> getJobtypes() {
		return jobtypes;
	}

	public void setJobtypes(List<String> jobtypes) {
		this.jobtypes = jobtypes;
	}
	
	
	
}
