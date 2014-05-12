package org.gcube.messaging.common.messages;

import java.util.ArrayList;

import org.gcube.common.scope.impl.ScopeBean;
import org.gcube.common.scope.impl.ScopeBean.Type;
import org.gcube.messaging.common.messages.records.BaseRecord;
import org.gcube.messaging.common.messages.util.Utils;

/**
 * 
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public class  PortalAccountingMessage<RECORD extends BaseRecord> extends GCUBEMessage{

	private String  vre;
	private String user;
	private String id;

	private ArrayList<RECORD> records =null;
	
	private static final long serialVersionUID = -5914599673844136388L;
	
	/**
	 * the topic base
	 */
	public static final String PortalAccounting="ACCOUNTING.PORTAL";
	
	/**
	 * create a new portalAccoutingMessage
	 */
	public PortalAccountingMessage(){
		records = new ArrayList<RECORD>();
	}

	/**
	 * Add a record to the message
	 * @param record the record
	 */
	public void addRecord(RECORD record){
		records.add(record);
	}
	
	/**
	 * get the user 
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * ser the user
	 * @param user the user
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * get the vre
	 * @return the vre
	 */
	public String getVre() {
		return vre;
	}

	/**
	 * set the vre
	 * @param vre the vre
	 */
	public void setVre(String vre) {
		this.vre = vre;
	}

	/**
	 * get the records
	 * @return the records
	 */
	public ArrayList<RECORD> getRecords() {
		return records;
	}

	/**
	 * set the records
	 * @param records
	 */
	public void setRecords(ArrayList<RECORD> records) {
		this.records = records;
	};
	
	/**
	 * the id
	 * @return id
	 */
	public String getId() {
		return id;
	}

	/**
	 * set the id for the message
	 * @param id the id
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * creates the topic name for this message
	 * @param scope the message scope
	 */
	public void createTopicName(String scope){
		ScopeBean bean = new ScopeBean(scope);
		if (bean.is(Type.INFRASTRUCTURE)){
			this.topic = Utils.replaceUnderscore(bean.name())+
			"."+PortalAccountingMessage.PortalAccounting +
			"."+Utils.replaceUnderscore(sourceGHN);
		}
		else if (bean.is(Type.VO))
		{
			this.topic = Utils.replaceUnderscore(Utils.getInfraScope(scope))+
			"."+Utils.replaceUnderscore(scope)+
			"."+PortalAccountingMessage.PortalAccounting +
			"."+Utils.replaceUnderscore(sourceGHN);
		}
	}

}
