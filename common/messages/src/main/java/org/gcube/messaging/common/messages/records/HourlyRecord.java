package org.gcube.messaging.common.messages.records;

/**
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public class HourlyRecord extends IntervalRecord{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static Long HOUR_INTERVAL = new Long(3600);
	
	public HourlyRecord (){
		super(HOUR_INTERVAL);
	}

}

