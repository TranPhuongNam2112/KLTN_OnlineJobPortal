package com.datn.onlinejobportal.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;

import com.datn.onlinejobportal.exception.ResourceNotFoundException;
import com.datn.onlinejobportal.model.User;
import com.datn.onlinejobportal.repository.UserRepository;

@Service
public class UserService {
	@Autowired
	UserRepository userRepository;
	
    @Autowired
    private SessionRegistry sessionRegistry;
	
	
	public User getUserByEmail(String email) throws ResourceNotFoundException {
		User user = userRepository.findByEmail(email);

		if (user != null) {
			return user;
		} else {
			throw new ResourceNotFoundException("User", "email", email);
		}
	}
	
	 public List<String> getUsersFromSessionRegistry() {
	        return sessionRegistry.getAllPrincipals()
	            .stream()
	            .filter((u) -> !sessionRegistry.getAllSessions(u, false)
	                .isEmpty())
	            .map(o -> {
	                if (o instanceof User) {
	                    return ((User) o).getEmail();
	                } else {
	                    return o.toString()
	            ;
	                }
	            }).collect(Collectors.toList());
	    }

}