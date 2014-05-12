package org.gcube.messaging.common.consumer.accounting;

import org.gcube.common.core.scope.GCUBEScope;
import org.gcube.common.core.utils.logging.GCUBELog;
import org.gcube.messaging.common.consumer.MessageChecker;
import org.gcube.messaging.common.consumer.ServiceContext;
import org.gcube.messaging.common.messages.NodeAccountingMessage;
import org.gcube.messaging.common.messages.records.IntervalRecord;

/**
 * 
 * @author AndreaManzi(CERN)
 *
 */
public class NodeAccountingMessageChecker extends MessageChecker<NodeAccountingMessage<IntervalRecord>>{

	/**
	 * logger
	 */
	public  static GCUBELog logger = new GCUBELog(NodeAccountingMessageChecker.class);
	
	
	public NodeAccountingMessageChecker (GCUBEScope scope ) {super(scope);}
	
	/**
	 * check and store the message
	 * @param message the message
	 * @throws Exception
	 */
	public  void check(NodeAccountingMessage<IntervalRecord> msg) {
		
		//check the record to store
		try {
			for (String ip : msg.getRecords().keySet()) 
				ServiceContext.getContext().getAccountingManager().storeNodeAccoutingInfo(msg.getRecords().get(ip),msg);
			
		}catch (Exception e){
			logger.error("Error storing NodeAccountingMessage",e);
		}
	}
		
}
