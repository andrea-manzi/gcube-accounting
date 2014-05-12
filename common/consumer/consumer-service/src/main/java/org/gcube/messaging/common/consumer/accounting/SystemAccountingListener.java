package org.gcube.messaging.common.consumer.accounting;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.gcube.common.core.scope.GCUBEScope;
import org.gcube.common.core.utils.logging.GCUBELog;
import org.gcube.messaging.common.consumer.ServiceContext;
import org.gcube.messaging.common.messages.SystemAccountingMessage;

/**
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public class SystemAccountingListener implements MessageListener {


	/**
	 * 
	 * @author Andrea Manzi(CERN)
	 *
	 */
	public  static GCUBELog logger = new GCUBELog(SystemAccountingListener.class);

	/**
	 * 
	 */
	
	private GCUBEScope scope = null;
	/**
	 * default constructor
	 */
	public SystemAccountingListener(){}
	
	/**
	 * create the nodeListener for the given scope
	 * @param scope
	 */
	public SystemAccountingListener (GCUBEScope scope){
		this.scope = scope;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void onMessage(Message message) {
		try {
			if (message instanceof ObjectMessage) {
				ObjectMessage msg = (ObjectMessage ) message;

				if (msg.getObject() instanceof SystemAccountingMessage)
					((SystemAccountingMessageChecker)ServiceContext.getContext().getMessageCheckerMap().get(scope).get(SystemAccountingMessage.class)).check((SystemAccountingMessage)msg.getObject());
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			}
		}
}