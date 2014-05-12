package org.gcube.messaging.common.messages.records;


/**
 * 
 * @author Andrea Manzi (CERN)
 *
 */
public class LoginRecord extends BaseRecord{

	private String message;
	/**
	 * 
	 */
	private static final long serialVersionUID = 5653687380557751943L;
	
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
