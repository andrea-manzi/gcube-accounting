package org.gcube.common.accounting.node.util;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import org.gcube.messaging.common.producer.GCUBELocalProducer;


/**
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public class DateInterval implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Calendar startDate = Calendar.getInstance();
	private Calendar endDate = Calendar.getInstance();
	private Long interval;
	
	/**
	 * 
	 * @param interval in ms
	 */
	public DateInterval(Long interval){
		this.interval = interval;
		endDate= Calendar.getInstance();
		startDate.setTime(new Date(Calendar.getInstance().getTimeInMillis() - interval));
	}
	
	/**
	 * 
	 * @param start
	 * @param end
	 */
	public DateInterval(Calendar start,Calendar end, Long interval){
		endDate= end;
		startDate = start;
		this.interval = interval;
	}
	public Long getInterval() {
		return interval;
	}

	public void setInterval(Long interval) {
		this.interval = interval;
	}

	

	/**
	 * The date is contained inside the date interval;
	 * @param date the date
	 * @return true/false
	 */
	public Boolean contains (Date date){
		//GCUBELocalProducer.logger.debug("Checking date :"+ Util.format.format(date));
		if (interval.compareTo(new Long(0))== 0)
			return true;
		Calendar tmp = Calendar.getInstance();
		tmp.setTime(date);
		if ( tmp.after(endDate) || tmp.before(startDate))
			return false;
		else return true;
			
	}
	public Calendar getStartDate() {
		return startDate;
	}
	
	public String getStartDateAsShortString() {
		return Util.format_day.format(startDate.getTime());
	}
	
	public String getStartDateAsString() {
		return Util.format.format(startDate.getTime());
	}

	public void setStartDate(Calendar startDate) {
		this.startDate = startDate;
	}

	public Calendar getEndDate() {
		return endDate;
	}
	public String getEndDateAsString() {
		return Util.format.format(endDate.getTime());
	}
	
	public String getEndDateAsShortString() {
		return Util.format_day.format(endDate.getTime());
	}

	public void setEndDate(Calendar endDate) {
		this.endDate = endDate;
	}
}
