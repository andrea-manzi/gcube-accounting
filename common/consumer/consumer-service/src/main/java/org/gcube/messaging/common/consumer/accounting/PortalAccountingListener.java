package org.gcube.messaging.common.consumer.accounting;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.gcube.common.core.scope.GCUBEScope;
import org.gcube.common.core.utils.logging.GCUBELog;
import org.gcube.messaging.common.consumer.ServiceContext;
import org.gcube.messaging.common.messages.PortalAccountingMessage;

/**
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public class PortalAccountingListener implements MessageListener {

	public  static GCUBELog logger = new GCUBELog(PortalAccountingListener.class);
	
	private GCUBEScope scope = null;
	
	/**
	 * Default constructor
	 */
	public PortalAccountingListener(){}
	
	/**
	 * construct the Listener for the given scope
	 * @param scope
	 */
	public PortalAccountingListener (GCUBEScope scope){
		this.scope = scope;
	}
	/**
	 * {@inheritDoc}
	 */
	public void onMessage(Message message) {
		try {
			if (message instanceof ObjectMessage) {
				ObjectMessage msg = (ObjectMessage ) message;
				if (msg.getObject() instanceof PortalAccountingMessage<?>)
					((PortalAccountingMessageChecker)ServiceContext.getContext().getMessageCheckerMap().get(scope).get(PortalAccountingMessage.class)).check((PortalAccountingMessage)msg.getObject());
			} 
		}
		catch (Exception e) {
			logger.error(e);
		}
	}
}

