package com.datn.onlinejobportal.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

@Entity
@Table(name="employer_file")
public class CandidateFile {

	@EmbeddedId
	private CandidateFileId candidateFileId;

	@ManyToOne
	@MapsId("candidateId")
	private Candidate candidate;

	@ManyToOne
	@MapsId("fileId")
	private DBFile files;

	@Column
	private Date added_date;

	public CandidateFile() {
		super();
	}

	public CandidateFile(Candidate candidate, DBFile files, Date added_date) {
		super();
		this.candidate = candidate;
		this.files = files;
		this.added_date = added_date;
	}

	public Candidate getCandidate() {
		return candidate;
	}

	public void setCandidate(Candidate candidate) {
		this.candidate = candidate;
	}

	public DBFile getFiles() {
		return files;
	}

	public void setFiles(DBFile files) {
		this.files = files;
	}

	public Date getAdded_date() {
		return added_date;
	}

	public void setAdded_date(Date added_date) {
		this.added_date = added_date;
	}
	
	
}
