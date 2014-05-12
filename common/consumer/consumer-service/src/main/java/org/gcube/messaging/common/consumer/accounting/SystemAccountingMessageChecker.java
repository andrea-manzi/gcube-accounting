package org.gcube.messaging.common.consumer.accounting;

import org.gcube.common.core.scope.GCUBEScope;
import org.gcube.common.core.utils.logging.GCUBELog;
import org.gcube.messaging.common.consumer.MessageChecker;
import org.gcube.messaging.common.consumer.ServiceContext;
import org.gcube.messaging.common.messages.NodeAccountingMessage;
import org.gcube.messaging.common.messages.SystemAccountingMessage;
import org.gcube.messaging.common.messages.records.IntervalRecord;

/**
 * 
 * @author AndreaManzi(CERN)
 *
 */
public class SystemAccountingMessageChecker extends MessageChecker<SystemAccountingMessage>{

	/**
	 * logger
	 */
	public  static GCUBELog logger = new GCUBELog(SystemAccountingMessageChecker.class);
	
	
	public SystemAccountingMessageChecker (GCUBEScope scope ) {super(scope);}
	
	/**
	 * check and store the message
	 * @param message the message
	 * @throws Exception
	 */
	public  void check(SystemAccountingMessage msg) {
		
		//check the record to store
		try { 
			//just print the message
			logger.debug(msg.toString());
			ServiceContext.getContext().getAccountingSystemManager().storeSystemAccoutingInfo(msg);
			
		}catch (Exception e){
			logger.error("Error storing SystemAccounting Message",e);
		}
	}
		
}
