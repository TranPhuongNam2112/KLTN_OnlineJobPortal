package com.datn.onlinejobportal.payload;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

public class JobPostDetails {
	
	private String jobtitle;
	
	private String jobtypes;
	
	@DateTimeFormat(pattern = "dd.MM.yyyy")
	private LocalDate createdDate;
	
	private String description;
	
	private List<String> industry;
	
	@DateTimeFormat(pattern = "dd.MM.yyyy")
	private Date expirationDate;
	
	private Long maxSalary;
	
	private Long minSalary;
	
	private Long requiredexperienceyears;
	
	private String street_address;
	
	private String city_province;

	public JobPostDetails() {
		super();
	}

	public String getJobtitle() {
		return jobtitle;
	}

	public void setJobtitle(String jobtitle) {
		this.jobtitle = jobtitle;
	}

	public String getJobtypes() {
		return jobtypes;
	}

	public void setJobtypes(String jobtypes) {
		this.jobtypes = jobtypes;
	}

	public LocalDate getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDate createdDate) {
		this.createdDate = createdDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	

	public List<String> getIndustry() {
		return industry;
	}

	public void setIndustry(List<String> industry) {
		this.industry = industry;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public Long getMaxSalary() {
		return maxSalary;
	}

	public void setMaxSalary(Long maxSalary) {
		this.maxSalary = maxSalary;
	}

	public Long getMinSalary() {
		return minSalary;
	}

	public void setMinSalary(Long minSalary) {
		this.minSalary = minSalary;
	}

	public Long getRequiredexperienceyears() {
		return requiredexperienceyears;
	}

	public void setRequiredexperienceyears(Long requiredexperienceyears) {
		this.requiredexperienceyears = requiredexperienceyears;
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
