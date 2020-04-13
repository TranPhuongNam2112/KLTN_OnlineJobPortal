package com.datn.onlinejobportal.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="company_location")
public class CompanyLocation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String street_address;

	private String city_province;

	@ManyToOne
	@JoinColumn(name="company_id")
	private Employer employer;
	
	public CompanyLocation() {
		
	}

	public CompanyLocation(String street_address, String city_province, Employer employer) {
		super();
		this.street_address = street_address;
		this.city_province = city_province;
		this.employer = employer;
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

	public Employer getEmployer() {
		return employer;
	}

	public void setEmployer(Employer employer) {
		this.employer = employer;
	}
	
	
}
