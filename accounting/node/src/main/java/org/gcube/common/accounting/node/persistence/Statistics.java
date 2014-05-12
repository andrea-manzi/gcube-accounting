package org.gcube.common.accounting.node.persistence;

import java.io.Serializable;

/**
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public class Statistics implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private long totalAVG;
	private double avg;
	private long temp	;
	private long totalCalls;
	private long interval;
	private long origInterval;
	
	/**
	 * 
	 */
	Statistics(long interval){
		this.interval = interval;
		this.totalAVG= 0;
		this.avg = 0.0;
		this.temp = 0;
		this.totalCalls = 0;
		this.origInterval = interval;
	}
	

	
	public long getTotalCalls() {
		return totalCalls;
	}

	
	public double getAvg() {
		if (this.totalAVG==0)
			return temp;
		else return avg;
	}

	public void setAvg(double avg) {
		this.avg = avg;
	}

	
	public void addAvg(long temp){
		this.temp += temp;
		this.totalCalls+=temp;
		this.interval--;
		if ( this.interval==0 ) {
			this.totalAVG++;
			this.avg=(this.avg+this.temp)/this.totalAVG;
			this.interval= origInterval;
			this.temp = 0;
		}
	}
}
