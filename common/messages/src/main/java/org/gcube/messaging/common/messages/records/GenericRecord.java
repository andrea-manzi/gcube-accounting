package org.gcube.messaging.common.messages.records;

/**
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public class GenericRecord extends BaseRecord{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String message ;
	/**
	 * constructor
	 */
	public GenericRecord (){}
	
	/**
	 * get the message
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * set the message 
	 * @param message the message
	 */
	public void setMessage(String message) {
		this.message = message;
	}
}
