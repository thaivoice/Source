package com.impact.preshopping.model;

import java.util.List;

public class ProductList {
	private List mediaList;

	private String grpID;
	private String isActive;
	private String maker;
	private String model;
	private String prodDesc;
	private String prodID;
	private String prodIcon;
	private String prodLongName;
	private String prodName;
	private String prodShortDesc;
	private String qrData;
	private String rating;
	public String getGrpID() {
		return this.grpID;
	}

	public String getIsActive() {
		return this.isActive;
	}

	public String getMaker() {
		return this.maker;
	}

	public List getMediaList() {
		return this.mediaList;
	}

	public String getModel() {
		return this.model;
	}

	public String getProdDesc() {
		return this.prodDesc;
	}

	public String getProdIcon() {
		return this.prodIcon;
	}

	public String getProdID() {
		return this.prodID;
	}

	public String getProdLongName() {
		return this.prodLongName;
	}

	public String getProdName() {
		return this.prodName;
	}

	public String getProdShortDesc() {
		return this.prodShortDesc;
	}

	public String getQrData() {
		return this.qrData;
	}

	public String getRating() {
		return this.rating;
	}

	public void setGrpID(String grpID) {
		this.grpID = grpID;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public void setMaker(String maker) {
		this.maker = maker;
	}

	public void setMediaList(List mediaList) {
		this.mediaList = mediaList;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public void setProdDesc(String prodDesc) {
		this.prodDesc = prodDesc;
	}

	public void setProdIcon(String prodIcon) {
		this.prodIcon = prodIcon;
	}

	public void setProdID(String prodID) {
		this.prodID = prodID;
	}

	public void setProdLongName(String prodLongName) {
		this.prodLongName = prodLongName;
	}

	public void setProdName(String prodName) {
		this.prodName = prodName;
	}

	public void setProdShortDesc(String prodShortDesc) {
		this.prodShortDesc = prodShortDesc;
	}

	public void setQrData(String qrData) {
		this.qrData = qrData;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	@Override
	public String toString() {
		return "ProductList [mediaList=" + mediaList + ", grpID=" + grpID + ", isActive=" + isActive + ", maker=" + maker + ", model=" + model + ", prodDesc=" + prodDesc
				+ ", prodID=" + prodID + ", prodIcon=" + prodIcon + ", prodLongName=" + prodLongName + ", prodName=" + prodName + ", prodShortDesc=" + prodShortDesc + ", qrData="
				+ qrData + ", rating=" + rating + "]";
	}
}
