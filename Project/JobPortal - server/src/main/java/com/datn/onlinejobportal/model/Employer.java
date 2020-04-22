package com.datn.onlinejobportal.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;


import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;



@Entity
@Table(name = "employer")
public class Employer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String companyname;
	
	private String description;
	
	private Date establishmentdate;
	
	private String websiteurl;
	
	private String main_address;
	
	private String phone_number;
	
	private String industry;
	
	public String getPhone_number() {
		return phone_number;
	}

	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}
	
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private User user;
	
	@OneToMany(mappedBy = "employer", cascade = CascadeType.ALL)
	private Set<SavedCandidate> savedCandidates = new HashSet<>();
	
	
	public Employer() {
		super();
	}

	public Employer(String companyname, String description, Date establishmentdate, String websiteurl,
			String main_address, String phone_number, String industry) {
		super();
		this.companyname = companyname;
		this.description = description;
		this.establishmentdate = establishmentdate;
		this.websiteurl = websiteurl;
		this.main_address = main_address;
		this.phone_number = phone_number;
		this.industry = industry;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCompanyname() {
		return companyname;
	}

	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getEstablishmentdate() {
		return establishmentdate;
	}

	public void setEstablishmentdate(Date establishmentdate) {
		this.establishmentdate = establishmentdate;
	}

	public String getWebsiteurl() {
		return websiteurl;
	}

	public void setWebsiteurl(String websiteurl) {
		this.websiteurl = websiteurl;
	}

	public String getMain_address() {
		return main_address;
	}

	public void setMain_address(String main_address) {
		this.main_address = main_address;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Set<SavedCandidate> getSavedCandidates() {
		return savedCandidates;
	}

	public void setSavedCandidates(Set<SavedCandidate> savedCandidates) {
		this.savedCandidates = savedCandidates;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}
}
