package com.datn.webcrawler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Test {

	public static void main(String[] args) throws Exception {
		Connection conn = null;
		try {
			conn =
					DriverManager.getConnection("jdbc:mysql://localhost:3306/jobportal?" +
							"user=root&password=1234");
			PreparedStatement insertNewJobLocationStatement;
			PreparedStatement insertNewEmployerStatement;
			PreparedStatement insertNewJobTypeStatement;
			PreparedStatement insertNewIndustryStatement;
			PreparedStatement insertNewJobPostStatement;
			PreparedStatement selectJobTypeStatement;
			PreparedStatement selectIndustryStatement;
			PreparedStatement selectEmployerStatement;
			PreparedStatement insertManyToManyStatement;

			insertNewJobLocationStatement =DriverManager.getConnection("jdbc:mysql://localhost:3306/jobportal?" +
					"user=root&password=1234").prepareStatement("insert into joblocation(city_province,street_address) values " +
							"(?,?)", Statement.RETURN_GENERATED_KEYS);

			selectJobTypeStatement = DriverManager.getConnection("jdbc:mysql://localhost:3306/jobportal?" +
					"user=root&password=1234").prepareStatement("select id from job_type where "
							+ "job_type_name LIKE ?");
			insertNewJobTypeStatement = DriverManager.getConnection("jdbc:mysql://localhost:3306/jobportal?" +
					"user=root&password=1234").prepareStatement("insert into job_type(job_type_name) values "
							+ "(?)", Statement.RETURN_GENERATED_KEYS);
			selectEmployerStatement = DriverManager.getConnection("jdbc:mysql://localhost:3306/jobportal?" +
					"user=root&password=1234").prepareStatement("select * from employer where "
							+ "companyname LIKE ?");
			insertNewEmployerStatement = DriverManager.getConnection("jdbc:mysql://localhost:3306/jobportal?" +
					"user=root&password=1234").prepareStatement("insert into "
							+ "employer(companyname, description, establishmentdate, industry, main_address, phone_number, websiteurl, account_id) "
							+ "values (?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
			insertNewJobPostStatement = DriverManager.getConnection("jdbc:mysql://localhost:3306/jobportal?" +
					"user=root&password=1234").prepareStatement("insert into job_post(created_at, updated_at, expiration_date, job_description, job_title, "
							+ "max_salary, min_salary, requiredexperienceyears, sourceUrl, posted_by, joblocation_id, job_type_id) values "
							+ "(?,?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);

			selectEmployerStatement.setString(1,'%' + "CÔNG TY TNHH EASYCOLLECT SERVICES VIỆT NAM" + '%');
			ResultSet employercounts = selectEmployerStatement.executeQuery();
			if (employercounts.next() == false) {
				insertNewEmployerStatement.setString(1, "CÔNG TY TNHH EASYCOLLECT SERVICES VIỆT NAM");
				insertNewEmployerStatement.setNull(2, java.sql.Types.VARCHAR);
				insertNewEmployerStatement.setNull(3, java.sql.Types.DATE);
				insertNewEmployerStatement.setNull(4, java.sql.Types.VARCHAR);
				insertNewEmployerStatement.setNull(5, java.sql.Types.VARCHAR);
				insertNewEmployerStatement.setNull(6, java.sql.Types.VARCHAR);
				insertNewEmployerStatement.setNull(7, java.sql.Types.VARCHAR);
				insertNewEmployerStatement.setNull(8, java.sql.Types.BIGINT);
				insertNewEmployerStatement.executeUpdate();
				ResultSet insertedemployer = insertNewEmployerStatement.getGeneratedKeys();

				while (insertedemployer.next()) {
					System.out.println(insertedemployer.getString(1));
					insertNewJobPostStatement.setLong(10, Long.parseLong(insertedemployer.getString(1)));

				}
			} 
			else {
				while (employercounts.next()) {
					System.out.println(employercounts.getLong("id"));
					insertNewJobPostStatement.setLong(10, employercounts.getLong("id"));
				}
			}
		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}
}
