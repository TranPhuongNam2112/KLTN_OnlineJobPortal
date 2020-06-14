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


	@SuppressWarnings("unused")
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
	private PreparedStatement selectDuplicateJobPost;

	public DBServiceImpl(ComboPooledDataSource comboPooledDataSource) throws SQLException {
		this.comboPooledDataSource = comboPooledDataSource;
		insertNewJobLocationStatement = comboPooledDataSource.getConnection().prepareStatement("insert into joblocation(city_province,street_address) values " +
				"(?,?)", Statement.RETURN_GENERATED_KEYS);

		selectDuplicateJobPost = comboPooledDataSource.getConnection().prepareStatement("select * from job_post where "
				+ "source_Url LIKE ?");
		selectJobTypeStatement = comboPooledDataSource.getConnection().prepareStatement("select id from job_type where "
				+ "job_type_name LIKE ?");
		insertNewJobTypeStatement = comboPooledDataSource.getConnection().prepareStatement("insert into job_type(job_type_name) values "
				+ "(?)", Statement.RETURN_GENERATED_KEYS);
		selectEmployerStatement = comboPooledDataSource.getConnection().prepareStatement("select * from employer where "
				+ "companyname LIKE ?");
		insertNewEmployerStatement = comboPooledDataSource.getConnection().prepareStatement("insert into "
				+ "employer(companyname, description, establishmentdate, industry, main_address, phone_number, websiteurl, account_id, image_url) "
				+ "values (?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
		insertNewJobPostStatement = comboPooledDataSource.getConnection().prepareStatement("insert into job_post(created_at, updated_at, expiration_date, job_description, job_title, "
				+ "max_salary, min_salary, requiredexperienceyears, source_Url, posted_by, joblocation_id, job_type_id) values "
				+ "(?,?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
		insertNewIndustryStatement = comboPooledDataSource.getConnection().prepareStatement("insert into industry(industryname) values "
				+ "(?)", Statement.RETURN_GENERATED_KEYS);
		selectIndustryStatement = comboPooledDataSource.getConnection().prepareStatement("select * from industry where "
				+ "industryname LIKE ?");
		insertManyToManyStatement = comboPooledDataSource.getConnection().prepareStatement("insert into jobpost_industry(jobpost_id, industry_id) values"
				+ "(?,?)");
	}

	@Override
	public void saveJobPost(String jobtitle, String jobtype, List<String> industries, Long minSalary, 
			Long maxSalary, String companyname, String sourceUrl, Date expirationDate, Long requiredexperienceyears
			, String street_address, String city_province, String imageUrl) {

		try {
			selectDuplicateJobPost.setString(1, sourceUrl);
			selectDuplicateJobPost.executeQuery();
			ResultSet duplicatejobposts = selectDuplicateJobPost.executeQuery();
			if (duplicatejobposts.next() == false) {

				insertNewJobLocationStatement.setString(2, street_address);
				insertNewJobLocationStatement.setString(1, city_province);
				insertNewJobLocationStatement.executeUpdate();
				insertNewJobPostStatement.setNull(1, java.sql.Types.DATE);
				insertNewJobPostStatement.setNull(2, java.sql.Types.DATE);            
				insertNewJobPostStatement.setDate(3, expirationDate);
				insertNewJobPostStatement.setNull(4, java.sql.Types.VARCHAR);
				insertNewJobPostStatement.setString(5, jobtitle);
				insertNewJobPostStatement.setLong(6, maxSalary);
				insertNewJobPostStatement.setLong(7, minSalary);
				insertNewJobPostStatement.setLong(8, requiredexperienceyears);
				insertNewJobPostStatement.setString(9, sourceUrl);
				ResultSet insertedjoblocation = insertNewJobLocationStatement.getGeneratedKeys();
				while (insertedjoblocation.next())
				{
					insertNewJobPostStatement.setInt(11, insertedjoblocation.getInt(1));
				}

				selectEmployerStatement.setString(1,'%'+ companyname + '%');
				ResultSet employercounts = selectEmployerStatement.executeQuery();
				if (employercounts.next() == false) {
					insertNewEmployerStatement.setString(1, companyname);
					insertNewEmployerStatement.setNull(2, java.sql.Types.VARCHAR);
					insertNewEmployerStatement.setNull(3, java.sql.Types.DATE);
					insertNewEmployerStatement.setNull(4, java.sql.Types.VARCHAR);
					insertNewEmployerStatement.setNull(5, java.sql.Types.VARCHAR);
					insertNewEmployerStatement.setNull(6, java.sql.Types.VARCHAR);
					insertNewEmployerStatement.setNull(7, java.sql.Types.VARCHAR);
					insertNewEmployerStatement.setNull(8, java.sql.Types.BIGINT);
					insertNewEmployerStatement.setString(9, imageUrl);
					insertNewEmployerStatement.executeUpdate();
					ResultSet insertedemployer = insertNewEmployerStatement.getGeneratedKeys();
					while (insertedemployer.next())
						insertNewJobPostStatement.setLong(10, Long.parseLong(insertedemployer.getString(1)));
				} 
				else {
					ResultSet employercount = selectEmployerStatement.executeQuery();
					while (employercount.next()) {
						insertNewJobPostStatement.setLong(10, employercount.getLong("id"));
					}
				}

				selectJobTypeStatement.setString(1,'%' + jobtype + '%');
				ResultSet count = selectJobTypeStatement.executeQuery();
				if (count.next() == false)
				{
					insertNewJobTypeStatement.setString(1, jobtype);
					insertNewJobTypeStatement.executeUpdate();
					ResultSet insertedjobtype = insertNewJobTypeStatement.getGeneratedKeys();
					while (insertedjobtype.next())
					{
						insertNewJobPostStatement.setLong(12, Long.parseLong(insertedjobtype.getString(1)));

					}
				} else {
					ResultSet jobtypecount = selectJobTypeStatement.executeQuery();
					while (jobtypecount.next()) {
						insertNewJobPostStatement.setLong(12, jobtypecount.getLong("id"));
					}
				}

				insertNewJobPostStatement.executeUpdate();

				for (String industry: industries) {
					selectIndustryStatement.setString(1, industry);
					ResultSet industrycount = selectIndustryStatement.executeQuery();
					if (industrycount.next() == false) {
						insertNewIndustryStatement.setString(1, industry);
						insertNewIndustryStatement.executeUpdate();
						ResultSet insertedindustry = insertNewIndustryStatement.getGeneratedKeys();
						while (insertedindustry.next())
						{
							ResultSet insertedjobpost = insertNewJobPostStatement.getGeneratedKeys();
							while (insertedjobpost.next())
							{
								insertManyToManyStatement.setLong(1, Long.parseLong(insertedjobpost.getString(1)));
								insertManyToManyStatement.setLong(2, Long.parseLong(insertedindustry.getString(1)));
								insertManyToManyStatement.executeUpdate();
							}
						}
					}
					else {
						ResultSet duplicate =  selectIndustryStatement.executeQuery();
						ResultSet insertedjobpost = insertNewJobPostStatement.getGeneratedKeys();
						while (duplicate.next())
						{
							insertManyToManyStatement.setLong(2, duplicate.getLong("id"));
							while (insertedjobpost.next()) {
								insertManyToManyStatement.setLong(1, Long.parseLong(insertedjobpost.getString(1)));
							}
						}
						insertManyToManyStatement.executeUpdate();
					}
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