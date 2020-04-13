package com.datn.onlinejobportal.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.datn.onlinejobportal.model.ERole;
import com.datn.onlinejobportal.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

	Optional<User> findByName(String name);
	
	List<User> findByRoles_Name(ERole name);

}