package com.datn.webcrawler.service;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.slf4j.Logger;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class DBServiceImpl implements DBService {

	private static final Logger logger = org.slf4j.LoggerFactory.getLogger(DBServiceImpl.class);

	private ComboPooledDataSource comboPooledDataSource;

	private PreparedStatement insertNewJobLocationStatement;
	private PreparedStatement insertNewEmployerStatement;
	private PreparedStatement insertNewJobTypeStatement;
	private PreparedStatement insertNewIndustryStatement;
	private PreparedStatement insertNewJobPostStatement;
	private PreparedStatement selectJobTypeStatement;
	private PreparedStatement selectIndustryStatement;
	private PreparedStatement selectEmployerStatement;
	private PreparedStatement insertManyToManyStatement;

	public DBServiceImpl(ComboPooledDataSource comboPooledDataSource) throws SQLException {
		this.comboPooledDataSource = comboPooledDataSource;
		insertNewJobLocationStatement = comboPooledDataSource.getConnection().prepareStatement("insert into joblocation(city_province,street_address) values " +
				"(?,?)", Statement.RETURN_GENERATED_KEYS);
		selectJobTypeStatement = comboPooledDataSource.getConnection().prepareStatement("select * from job_type where "
				+ "job_type_name LIKE ?");
		insertNewJobTypeStatement = comboPooledDataSource.getConnection().prepareStatement("insert into job_type(job_type_name) values "
				+ "(?)", Statement.RETURN_GENERATED_KEYS);
		selectIndustryStatement = comboPooledDataSource.getConnection().prepareStatement("select * from industry where "
				+ "name LIKE ?");
		insertNewIndustryStatement = comboPooledDataSource.getConnection().prepareStatement("insert into industry(name) values "
				+ "(?)", Statement.RETURN_GENERATED_KEYS);
		selectEmployerStatement = comboPooledDataSource.getConnection().prepareStatement("select count(id) as duplicate from employer where "
				+ "companyname LIKE ?");
		insertNewJobPostStatement = comboPooledDataSource.getConnection().prepareStatement("insert into job_post(created_at, updated_at, expiration_date, job_description, job_title, "
				+ "max_salary, min_salary, requiredexperienceyears, sourceUrl, posted_by, joblocation_id, job_type_id) values "
				+ "(?,?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
		insertManyToManyStatement = comboPooledDataSource.getConnection().prepareStatement("insert into jobpost_industry(jobpost_id, industry_id) values"
				+ "(?,?)");
	}

	@Override
	public void saveJobPost(String jobtitle, String jobtype, List<String> industries, Long minSalary, 
			Long maxSalary, String companyname, String sourceUrl, Date expirationDate, Long requiredexperienceyears
			, String street_address, String city_province) {
		try {
			insertNewJobLocationStatement.setString(2, street_address);
			insertNewJobLocationStatement.setString(1, city_province);
			insertNewJobLocationStatement.executeUpdate();
			selectJobTypeStatement.setString(1,'%' + jobtype + '%');
			ResultSet count = selectJobTypeStatement.executeQuery();
			if (count.next() == false)
			{
				insertNewJobTypeStatement.setString(1, jobtype);
				insertNewJobTypeStatement.executeUpdate();
			} else {
				do {
					insertNewJobPostStatement.setInt(12, count.getInt(1));
				} while (count.next());
			}
			selectEmployerStatement.setString(1, companyname);
			ResultSet employercounts = selectEmployerStatement.executeQuery();
			if (employercounts.next() == false) {
				insertNewEmployerStatement.setString(1, companyname);
				insertNewEmployerStatement.executeUpdate();
			} 
			else {
				do {
					insertNewJobPostStatement.setInt(10, employercounts.getInt(1));
				} while (count.next());
			}

			ResultSet insertedjoblocation = insertNewJobLocationStatement.getGeneratedKeys();
			insertNewJobPostStatement.setDate(1, null);
			insertNewJobPostStatement.setDate(2, null);            
			insertNewJobPostStatement.setDate(3, expirationDate);
			insertNewJobPostStatement.setString(4, null);
			insertNewJobPostStatement.setString(5, jobtitle);
			insertNewJobPostStatement.setLong(6, maxSalary);
			insertNewJobPostStatement.setLong(7, minSalary);
			insertNewJobPostStatement.setLong(8, requiredexperienceyears);
			insertNewJobPostStatement.setString(9, sourceUrl);
			while (insertedjoblocation.next()) {
				insertNewJobPostStatement.setInt(11, insertedjoblocation.getInt(1));
			}
			insertNewJobPostStatement.executeUpdate();
			ResultSet insertedJobPost = insertNewJobPostStatement.getGeneratedKeys();
			for (String industry: industries) {
				selectIndustryStatement.setString(1,'%' + industry + '%');
				ResultSet industrycount = selectIndustryStatement.executeQuery();
				if (industrycount.next() == false)
				{
					insertNewIndustryStatement.setString(1, industry);
					insertNewIndustryStatement.executeUpdate();
					ResultSet insertedIndustry = insertNewIndustryStatement.getGeneratedKeys();
					insertManyToManyStatement.setInt(1, insertedJobPost.getInt(1));
					insertManyToManyStatement.setInt(2, insertedIndustry.getInt(1));
					insertManyToManyStatement.executeUpdate();
				} 
				else {
					insertManyToManyStatement.setInt(1, insertedJobPost.getInt(1));
					insertManyToManyStatement.setInt(2, industrycount.getInt(1));
					insertManyToManyStatement.executeUpdate();
				}
			}


		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void close() {
		if (comboPooledDataSource != null) {
			comboPooledDataSource.close();
		}
	}

}