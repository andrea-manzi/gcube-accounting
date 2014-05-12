package org.gcube.messaging.common.consumer.ri;

import org.gcube.messaging.common.consumer.notifier.Notification;

/**
 * 
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public class RINotification extends Notification {
	
	String serviceName= null;
	String serviceClass = null;
	
	public RINotification () {}
	
	/**
	 * get the serviceName
	 * @return the serviceName
	 */
	public String getServiceName() {
		return serviceName;
	}
	/**
	 * set the ServiceName
	 * @param serviceName the seviceName
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	/**
	 * get the serviceclass
	 * @return serviceclass
	 */
	public String getServiceClass() {
		return serviceClass;
	}
	
	/**
	 * set teh serviceclass
	 * @param serviceClass
	 */
	public void setServiceClass(String serviceClass) {
		this.serviceClass = serviceClass;
	}

}
