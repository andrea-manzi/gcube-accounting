package org.gcube.messaging.common.consumer.accounting;

import org.gcube.common.core.scope.GCUBEScope;
import org.gcube.common.core.utils.logging.GCUBELog;
import org.gcube.messaging.common.consumer.MessageChecker;
import org.gcube.messaging.common.consumer.ServiceContext;
import org.gcube.messaging.common.messages.PortalAccountingMessage;
import org.gcube.messaging.common.messages.records.BaseRecord;


/**
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public class PortalAccountingMessageChecker extends MessageChecker<PortalAccountingMessage<?>>{
	/**
	 * logger
	 */
	public  static GCUBELog logger = new GCUBELog(PortalAccountingMessageChecker.class);
	
	public PortalAccountingMessageChecker(GCUBEScope scope) {super(scope);}
	
	/**
	 *{@inheritDoc}
	 */
	public void check(PortalAccountingMessage<?> msg) {
		for ( BaseRecord record :msg.getRecords()){
			try {
				ServiceContext.getContext().getAccountingManager().storePortalMessage(msg,record);
			}
			catch (Exception e){logger.error("Error Storing PortalAccountMessage");}
		}
	}
		
}

