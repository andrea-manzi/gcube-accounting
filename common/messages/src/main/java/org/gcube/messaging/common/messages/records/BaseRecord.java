package org.gcube.messaging.common.messages.records;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public abstract class BaseRecord implements Serializable{

	
	protected Date date;
	/**
	 * 
	 */
	private static final long serialVersionUID = -1226236649526629941L;
	
	/**
	 * get the date object
	 * @return the date object
	 */
	public Date getDate() {
		return date;
	}
	/**
	 * set the date object
	 * @param date the date object
	 */
	public void setDate(Date date) {
		this.date = date;
	}

}
