package com.impact.preshopping.model;


public class MediaList {
	private String fileSize;

	private String longDesc;
	private String mediaID;
	private String mediaType;
	private String mediaURL;
	private String prodID;
	private String shortDesc;
	public String getFileSize() {
		return this.fileSize;
	}

	public String getLongDesc() {
		return this.longDesc;
	}

	public String getMediaID() {
		return this.mediaID;
	}

	public String getMediaType() {
		return this.mediaType;
	}

	public String getMediaURL() {
		return this.mediaURL;
	}

	public String getProdID() {
		return this.prodID;
	}

	public String getShortDesc() {
		return this.shortDesc;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	public void setLongDesc(String longDesc) {
		this.longDesc = longDesc;
	}

	public void setMediaID(String mediaID) {
		this.mediaID = mediaID;
	}

	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}

	public void setMediaURL(String mediaURL) {
		this.mediaURL = mediaURL;
	}

	public void setProdID(String prodID) {
		this.prodID = prodID;
	}

	public void setShortDesc(String shortDesc) {
		this.shortDesc = shortDesc;
	}

	@Override
	public String toString() {
		return "MediaList [fileSize=" + fileSize + ", longDesc=" + longDesc + ", mediaID=" + mediaID + ", mediaType=" + mediaType + ", mediaURL=" + mediaURL + ", prodID=" + prodID
				+ ", shortDesc=" + shortDesc + "]";
	}
}
