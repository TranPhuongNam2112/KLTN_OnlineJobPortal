package com.datn.onlinejobportal.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.datn.onlinejobportal.model.Role;
import com.datn.onlinejobportal.model.ERole;

public interface RoleRepository extends JpaRepository<Role, Long> {
	Optional<Role> findByName(ERole name);
}
