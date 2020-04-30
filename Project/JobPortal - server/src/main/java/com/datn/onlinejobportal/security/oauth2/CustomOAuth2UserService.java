package com.datn.onlinejobportal.security.oauth2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.datn.onlinejobportal.exception.OAuth2AuthenticationProcessingException;
import com.datn.onlinejobportal.model.AuthProvider;
import com.datn.onlinejobportal.model.Candidate;
import com.datn.onlinejobportal.model.ERole;
import com.datn.onlinejobportal.model.Role;
import com.datn.onlinejobportal.model.User;
import com.datn.onlinejobportal.repository.CandidateRepository;
import com.datn.onlinejobportal.repository.RoleRepository;
import com.datn.onlinejobportal.repository.UserRepository;
import com.datn.onlinejobportal.security.UserPrincipal;
import com.datn.onlinejobportal.security.oauth2.user.OAuth2UserInfo;
import com.datn.onlinejobportal.security.oauth2.user.OAuth2UserInfoFactory;

import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
@Transactional
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;
	@Autowired
	private CandidateRepository candidateRepository;
	@Autowired
	private RoleRepository roleRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());
        if(StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }

        User userOptional = userRepository.findByEmail(oAuth2UserInfo.getEmail());
        User user;
        if(userOptional != null) {
            user = userOptional;
            if(!user.getProvider().equals(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()))) {
                throw new OAuth2AuthenticationProcessingException("Looks like you're signed up with " +
                        user.getProvider() + " account. Please use your " + user.getProvider() +
                        " account to login.");
            }
            user = updateExistingUser(user, oAuth2UserInfo);
        } else {
            user = registerNewUser(oAuth2UserRequest, oAuth2UserInfo);
        }

        return UserPrincipal.create(user, oAuth2User.getAttributes());
    }

    private User registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
    	 User user = new User();

         user.setProvider(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()));
         user.setProviderId(oAuth2UserInfo.getId());
         user.setName(oAuth2UserInfo.getName());
         user.setEmail(oAuth2UserInfo.getEmail());
         user.setImageUrl(oAuth2UserInfo.getImageUrl());
         Set<Role> roles = new HashSet<>();
 		Role candidateRole = roleRepository.findByName(ERole.ROLE_CANDIDATE)
 				.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
 		roles.add(candidateRole);
 		user.setRoles(roles);
 		user.setCreatedAt(LocalDate.now());
 		user.setEmailVerified(true);
 		Candidate candidate = new Candidate();
 		candidate.setUser(user);
 		candidateRepository.save(candidate);
         return userRepository.save(user);
    }
    
    private User updateExistingUser(User existingUser, OAuth2UserInfo oAuth2UserInfo) {
        existingUser.setName(oAuth2UserInfo.getName());
        existingUser.setImageUrl(oAuth2UserInfo.getImageUrl());
        existingUser.setUpdatedAt(LocalDate.now());
        return userRepository.save(existingUser);
    }

}
