package org.gcube.messaging.accounting.portalaccountingportlet.client.data;

import java.io.Serializable;

public class GHNDetail implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String ghnID;
	
	private String ghnName;
	
	public String getGhnID() {
		return ghnID;
	}
	public void setGhnID(String ghnID) {
		this.ghnID = ghnID;
	}
	public String getGhnName() {
		return ghnName;
	}
	public void setGhnName(String ghnName) {
		this.ghnName = ghnName;
	}


}
