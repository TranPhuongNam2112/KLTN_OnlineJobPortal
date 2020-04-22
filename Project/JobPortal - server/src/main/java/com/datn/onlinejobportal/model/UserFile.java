package com.datn.onlinejobportal.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

@Entity
@Table(name="user_file")
public class UserFile {
	
	@EmbeddedId
	private UserFileId userfileId;

	@ManyToOne
	@MapsId("userId")
	private User user;

	@ManyToOne
	@MapsId("fileId")
	private DBFile dbFile;

	public UserFile(User user, DBFile dbFile) {
		super();
		this.user = user;
		this.dbFile = dbFile;
	}

	public UserFileId getUserfileId() {
		return userfileId;
	}

	public void setUserfileId(UserFileId userfileId) {
		this.userfileId = userfileId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public DBFile getDbFile() {
		return dbFile;
	}

	public void setDbFile(DBFile dbFile) {
		this.dbFile = dbFile;
	}
	
	
}
