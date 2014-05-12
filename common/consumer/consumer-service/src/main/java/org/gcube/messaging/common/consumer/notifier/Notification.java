package org.gcube.messaging.common.consumer.notifier;

import org.gcube.common.core.scope.GCUBEScope;

/**
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public abstract class Notification {

	private String message;
	
	private String sourceGHN;
	
	private GCUBEScope scope;
	
	private String type;
	
	private String time;
	
	/**
	 * The Notification time
	 * @return
	 */
	public String getTime() {
		return time;
	}

	/**
	 * The Notification time
	 * @param time
	 */
	public void setTime(String time) {
		this.time = time;
	}

	/**
	 * type
	 * @return the notification type
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * set the type
	 * @param type the type
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * default constructor
	 *
	 */
	public Notification(){
		
	}
	/**
	 * Get the source GHN
	 * @return the source GHN
	 */
	public String getSourceGHN() {
		return sourceGHN;
	}
	/**
	 * Set the source GHN
	 * @param sourceGHN the source GHN
	 */
	public void setSourceGHN(String sourceGHN) {
		this.sourceGHN = sourceGHN;
	}
	
	/**
	 * get the message
	 * 
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * set the message
	 * @param message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	
	/**
	 * return the scope
	 * @return the scope
	 */
	public GCUBEScope getScope() {
		return scope;
	}

	/**
	 * set the scope
	 * @param scope the scope
	 */
	public void setScope(GCUBEScope scope) {
		this.scope = scope;
	}
}
