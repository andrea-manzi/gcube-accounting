package org.gcube.messaging.common.messages.records;

import java.util.HashMap;

/**
 * 
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public class AdvancedSearchRecord extends BaseRecord{

	HashMap<String,String> collections = null;
	HashMap<String,String> terms = null;
	OperatorType operator;
	
	/**
	 * the type of operator
	 * @author Andrea Manzi
	 *
	 */
	public enum OperatorType {
		AND("AND"),
		OR("OR"),
		None("None");
		String operator;
		OperatorType(String operator) {this.operator= operator;}
		public String toString() {return this.operator;} 
	}
	
	/**
	 * constructor
	 */
	public AdvancedSearchRecord (){
		collections = new HashMap<String, String>();
		terms = new HashMap<String, String>();
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * get the collection map
	 * @return the collection map
	 */
	public HashMap<String, String> getCollections() {
		return collections;
	}
	/**
	 * set the collection map
	 * @param collections the collection map
	 */
	public void setCollections(HashMap<String, String> collections) {
		this.collections = collections;
	}
	
	/**
	 * get the term map
	 * @return the term map
	 */
	public HashMap<String, String> getTerms() {
		return terms;
	}
	/**
	 * set the term map
	 * @param terms the term map
	 */
	public void setTerms(HashMap<String, String> terms) {
		this.terms = terms;
	}
	
	/**
	 * get the operator type
	 * @return the operator type
	 */
	public OperatorType getOperator() {
		return operator;
	}
	/**
	 * set the operator type
	 * @param operator the operator type
	 */
	public void setOperator(OperatorType operator) {
		this.operator = operator;
	}

}
