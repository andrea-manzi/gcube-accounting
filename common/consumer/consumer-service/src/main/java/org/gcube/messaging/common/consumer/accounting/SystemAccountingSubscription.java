package org.gcube.messaging.common.consumer.accounting;

import org.gcube.common.core.scope.GCUBEScope;
import org.gcube.messaging.common.consumer.BrokerSubscription;
import org.gcube.messaging.common.messages.SystemAccountingMessage;
import org.gcube.messaging.common.messages.Test;
import org.gcube.messaging.common.messages.util.Utils;

/**
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public class SystemAccountingSubscription extends BrokerSubscription<SystemAccountingListener>{
	
	@Override
	public void setScope(GCUBEScope scope) {
		couple = new TopicCouple();
		couple.setScope(scope);
		String scopeString = scope.toString(); 
		scopeString = Utils.replaceUnderscore(scopeString);
		scopeString = scopeString.replaceAll("\\/", ".");
		scopeString = scopeString.substring(1);
		couple.setTopicName(scopeString+"."+SystemAccountingMessage.systemAccounting+".*");
		listener = new SystemAccountingListener(scope);
		queue = true;
	}

}