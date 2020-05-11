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
	private String phonenumber;
	private String work_title;
	private String address;
	private List<ExperienceResponse> experiences;
	private List<EducationResponse> educations;
	private String CV_Uri;
	
	
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
