package com.impact.preshopping.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Datum {

	private String companyID;
	private String comName;
	private String taxID;
	private String contactPerson;
	private String phoneNumber;
	private String faxNumber;
	private String logoIcon;
	private String logoImage;
	private List<com.impact.preshopping.model.CatagoryList> CatagoryList = new ArrayList<com.impact.preshopping.model.CatagoryList>();
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	public List<com.impact.preshopping.model.CatagoryList> getCatagoryList() {
		return CatagoryList;
	}

	public String getComName() {
		return comName;
	}

	public String getCompanyID() {
		return companyID;
	}

	public String getContactPerson() {
		return contactPerson;
	}

	public String getFaxNumber() {
		return faxNumber;
	}

	public String getLogoIcon() {
		return logoIcon;
	}

	public String getLogoImage() {
		return logoImage;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public String getTaxID() {
		return taxID;
	}

	public void setAdditionalProperties(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

	public void setCatagoryList(List<com.impact.preshopping.model.CatagoryList> CatagoryList) {
		this.CatagoryList = CatagoryList;
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

	public Datum withCatagoryList(List<com.impact.preshopping.model.CatagoryList> CatagoryList) {
		this.CatagoryList = CatagoryList;
		return this;
	}

	public Datum withComName(String comName) {
		this.comName = comName;
		return this;
	}

	public Datum withCompanyID(String companyID) {
		this.companyID = companyID;
		return this;
	}

	public Datum withContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
		return this;
	}

	public Datum withFaxNumber(String faxNumber) {
		this.faxNumber = faxNumber;
		return this;
	}

	public Datum withLogoIcon(String logoIcon) {
		this.logoIcon = logoIcon;
		return this;
	}

	public Datum withLogoImage(String logoImage) {
		this.logoImage = logoImage;
		return this;
	}

	public Datum withPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
		return this;
	}

	public Datum withTaxID(String taxID) {
		this.taxID = taxID;
		return this;
	}

}