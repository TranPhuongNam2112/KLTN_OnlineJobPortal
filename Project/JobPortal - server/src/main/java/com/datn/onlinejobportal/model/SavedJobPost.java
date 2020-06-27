package com.datn.onlinejobportal.model;


import java.util.Objects;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import com.datn.onlinejobportal.model.audit.DateAudit;



@Entity
@Table(name="saved_job_post")
public class SavedJobPost {

	/**
	 * 
	 */
	private static final long serialVersionUID = 260892530817098257L;

	@EmbeddedId
	private SavedJobPostId savedJobPostId;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("candidateId")
	private Candidate candidate;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("jobpostId")
	private JobPost jobpost;

	public SavedJobPost() {
		
	}

	public SavedJobPost(Candidate candidate, JobPost jobpost) {
		this.candidate = candidate;
		this.jobpost = jobpost;
		this.savedJobPostId = new SavedJobPostId(candidate.getId(), jobpost.getId());
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
	
	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
 
        if (o == null || getClass() != o.getClass())
            return false;
 
        SavedJobPost that = (SavedJobPost) o;
        return Objects.equals(candidate, that.candidate) &&
               Objects.equals(jobpost, that.jobpost);
    }
 
    @Override
    public int hashCode() {
        return Objects.hash(candidate, jobpost);
    }


	
}