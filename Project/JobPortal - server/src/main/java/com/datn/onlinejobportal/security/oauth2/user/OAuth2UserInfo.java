package com.datn.onlinejobportal.security.oauth2.user;


import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public  class OAuth2UserInfo {

	protected Map<String, Object> attributes;

	public OAuth2UserInfo(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public String id;
	public String name;

	public String email;

	public String photoUrl;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	@JsonCreator

	public OAuth2UserInfo(@JsonProperty("id") String id, @JsonProperty("name") String name,
			@JsonProperty("email") String email, @JsonProperty("photoUrl") String photoUrl) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.photoUrl = photoUrl;
	}
	public OAuth2UserInfo(){}
}
