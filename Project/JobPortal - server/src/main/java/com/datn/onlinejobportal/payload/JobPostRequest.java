package com.datn.onlinejobportal.payload;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;


public class JobPostRequest {
	@NotBlank
	private String jobtitle;
	
	@NotNull
	private String jobType;
	
	@NotNull
	private Long requiredexperience;
	
	@NotBlank
	@Size(max=300)
	private String jobdescription;
	
	@NotNull
	private List<String> industry;
	
    @NotNull(message = "Please enter the correct amount!")
	private Long minSalary;
	
    @NotNull(message = "Please enter the correct amount!")
	private Long maxSalary;
	
	@NotNull
	@DateTimeFormat(pattern = "dd.MM.yyyy")
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

	public String getJobType() {
		return jobType;
	}

	public void setJobType(String jobType) {
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
	
	

	public List<String> getIndustry() {
		return industry;
	}

	public void setIndustry(List<String> industry) {
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
