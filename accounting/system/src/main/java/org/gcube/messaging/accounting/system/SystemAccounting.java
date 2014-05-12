package org.gcube.messaging.accounting.system;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.axis.message.addressing.EndpointReferenceType;
import org.gcube.common.core.contexts.GCUBEServiceContext;
import org.gcube.common.core.contexts.GHNContext;
import org.gcube.common.core.scope.GCUBEScope;
import org.gcube.common.core.scope.GCUBEScopeNotSupportedException;
import org.gcube.messaging.common.messages.GCUBEMessage;
import org.gcube.messaging.common.messages.MessageField;
import org.gcube.messaging.common.messages.SystemAccountingMessage;
import org.gcube.messaging.common.messages.SystemAccountingMessage.ReservedFieldException;
import org.gcube.messaging.common.messages.util.SQLType;
import org.gcube.messaging.common.producer.ActiveMQClient;
import org.gcube.messaging.common.producer.GCUBELocalProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The class implements the System Accounting 
 * 
 * System accounting give the possibility to account custom information 
 * 
 * There is  a restrictions on the type of the Parameters to send
 * 
 *     	- java.lang.String
    	- java.math.BigDecimal
    	- java.lang.Boolean	
    	- java.lang.Integer
    	- java.lang.Long
    	- java.lang.Float
    	- java.lang.Double
    	- byte[]
    	- java.sql.Date
    	- java.sql.Time
    	- java.sql.Timestamp
    	- java.sql.Clob
    	- java.sql.Blob
    	- java.sql.Array	
    	- java.sql.Struct   		
    	- java.sql.Ref
    		
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public class SystemAccounting {
	
	/** Logger */
	private static Logger logger = LoggerFactory.getLogger(SystemAccounting.class);

	

	protected SystemAccounting() throws IOException, GCUBEScopeNotSupportedException{
		
		if (isClientMode())
			this.startLocalProducer(GCUBEScope.getScope("/"+ GHNContext.getContext()
					.getProperty(GHNContext.INFRASTRUCTURE_NAME, true)));
	}
	
	/**
	 * The method create SystemAccountingMessages and send them to the D4science MessageBroker configured in the Infrastructure.
	 *
	 * This method is intended to be executed by Service running on a GHN container.
	 * 
	 * The information about GCUBEScope, ServiceClass and ServiceName are automatically retrieved from the 
	 * ServiceContext. 
	 * 
	 * By default a message for each service scope is generated, otherwise is it possible to specify the GCUBEScope as optional parameter. 
	 * 
	 * 
	 * @param context the Service Context 
	 * @param type The type of message
	 * @param parameters the map of parameters to send
	 * @param scope, if the service is running in more than one scope and we want to specify the caller scope, is it possible to use this optional parameters
	 * @throws GHNClientModeException the Exception is thrown when the method is invoked from a client
	 * @throws ReservedFieldException if one or more parameters clash the ones already reserved by the system
	 * @throws IllegalArgumentException if one or more parameters type are not supported by the system
	 * @throws Exception 
	 */
	public void sendSystemAccountingMessage(GCUBEServiceContext context,String type,HashMap<String,Object> parameters, 
			GCUBEScope ...scope) 	throws GHNClientModeException , ReservedFieldException ,IllegalArgumentException, Exception {
		
		if (isClientMode()){
			throw new GHNClientModeException("Container is not running, this method is not intented to be used in GHN client mode");
		}
		
		
		SystemAccountingMessage message = new SystemAccountingMessage();
		
		//adding callerscope to parameters;

		try {
			message.setMessageType(type);
			message.setServiceClass(context.getServiceClass());
			message.setServiceName(context.getName());
			message.setSourceGHN(GHNContext.getContext().getHostnameAndPort());
			message.setTimeNow();
			
			
			for (String key :parameters.keySet()){
				MessageField field = new MessageField();
				field.setName(key);
				field.setValue(parameters.get(key));
				field.setSqlType(SQLType.getSQLType(parameters.get(key)));	
				message.addField(field);
			}
			
			if (scope.length > 0) {
				parameters.put("callerScope", scope[0].toString());
				message.createTopicName(scope[0].getInfrastructure().toString());
				message.setScope(scope[0].getInfrastructure().toString());	
				logger.debug(message.toString());
				message.checkReservedField();
				this.sendMessage(message);
				
			}else {//a message  for each scope is sent
				
				for (GCUBEScope sco : context.getInstance().getScopes().values()){
					parameters.put("callerScope", sco.toString());
					message.createTopicName(sco.getInfrastructure().toString());
					message.setScope(sco.getInfrastructure().toString());	
					logger.debug(message.toString());
					message.checkReservedField();
					this.sendMessage(message);
				}
			}
				
		}catch (Exception e){
			logger.error("Error Sending System accounting message",e);
			e.printStackTrace();
			throw e;
		}

		
	}
	/**
	 * 
	 * The method creates a SystemAccoutingMessage and sends it to the D4science MessageBroker configured in the given scope 
	 * 
	 * This method is intended to be executed by clients using client GHN libraries
	 *   
	 * @param type The type of the message
	 * @param scope the GCUBEScope where the message will be published
	 * @param sourceGHN the GHN Source of the message
	 * @param parameters the map of parameters to send
	 * @throws GCUBEScopeNotSupportedException the scope is not supported in the current GHN
	 * @throws ReservedFieldException if one or more parameters clash the ones already reserved by the system
	 * @throws IllegalArgumentException if one or more parameters type are not supported by the system
	 * @throws Exception 
	 */
	public void sendSystemAccountingMessage(String type,GCUBEScope scope,String sourceGHN, 
			HashMap<String,Object> parameters) 	throws  GCUBEScopeNotSupportedException, ReservedFieldException,IllegalArgumentException, Exception {
		
		SystemAccountingMessage message = new SystemAccountingMessage();
		
		message.setMessageType(type);
		message.setScope(scope.toString());
		message.setSourceGHN(sourceGHN);
		message.setTimeNow();
		message.createTopicName(scope.toString());
		
		for (String key :parameters.keySet()){
			MessageField field = new MessageField();
			field.setName(key);
			field.setValue(parameters.get(key));
			field.setSqlType(SQLType.getSQLType(parameters.get(key)));	
			message.addField(field);
		}
		
		logger.debug(message.toString());
		
		message.checkReservedField();
		this.sendMessage(message);
	}
	
	
	//The library should understand if the message has to be sent from within a running container or not
	private boolean isClientMode(){
		return GHNContext.getContext().isClientMode();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	private void sendMessage(GCUBEMessage message) {
		ActiveMQClient.getSingleton().sendMessageToQueue(message);
	}
	
	
	private void startLocalProducer(GCUBEScope scope) throws GCUBEScopeNotSupportedException{
		GCUBELocalProducer local = new GCUBELocalProducer();
		long interval = 1200;
		
		ArrayList<EndpointReferenceType> eprs = new ArrayList<EndpointReferenceType>();
		
		if (scope.getServiceMap().getEndpoints(GHNContext.MSGBROKER)!= null) 
		{
			
			for (EndpointReferenceType msgBrokerEpr:scope.getServiceMap().getEndpoints(GHNContext.MSGBROKER)) {
				eprs.add(msgBrokerEpr);
			}
		}
		
		HashMap<GCUBEScope,ArrayList<EndpointReferenceType>> monitoredScopes = new HashMap<GCUBEScope,ArrayList<EndpointReferenceType>>();
		monitoredScopes.put(scope,eprs);
		
		local.setBrokerMap(monitoredScopes);
		local.setInterval(interval);
		local.run();
		
	}

	
	class GHNClientModeException extends Exception {

		public GHNClientModeException(String string) {
			super (string);
		}

		/**
		 * 
		 */
		private static final long serialVersionUID = 2107643850291448530L;
	};
}
