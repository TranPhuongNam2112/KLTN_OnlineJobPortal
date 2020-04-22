package com.datn.onlinejobportal.payload;

public class JobLocationRequest {
	
	private String street_address;
	
	private String city_province;

	public String getStreet_address() {
		return street_address;
	}

	public void setStreet_address(String street_address) {
		this.street_address = street_address;
	}

	public String getCity_province() {
		return city_province;
	}

	public void setCity_province(String city_province) {
		this.city_province = city_province;
	}
	
	

}
