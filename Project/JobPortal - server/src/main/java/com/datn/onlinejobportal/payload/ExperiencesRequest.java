package com.datn.onlinejobportal.payload;

import java.util.Set;

import javax.validation.Valid;

public class ExperiencesRequest {

	@Valid
	private Set<ExperienceRequest> experiences;
	
	public Set<ExperienceRequest> getExperiences() {
		return experiences;
	}

	public void setExperiences(Set<ExperienceRequest> experiences) {
		this.experiences = experiences;
	}
}
