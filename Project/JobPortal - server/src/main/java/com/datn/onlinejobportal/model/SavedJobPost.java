package com.datn.onlinejobportal.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import com.datn.onlinejobportal.model.audit.DateAudit;



@Entity
@Table(name="saved_job_post")
public class SavedJobPost extends DateAudit {

	/**
	 * 
	 */
	private static final long serialVersionUID = 260892530817098257L;

	@EmbeddedId
	private SavedJobPostId savedJobPostId;

	@ManyToOne
	@MapsId("candidateId")
	private Candidate candidate;

	@ManyToOne
	@MapsId("jobpostId")
	private JobPost jobpost;

	public SavedJobPost() {
		
	}

	
	public SavedJobPost(Candidate candidate, JobPost jobpost) {
		this.candidate = candidate;
		this.jobpost = jobpost;
	}
	


	public Candidate getCandidate() {
		return candidate;
	}


	public void setCandidate(Candidate candidate) {
		this.candidate = candidate;
	}


	public JobPost getJobpost() {
		return jobpost;
	}


	public void setJobpost(JobPost jobpost) {
		this.jobpost = jobpost;
	}


	
}