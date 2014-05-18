package com.impact.preshopping.model;

import java.util.ArrayList;
import java.util.List;

public class Product {

//	"prodID": "1",
//    "grpID": "1",
//    "prodName": "Time",
//    "prodLongName": "TIME Magazine",
//    "prodShortDesc": "TIME Magazine",
//    "prodDesc": "TIME Magazine",
//    "rating": "5",
//    "prodIcon": "itime.png",
//    "qrData": "",
//    "isActive": "\\u0001",
//    "maker": "",
//    "model": "",
//    "MediaList": [
	
	private String productId;
	private String groupId;
	private String productName;
	private String productLongName;
	private String productShortDesc;
	private String productLongDesc;
	private String rating;
	private String productIconUrl;
	private String qrData;
	private String maker;
	private String model;
	private List<ImMedia> medias = new ArrayList<ImMedia>();
	
	public void setMedias(List<ImMedia> medias) {
		this.medias = medias;
	}
	private String productIconFilePath;
	
	
	public Product(String productId, String groupId, String productName, String productLongName, String productShortDesc, String productLongDesc, String rating,
			String productIconUrl, String qrData, String maker, String model) {
		super();
		this.productId = productId;
		this.groupId = groupId;
		this.productName = productName;
		this.productLongName = productLongName;
		this.productShortDesc = productShortDesc;
		this.productLongDesc = productLongDesc;
		this.rating = rating;
		this.productIconUrl = productIconUrl;
		this.qrData = qrData;
		this.maker = maker;
		this.model = model;
	}
	public String getGroupId() {
		return groupId;
	}
	public String getMaker() {
		return maker;
	}
	public List<ImMedia> getMedias() {
		return medias;
	}
	public String getModel() {
		return model;
	}
	public String getProductIconFilePath() {
		return productIconFilePath;
	}
	public String getProductIconUrl() {
		return productIconUrl;
	}
	public String getProductId() {
		return productId;
	}
	public String getProductLongDesc() {
		return productLongDesc;
	}
	public String getProductLongName() {
		return productLongName;
	}
	public String getProductName() {
		return productName;
	}
	public String getProductShortDesc() {
		return productShortDesc;
	}
	public String getBarcodeData() {
		return qrData;
	}
	public String getRating() {
		return rating;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public void setMaker(String maker) {
		this.maker = maker;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public void setProductIconFilePath(String productIconFilePath) {
		this.productIconFilePath = productIconFilePath;
	}
	public void setProductIconUrl(String productIconUrl) {
		this.productIconUrl = productIconUrl;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public void setProductLongDesc(String productLongDesc) {
		this.productLongDesc = productLongDesc;
	}
	public void setProductLongName(String productLongName) {
		this.productLongName = productLongName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public void setProductShortDesc(String productShortDesc) {
		this.productShortDesc = productShortDesc;
	}
	public void setQrData(String qrData) {
		this.qrData = qrData;
	}
	public void setRating(String rating) {
		this.rating = rating;
	}
	
	
	
	
	
}
