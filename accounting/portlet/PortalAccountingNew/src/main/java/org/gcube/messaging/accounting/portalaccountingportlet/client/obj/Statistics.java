package org.gcube.messaging.accounting.portalaccountingportlet.client.obj;

import java.io.Serializable;

import com.kfuntak.gwt.json.serialization.client.JsonSerializable;

public class Statistics implements JsonSerializable, Serializable{
	private static final long serialVersionUID = 1L;
	private static int COUNTER = 0;
	private int id;
	private String user;
	private String type;
	private String vre;
	private String date;
	private String CNT; //value

	public Statistics(){
	    this.id = Integer.valueOf(COUNTER++);
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getVre() {
		return vre;
	}
	public void setVre(String vre) {
		this.vre = vre;
	}
	public String getCNT() {
		return CNT;
	}
	public void setCNT(String cNT) {
		CNT = cNT;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}

	

}
