package com.datn.onlinejobportal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.datn.onlinejobportal.config.AppProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class JobPortalApplication {

	public static void main(String[] args) {
		SpringApplication.run(JobPortalApplication.class, args);
	}

}
