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

import com.datn.onlinejobportal.model.ERole;
import com.datn.onlinejobportal.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	@Query(value = "Select u from ConfimationToken ct JOIN ct.user u where ct.confirmationtoken = :token", nativeQuery=true)
	User findByConfirmationToken(@Param("token") String confirmationToken);

	User findByEmail(String email);

	Boolean existsByEmail(String email);

	Optional<User> findByName(String name);

	List<User> findByRoles_Name(ERole name);

	@Query(value="SELECT u from User u where u.roles.role_id = 1", nativeQuery=true)
	Page<User> findAllCandidates(Pageable pageable);

	@Query(value="SELECT u from User u where u.roles.role_id = 2", nativeQuery = true)
	Page<User> findAllEmployers(Pageable pageable);

	@Query(value="SELECT u.name FROM User u JOIN u.candidate c JOIN c.savedcandidates sc WHERE sc.candidateId in :candidateIds", nativeQuery = true)
	List<String> getNameByCandidateIn(@Param("candidateIds") List<Long> candidateIds);

	@Modifying
	@Query("update User u set u.password = :password where u.id = :id")
	void updatePassword(@Param("password") String password, @Param("id") Long id);

	@Query(value = "Select u from User u Join u.employer e where e.comapnyname= :companyname ", nativeQuery = true)
	User findUserByCompanyName(@Param("companyname") String companyname);
}