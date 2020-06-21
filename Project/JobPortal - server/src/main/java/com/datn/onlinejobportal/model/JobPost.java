package com.datn.onlinejobportal.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.datn.onlinejobportal.model.audit.DateAudit;
import com.fasterxml.jackson.annotation.JsonIgnore;


@SuppressWarnings("serial")
@Entity
@Table(name = "job_post")
public class JobPost extends DateAudit {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String job_title;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "posted_by", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private Employer employer;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "job_type_id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private JobType jobtype;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "jobpost_industry", 
	joinColumns = @JoinColumn(name = "jobpost_id"), 
	inverseJoinColumns = @JoinColumn(name = "industry_id"))
	private Set<Industry> industries = new HashSet<>();
	
	private String job_description;
	
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "joblocation_id", referencedColumnName = "id")
    private JobLocation joblocation;
	
	@OneToMany(mappedBy = "jobpost", cascade = CascadeType.MERGE)
	@JsonIgnore
    private Set<SavedJobPost> savedjobpost;
	
	@OneToMany(mappedBy = "jobpost", cascade = CascadeType.MERGE)
	@JsonIgnore
    private Set<CandidateHistory> candidatehistories;
	
	private Long requiredexperienceyears;
	
	@NotNull
    private Date expirationDate;

	private Long min_salary;
	
	private Long max_salary;
	
	private String sourceUrl;
	
	private String sourceWebsite;
	
	public JobPost() {
		super();
	}
	

	public String getSourceWebsite() {
		return sourceWebsite;
	}


	public void setSourceWebsite(String sourceWebsite) {
		this.sourceWebsite = sourceWebsite;
	}


	public Set<CandidateHistory> getCandidatehistories() {
		return candidatehistories;
	}


	public void setCandidatehistories(Set<CandidateHistory> candidatehistories) {
		this.candidatehistories = candidatehistories;
	}


	public String getSourceUrl() {
		return sourceUrl;
	}


	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
	}


	public Long getRequiredexpreienceyears() {
		return requiredexperienceyears;
	}

	public void setRequiredexpreienceyears(Long requiredexpreienceyears) {
		this.requiredexperienceyears = requiredexpreienceyears;
	}

	public String getJob_title() {
		return job_title;
	}


	public void setJob_title(String job_title) {
		this.job_title = job_title;
	}


	public Set<SavedJobPost> getSavedjobpost() {
		return savedjobpost;
	}


	public void setSavedjobpost(Set<SavedJobPost> savedjobpost) {
		this.savedjobpost = savedjobpost;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Employer getEmployer() {
		return employer;
	}

	public void setEmployer(Employer employer) {
		this.employer = employer;
	}

	public JobType getJobtype() {
		return jobtype;
	}

	public void setJobtype(JobType jobtype) {
		this.jobtype = jobtype;
	}


	public Set<Industry> getIndustries() {
		return industries;
	}


	public void setIndustries(Set<Industry> industries) {
		this.industries = industries;
	}


	public Long getRequiredexperienceyears() {
		return requiredexperienceyears;
	}

	public void setRequiredexperienceyears(Long requiredexperienceyears) {
		this.requiredexperienceyears = requiredexperienceyears;
	}


	public String getJob_description() {
		return job_description;
	}

	public void setJob_description(String job_description) {
		this.job_description = job_description;
	}

	public JobLocation getJoblocation() {
		return joblocation;
	}


	public void setJoblocation(JobLocation joblocation) {
		this.joblocation = joblocation;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}


	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}


	public Long getMin_salary() {
		return min_salary;
	}


	public void setMin_salary(Long min_salary) {
		this.min_salary = min_salary;
	}


	public Long getMax_salary() {
		return max_salary;
	}


	public void setMax_salary(Long max_salary) {
		this.max_salary = max_salary;
	}
	
	

}