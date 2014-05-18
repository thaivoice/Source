package com.impact.preshopping.model;

import java.util.ArrayList;
import java.util.List;

public class ServerResponse_Login {

	private List<Company> companies = new ArrayList<Company>();
	public void setCompanies(List<Company> companies) {
		this.companies = companies;
	}

	private String responseCode;
	private String responseDesc;
	
	public ServerResponse_Login(String responseCode, String responseDesc) {
		super();
		this.responseCode = responseCode;
		this.responseDesc = responseDesc;
	}

	public List<Company> getCompanies() {
		return companies;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public String getResponseDesc() {
		return responseDesc;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public void setResponseDesc(String responseDesc) {
		this.responseDesc = responseDesc;
	}
	
	
	
}
