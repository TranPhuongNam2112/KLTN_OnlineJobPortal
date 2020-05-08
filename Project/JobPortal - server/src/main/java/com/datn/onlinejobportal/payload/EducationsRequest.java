package com.datn.onlinejobportal.payload;

import java.util.Set;

import javax.validation.Valid;

public class EducationsRequest {
	@Valid
	private Set<EducationRequest> educations;
	
	public Set<EducationRequest> getEducations() {
		return educations;
	}

	public void setEducations(Set<EducationRequest> educations) {
		this.educations = educations;
	}
}
