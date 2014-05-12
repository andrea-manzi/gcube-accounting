package org.gcube.messaging.common.messages;

import java.lang.reflect.Field;
import java.util.TreeMap;

import org.gcube.common.scope.impl.ScopeBean;
import org.gcube.common.scope.impl.ScopeBean.Type;
import org.gcube.messaging.common.messages.util.Utils;


/**
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public class SystemAccountingMessage extends GCUBEMessage{
	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	private String serviceClass;
	private String serviceName;
	private String messageType;
	
	private TreeMap<String,MessageField> fieldMap = new TreeMap<String,MessageField>();
	
	
	/**
	 * queue base
	 */
	public static final String systemAccounting="ACCOUNTING.SYSTEM";
	
	
	public String getServiceClass() {
		return serviceClass;
	}

	public void setServiceClass(String serviceClass) {
		this.serviceClass = serviceClass;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public  TreeMap<String,MessageField> getFieldMap() {
		return fieldMap;
	}

	public void setFieldMap(TreeMap<String,MessageField> fieldMap) {
		this.fieldMap = fieldMap;
	}


	public synchronized void addField(MessageField field){
		this.fieldMap.put(field.getName(),field);
	}
	
	/**
	 * creates the topic name for this message
	 * @param scope the message scope
	 */
	public void createTopicName(String scope){
		ScopeBean bean = new ScopeBean(scope);
		if (bean.is(Type.INFRASTRUCTURE)){
			this.topic = Utils.replaceUnderscore(bean.name())+
			"."+SystemAccountingMessage.systemAccounting +
			"."+Utils.replaceUnderscore(sourceGHN);
		}
		else if (bean.is(Type.VO))
		{
			this.topic = Utils.replaceUnderscore(Utils.getInfraScope(scope))+
			"."+Utils.replaceUnderscore(scope)+
			"."+SystemAccountingMessage.systemAccounting +
			"."+Utils.replaceUnderscore(sourceGHN);
		}
	}
	
	@Override
	public String toString(){
		StringBuilder builder = new StringBuilder();
	
		builder.append(this.messageType+"\t");
		builder.append(this.scope+"\t");
		builder.append(this.serviceClass+"\t");
		builder.append(this.serviceName+"\t");
		builder.append(this.sourceGHN+"\t");	
		builder.append(this.time+"\t");
		builder.append(this.topic+"\t");
		
		for (MessageField field : fieldMap.values()){
			builder.append(field.getName()+"\t");
			builder.append(field.getValue()+"\t");
			builder.append(field.getSqlType()+"\t");
		}
		return builder.toString();
		
	}
	
	/**
	 * 
	 * @throws ReservedFieldException
	 */
	public void checkReservedField() throws ReservedFieldException{
		
		Field [] fields = this.getClass().getDeclaredFields();
		Field [] fields2 =  GCUBEMessage.class.getDeclaredFields();
		
		for (Field f : fields){
			if (fieldMap.containsKey(f.getName()))
			throw new ReservedFieldException("The parameters name "+ f.getName()+" is already used by the application"); 
		}
		for (Field f : fields2){
			if (fieldMap.containsKey(f.getName()))
				throw new ReservedFieldException("The parameters name "+ f.getName()+" is already used by the application");
		}
		if (fieldMap.containsKey("id"))
				throw new ReservedFieldException("The parameters name id is already used by the application");
		
	}
	
	public class ReservedFieldException extends Exception {
		
		public ReservedFieldException(String string) {
			super (string);
		}

		/**
		 * 
		 */
		private static final long serialVersionUID = 2107643850291448530L;
	};
	
}


