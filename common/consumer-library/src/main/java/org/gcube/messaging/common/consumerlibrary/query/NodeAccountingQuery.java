package org.gcube.messaging.common.consumerlibrary.query;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.gcube.messaging.common.consumerlibrary.json.JSONArray;
import org.gcube.messaging.common.consumerlibrary.json.JSONException;

/**
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public class NodeAccountingQuery extends AccountingQuery{

	public  static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public	 static SimpleDateFormat format= new SimpleDateFormat(DATE_FORMAT);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * getInvocationPerInterval
	 * @param serviceClass serviceClass
	 * @param serviceName serviceName
	 * @param GHNName GHNName
	 * @param startDate startDate ( in the form "yyyy-MM-dd HH:mm:ss")
	 * @param endDate endDate ( in the form "yyyy-MM-dd HH:mm:ss")
	 * @param callerScope callerScope
	 * @return InvocationInfo
	 * @throws Exception callerScope
	 */
	public InvocationInfo getInvocationPerInterval(String serviceClass, String serviceName, String GHNName, String startDate,String endDate, String ...callerScope  ) throws Exception {
		InvocationInfo info= new InvocationInfo();

		this.setQuery("SELECT SUM(invocationNumber) AS SUMINVOCATION, AVG(averageInvocationTime) AS AVERAGE FROM NODEACCOUNTING " +
				"WHERE ServiceClass='"+serviceClass+"' AND ServiceName='"+serviceName+"' AND startDate >= '"+startDate+"' AND endDate <= '"+endDate+"' " +
				"AND GHNName='"+GHNName+"'" +(callerScope.length>0?" AND callerScope='"+callerScope[0].toString()+"'":""));
		try {
			this.query();
			logger.debug(toJSON().toString());
			info.setInvocationCount(toJSON().getJSONObject(0).getDouble("SUMINVOCATION"));
			info.setAvgInvocationTime(toJSON().getJSONObject(0).getDouble("AVERAGE"));
		}catch (JSONException e){
			logger.error("Error executing query");
			throw new Exception(e);
		}
		catch (EmptyResultException e){
			logger.error("Error executing query");
			throw e;
		}
		return info;

	}


	public InvocationInfo getInvocationPerInterval(String serviceClass, String serviceName, String startDate,String endDate, String ...callerScope  ) throws Exception {
		InvocationInfo info= new InvocationInfo();

		this.setQuery("SELECT SUM(invocationNumber) AS SUMINVOCATION, AVG(averageInvocationTime) AS AVERAGE FROM NODEACCOUNTING " +
				"WHERE ServiceClass='"+serviceClass+"' AND ServiceName='"+serviceName+"' AND startDate >= '"+startDate+"' AND endDate <= '"+endDate+"' " +
				(callerScope.length>0?" AND callerScope='"+callerScope[0].toString()+"'":""));
		try {
			this.query();
			logger.debug(toJSON().toString());
			info.setInvocationCount(toJSON().getJSONObject(0).getDouble("SUMINVOCATION"));
			info.setAvgInvocationTime(toJSON().getJSONObject(0).getDouble("AVERAGE"));
		}catch (JSONException e){
			logger.error("Error executing query");
			throw new Exception(e);
		}
		catch (EmptyResultException e){
			logger.error("Error executing query");
			throw e;
		}
		return info;

	}
	
	public String getInvocationPerInterval(String serviceClass, String serviceName, String GHNName,String startDate,
			String endDate, String callerScope,String groupBy  ) throws Exception {
		this.setQuery("SELECT SUM(invocationNumber) AS SUMINVOCATION, AVG(averageInvocationTime) AS AVERAGE, "+ groupBy+" FROM NODEACCOUNTING " +
				"WHERE startDate >= '"+startDate+"' AND endDate <= '"+endDate+"' " +
				(serviceClass.compareTo("")==0?"":" AND ServiceClass='"+serviceClass+"'") +
				(serviceName.compareTo("")==0?"":" AND ServiceName='"+serviceName+"'")+
				(GHNName.compareTo("")==0?"":" AND GHNName='"+GHNName+"'")+
				(callerScope.compareTo("")==0?"":" AND callerScope='"+callerScope+"'")+
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
	/**
	 * getInvocationPerHour
	 * 
	 * @param startDate ( in the form "yyyy-MM-dd HH:mm:ss")
	 * @param endDate ( in the form "yyyy-MM-dd HH:mm:ss")
	 * @param callerScope  callerScope
	 * @return InvocationInfo
	 * @throws Exception
	 */
	public InvocationInfo getInvocationPerHour(String startDate,String endDate, String ...callerScope  ) throws Exception {
		InvocationInfo info= new InvocationInfo();
		//convert date to missils
		Date start = format.parse(startDate);
		Date end  = format.parse(endDate);
		long interval = end.getTime() - start.getTime();
		long hoursInInterval = interval/3600000;
		logger.debug("Intervals = "+hoursInInterval);

		this.setQuery("SELECT COUNT(DISTINCT GHNName) FROM NODEACCOUNTING "+
				"WHERE startDate >= '"+startDate+"' AND endDate <= '"+endDate+"' " +
				(callerScope.length>0?" AND callerScope='"+callerScope[0].toString()+"'":""));
		this.query();
		Long results = Long.valueOf(this.getResultsAsArray().get(1).get(0));
		logger.debug("numner of nodes: " +results);
		
		this.setQuery("SELECT SUM(invocationNumber) AS SUMINVOCATION, AVG(averageInvocationTime) AS AVERAGE FROM NODEACCOUNTING " +
				"WHERE startDate >= '"+startDate+"' AND endDate <= '"+endDate+"' " +
				(callerScope.length>0?" AND callerScope='"+callerScope[0].toString()+"'":""));
		try {
			this.query();
			info.setInvocationCount(toJSON().getJSONObject(0).getDouble("SUMINVOCATION")/hoursInInterval/results);
			info.setAvgInvocationTime(toJSON().getJSONObject(0).getDouble("AVERAGE"));
		}catch (JSONException e){
			logger.error("Error executing query");
			throw new Exception(e);
		}
		catch (EmptyResultException e){
			logger.error("Error executing query");
			throw e;
		}
		return info;

	}

	/**
	 * getHourlyInvocations
	 * @param ServiceClass  ServiceClass
	 * @param ServiceName ServiceName
	 * @param GHNName ServiceName
	 * @param date date
	 * @param hourInterval String in the form (12-13) 
	 * @param callerScope callerScope optional 
	 * @return
	 * @throws Exception Exception
	 * @throws EmptyResultException Exception
	 * 
	 */
	@Deprecated
	public InvocationInfo getHourlyInvocations(String ServiceClass,String ServiceName,String GHNName,String date, String hourInterval, String ... callerScope ) throws Exception,EmptyResultException{
		InvocationInfo info= new InvocationInfo();

		this.setQuery("SELECT SUM(invocationNumber) AS SUMINVOCATION, AVG(averageInvocationTime) AS AVERAGE FROM NODEINVOCATION " +
				"WHERE ServiceClass='"+ServiceClass+"' AND ServiceName='"+ServiceName+"' AND date='"+date+"' AND timeframe='"+hourInterval+"' " +
				"AND GHNName='"+GHNName+"'" +(callerScope.length>0?" AND callerScope='"+callerScope[0].toString()+"'":""));
		try {
			this.query();
			info.setInvocationCount(toJSON().getJSONObject(0).getDouble("SUMINVOCATION"));
			info.setAvgInvocationTime(toJSON().getJSONObject(0).getDouble("AVERAGE"));
		}catch (JSONException e){
			logger.error("Error executing query");
			throw new Exception(e);
		}
		catch (EmptyResultException e){
			logger.error("Error executing query");
			throw e;
		}
		return info;
	}
	/**
	 * getSumInvocations
	 * @param ServiceClass  ServiceClass
	 * @param ServiceName ServiceName
	 * @param GHNName ServiceName
	 * @param date date
	 * @param hourInterval String in the form (12-13) 
	 * @param callerScope callerScope optional 
	 * @return
	 * @throws Exception Exception
	 * @throws EmptyResultException Exception
	 */
	@Deprecated
	public Integer getSumInvocations(String ServiceClass,String ServiceName, String ... callerScope ) throws Exception,EmptyResultException{
		Integer info = null;

		this.setQuery("SELECT SUM(invocationNumber) AS SUMINVOCATION FROM NODEINVOCATION " +
				"WHERE ServiceClass='"+ServiceClass+"' AND ServiceName='"+ServiceName+
				"'" +(callerScope.length>0?" AND callerScope='"+callerScope[0].toString()+"'":""));
		try {
			this.query();
			info = toJSON().getJSONObject(0).getInt("SUMINVOCATION");

		}catch (JSONException e){
			logger.error("Error executing query");
			throw new Exception(e);
		}
		catch (EmptyResultException e){
			logger.error("Error executing query");
			throw e;
		}
		return info;
	}

	/**
	 * getAverageHourlyInvocations
	 * @param dateLimits dateLimits
	 * @param callerScope optional
	 * @return the average number invocation per hour
	 * @throws Exception
	 * @throws EmptyResultException
	 */
	@Deprecated
	public InvocationInfo getAverageHourlyInvocationInfo( String ServiceClass,String ServiceName,String []dateLimits, String ... callerScope) throws Exception,EmptyResultException{		
		InvocationInfo info= new InvocationInfo();

		this.setQuery("SELECT AVG(invocationNumber) AS AVGINV, AVG(averageInvocationTime) AS AVGTIME FROM NODEINVOCATION " +
				"WHERE ServiceClass='"+ServiceClass+"' AND ServiceName='"+ServiceName+
				"' AND date BETWEEN '"+dateLimits[0]+"' AND '"+dateLimits[1]+"'"
				+(callerScope.length>0?" AND callerScope='"+callerScope[0].toString()+"'":""));
		try {
			this.query();
			info.setInvocationCount(toJSON().getJSONObject(0).getDouble("AVGINV"));
			info.setAvgInvocationTime(toJSON().getJSONObject(0).getDouble("AVGTIME"));
		}catch (JSONException e){
			logger.error("Error executing query");
			throw new Exception(e);
		}
		catch (EmptyResultException e){
			logger.error("Error executing query");
			throw e;
		}
		return info;
	}
	/**
	 * getAverageHourlyInvocations
	 * @param dateLimits dateLimits
	 * @param callerScope optional
	 * @return the average number invocation per hour
	 * @throws Exception
	 * @throws EmptyResultException
	 */
	@Deprecated
	public Long getAverageHourlyInvocations( String ServiceClass,String ServiceName,String []dateLimits, String ... callerScope) throws Exception,EmptyResultException{		
		Long tmp = null;
		this.setQuery("SELECT AVG(invocationNumber) AS AVG FROM NODEINVOCATION " +
				"WHERE ServiceClass='"+ServiceClass+"' AND ServiceName='"+ServiceName+
				"' AND date BETWEEN '"+dateLimits[0]+"' AND '"+dateLimits[1]+"'"
				+(callerScope.length>0?" AND callerScope='"+callerScope[0].toString()+"'":""));
		try {
			this.query();
			tmp= toJSON().getJSONObject(0).getLong("AVG");
		}catch (JSONException e){
			logger.error("Error executing query");
			throw new Exception(e);
		}
		catch (EmptyResultException e){
			logger.error("Error executing query");
			throw e;
		}
		return tmp;
	}

	/**

	/**
	 * getDailyInvocations
	 * @param ServiceClass  ServiceClass
	 * @param ServiceName ServiceName
	 * @param GHNName GHNName
	 * @param date date string
	 * @param callerScope optional
	 * @return InvocationInfo object
	 * @throws Exception  Exception
	 * @throws EmptyResultException  Exception
	 */
	@Deprecated
	public InvocationInfo getDailyInvocations(String ServiceClass,String ServiceName,String GHNName,String date,String ... callerScope) throws Exception,EmptyResultException{

		InvocationInfo info= new InvocationInfo();

		this.setQuery("SELECT SUM(invocationNumber) AS SUMINVOCATION, AVG(averageInvocationTime) AS AVERAGE FROM NODEINVOCATION " +
				"WHERE ServiceClass='"+ServiceClass+"' AND ServiceName='"+ServiceName+"' AND date='"+date+"' " +
				"AND GHNName='"+GHNName+"'"+(callerScope.length>0?" AND callerScope='"+callerScope[0].toString()+"'":""));
		try {
			this.query();
			info.setInvocationCount(toJSON().getJSONObject(0).getDouble("SUMINVOCATION"));
			info.setAvgInvocationTime(toJSON().getJSONObject(0).getDouble("AVERAGE"));
		}catch (JSONException e){
			logger.error("Error executing query");
			throw new Exception(e);
		}
		catch (EmptyResultException e){
			logger.error("Error executing query");
			throw e;
		}
		return info;
	}

	/**
	 * getDailyInvocations
	 * @param ServiceClass  ServiceClass
	 * @param ServiceName ServiceName
	 * @param GHNName GHNName
	 * @param date date string
	 * @param callerScope optional
	 * @return InvocationInfo object
	 * @throws Exception  Exception
	 * @throws EmptyResultException  Exception
	 */
	@Deprecated
	public ArrayList<InvocationInfo> getDailyInvocationsGroupByIp(String ServiceClass,String ServiceName,String GHNName,String date,String ... callerScope) throws Exception,EmptyResultException{

		ArrayList<InvocationInfo> infos= new ArrayList<InvocationInfo>();

		this.setQuery("SELECT SUM(invocationNumber) AS SUMINVOCATION, AVG(averageInvocationTime) AS AVERAGE, callerIP FROM NODEINVOCATION " +
				"WHERE ServiceClass='"+ServiceClass+"' AND ServiceName='"+ServiceName+"' AND date='"+date+"' " +
				"AND GHNName='"+GHNName+"'"+(callerScope.length>0?" AND callerScope='"+callerScope[0].toString()+"'":"")+ " GROUP BY callerIP");
		try {
			this.query();
			JSONArray tmp = toJSON();
			for(int i= 0;i<tmp.length();i++){
				InvocationInfo info = new  InvocationInfo();
				info.setInvocationCount(tmp.getJSONObject(i).getDouble("SUMINVOCATION"));
				info.setAvgInvocationTime(tmp.getJSONObject(i).getDouble("AVERAGE"));
				info.setIP(tmp.getJSONObject(i).getString("callerIP"));
				infos.add(info);
			}
		}catch (JSONException e){
			logger.error("Error executing query");
			throw new Exception(e);
		}
		catch (EmptyResultException e){
			logger.error("Error executing query");
			throw e;
		}
		return infos;
	}

	public Map<String,ArrayList<String>> getServices() throws Exception {
		Map<String,ArrayList<String>> map = new HashMap<String, ArrayList<String>>();

		this.setQuery("SELECT DISTINCT ServiceClass, ServiceName  FROM NODEACCOUNTING ORDER BY ServiceClass");
		try {
			this.query();
			JSONArray tmp = toJSON();
			for(int i= 0;i<tmp.length();i++){
				String clazz=tmp.getJSONObject(i).getString("ServiceClass");
				ArrayList<String> list = null;
				if ((list = map.get(clazz)) == null)
					list = new ArrayList<String>();
				list.add(tmp.getJSONObject(i).getString("ServiceName"));
				map.put(clazz, list);
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

	public Map<String,ArrayList<String>> getGHNs()throws Exception {
		Map<String,ArrayList<String>> map = new HashMap<String, ArrayList<String>>();

		this.setQuery("SELECT DISTINCT GHNName  FROM NODEACCOUNTING ORDER BY GHNname");
		try {
			this.query();
			JSONArray tmp = toJSON();
			for(int i= 0;i<tmp.length();i++){
				String ghn=tmp.getJSONObject(i).getString("GHNName");
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
	
	public ArrayList<ArrayList<String>> getScopes()throws Exception {
		this.setQuery("SELECT DISTINCT callerScope FROM NODEACCOUNTING ORDER BY callerScope");
		this.query();
		return this.getResultsAsArray();

	}

	
	/**
	 * 
	 * @author Andrea Manzi(CERN)
	 *
	 */
	public class InvocationInfo{
		Double invocationCount;
		Double avgInvocationTime;
		String IP;

		/**
		 * get the caller IP
		 * @return the IP
		 */
		public String getIP() {
			return IP;
		}
		/**
		 * set the caller IP
		 * @param iP
		 */
		public void setIP(String iP) {
			IP = iP;
		}
		/**
		 * get the invocation count
		 * @return the invocation count
		 */
		public Double getInvocationCount() {
			return invocationCount;
		}
		/**
		 * set the invocationCount 
		 * @param invocationCount
		 */
		public void setInvocationCount(Double invocationCount) {
			this.invocationCount = invocationCount;
		}
		/**
		 * get the avg invocation time
		 * @return the avg invocation time
		 */
		public Double getAvgInvocationTime() {
			return avgInvocationTime;
		}
		/**
		 * set the avg invocatin time
		 * @param avgInvocationTime the avg invocation time
		 */
		public void setAvgInvocationTime(Double avgInvocationTime) {
			this.avgInvocationTime = avgInvocationTime;
		}

	}
}
