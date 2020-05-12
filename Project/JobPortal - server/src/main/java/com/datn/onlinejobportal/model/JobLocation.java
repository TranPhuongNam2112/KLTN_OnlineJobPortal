package com.datn.onlinejobportal.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "joblocation")
public class JobLocation {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String street_address;
	
	private String city_province;
	
	@OneToOne(mappedBy = "joblocation")
	@JsonIgnore
    private JobPost jobpost;

	public JobLocation(String street_address, String city_province) {
		super();
		this.street_address = street_address;
		this.city_province = city_province;
	}

	public JobLocation() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStreet_address() {
		return street_address;
	}

	public void setStreet_address(String street_address) {
		this.street_address = street_address;
	}

	public String getCity_province() {
		return city_province;
	}

	public void setCity_province(String city_province) {
		this.city_province = city_province;
	}

	public JobPost getJobpost() {
		return jobpost;
	}

	public void setJobpost(JobPost jobpost) {
		this.jobpost = jobpost;
	}
	
	
	
}
