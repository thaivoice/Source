package com.impact.preshopping.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CatagoryList {

	private String catID;
	private String catName;
	private String catIcon;
	private String catImage;
	private List<com.impact.preshopping.model.GroupList> GroupList = new ArrayList<com.impact.preshopping.model.GroupList>();
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	public String getCatIcon() {
		return catIcon;
	}

	public String getCatID() {
		return catID;
	}

	public String getCatImage() {
		return catImage;
	}

	public String getCatName() {
		return catName;
	}

	public List<com.impact.preshopping.model.GroupList> getGroupList() {
		return GroupList;
	}

	public void setAdditionalProperties(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

	public void setCatIcon(String catIcon) {
		this.catIcon = catIcon;
	}

	public void setCatID(String catID) {
		this.catID = catID;
	}

	public void setCatImage(String catImage) {
		this.catImage = catImage;
	}

	public void setCatName(String catName) {
		this.catName = catName;
	}

	public void setGroupList(List<com.impact.preshopping.model.GroupList> GroupList) {
		this.GroupList = GroupList;
	}

	public CatagoryList withCatIcon(String catIcon) {
		this.catIcon = catIcon;
		return this;
	}

	public CatagoryList withCatID(String catID) {
		this.catID = catID;
		return this;
	}

	public CatagoryList withCatImage(String catImage) {
		this.catImage = catImage;
		return this;
	}

	public CatagoryList withCatName(String catName) {
		this.catName = catName;
		return this;
	}

	public CatagoryList withGroupList(List<com.impact.preshopping.model.GroupList> GroupList) {
		this.GroupList = GroupList;
		return this;
	}

}
