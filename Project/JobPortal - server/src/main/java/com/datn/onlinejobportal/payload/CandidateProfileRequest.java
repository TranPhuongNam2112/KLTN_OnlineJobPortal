package com.datn.onlinejobportal.payload;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;


public class CandidateProfileRequest {
	
	@NotBlank
	@Size(max=100)
	private String fullname;
	
	@DateTimeFormat(pattern = "dd.MM.yyyy")
	private Date DoB;
	
	private String gender;
	
	private String homeaddress;
	
	private String city_province;
	
	private String work_title;
	
	@Size(max=11)
	private String phone_number;
	
	private List<String> jobtypes;
	
	private Boolean profile_visible;
	
	private Long experiencedyears;
	
	private Long expectedsalary;
	

	
	public Long getExpectedsalary() {
		return expectedsalary;
	}

	public void setExpectedsalary(Long expectedsalary) {
		this.expectedsalary = expectedsalary;
	}

	public Boolean getProfile_visible() {
		return profile_visible;
	}

	public void setProfile_visible(Boolean profile_visible) {
		this.profile_visible = profile_visible;
	}

	public Long getExperiencedyears() {
		return experiencedyears;
	}

	public void setExperiencedyears(Long experiencedyears) {
		this.experiencedyears = experiencedyears;
	}

	public String getCity_province() {
		return city_province;
	}

	public void setCity_province(String city_province) {
		this.city_province = city_province;
	}

	public String getWork_title() {
		return work_title;
	}

	public void setWork_title(String work_title) {
		this.work_title = work_title;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public Date getDoB() {
		return DoB;
	}

	public void setDoB(Date doB) {
		DoB = doB;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getHomeaddress() {
		return homeaddress;
	}

	public void setHomeaddress(String homeaddress) {
		this.homeaddress = homeaddress;
	}

	public String getPhone_number() {
		return phone_number;
	}

	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}
	
	public List<String> getJobtypes() {
		return jobtypes;
	}

	public void setJobtypes(List<String> jobtypes) {
		this.jobtypes = jobtypes;
	}
	
	
	
}
