package com.datn.onlinejobportal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.datn.onlinejobportal.security.ActiveUserStore;

@Configuration
public class AppConfig {

	 @Bean
	    public ActiveUserStore activeUserStore() {
	        return new ActiveUserStore();
	    }

	
}
