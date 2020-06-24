package com.datn.onlinejobportal.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "industry")
public class Industry {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long id;
	
	public String industryname;
	
	@ManyToMany(mappedBy="industries")
	@JsonIgnore
    private Set<JobPost> jobpost;

	
	@ManyToMany(mappedBy="industries")
	@JsonIgnore
    private Set<Candidate> industry;

	public Industry() {
		super();
	}

	public Industry(String industryname) {
		super();
		this.industryname = industryname;
	}

	

	public Set<JobPost> getJobpost() {
		return jobpost;
	}

	public void setJobpost(Set<JobPost> jobpost) {
		this.jobpost = jobpost;
	}

	public Set<Candidate> getIndustry() {
		return industry;
	}

	public void setIndustry(Set<Candidate> industry) {
		this.industry = industry;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIndustryname() {
		return industryname;
	}

	public void setIndustryname(String industryname) {
		this.industryname = industryname;
	}

}
