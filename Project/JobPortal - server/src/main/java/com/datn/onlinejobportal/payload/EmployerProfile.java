package com.datn.onlinejobportal.payload;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class EmployerProfile {
	
	private Long id;
	
	private byte[] image;
	
	private String companyname;
	private String image_url;
	
	public String getImage_url() {
		return image_url;
	}

	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}

	@DateTimeFormat(pattern = "dd.MM.yyyy")
	private Date establishmentdate;
	
	private String industry;
	
	private String main_address;
	
	private String phone_number;
	
	private String websiteUrl;
	
	private String description;

	public EmployerProfile() {
		super();
	}

	public EmployerProfile(Long id, byte[] image, String companyname, Date establishmentdate, String industry, String main_address,
			String phone_number, String websiteUrl, String description, String image_url) {
		super();
		this.id = id;
		this.image = image;
		this.companyname = companyname;
		this.establishmentdate = establishmentdate;
		this.industry = industry;
		this.main_address = main_address;
		this.phone_number = phone_number;
		this.websiteUrl = websiteUrl;
		this.description = description;
		this.image_url = image_url;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
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

	public Date getEstablishmentdate() {
		return establishmentdate;
	}

	public void setEstablishmentdate(Date establishmentdate) {
		this.establishmentdate = establishmentdate;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public String getMain_address() {
		return main_address;
	}

	public void setMain_address(String main_address) {
		this.main_address = main_address;
	}

	public String getPhone_number() {
		return phone_number;
	}

	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}

	public String getWebsiteUrl() {
		return websiteUrl;
	}

	public void setWebsiteUrl(String websiteUrl) {
		this.websiteUrl = websiteUrl;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	

}
