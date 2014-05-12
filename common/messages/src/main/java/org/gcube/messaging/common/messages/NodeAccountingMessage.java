package org.gcube.messaging.common.messages;

import java.util.HashMap;

import org.gcube.common.scope.impl.ScopeBean;
import org.gcube.common.scope.impl.ScopeBean.Type;
import org.gcube.messaging.common.messages.records.IntervalRecord;
import org.gcube.messaging.common.messages.util.Utils;

/**
 * 
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public class NodeAccountingMessage<RECORD extends IntervalRecord> extends GCUBEMessage{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * topic base
	 */
	public static final String nodeAccounting="ACCOUNTING.GHN";
	
	private String callScope;

	private String serviceName;
	
	private String serviceClass;
	
	private HashMap<String,RECORD> records = new  HashMap<String,RECORD>();
	

	
	/**
	 * get the service Name
	 * @return the service Name
	 */
	public String getServiceName() {
		return serviceName;
	}


	/**
	 * set the service Name
	 * @param serviceName
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}


	/**
	 * get the service class
	 * @return the service class
	 */
	public String getServiceClass() {
		return serviceClass;
	}

	/**
	 * set the service class
	 * @param serviceClass serviceClass
	 */
	public void setServiceClass(String serviceClass) {
		this.serviceClass = serviceClass;
	}

	/**
	 * create the topic name
	 * @param scope scope
	 */
	public void createTopicName(String scope){
		ScopeBean bean = new ScopeBean(scope);
		if (bean.is(Type.INFRASTRUCTURE)){
			this.topic = Utils.replaceUnderscore(bean.name())+
			"."+NodeAccountingMessage.nodeAccounting +
			"."+Utils.replaceUnderscore(sourceGHN);
		}
		else if (bean.is(Type.VO))
		{
			this.topic = Utils.replaceUnderscore(Utils.getInfraScope(scope))+
			"."+Utils.replaceUnderscore(bean.name())+
			"."+NodeAccountingMessage.nodeAccounting +
			"."+Utils.replaceUnderscore(sourceGHN);
		}
	}


	/**
	 * get the caller scope
	 * @return caller scope
	 */
	public String getCallScope() {
		return callScope;
	}


	/**
	 * set the caller scope
	 * @param callScope callScope
	 */
	public void setCallScope(String callScope) {
		this.callScope = callScope;
	}
	
	
	
	
	/**
	 * get the RECORD map 
	 * @return the RECORD map
	 */
	public HashMap<String, RECORD> getRecords() {
		return records;
	}

	/**
	 * set the RECORD map
	 * @param records the records
	 */
	public void setRecords(HashMap<String, RECORD> records) {
		this.records = records;
	}
	/**
	 * Get a record given the IP key 
	 * @param timeSlot IP key
	 * @return the record associated to the timeSlot
	 */
	public RECORD getRecord(String key){
		return this.records.get(key);
	}
	
	/**
	 * set the record for the given IP
	 * @param key the IP
	 * @param record the record
	 */
	public void setRecord(String key,RECORD record){
		this.records.put(key, record);
		
	}
}
