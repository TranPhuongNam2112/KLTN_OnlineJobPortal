package com.datn.onlinejobportal.dto;

import java.time.LocalDate;

public class UserProfile {
	
	private Long id;
	private String name;
	private byte[] image;
	private LocalDate joinedAt;
	
	

	public UserProfile(Long id, String name, byte[] image, LocalDate joinedAt) {
		super();
		this.id = id;
		this.name = name;
		this.image = image;
		this.joinedAt = joinedAt;
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
	public LocalDate getJoinedAt() {
		return joinedAt;
	}
	public void setJoinedAt(LocalDate joinedAt) {
		this.joinedAt = joinedAt;
	}
	public byte[] getImage() {
		return image;
	}
	public void setImage(byte[] image) {
		this.image = image;
	}
	
}
