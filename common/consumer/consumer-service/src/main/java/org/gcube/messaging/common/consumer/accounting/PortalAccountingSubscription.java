package org.gcube.messaging.common.consumer.accounting;

import org.gcube.common.core.scope.GCUBEScope;
import org.gcube.messaging.common.consumer.BrokerSubscription;
import org.gcube.messaging.common.messages.PortalAccountingMessage;
import org.gcube.messaging.common.messages.Test;
import org.gcube.messaging.common.messages.util.Utils;

/**
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public class PortalAccountingSubscription extends BrokerSubscription<PortalAccountingListener>{
	
	@Override
	public void setScope(GCUBEScope scope) {
		couple = new TopicCouple();
		couple.setScope(scope);
		String scopeString = scope.toString();
		scopeString = Utils.replaceUnderscore(scopeString);
		scopeString = scopeString.replaceAll("\\/", ".");
		scopeString = scopeString.substring(1);
		couple.setTopicName(scopeString+"."+PortalAccountingMessage.PortalAccounting+".*");
		listener = new PortalAccountingListener(scope);
		queue = true;
	}

}
