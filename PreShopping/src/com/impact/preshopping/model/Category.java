package com.impact.preshopping.model;

import java.util.ArrayList;
import java.util.List;

public class Category {

//	 "catID": "1",
//     "catName": "Books",
//     "catIcon": "http:\\/\\/mobile.bgnsolutions.com\\/media\\/image\\/thumbnail\\/1a.png",
//     "catImage": "http:\\/\\/mobile.bgnsolutions.com\\/media\\/image\\/1a.jpg",
//     "GroupList": [
	private String companyId;
	private String categoryId;
	private String categoryName;
	private String categoryIconUrl;
	private String categoryIconFilePath;
	private List<Group> groups = new ArrayList<Group>();
	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}
	public Category(String companyId, String categoryId, String categoryName, String categoryIconUrl) {
		super();
        this.companyId = companyId;
		this.categoryId = categoryId;
		this.categoryName = categoryName;
		this.categoryIconUrl = categoryIconUrl;
	}
	public String getCategoryIconFilePath() {
		return categoryIconFilePath;
	}
	public String getCategoryIconUrl() {
		return categoryIconUrl;
	}
	public String getCategoryId() {
		return categoryId;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public List<Group> getGroups() {
		return groups;
	}
	public void setCategoryIconFilePath(String categoryIconFilePath) {
		this.categoryIconFilePath = categoryIconFilePath;
	}
	public void setCategoryIconUrl(String categoryIconUrl) {
		this.categoryIconUrl = categoryIconUrl;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}


    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }
}
