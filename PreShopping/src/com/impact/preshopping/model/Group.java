package com.impact.preshopping.model;

import java.util.ArrayList;
import java.util.List;

public class Group {

//	"grpID": "1",
//    "catID": "1",
//    "grpName": "Magazine",
//    "grpIcon": "http:\\/\\/mobile.bgnsolutions.com\\/media\\/image\\/thumbnail\\/1b.png",
//    "grpImage": "http:\\/\\/mobile.bgnsolutions.com\\/media\\/image\\/1b.jpg",
//    "ProductList": [
	
	private String groupId;
	private String categoryId;
	private String groupName;
	private String groupIconUrl;
	private String groupIconFilePath;
	
	private List<Product> products = new ArrayList<Product>();
	public void setProducts(List<Product> products) {
		this.products = products;
	}
	public Group(String groupId, String categoryId, String groupName, String groupIconUrl) {
		super();
		this.groupId = groupId;
		this.categoryId = categoryId;
		this.groupName = groupName;
		this.groupIconUrl = groupIconUrl;
	}
	public String getCategoryId() {
		return categoryId;
	}
	public String getGroupIconFilePath() {
		return groupIconFilePath;
	}
	public String getGroupIconUrl() {
		return groupIconUrl;
	}
	public String getGroupId() {
		return groupId;
	}
	public String getGroupName() {
		return groupName;
	}
	public List<Product> getProducts() {
		return products;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public void setGroupIconFilePath(String groupIconFilePath) {
		this.groupIconFilePath = groupIconFilePath;
	}
	public void setGroupIconUrl(String groupIconUrl) {
		this.groupIconUrl = groupIconUrl;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	
	
}
