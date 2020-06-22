package com.datn.onlinejobportal.dto;

public class EmployerSummary {
	
	private Long id;
	private byte[] image;
	private String imageUrl;
	private String companyname;
	private String industry;
	
	public EmployerSummary() {
		super();
	}


	public EmployerSummary(Long id, byte[] image, String imageUrl, String companyname, String industry) {
		super();
		this.id = id;
		this.image = image;
		this.imageUrl = imageUrl;
		this.companyname = companyname;
		this.industry = industry;
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
	public String getIndustry() {
		return industry;
	}
	public void setIndustry(String industry) {
		this.industry = industry;
	}
	
	

}
