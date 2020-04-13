package com.datn.onlinejobportal.controller;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;


import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.datn.onlinejobportal.exception.BadRequestException;
import com.datn.onlinejobportal.model.AuthProvider;
import com.datn.onlinejobportal.model.Candidate;
import com.datn.onlinejobportal.model.CompanyLocation;
import com.datn.onlinejobportal.model.ERole;
import com.datn.onlinejobportal.model.Employer;
import com.datn.onlinejobportal.model.Role;
import com.datn.onlinejobportal.model.User;
import com.datn.onlinejobportal.payload.ApiResponse;
import com.datn.onlinejobportal.payload.AuthResponse;
import com.datn.onlinejobportal.payload.CandidateSignUpRequest;
import com.datn.onlinejobportal.payload.EmployerSignUpRequest;
import com.datn.onlinejobportal.payload.LoginRequest;
import com.datn.onlinejobportal.payload.SignUpRequest;
import com.datn.onlinejobportal.repository.CandidateRepository;
import com.datn.onlinejobportal.repository.CompanyLocationRepository;
import com.datn.onlinejobportal.repository.EmployerRepository;
import com.datn.onlinejobportal.repository.RoleRepository;
import com.datn.onlinejobportal.repository.UserRepository;
import com.datn.onlinejobportal.security.TokenProvider;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private TokenProvider tokenProvider;
	
	@Autowired
	private EmployerRepository employerRepository;
	
	@Autowired
	private CompanyLocationRepository companyLocationRepository;
	
	@Autowired
	private CandidateRepository candidateRepository;
	

	@PostMapping("/login")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						loginRequest.getEmail(),
						loginRequest.getPassword()
						)
				);

		SecurityContextHolder.getContext().setAuthentication(authentication);

		String token = tokenProvider.createToken(authentication);
		return ResponseEntity.ok(new AuthResponse(token));
	}
	
	@PostMapping("/signupforemployer")
	public ResponseEntity<?> registerEmployer(@Valid @RequestBody EmployerSignUpRequest employersignUpRequest) {
		if(userRepository.existsByEmail(employersignUpRequest.getEmail())) {
			throw new BadRequestException("Email address already in use.");
		}

		// Creating user's account
		User user = new User();
		user.setName(employersignUpRequest.getName());
		user.setEmail(employersignUpRequest.getEmail());
		user.setPassword(employersignUpRequest.getPassword());
		user.setProvider(AuthProvider.local);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		Set<Role> roles = new HashSet<>();
		Role adminRole = roleRepository.findByName(ERole.ROLE_EMPLOYER)
				.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
		roles.add(adminRole);
		user.setRoles(roles);
		User result = userRepository.save(user);
		
		Employer company = new Employer();
		company.setCompanyname(employersignUpRequest.getCompanyname());
		company.setPhone_number(employersignUpRequest.getPhonenumber());
		company.setIndustry(employersignUpRequest.getIndustry());
		company.setUser(user);
		employerRepository.save(company);
		
		CompanyLocation companyLocation = new CompanyLocation();
		companyLocation.setCity_province(employersignUpRequest.getCity_province());
		companyLocation.setEmployer(company);
		companyLocationRepository.save(companyLocation);
		

		URI location = ServletUriComponentsBuilder
				.fromCurrentContextPath().path("/user/me")
				.buildAndExpand(result.getId()).toUri();

		return ResponseEntity.created(location)
				.body(new ApiResponse(true, "User registered successfully!"));
	}
	
	@PostMapping("/signupforcandidate")
	public ResponseEntity<?> registerCandidate(@Valid @RequestBody CandidateSignUpRequest candidatesignUpRequest) {
		if(userRepository.existsByEmail(candidatesignUpRequest.getEmail())) {
			throw new BadRequestException("Email address already in use.");
		}

		// Creating user's account
		User user = new User();
		user.setName(candidatesignUpRequest.getName());
		user.setEmail(candidatesignUpRequest.getEmail());
		user.setPassword(candidatesignUpRequest.getPassword());
		user.setProvider(AuthProvider.local);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		Set<Role> roles = new HashSet<>();
		Role candidateRole = roleRepository.findByName(ERole.ROLE_CANDIDATE)
				.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
		roles.add(candidateRole);
		user.setRoles(roles);
		User result = userRepository.save(user);
		
		Candidate candidate = new Candidate();
		candidate.setUser(user);
		candidateRepository.save(candidate);

		URI location = ServletUriComponentsBuilder
				.fromCurrentContextPath().path("/candidate/me")
				.buildAndExpand(result.getId()).toUri();

		return ResponseEntity.created(location)
				.body(new ApiResponse(true, "User registered successfully!"));
	}


	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
		if(userRepository.existsByEmail(signUpRequest.getEmail())) {
			throw new BadRequestException("Email address already in use.");
		}

		// Creating user's account
		User user = new User();
		user.setName(signUpRequest.getName());
		user.setEmail(signUpRequest.getEmail());
		user.setPassword(signUpRequest.getPassword());
		user.setProvider(AuthProvider.local);

		user.setPassword(passwordEncoder.encode(user.getPassword()));

		Set<String> strRoles = signUpRequest.getRoles();
		Set<Role> roles = new HashSet<>();

		if (strRoles == null) {
			Role candidateRole = roleRepository.findByName(ERole.ROLE_CANDIDATE)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(candidateRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
				case "admin":
					Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(adminRole);

					break;
				case "employer":
					Role employerRole = roleRepository.findByName(ERole.ROLE_EMPLOYER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(employerRole);
					//Employer employer = new Employer()


					break;
				default:
					Role userRole = roleRepository.findByName(ERole.ROLE_CANDIDATE)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(userRole);
				}
			});
		}

		user.setRoles(roles);
		User result = userRepository.save(user);

		

		URI location = ServletUriComponentsBuilder
				.fromCurrentContextPath().path("/user/me")
				.buildAndExpand(result.getId()).toUri();

		return ResponseEntity.created(location)
				.body(new ApiResponse(true, "User registered successfully!"));
	}

}
