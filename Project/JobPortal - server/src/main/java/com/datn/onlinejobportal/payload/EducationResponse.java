package com.datn.onlinejobportal.payload;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class EducationResponse {
	
	private Long id;
	private String university_college;
	private String major;
	
	@DateTimeFormat(pattern = "dd.MM.yyyy")
	private Date start_date;
	
	@DateTimeFormat(pattern = "dd.MM.yyyy")
	private Date completion_date;
	private Long gpa;
	
	
	
	public EducationResponse() {
		super();
	}
	public EducationResponse(Long id, String university_college, String major, Date start_date, Date completion_date,
			Long gpa) {
		super();
		this.id = id;
		this.university_college = university_college;
		this.major = major;
		this.start_date = start_date;
		this.completion_date = completion_date;
		this.gpa = gpa;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
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
	public Date getStart_date() {
		return start_date;
	}
	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}
	public Date getCompletion_date() {
		return completion_date;
	}
	public void setCompletion_date(Date completion_date) {
		this.completion_date = completion_date;
	}
	public Long getGpa() {
		return gpa;
	}
	public void setGpa(Long gpa) {
		this.gpa = gpa;
	}
	
	

}
