package org.gcube.messaging.accounting.portalaccountingportlet.client.obj;

import java.io.Serializable;

public class TermPair implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String termName;
	private String termValue;
	
	public String getTermName() {
		return termName;
	}
	public void setTermName(String termName) {
		this.termName = termName;
	}
	public String getTermValue() {
		return termValue;
	}
	public void setTermValue(String termValue) {
		this.termValue = termValue;
	}

}
