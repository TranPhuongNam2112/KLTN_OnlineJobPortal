package com.datn.onlinejobportal.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.datn.onlinejobportal.dto.UserProfile;
import com.datn.onlinejobportal.model.ERole;
import com.datn.onlinejobportal.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	@Query("Select u from ConfirmationToken ct LEFT JOIN ct.user u"
			+ " where ct.confirmationToken = :token")
	User findByConfirmationToken(@Param("token") String confirmationToken);

	User findByEmail(String email);

	Boolean existsByEmail(String email);

	Optional<User> findByName(String name);

	List<User> findByRoles_Name(ERole name);

	@Query("SELECT u from User u Where u.id IN (SELECT c.user FROM Candidate c)")
	Page<User> findAllCandidates(Pageable pageable);

	@Query("SELECT u from User u Where u.id IN (SELECT e.user FROM Employer e)")
	Page<User> findAllEmployers(Pageable pageable);


	@Transactional
	@Modifying
	@Query("update User u set u.password = :password where u.id = :id")
	void updatePassword(@Param("password") String password, @Param("id") Long id);

	@Query("Select u from User u LEFT JOIN u.employer e Where e.companyname = ?1")
	User findUserByCompanyName(String companyname);
	
	@Query("Select new com.datn.onlinejobportal.dto.UserProfile(u.id, u.name, f.data, u.createdAt) From User u LEFT JOIN u.files f")
	Page<UserProfile> getAllUsers(Pageable pageable);

	@Query("Select u.name From User u LEFT JOIN u.candidate c Where c.id = ?1")
	String getNameByCandidateId(Long candidateId);
	
	@Query("Select f.data From User u LEFT JOIN u.files f Where u.id = ?1")
	byte[] getImage(Long userId);
	
	@Query("Select f.data From User u LEFT JOIN u.files f LEFT JOIN u.employer e Where e.id = :employerId")
	byte[] getEmployerImage(@Param("employerId") Long employerId);
	
	@Query("Select f.data From User u LEFT JOIN u.files f LEFT JOIN u.candidate c Where c.id = :candidateId")
	byte[] getCandidateImage(@Param("candidateId") Long candidateId);
	
	@Query("Select u.imageUrl From User u LEFT JOIN u.candidate c Where c.id = :candidateId")
	String getCandidateImageUrl(@Param("candidateId") Long candidateId);
	
	
}