package org.gcube.messaging.common.consumer.ri;

import org.gcube.common.core.scope.GCUBEScope;
import org.gcube.messaging.common.consumer.BrokerSubscription;
import org.gcube.messaging.common.messages.RIMessage;
import org.gcube.messaging.common.messages.Test;
import org.gcube.messaging.common.messages.util.Utils;

/**
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public class RISubscription extends BrokerSubscription<RIListener>{
	
	@Override
	public void setScope(GCUBEScope scope) {
		couple = new TopicCouple();
		couple.setScope(scope);
		String scopeString = scope.toString(); 
		scopeString = Utils.replaceUnderscore(scopeString);
		scopeString = scopeString.replaceAll("\\/", ".");
		scopeString = scopeString.substring(1);
		couple.setTopicName(scopeString+"."+RIMessage.RI+".*");
		
		listener = new RIListener(scope);
	}

}
