package org.gcube.messaging.accounting.portalaccountingportlet.client.obj;

import java.io.Serializable;


public class CollectionPair implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String name;
	private String id;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
}
