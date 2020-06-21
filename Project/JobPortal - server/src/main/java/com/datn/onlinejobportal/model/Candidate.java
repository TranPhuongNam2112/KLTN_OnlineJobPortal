package com.datn.onlinejobportal.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.transaction.Transactional;

import com.datn.onlinejobportal.model.audit.DateAudit;


@SuppressWarnings("serial")
@Entity
@Table(	name = "candidate", 
uniqueConstraints = { 
		@UniqueConstraint(columnNames = "phone_number") 
})
public class Candidate extends DateAudit {
	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String phone_number;

	private String homeaddress;

	private String gender;

	private Date DoB;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "account_id", referencedColumnName = "id")
	private User user;

	@OneToMany(mappedBy = "candidate", cascade = CascadeType.MERGE)
	private Set<SavedJobPost> savedJobPosts = new HashSet<>();
	
	@OneToMany(mappedBy = "candidate", cascade = CascadeType.MERGE)
	private Set<CandidateHistory> candidatehistories = new HashSet<>();

	@OneToMany(mappedBy = "candidate", cascade = CascadeType.MERGE)
	private Set<SavedCandidate> savedCandidates = new HashSet<>();
	
	@OneToMany(mappedBy = "candidate", cascade = CascadeType.MERGE)
	private Set<EmployerHistory> employerhistories = new HashSet<>();

	@OneToMany(
			mappedBy = "candidate",
			cascade = CascadeType.ALL
			)

	private Set<Education> educations = new HashSet<>();

	@OneToMany(
			mappedBy = "candidate",
			cascade = CascadeType.ALL
			)
	private Set<Experience> experiences = new HashSet<>();

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "CV_id", referencedColumnName = "id")
	private DBFile files;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "candidate_job_type", 
	joinColumns = @JoinColumn(name = "candidate_id"), 
	inverseJoinColumns = @JoinColumn(name = "jobtype_id"))
	private Set<JobType> jobtypes = new HashSet<>();
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "candidate_industry", 
	joinColumns = @JoinColumn(name = "candidate_id"), 
	inverseJoinColumns = @JoinColumn(name = "industry_id"))
	private Set<Industry> industries = new HashSet<>();

	private String work_title;
	
	private String city_province;
	
	private Long expectedsalary;
	
	private Long yearsofexperience;
	
	private Boolean profile_visible;
	
	public Set<Industry> getIndustries() {
		return industries;
	}

	public void setIndustries(Set<Industry> industries) {
		this.industries = industries;
	}

	public Set<EmployerHistory> getEmployerhistories() {
		return employerhistories;
	}

	public void setEmployerhistories(Set<EmployerHistory> employerhistories) {
		this.employerhistories = employerhistories;
	}

	public Long getYearsofexperience() {
		return yearsofexperience;
	}

	public Set<CandidateHistory> getCandidatehistories() {
		return candidatehistories;
	}

	public void setCandidatehistories(Set<CandidateHistory> candidatehistories) {
		this.candidatehistories = candidatehistories;
	}

	public void setYearsofexperience(Long yearsofexperience) {
		this.yearsofexperience = yearsofexperience;
	}

	

	public Boolean getProfile_visible() {
		return profile_visible;
	}

	public void setProfile_visible(Boolean profile_visible) {
		this.profile_visible = profile_visible;
	}

	public Long getExpectedsalary() {
		return expectedsalary;
	}

	public void setExpectedsalary(Long expectedsalary) {
		this.expectedsalary = expectedsalary;
	}

	public String getCity_province() {
		return city_province;
	}

	public void setCity_province(String city_province) {
		this.city_province = city_province;
	}

	public Set<JobType> getJobtypes() {
		return jobtypes;
	}

	public void setJobtypes(Set<JobType> jobtypes) {
		this.jobtypes = jobtypes;
	}

	public String getWork_title() {
		return work_title;
	}

	public void setWork_title(String work_title) {
		this.work_title = work_title;
	}

	public Candidate() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPhone_number() {
		return phone_number;
	}

	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}

	public String getHomeaddress() {
		return homeaddress;
	}

	public void setHomeaddress(String homeaddress) {
		this.homeaddress = homeaddress;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Date getDoB() {
		return DoB;
	}

	public void setDoB(Date doB) {
		DoB = doB;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Set<SavedJobPost> getSavedJobPosts() {
		return savedJobPosts;
	}

	public void setSavedJobPosts(Set<SavedJobPost> savedJobPosts) {
		this.savedJobPosts = savedJobPosts;
	}


	public Set<SavedCandidate> getSavedCandidates() {
		return savedCandidates;
	}

	public void setSavedCandidates(Set<SavedCandidate> savedCandidates) {
		this.savedCandidates = savedCandidates;
	}

	public void addEducation(Education education) {
		educations.add(education);
		education.setCandidate(this);
	}

	public void removeEducation(Education education) {
		educations.remove(education);
		education.setCandidate(null);
	}

	public void addExperience(Experience experience) {
		experiences.add(experience);
		experience.setCandidate(this);
	}

	public void removeExperience(Experience experience) {
		experiences.remove(experience);
		experience.setCandidate(null);
	}

	public Set<Education> getEducations() {
		return educations;
	}

	public void setEducations(Set<Education> educations) {
		this.educations = educations;
	}

	public Set<Experience> getExperiences() {
		return experiences;
	}

	public void setExperiences(Set<Experience> experiences) {
		this.experiences = experiences;
	}

	public DBFile getFiles() {
		return files;
	}

	public void setFiles(DBFile files) {
		this.files = files;
	}
	
	@Transactional
	public void addJobPost(JobPost jobpost) {
        SavedJobPost savedjobpost = new SavedJobPost(this, jobpost);
        savedJobPosts.add(savedjobpost);
        jobpost.getSavedjobpost().add(savedjobpost);
    }
 
	@Transactional
    public void removeJobPost(JobPost jobpost) {
        for (Iterator<SavedJobPost> iterator = savedJobPosts.iterator();
             iterator.hasNext(); ) {
            SavedJobPost savedjobpost = iterator.next();
 
            if (savedjobpost.getCandidate().equals(this) &&
                    savedjobpost.getJobpost().equals(jobpost)) {
                iterator.remove();
                savedjobpost.getJobpost().getSavedjobpost().remove(savedjobpost);
                savedjobpost.setCandidate(null);
                savedjobpost.setJobpost(null);
            }
        }
    }


}