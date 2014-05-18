package com.impact.preshopping.model;

import java.util.ArrayList;
import java.util.List;

public class Company {

//	 "companyID": "1",
//     "comName": "Antenna Audio",
//     "taxID": "123456789",
//     "contactPerson": "Vi",
//     "phoneNumber": "0123456789",
//     "faxNumber": "",
//     "logoIcon": "http:\\/\\/mobile.bgnsolutions.com\\/media\\/image\\/thumbnail\\/2a.png",
//     "logoImage": "",
//     "CatagoryList": [
                      
	private String companyId;
	private String companyName;
	private String taxId;
	private String contactPerson;
	private String phoneNumber;
	private String faxNumber;
	private String logoIconUrl;
	private String logoIconFilePath;
	private List<Category> categories = new ArrayList<Category>();
	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}
	public Company(String companyId, String companyName, String taxId, String contactPerson, String phoneNumber, String faxNumber, String logoIconUrl) {
		super();
		this.companyId = companyId;
		this.companyName = companyName;
		this.taxId = taxId;
		this.contactPerson = contactPerson;
		this.phoneNumber = phoneNumber;
		this.faxNumber = faxNumber;
		this.logoIconUrl = logoIconUrl;
	}
	public List<Category> getCategories() {
		return categories;
	}
	public String getCompanyId() {
		return companyId;
	}
	public String getCompanyName() {
		return companyName;
	}
	public String getContactPerson() {
		return contactPerson;
	}
	public String getFaxNumber() {
		return faxNumber;
	}
	public String getLogoIconFilePath() {
		return logoIconFilePath;
	}
	public String getLogoIconUrl() {
		return logoIconUrl;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public String getTaxId() {
		return taxId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}
	public void setFaxNumber(String faxNumber) {
		this.faxNumber = faxNumber;
	}
	public void setLogoIconFilePath(String logoIconFilePath) {
		this.logoIconFilePath = logoIconFilePath;
	}
	public void setLogoIconUrl(String logoIconUrl) {
		this.logoIconUrl = logoIconUrl;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public void setTaxId(String taxId) {
		this.taxId = taxId;
	}
	
	
                      
}
