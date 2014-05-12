package org.gcube.common.accounting.node.persistence;

import java.io.Serializable;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public class TopCallerData implements Serializable{
	
	/** Logger */
	private static Logger logger = LoggerFactory.getLogger(TopCallerData.class);


	//hourInterval
	
	public static Long HOUR_INTERVAL_S=new Long(3600);
	public static Long DAY_INTERVAL_S= HOUR_INTERVAL_S*24;
	public Long intervalXHour;
	public Long intervalXday;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long interval = 0;
	
	public  TopCallerData(long interval){
		this.interval = interval;
		intervalXHour= HOUR_INTERVAL_S/this.interval;
		intervalXday= DAY_INTERVAL_S/this.interval;
	}
	
	//Map for topCallerHost
	private HashMap<String,HashMap<Long,Statistics>> callerMap = null;
	
	
	public HashMap<String, HashMap<Long, Statistics>> getCallerMap() {
		return callerMap;
	}


	public void addCallerInfo(String caller,Long calls) throws Exception{
		if (callerMap == null)
			callerMap = new HashMap<String,HashMap<Long,Statistics>>();	
		synchronized (callerMap) 
		{ 	
			HashMap<Long,Statistics> statMap = null;
			if (callerMap.get(caller)== null) {
				try {
					statMap= new HashMap<Long,Statistics>();
				 	Statistics stat = new Statistics(intervalXHour);
				 	stat.addAvg(calls);
				 	Statistics statDay = new Statistics(intervalXday);
				 	statDay.addAvg(calls);
				 	statMap.put(intervalXHour,stat);
				 	statMap.put(intervalXday, statDay);
				}catch (Exception e ){
					logger.debug("Error creating top caller map Data for caller "+caller,e);
					throw e;
				}
			}
			else {
				try {
					statMap =callerMap.get(caller);
					statMap.get(intervalXHour).addAvg(calls);
					statMap.get(intervalXday).addAvg(calls);
				}catch (Exception e ){
					logger.debug("Error updating top caller map Data for caller "+caller,e);
					throw e;
				}
				
			}
			callerMap.put(caller,statMap);
			}
	}
	
	public TopCallerInfo getTopCallerInfo(){
		String callerHost = null;
		long maxCalls = 0;
		for (String host :callerMap.keySet()){
			if (callerMap.get(host).get(intervalXHour).getTotalCalls() >= maxCalls){
				maxCalls= callerMap.get(host).get(intervalXHour).getTotalCalls();
				callerHost = host;
			}
		}
		return new TopCallerInfo(callerHost,maxCalls);
		
	}
	public Long getIntervalXHour() {
		return intervalXHour;
	}



	public void setIntervalXHour(Long intervalXHour) {
		this.intervalXHour = intervalXHour;
	}



	public Long getIntervalXday() {
		return intervalXday;
	}



	public void setIntervalXday(Long intervalXday) {
		this.intervalXday = intervalXday;
	}

	
	public class TopCallerInfo implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String callerHost;
		private long totalCalls;
		
		public TopCallerInfo(String host,long totalCalls){
			this.callerHost=host;
			this.totalCalls=totalCalls;
		}
		public String getCallerHost() {
			return callerHost;
		}
		public void setCallerHost(String callerHost) {
			this.callerHost = callerHost;
		}
		public long getTotalCalls() {
			return totalCalls;
		}
		public void setTotalCalls(long totalCall) {
			this.totalCalls = totalCall;
		}
	}
	
}
