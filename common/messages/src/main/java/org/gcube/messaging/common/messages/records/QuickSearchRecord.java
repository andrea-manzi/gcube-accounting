package org.gcube.messaging.common.messages.records;

/**
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public class QuickSearchRecord extends BaseRecord{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String  term;

	/**
	 * get the term
	 * @return the term
	 */
	public String getTerm() {
		return term;
	}

	/**
	 * set the term
	 * @param term
	 */
	public void setTerm(String term) {
		this.term = term;
	}
}
