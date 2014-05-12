package org.gcube.messaging.common.consumerlibrary.query;

import java.util.ArrayList;
import java.util.HashMap;

import org.gcube.messaging.common.consumerlibrary.ConsumerCL;
import org.gcube.messaging.common.consumerlibrary.json.JSONArray;
import org.gcube.messaging.common.consumerlibrary.json.JSONException;

/**
 * 
 * @author Andrea Manzi(CERN)
 *
 */
		
public class NotificationQuery extends Query<ConsumerCL>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected String performQuery() throws Exception {
		logger.debug(getQuery());
		Long startTime = System.currentTimeMillis();
		String result = call.queryMonitoringDB(getQuery());
		Long endTime  = System.currentTimeMillis();
		logger.debug("Query time = "+ (endTime - startTime)+" ms");
		return result;
		
	}
	
	/** 
	 * Get the available notification types
	 * @return
	 * @throws Exception
	 */
	public HashMap<String,ArrayList<String>> getNotificationList() throws Exception {
		 HashMap<String,ArrayList<String>> hash = new HashMap<String,ArrayList<String>>();

		this.setQuery("SELECT DISTINCT testType, testSubType FROM NOTIFICATION ORDER BY testType");
		try {
			this.query();
			JSONArray tmp = toJSON();
			for(int i= 0;i<tmp.length();i++){
				String type=tmp.getJSONObject(i).getString("testType");
				String subtype=tmp.getJSONObject(i).getString("testSubType");
				ArrayList<String> list = null;
				if ((list = hash.get(type)) == null)
					list = new ArrayList<String>();
				list.add(subtype);
				hash.put(type, list);
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
	
	public String getStatistics(String testType, String GHNName,String startDate,
			String endDate, String groupBy  ) throws Exception {
		this.setQuery("SELECT COUNT(*) AS CNT, "+ groupBy+" FROM NOTIFICATION " +
				"WHERE date >= '"+startDate+"' AND date <= '"+endDate+"' " +
				(testType.compareTo("")==0?"":" AND testSubType='"+testType+"'") +
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
	
}

