package org.gcube.messaging.common.messages.records;

import java.util.HashMap;

/**
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public class SimpleSearchRecord extends BaseRecord{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	HashMap<String,String> collections = null;
	String term;
	
	/**
	 * get collections map
	 * @return the collections map
	 */
	public HashMap<String, String> getCollections() {
		return collections;
	}

	/**
	 * set the collections map
	 * @param collections the collections map
	 */
	public void setCollections(HashMap<String, String> collections) {
		this.collections = collections;
	}

	/**
	 * get the term map
	 * @return the term map
	 */
	public String getTerm() {
		return term;
	}

	/**
	 * set the term map
	 * @param term the term map
	 */
	public void setTerm(String term) {
		this.term = term;
	}

	/**
	 * create a simple search record 
	 */
	public SimpleSearchRecord(){
		this.collections = new HashMap<String, String>();
	}
	
}
