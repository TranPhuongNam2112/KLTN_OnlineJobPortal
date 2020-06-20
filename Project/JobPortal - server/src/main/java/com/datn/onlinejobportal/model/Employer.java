package com.datn.onlinejobportal.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
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
	
	private String imageUrl;
	
	
	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getPhone_number() {
		return phone_number;
	}

	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}
	
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private User user;
	
	@OneToMany(mappedBy = "employer", cascade = CascadeType.MERGE)
	private Set<SavedCandidate> savedCandidates = new HashSet<>();
	
	@OneToMany(mappedBy = "employer", cascade = CascadeType.MERGE)
	private Set<EmployerHistory> employerhistories = new HashSet<>();
	
	
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

	public Set<EmployerHistory> getEmployerhistories() {
		return employerhistories;
	}

	public void setEmployerhistories(Set<EmployerHistory> employerhistories) {
		this.employerhistories = employerhistories;
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
	
	public void addCandidate(Candidate candidate) {
        SavedCandidate savedCandidate = new SavedCandidate(this, candidate);
        savedCandidates.add(savedCandidate);
        candidate.getSavedCandidates().add(savedCandidate);
    }
 
    public void removeCandidate(Candidate candidate) {
        for (Iterator<SavedCandidate> iterator = savedCandidates.iterator();
             iterator.hasNext(); ) {
            SavedCandidate savedCandidate = iterator.next();
 
            if (savedCandidate.getEmployer().equals(this) &&
                    savedCandidate.getCandidate().equals(candidate)) {
                iterator.remove();
                savedCandidate.getCandidate().getSavedCandidates().remove(savedCandidate);
                savedCandidate.setEmployer(null);
                savedCandidate.setCandidate(null);
            }
        }
    }
}
