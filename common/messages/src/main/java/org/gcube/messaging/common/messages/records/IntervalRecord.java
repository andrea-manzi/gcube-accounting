package org.gcube.messaging.common.messages.records;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author Andrea Manzi
 *
 */
public class IntervalRecord implements Serializable{

	private static final long serialVersionUID = 1L;

	private Long invocationNumber;

	private Double averageInvocationTime;

	private Long interval;

	private String IP;
	
	private Date startInterval;
	
	private Date endInterval;

	public IntervalRecord(){};

	/**
	 * create an Interval record for the given Interval
	 * @param timeSlot the timeSlot
	 */
	public IntervalRecord(Long interval){
		this.interval= interval;
	}

	/**
	 * get the averageInvocationTime
	 * @return the average invocation time
	 */
	public Double getAverageInvocationTime() {
		return averageInvocationTime;
	}

	/**
	 * set the averageInvocationTime
	 * @param averageInvocationTime
	 */
	public void setAverageInvocationTime(Double averageInvocationTime) {
		this.averageInvocationTime = averageInvocationTime;
	}

	/**
	 * get the invocation Number
	 * @return the invocation number
	 */
	public Long getInvocationNumber() {
		return invocationNumber;
	}

	/**
	 * set the invocationNumbers
	 * @param invocationNumber  invocationNumber
	 */
	public void setInvocationNumber(Long invocationNumber) {
		this.invocationNumber = invocationNumber;
	}

	/**
	 * get the interval
	 * @return the interval
	 */
	public Long getInterval() {
		return interval;
	}

	/**
	 * set the time slot
	 * @param timeSlot the time slot
	 */
	public void setInterval(Long interval) {
		this.interval = interval;
	}
	/** 
	 * the caller IP
	 * @return the caller IP
	 */
	public String getIP() {
		return IP;
	}
	/** 
	 * the caller IP
	 * @param iP the caller IP
	 */
	public void setIP(String iP) {
		IP = iP;
	}
	
	public Date getStartInterval() {
		return startInterval;
	}

	public void setStartInterval(Date startInterval) {
		this.startInterval = startInterval;
	}

	public Date getEndInterval() {
		return endInterval;
	}

	public void setEndInterval(Date endInterval) {
		this.endInterval = endInterval;
	}
	

}