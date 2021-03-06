package com.datn.onlinejobportal.dto;

import java.util.Date;


public class JobPostSummary {
	
	private Long id;
	private byte[] image;
	private String imageUrl;
	private String companyname;
	private String jobtitle;
	private Long experienceyears;
	private String city;
	private String jobtype;
	private Date expirationDate;
	private Long minSalary;
	private Long maxSalary;
	private String sourceUrl;
	
	
	public JobPostSummary(Long id, byte[] image, String imageUrl, String companyname, String jobtitle, Long experienceyears, String city,
			String jobtype, Date expirationDate, Long minSalary, Long maxSalary, String sourceUrl) {
		super();
		this.id = id;
		this.image = image;
		this.imageUrl = imageUrl;
		this.companyname = companyname;
		this.jobtitle = jobtitle;
		this.experienceyears = experienceyears;
		this.city = city;
		this.jobtype = jobtype;
		this.expirationDate = expirationDate;
		this.minSalary = minSalary;
		this.maxSalary = maxSalary;
		this.sourceUrl = sourceUrl;
	}
	
	public String getSourceUrl() {
		return sourceUrl;
	}

	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getExperienceyears() {
		return experienceyears;
	}


	public void setExperienceyears(Long experienceyears) {
		this.experienceyears = experienceyears;
	}


	public byte[] getImage() {
		return image;
	}
	public void setImage(byte[] image) {
		this.image = image;
	}
	public String getCompanyname() {
		return companyname;
	}
	public void setCompanyname(String companyname) {
		this.companyname = companyname;
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
