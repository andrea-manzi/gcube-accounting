package org.gcube.messaging.accounting.portalaccountingportlet.client.obj;

public class GroupObj {

	private static int COUNTER = 0;
	private int id;
	private String name;

	public GroupObj(){
	    this.id = Integer.valueOf(COUNTER++);
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
