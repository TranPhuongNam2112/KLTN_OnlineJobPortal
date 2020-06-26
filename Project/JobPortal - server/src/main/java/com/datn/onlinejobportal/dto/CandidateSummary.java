package com.datn.onlinejobportal.dto;

import java.time.LocalDate;

public class CandidateSummary {
	
	private Long id;
	private byte[] image;
	private String name;
	private String city;
	private String work_title;
	private LocalDate last_updated;
	private String imageUrl;
	
	
	public CandidateSummary(Long id, byte[] image, String name, String city, String work_title, LocalDate last_updated, String imageUrl) {
		super();
		this.id = id;
		this.image = image;
		this.name = name;
		this.city = city;
		this.work_title = work_title;
		this.last_updated = last_updated;
		this.imageUrl = imageUrl;
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
