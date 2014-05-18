package com.impact.preshopping.model;

import java.util.List;

public class Data {
	private List catagoryList;

	private String comName;
	private String companyID;
	private String contactPerson;
	private String faxNumber;
	private String logoIcon;
	private String logoImage;
	private String phoneNumber;
	private String taxID;
	public List getCatagoryList() {
		return this.catagoryList;
	}

	public String getComName() {
		return this.comName;
	}

	public String getCompanyID() {
		return this.companyID;
	}

	public String getContactPerson() {
		return this.contactPerson;
	}

	public String getFaxNumber() {
		return this.faxNumber;
	}

	public String getLogoIcon() {
		return this.logoIcon;
	}

	public String getLogoImage() {
		return this.logoImage;
	}

	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public String getTaxID() {
		return this.taxID;
	}

	public void setCatagoryList(List catagoryList) {
		this.catagoryList = catagoryList;
	}

	public void setComName(String comName) {
		this.comName = comName;
	}

	public void setCompanyID(String companyID) {
		this.companyID = companyID;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	public void setFaxNumber(String faxNumber) {
		this.faxNumber = faxNumber;
	}

	public void setLogoIcon(String logoIcon) {
		this.logoIcon = logoIcon;
	}

	public void setLogoImage(String logoImage) {
		this.logoImage = logoImage;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public void setTaxID(String taxID) {
		this.taxID = taxID;
	}

	@Override
	public String toString() {
		return "Data [catagoryList=" + catagoryList + ", comName=" + comName + ", companyID=" + companyID + ", contactPerson=" + contactPerson + ", faxNumber=" + faxNumber
				+ ", logoIcon=" + logoIcon + ", logoImage=" + logoImage + ", phoneNumber=" + phoneNumber + ", taxID=" + taxID + "]";
	}
}
