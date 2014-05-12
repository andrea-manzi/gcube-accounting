package org.gcube.messaging.common.consumer.ghn;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.gcube.common.core.scope.GCUBEScope;
import org.gcube.common.core.utils.logging.GCUBELog;
import org.gcube.messaging.common.consumer.ServiceContext;
import org.gcube.messaging.common.messages.*;

/**
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public class GHNListener implements MessageListener{
	
	private  static GCUBELog logger = new GCUBELog(GHNListener.class);

	
	private GCUBEScope scope = null;
	
	/**
	 * constructor
	 */
	public GHNListener(){}
	
	/**
	 * constructor with scope
	 * @param scope
	 */
	public GHNListener (GCUBEScope scope){
		this.scope = scope;
	}
	/**
	 * {@inheritDoc}
	 */
	public void onMessage(Message message) {
		try {
			if (message instanceof ObjectMessage) {
				ObjectMessage msg = (ObjectMessage ) message;
				if (msg.getObject() instanceof GHNMessage<?>)
					((GHNMessageChecker)ServiceContext.getContext().getMessageCheckerMap().get(scope).get(GHNMessage.class)).check((GHNMessage)msg.getObject());
			} 
		}
		catch (Exception e) {
			logger.error(e);
		}
	}
}