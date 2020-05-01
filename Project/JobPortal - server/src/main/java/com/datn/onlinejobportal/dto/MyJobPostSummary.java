package com.datn.onlinejobportal.dto;

import java.util.Date;

public class MyJobPostSummary {

	private String jobtitle;
	private String city;
	private String jobtype;
	private Date expirationDate;
	private Long minSalary;
	private Long maxSalary;
	
	
	
	public MyJobPostSummary(String jobtitle, String city, String jobtype, Date expirationDate, Long minSalary,
			Long maxSalary) {
		super();
		this.jobtitle = jobtitle;
		this.city = city;
		this.jobtype = jobtype;
		this.expirationDate = expirationDate;
		this.minSalary = minSalary;
		this.maxSalary = maxSalary;
	}
	public String getJobtitle() {
		return jobtitle;
	}
	public void setJobtitle(String jobtitle) {
		this.jobtitle = jobtitle;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getJobtype() {
		return jobtype;
	}
	public void setJobtype(String jobtype) {
		this.jobtype = jobtype;
	}
	public Date getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}
	public Long getMinSalary() {
		return minSalary;
	}
	public void setMinSalary(Long minSalary) {
		this.minSalary = minSalary;
	}
	public Long getMaxSalary() {
		return maxSalary;
	}
	public void setMaxSalary(Long maxSalary) {
		this.maxSalary = maxSalary;
	}
}
