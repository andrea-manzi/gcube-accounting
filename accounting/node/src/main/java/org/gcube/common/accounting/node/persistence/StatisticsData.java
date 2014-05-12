package org.gcube.common.accounting.node.persistence;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;

import org.gcube.common.accounting.node.persistence.RIAccountingData.statisticsType;

/**
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public class StatisticsData implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long interval = 0;
	
	

	private statisticsType type = null;
	
	private LinkedBlockingQueue<Double> queue = null;
	
	private HashMap<Long,Long> intervalMapping = new HashMap<Long,Long> ();
	
	
	
	/**
	 * 
	 * @param type
	 */
	public StatisticsData(statisticsType type,long interval){
		this.type= type;
		this.interval = interval;
		queue = new LinkedBlockingQueue<Double>(5);
		intervalMapping.put(interval, new Long(0));
		intervalMapping.put(interval*3, new Long(2));
		intervalMapping.put(interval*5, new Long(4));
		
	}

	public statisticsType getType() {
		return type;
	}

	public void setType(statisticsType type) {
		this.type = type;
	}
	
	public void addStatistics(Double value) throws Exception{
		if (!(this.queue.offer((value)))){
			//first remove the head element
			this.queue.take();
			if (!((this.queue.offer(value)))) throw new Exception();
		}
	}
	
	public Double getStatistics(Long index) throws Exception{
		if (this.queue.size() ==0 ) return Double.valueOf(0.0);
		Iterator<Double> it = this.queue.iterator();
		int i = 0;
		Double value = new Double(0.0);
		while (i<=index && it.hasNext()){
			value+= it.next();
			i++;
		}
		return (value == null || value.equals(Double.valueOf(0.0)))?Double.valueOf(0.0):value/i;
	}
	
	public long getInterval() {
		return interval;
	}

	public void setInterval(long interval) {
		this.interval = interval;
	}


	public HashMap<Long, Long> getIntervalMapping() {
		return intervalMapping;
	}

	public void setIntervalMapping(HashMap<Long, Long> intervalMapping) {
		this.intervalMapping = intervalMapping;
	}

}


