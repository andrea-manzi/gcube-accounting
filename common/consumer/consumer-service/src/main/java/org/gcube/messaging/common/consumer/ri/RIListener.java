package org.gcube.messaging.common.consumer.ri;

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
public class RIListener implements MessageListener{
	
	public  static GCUBELog logger = new GCUBELog(RIListener.class);

	private GCUBEScope scope = null;
	
	public RIListener(){}
	
	public RIListener (GCUBEScope scope){
		this.scope = scope;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void onMessage(Message message) {
		try {
			if (message instanceof ObjectMessage) {
				ObjectMessage msg = (ObjectMessage ) message;

				if (msg.getObject() instanceof RIMessage<?>){
					((RIMessageChecker)ServiceContext.getContext().getMessageCheckerMap().get(scope).get(RIMessage.class)).check((RIMessage)msg.getObject());
				}
			} 
		}
		catch (Exception e) {
			logger.error(e);
		}
	}
}