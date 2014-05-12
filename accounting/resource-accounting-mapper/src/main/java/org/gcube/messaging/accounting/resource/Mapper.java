package org.gcube.messaging.accounting.resource;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.gcube.accounting.datamodel.RawUsageRecord;
import org.gcube.accounting.exception.InvalidValueException;
import org.gcube.accounting.messaging.ResourceAccounting;
import org.gcube.accounting.messaging.ResourceAccountingFactory;
import org.gcube.common.scope.api.ScopeProvider;
import org.gcube.messaging.common.consumerlibrary.ConsumerCL;
import org.gcube.messaging.common.consumerlibrary.json.JSONArray;
import org.gcube.messaging.common.consumerlibrary.json.JSONException;
import org.gcube.messaging.common.consumerlibrary.proxies.Proxies;
import org.gcube.messaging.common.consumerlibrary.query.NodeAccountingQuery;
import org.gcube.messaging.common.consumerlibrary.query.QueryNotSetException;

/**
 * 
 * @author andrea
 * the class queries for Node Accounting data and use the common accounting lib to publish the resource accounting info
 */
public class Mapper {

	static ConsumerCL library = null;
	static ResourceAccounting raFactory= null;

	public  static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public	 static SimpleDateFormat format= new SimpleDateFormat(DATE_FORMAT);

	public Mapper() {

		ScopeProvider.instance.set("/d4science.research-infrastructures.eu");
		library = Proxies.consumerService().withTimeout(1, TimeUnit.MINUTES).build();
		//library = Proxies.consumerService().at("pcd4science3.cern.ch",8080).withTimeout(1, TimeUnit.MINUTES).build();

		raFactory = null;
		try {
			raFactory = ResourceAccountingFactory.getResourceAccountingInstance();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}

	}	
	public List<RawUsageRecord> retrieveAccountingRecords() throws QueryNotSetException, Exception{

		NodeAccountingQuery query = library.getQuery(NodeAccountingQuery.class,library);

		query.setQuery("SELECT * FROM NODEACCOUNTING");
		query.setLimitClause(new Integer(1));
		query.query();
		System.out.println(query.toJSON());
		return createRURecord(query.toJSON());	
	}

	public List<RawUsageRecord> createRURecord(JSONArray array ) throws JSONException, InvalidValueException, ParseException{

		List<RawUsageRecord> list = new ArrayList<RawUsageRecord>();


		for (int i = 0;i < array.length();i++){

			RawUsageRecord ur = new RawUsageRecord();
			
		

			//generic properties
			ur.setResourceType("service");	
			ur.setConsumerId("N/A");
			ur.setResourceOwner("N/A");
			ur.setResourceScope("/gcube");

			Calendar createTime = new GregorianCalendar();

			ur.setCreateTime(createTime.getTime());

			ur.setStartTime(format.parse(array.getJSONObject(i).getString("startDate")));
			ur.setEndTime(format.parse(array.getJSONObject(i).getString("endDate")));


			ur.setResourceSpecificProperty("serviceClass",array.getJSONObject(i).getString("ServiceClass"));
			ur.setResourceSpecificProperty("serviceName",array.getJSONObject(i).getString("ServiceName"));
			ur.setResourceSpecificProperty("callerIP",array.getJSONObject(i).getString("callerIP"));	
			ur.setResourceSpecificProperty("refHost",array.getJSONObject(i).getString("GHNName"));	
			ur.setResourceSpecificProperty("invocationCount",array.getJSONObject(i).getString("invocationNumber"));		
			ur.setResourceSpecificProperty("averageInvocationTime",array.getJSONObject(i).getString("averageInvocationTime"));		
			ur.setResourceSpecificProperty("callerScope", array.getJSONObject(0).getString("callerScope"));
			
			list.add(ur);

		}
		return list;

	}

	public static void main (String [] args){
		Mapper mapper = new Mapper();

		List<RawUsageRecord> list  = null;
		try {
			list = mapper.retrieveAccountingRecords();
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (RawUsageRecord record : list)
			raFactory.sendAccountingMessage(record);
		
	}
}
