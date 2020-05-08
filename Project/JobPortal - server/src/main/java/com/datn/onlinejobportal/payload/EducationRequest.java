package com.datn.onlinejobportal.payload;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class EducationRequest {
	
	@NotBlank
	private String university_college;
	
	@NotBlank
	private String major;
	
	@NotNull
	private Date startdate;
	
	@NotNull
	private Date completiondate;
	
	@NotNull
	private Long gpa;

	public String getUniversity_college() {
		return university_college;
	}

	public void setUniversity_college(String university_college) {
		this.university_college = university_college;
	}

	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	public Date getStartdate() {
		return startdate;
	}

	public void setStartdate(Date startdate) {
		this.startdate = startdate;
	}

	public Date getCompletiondate() {
		return completiondate;
	}

	public void setCompletiondate(Date completiondate) {
		this.completiondate = completiondate;
	}

	public Long getGpa() {
		return gpa;
	}

	public void setGpa(Long gpa) {
		this.gpa = gpa;
	}
	
	

}
