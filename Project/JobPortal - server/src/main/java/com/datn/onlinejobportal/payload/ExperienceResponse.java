package com.datn.onlinejobportal.payload;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class ExperienceResponse {
	
	private Long id;
	private String companyname;
	private String job_title;
	
	@DateTimeFormat(pattern = "dd.MM.yyyy")
	private Date start_date;
	
	@DateTimeFormat(pattern = "dd.MM.yyyy")
	private Date end_date;
	private String description;
	
	
	
	public ExperienceResponse() {
		super();
	}
	public ExperienceResponse(Long id, String companyname, String job_title, Date start_date, Date end_date,
			String description) {
		super();
		this.id = id;
		this.companyname = companyname;
		this.job_title = job_title;
		this.start_date = start_date;
		this.end_date = end_date;
		this.description = description;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCompanyname() {
		return companyname;
	}
	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}
	public String getJob_title() {
		return job_title;
	}
	public void setJob_title(String job_title) {
		this.job_title = job_title;
	}
	public Date getStart_date() {
		return start_date;
	}
	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}
	public Date getEnd_date() {
		return end_date;
	}
	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

}
