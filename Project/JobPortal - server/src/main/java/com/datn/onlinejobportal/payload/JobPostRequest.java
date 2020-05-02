package com.datn.onlinejobportal.payload;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.datn.onlinejobportal.model.JobType;

public class JobPostRequest {
	@NotBlank
	private String jobtitle;
	
	@NotNull
	private JobType jobType;
	
	@NotNull
	private Long requiredexperience;
	
	@NotBlank
	@Size(max=300)
	private String jobdescription;
	
	@NotBlank
	private String industry;
	
	@NotBlank
	private Long minSalary;
	
	@NotBlank
	private Long maxSalary;
	
	@NotNull
	private Date expiredDate;
	
	@NotBlank
	private String street_address;
	
	@NotBlank
	private String city_province;
	
	

	public Long getRequiredexperience() {
		return requiredexperience;
	}

	public void setRequiredexperience(Long requiredexperience) {
		this.requiredexperience = requiredexperience;
	}

	public String getJobtitle() {
		return jobtitle;
	}

	public void setJobtitle(String jobtitle) {
		this.jobtitle = jobtitle;
	}

	public JobType getJobType() {
		return jobType;
	}

	public void setJobType(JobType jobType) {
		this.jobType = jobType;
	}

	public String getJobdescription() {
		return jobdescription;
	}

	public void setJobdescription(String jobdescription) {
		this.jobdescription = jobdescription;
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

	public Date getExpiredDate() {
		return expiredDate;
	}

	public void setExpiredDate(Date expiredDate) {
		this.expiredDate = expiredDate;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public String getStreet_address() {
		return street_address;
	}

	public void setStreet_address(String street_address) {
		this.street_address = street_address;
	}

	public String getCity_province() {
		return city_province;
	}

	public void setCity_province(String city_province) {
		this.city_province = city_province;
	}

}