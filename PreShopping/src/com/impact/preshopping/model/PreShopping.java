package com.impact.preshopping.model;

import java.util.List;

public class PreShopping {
	private List data;

	private String msg;
	private String status;
	public List getData() {
		return this.data;
	}

	public String getMsg() {
		return this.msg;
	}

	public String getStatus() {
		return this.status;
	}

	public void setData(List data) {
		this.data = data;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "PreShopping [data=" + data + ", msg=" + msg + ", status=" + status + "]";
	}
}
