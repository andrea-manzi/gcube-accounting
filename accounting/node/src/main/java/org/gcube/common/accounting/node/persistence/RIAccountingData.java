package org.gcube.common.accounting.node.persistence;

import java.io.Serializable;


/**
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public class RIAccountingData implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String serviceName;
	private String serviceClass;
	private StatisticsData avgInvocationTime = null;
	private StatisticsData avgCallsNumber =null;
	private long totalCalls = 0;
	private TopCallerData topCallerData = null;
	private long interval = 0;
	
	public  RIAccountingData (Long interval){
		this.interval = interval;
	}
	
	public enum statisticsType{
		InvocationTime("INVOCATIONTIME"),
		IncomingCalls("INCOMINGCALLS");
		String type;
		statisticsType(String type) {this.type = type;}
		public String toString() {return this.type;}
		
	}
	
	
	public StatisticsData getAvgInvocationTime() {
		if (avgInvocationTime == null)
			avgInvocationTime =  new StatisticsData(statisticsType.InvocationTime,interval);
		return avgInvocationTime;
	}
	public StatisticsData getAvgCallsNumber() {
		 if (avgCallsNumber == null)
			 avgCallsNumber =new StatisticsData(statisticsType.IncomingCalls,interval);
		return avgCallsNumber;
	}


	public void addCalls(long calls){
		totalCalls+= calls;
	}
	
	public long getTotalCalls(){
		return totalCalls;
	}
	
	public TopCallerData getTopCallerData() {
		if (topCallerData == null)
			 topCallerData = new TopCallerData(this.interval);
		return topCallerData;
	}

	
	public int hashcode (){
		return (serviceClass+serviceName).hashCode();
	}
	
	public boolean equals(Object obj) {
		
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		final RIAccountingData other = (RIAccountingData) obj;
		
		if (serviceName == null) {
			if (other.serviceName != null)
				return false;
		} else if (! serviceName.equals(other.serviceName))
			return false;
		
		if (serviceClass == null) {
			if (other.serviceClass != null)
				return false;
		} else if (! serviceClass.equals(other.serviceClass))
			return false;
		return true;
	}
	
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getServiceClass() {
		return serviceClass;
	}
	public void setServiceClass(String serviceClass) {
		this.serviceClass = serviceClass;
	}
}
