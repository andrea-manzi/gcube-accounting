package org.gcube.messaging.common.messages.records;

import java.util.HashMap;

/**
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public class BrowseRecord extends BaseRecord{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	HashMap<String,String> collections = null;
	
	/**
	 * create a browse record
	 */
	public BrowseRecord(){
		collections= new HashMap<String, String>();
	}
	
	String browseBy;
	boolean isDistinct = false;
	
	/**
	 * get the isDistinct value
	 * @return true/false
	 */
	public boolean isDistinct() {
		return isDistinct;
	}
	/**
	 * set the isDistinct param
	 * @param isDistinct isDistinct
	 */
	public void setDistinct(boolean isDistinct) {
		this.isDistinct = isDistinct;
	}
	
	/**
	 * get the browseBy value
	 * @return browse by
	 */
	public String getBrowseBy() {
		return browseBy;
	}
	/**
	 * set the browseBy
	 * @param browseBy browseBy
	 */
	public void setBrowseBy(String browseBy) {
		this.browseBy = browseBy;
	}
	/**
	 * get the collections map
	 * @return the collection map
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
}
