package com.datn.onlinejobportal.payload;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.datn.onlinejobportal.constraint.ValidPassword;

public class EmployerSignUpRequest {
	@NotBlank
	private String name;
	
	@NotBlank
	private String phonenumber;

	@NotBlank
	@Email
	private String email;

	@NotBlank
	@ValidPassword
	private String password;
	
	@NotBlank
	private String companyname;
	
	@NotBlank
	private String industry;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhonenumber() {
		return phonenumber;
	}

	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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
