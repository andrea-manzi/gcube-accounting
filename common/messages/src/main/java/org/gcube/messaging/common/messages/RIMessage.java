package org.gcube.messaging.common.messages;

import org.gcube.common.scope.impl.ScopeBean;
import org.gcube.common.scope.impl.ScopeBean.Type;
import org.gcube.messaging.common.messages.util.Utils;

/**
 * Model a RunningInstance Message
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public class RIMessage<TEST extends Test> extends GCUBEMessage{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * topic base
	 */
	public static final String RI="MONITORING.RI";

	public RIMessage(){};

	private TEST test;

	private String serviceName;
	private String serviceClass;

	/**
	 * get the serviceName
	 * @return the serviceName
	 */
	public String getServiceName() {
		return serviceName;
	}

	/**
	 * set the service name
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
	 * @param serviceClass the service class
	 */
	public void setServiceClass(String serviceClass) {
		this.serviceClass = serviceClass;
	}

	/**
	 * get the test Object
	 * @return  the test ojbect
	 */
	public TEST getTest() {
		return test;
	}

	/**
	 * set the test object
	 * @param test the test object
	 */
	public void setTest(TEST test) {
		this.test = test;
	}

	/**
	 * create a RIMessage for the given scope and ghn
	 * @param ghnName the ghn
	 * @param VO the scope
	 */
	public RIMessage(String ghnName, String VO){
		this.sourceGHN = ghnName;
		this.topic = createTopicName(ghnName, VO);
		this.scope= VO.toString();
	}


	/**
	 * to string
	 */
	public String toString() {
		return this.sourceGHN +"/"+this.serviceClass +"/"+this.serviceName +"/"+ this.time +"/" + this.test.toString()+"/"+this.topic+"/"+this.scope; 
	}

	private String createTopicName(String ghnName, String scope) {
		ScopeBean bean = new ScopeBean(scope);
		if (bean.is(Type.INFRASTRUCTURE)){
			return Utils.replaceUnderscore(scope)+
			"."+RIMessage.RI +
			"."+Utils.replaceUnderscore(ghnName);
		}
		else if (bean.is(Type.VO))
		{
			return Utils.replaceUnderscore(Utils.getInfraScope(scope))+
			"."+Utils.replaceUnderscore(scope)+
			"."+RIMessage.RI +
			"."+Utils.replaceUnderscore(ghnName);
		}
		else return null;

	}
}
