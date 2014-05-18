package com.impact.preshopping.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GroupList {

	private String grpID;
	private String catID;
	private String grpName;
	private String grpIcon;
	private String grpImage;
	private List<Object> ProductList = new ArrayList<Object>();
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	public String getCatID() {
		return catID;
	}

	public String getGrpIcon() {
		return grpIcon;
	}

	public String getGrpID() {
		return grpID;
	}

	public String getGrpImage() {
		return grpImage;
	}

	public String getGrpName() {
		return grpName;
	}

	public List<Object> getProductList() {
		return ProductList;
	}

	public void setAdditionalProperties(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

	public void setCatID(String catID) {
		this.catID = catID;
	}

	public void setGrpIcon(String grpIcon) {
		this.grpIcon = grpIcon;
	}

	public void setGrpID(String grpID) {
		this.grpID = grpID;
	}

	public void setGrpImage(String grpImage) {
		this.grpImage = grpImage;
	}

	public void setGrpName(String grpName) {
		this.grpName = grpName;
	}

	public void setProductList(List<Object> ProductList) {
		this.ProductList = ProductList;
	}

	public GroupList withCatID(String catID) {
		this.catID = catID;
		return this;
	}

	public GroupList withGrpIcon(String grpIcon) {
		this.grpIcon = grpIcon;
		return this;
	}

	public GroupList withGrpID(String grpID) {
		this.grpID = grpID;
		return this;
	}

	public GroupList withGrpImage(String grpImage) {
		this.grpImage = grpImage;
		return this;
	}

	public GroupList withGrpName(String grpName) {
		this.grpName = grpName;
		return this;
	}

	public GroupList withProductList(List<Object> ProductList) {
		this.ProductList = ProductList;
		return this;
	}

}
