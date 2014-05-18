package com.impact.preshopping.model;

public class ImMedia {
//	"mediaID": "1",
//    "prodID": "1",
//    "mediaURL": "http:\\/\\/mobile.bgnsolutions.com\\/media\\/image\\/20a.jpg",
//    "fileSize": "0",
//    "shortDesc": "",
//    "longDesc": "",
//    "mediaType": "1"
	
	private String mediaId;
	private String productId;
	private String mediaUrl;
	private String fileSize;
	private String shortDesc;
	private String longDesc;
	private String type;
	private String mediaFilePath;
	
	public ImMedia(String mediaId, String productId, String mediaUrl, String fileSize, String shortDesc, String longDesc, String type) {
		super();
		this.mediaId = mediaId;
		this.productId = productId;
		this.mediaUrl = mediaUrl;
		this.fileSize = fileSize;
		this.shortDesc = shortDesc;
		this.longDesc = longDesc;
		this.type = type;
	}
	public String getFileSize() {
		return fileSize;
	}
	public String getLongDesc() {
		return longDesc;
	}
	public String getMediaFilePath() {
		return mediaFilePath;
	}
	public String getMediaId() {
		return mediaId;
	}
	public String getMediaUrl() {
		return mediaUrl;
	}
	public String getProductId() {
		return productId;
	}
	public String getShortDesc() {
		return shortDesc;
	}
	public String getType() {
		return type;
	}
	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}
	public void setLongDesc(String longDesc) {
		this.longDesc = longDesc;
	}
	public void setMediaFilePath(String mediaFilePath) {
		this.mediaFilePath = mediaFilePath;
	}
	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}
	public void setMediaUrl(String mediaUrl) {
		this.mediaUrl = mediaUrl;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public void setShortDesc(String shortDesc) {
		this.shortDesc = shortDesc;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
    	
}
