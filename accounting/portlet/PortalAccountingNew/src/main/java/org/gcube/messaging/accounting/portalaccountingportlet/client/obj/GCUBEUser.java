package org.gcube.messaging.accounting.portalaccountingportlet.client.obj;

import java.io.Serializable;

public class GCUBEUser implements Serializable{

	private static final long serialVersionUID = 1L;
	private String vre;
	private String name;
	public String getVre() {
		return vre;
	}
	public void setVre(String vre) {
		this.vre = vre;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
