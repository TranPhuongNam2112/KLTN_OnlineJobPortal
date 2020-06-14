package com.datn.webcrawler.service;

import java.sql.Date;
import java.util.List;

public interface DBService {

	void saveJobPost(String jobtitle, String jobtype, List<String> industries, Long minSalary, 
			Long maxSalary, String companyname, String sourceUrl, Date expirationDate, Long requiredexperienceyears
			, String street_address, String city_province, String imageUrl);
	void close();
}
