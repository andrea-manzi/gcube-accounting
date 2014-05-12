package org.gcube.messaging.common.consumerlibrary.query;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.gcube.messaging.common.consumerlibrary.ConsumerCL;
import org.gcube.messaging.common.consumerlibrary.impl.Constants;
import org.gcube.messaging.common.consumerlibrary.json.JSONArray;
import org.gcube.messaging.common.consumerlibrary.json.JSONException;
import org.gcube.messaging.common.messages.Test;



/**
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public class MonitoringQuery extends Query<ConsumerCL>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Override
	public String performQuery() throws Exception {
		logger.debug(getQuery());
		Long startTime = System.currentTimeMillis();
		String result = call.queryMonitoringDB(getQuery());
		Long endTime  = System.currentTimeMillis();
		logger.debug("Query time = "+ (endTime - startTime) +" ms");
		return result;
		
	}
	
	/**
	 * getGHNAverage
	 * @param type Test.TestType 
	 * @param ghn the ghn
	 * @return average 
	 * @throws Exception
	 */
	public String getGHNAverage(Test.TestType type,String ghn)throws EmptyResultException,Exception {
		String res = null;
		this.setQuery("SELECT AVG(CAST(GHNMessage.result AS "+getType(type)+")) AS AVG FROM GHNMessage " +
				"WHERE GHNMessage.testType='"+type.name()+"' AND GHNMessage.GHNName='"+ghn+"'");
		try {
			this.query();
			res= toJSON().getJSONObject(0).getString("AVG");
		}catch (EmptyResultException e){
			logger.error("Error executing query");
			throw e;
		}
		catch (Exception e){
			logger.error("Error executing query");
			throw e;
		}
		return res;
	}
	

	/**
	 * getTodayGHNAverage
	 * @param type  Test.TestType
	 * @param ghn ghn name
	 * @return the today average
	 * @throws Exception
	 */
	public String getTodayGHNAverage(Test.TestType type,String ghn)throws EmptyResultException,Exception {
		String res = null;
		this.setQuery("SELECT AVG(CAST(GHNMessage.result AS "+getType(type)+")) AS AVG FROM GHNMessage " +
				"WHERE GHNMessage.testType='"+type.name()+"' AND GHNMessage.GHNName='"+ghn+"' AND GHNMessage.date='"+getTodayString()+"'");
		try {
			this.query();
			res= toJSON().getJSONObject(0).getString("AVG");
		}catch (EmptyResultException e){
			logger.error("Error executing query");
			throw e;
		}
		catch (Exception e){
			logger.error("Error executing query");
			throw e;
		}
		return res;
		
	}
	
	
	
	/** 
	 * Get the available probes
	 * @return
	 * @throws Exception
	 */
	public HashMap<String,String> getProbes() throws Exception {
		 HashMap<String,String> hash = new HashMap<String,String>();

		this.setQuery("SELECT DISTINCT testType, description  FROM GHNMessage ORDER BY testType");
		try {
			this.query();
			JSONArray tmp = toJSON();
			for(int i= 0;i<tmp.length();i++){
				String type=tmp.getJSONObject(i).getString("testType");
				String desc=tmp.getJSONObject(i).getString("description");
				hash.put(type, desc);
			}
		}catch (JSONException e){
			logger.error("Error executing query");
			throw new Exception(e);
		}
		catch (EmptyResultException e){
			logger.error("Error executing query");
			throw e;
		} catch (QueryNotSetException e) {
			logger.error("Error executing query");
			throw e;
		} catch (Exception e) {
			logger.error("Error executing query");
			throw e;
		}
		return hash;

	}
	
	/**
	 * return the GHNs 
	 * @return
	 * @throws Exception
	 */
	public Map<String,ArrayList<String>> getGHNs()throws Exception {
		Map<String,ArrayList<String>> map = new HashMap<String, ArrayList<String>>();

		this.setQuery("SELECT DISTINCT GHNName  FROM GHNMessage ORDER BY GHNName");
		try {
			this.query();
			JSONArray tmp = toJSON();
			for(int i= 0;i<tmp.length();i++){
				
				String ghn=tmp.getJSONObject(i).getString("GHNName");
				logger.debug("GHN: "+ ghn);
				//getting domain
				String temp = ghn.substring(ghn.indexOf(".")+1);
				String domain=temp.substring(0, temp.indexOf(":"));
				ArrayList<String> list = null;
				if ((list = map.get(domain)) == null)
					list = new ArrayList<String>();
				list.add(ghn);
				map.put(domain, list);
			}
		}catch (JSONException e){
			logger.error("Error executing query");
			throw new Exception(e);
		}
		catch (EmptyResultException e){
			logger.error("Error executing query");
			throw e;
		} catch (QueryNotSetException e) {
			logger.error("Error executing query");
			throw e;
		} catch (Exception e) {
			logger.error("Error executing query");
			throw e;
		}
		return map;

	}
	
	/**
	 * getAllTodayGHNAverage
	 * @param type Test.TestType
	 * @return the today average 
	 * @throws Exception exception
	 */
	public String getAllTodayGHNAverage(Test.TestType type)throws Exception {
		this.setQuery("SELECT AVG(CAST(GHNMessage.result AS "+getType(type)+")) AS AVG, " +
				"GHNMessage.GHNName FROM " +
				"GHNMessage WHERE GHNMessage.testType='"+type.name()+"'" +
				" AND GHNMessage.date='"+getTodayString()+"' GROUP BY GHNMessage.GHNName");
		return this.query();
		
	}
	
	/**
	 * get all the notification
	 * @return get all the notification
	 * @throws Exception exception
	 */
	public String getNotifications() throws Exception {
		this.setQuery("SELECT * FROM NOTIFICATION");
		return this.query();
	}
	
	/**
	 * getTodayNotifications
	 * @return JSONArray
	 * @throws Exception exception
	 */
	public String getTodayNotifications() throws Exception {
		this.setQuery("SELECT * FROM NOTIFICATION WHERE date='"+getTodayString()+"'");
		return this.query();
	}
	

	/**
	 * getTodayRIMessages
	 * @return JSONArray
	 * @throws Exception
	 * @throws EmptyResultException Exception
	 */
	public String getTodayRIMessages(String serviceClass,String serviceName) throws Exception {
		this.setQuery("SELECT * FROM RIMessage WHERE  ServiceName='"+serviceName+"' " +
				"AND ServiceClass='"+serviceClass+"' AND date='"+getTodayString()+"'");
		return this.query();
	}
	
	/**
	 * getRIMessages
	 * @return JSONArray
	 * @throws Exception
	 */
	public String getRIMessages(String serviceClass,String serviceName) throws Exception {
		this.setQuery("SELECT * FROM RIMessage WHERE  ServiceName='"+serviceName+"' " +
				"AND ServiceClass='"+serviceClass+"'");
		return this.query();
	
	}
	
	
	public String getStatistics(String testType, String GHNName,String startDate,
			String endDate, String groupBy  ) throws Exception {
		this.setQuery("SELECT COUNT(*) AS CNT, AVG(result) AS AVERAGE, "+ groupBy+" FROM GHNMessage " +
				"WHERE date >= '"+startDate+"' AND date <= '"+endDate+"' " +
				(testType.compareTo("")==0?"":" AND testType='"+testType+"'") +
				(GHNName.compareTo("")==0?"":" AND GHNName='"+GHNName+"'")+
						" GROUP BY "+groupBy);
		try {
			this.query();
		}catch (JSONException e){
			logger.error("Error executing query");
			throw new Exception(e);
		}
		catch (EmptyResultException e){
			logger.error("Error executing query");
			throw e;
		}
		return this.getResults();

	}
	
	private String getTodayString() {
		Date date = new Date();
	    return dateFormat.format(date.getTime());
	}
	
	private String getType(Test.TestType testType){
		if (testType.compareTo(Test.TestType.CPU_LOAD) ==0)
			return Constants.DECIMAL;
		else return Constants.UNSIGNED;
	}
}
