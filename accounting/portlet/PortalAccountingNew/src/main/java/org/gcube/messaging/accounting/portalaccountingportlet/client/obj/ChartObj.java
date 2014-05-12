package org.gcube.messaging.accounting.portalaccountingportlet.client.obj;

public class ChartObj {

	private static int COUNTER = 0;
	private int id;
	private double value;
	private String name; //contains: user or/and type or/and scope or/and date

	public ChartObj(){
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

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}
	
	
}
