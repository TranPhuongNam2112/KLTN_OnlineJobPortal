package com.datn.onlinejobportal.payload;

import java.util.Date;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CandidateProfileRequest {
	
	@NotBlank
	@Size(max=100)
	private String fullname;
	
	@NotNull
	private Date DoB;
	
	@NotBlank
	private String gender;
	
	@NotBlank
	private String homeaddress;
	
	@NotBlank
	@Size(max=11)
	private String phone_number;
	
	@NotNull
	@Valid
	private Set<EducationRequest> educations;
	
	@NotNull
	@Valid
	private Set<ExperienceRequest> experiences;

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
	
}
