package com.datn.onlinejobportal.dto;

import java.time.LocalDate;
import java.util.Date;

public class CandidateSummary {
	
	private byte[] image;
	private String name;
	private String city;
	private String work_title;
	private LocalDate last_updated;
	
	
	public CandidateSummary(byte[] image, String name, String city, String work_title, LocalDate last_updated) {
		super();
		this.image = image;
		this.name = name;
		this.city = city;
		this.work_title = work_title;
		this.last_updated = last_updated;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getWork_title() {
		return work_title;
	}
	public void setWork_title(String work_title) {
		this.work_title = work_title;
	}
	public LocalDate getLast_updated() {
		return last_updated;
	}
	public void setLast_updated(LocalDate last_updated) {
		this.last_updated = last_updated;
	}
	public byte[] getImage() {
		return image;
	}
	public void setImage(byte[] image) {
		this.image = image;
	}

}
