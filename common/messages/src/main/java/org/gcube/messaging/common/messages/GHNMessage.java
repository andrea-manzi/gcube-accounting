package org.gcube.messaging.common.messages;

import org.gcube.common.scope.impl.ScopeBean;
import org.gcube.common.scope.impl.ScopeBean.Type;
import org.gcube.messaging.common.messages.util.Utils;



/**
 * Models a GHN Message
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public class GHNMessage<TEST extends Test> extends GCUBEMessage{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * topic base
	 */
	public static final String GHN="MONITORING.GHN";

	public GHNMessage(){};

	private TEST test;

	/**
	 * get the test contained into the message
	 * @return the test
	 */
	public TEST getTest() {
		return test;
	}

	/**
	 * set the test object for this message
	 * @param test the test
	 */
	public void setTest(TEST test) {
		this.test = test;
	}


	/**
	 * build a new GHNMessage 
	 * @param ghnName the ghnname
	 * @param VO the vo
	 */
	public GHNMessage(String ghnName, String VO){
		this.sourceGHN = ghnName;
		this.topic = createTopicName(ghnName, VO);
		this.scope= VO.toString();
	}


	/**
	 * String representation
	 */
	public String toString() {
		return this.sourceGHN +"/"+ this.time +"/" + this.test.toString()+"/"+this.topic+"/"+this.scope; 
	}

	private String createTopicName(String ghnName, String scope) {
		ScopeBean bean = new ScopeBean(scope);
		if (bean.is(Type.INFRASTRUCTURE)){
			return Utils.replaceUnderscore(scope)+
			"."+GHNMessage.GHN+
			"."+Utils.replaceUnderscore(ghnName);
		}
		else if (bean.is(Type.VO))
		{
			//scope has to be the infrastructure
			return Utils.replaceUnderscore(Utils.getInfraScope(scope))+
			"."+Utils.replaceUnderscore(bean.name())+
			"."+GHNMessage.GHN+
			"."+Utils.replaceUnderscore(ghnName);
		}
		else return null;

	}
}
