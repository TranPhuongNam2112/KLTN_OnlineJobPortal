package com.datn.onlinejobportal.payload;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;


public class CandidateProfile {
	
	private byte[] image;
	private String name;
	private String gender;
	
	@DateTimeFormat(pattern = "dd.MM.yyyy")
	private Date DoB;
	
	private List<String> jobtypes;
	private List<String> industries;
	private String phonenumber;
	private String work_title;
	private String address;
	private List<ExperienceResponse> experiences;
	private List<EducationResponse> educations;
	private String CV_Uri;
	private String CV_name;
	private Long expectedsalary;
	private String imageUrl;
	private String city_province;
	
	
	public String getCity_province() {
		return city_province;
	}
	public void setCity_province(String city_province) {
		this.city_province = city_province;
	}
	public List<String> getIndustries() {
		return industries;
	}
	public void setIndustries(List<String> industries) {
		this.industries = industries;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getCV_name() {
		return CV_name;
	}
	public void setCV_name(String cV_name) {
		CV_name = cV_name;
	}
	public Long getExpectedsalary() {
		return expectedsalary;
	}
	public void setExpectedsalary(Long expectedsalary) {
		this.expectedsalary = expectedsalary;
	}
	public String getCV_Uri() {
		return CV_Uri;
	}
	public void setCV_Uri(String cV_Uri) {
		CV_Uri = cV_Uri;
	}
	public CandidateProfile() {
		super();
	}
			
	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public Date getDoB() {
		return DoB;
	}
	public void setDoB(Date doB) {
		DoB = doB;
	}
	public List<String> getJobtypes() {
		return jobtypes;
	}
	public void setJobtypes(List<String> jobtypes) {
		this.jobtypes = jobtypes;
	}
	public String getPhonenumber() {
		return phonenumber;
	}
	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}
	public String getWork_title() {
		return work_title;
	}
	public void setWork_title(String work_title) {
		this.work_title = work_title;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public List<ExperienceResponse> getExperiences() {
		return experiences;
	}
	public void setExperiences(List<ExperienceResponse> experiences) {
		this.experiences = experiences;
	}
	public List<EducationResponse> getEducations() {
		return educations;
	}
	public void setEducations(List<EducationResponse> educations) {
		this.educations = educations;
	}
	
	
}
