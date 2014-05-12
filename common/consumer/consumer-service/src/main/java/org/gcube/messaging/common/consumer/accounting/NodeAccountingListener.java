package org.gcube.messaging.common.consumer.accounting;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.gcube.common.core.scope.GCUBEScope;
import org.gcube.common.core.utils.logging.GCUBELog;
import org.gcube.messaging.common.consumer.ServiceContext;
import org.gcube.messaging.common.messages.NodeAccountingMessage;

/**
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public class NodeAccountingListener implements MessageListener {


	/**
	 * 
	 * @author Andrea Manzi(CERN)
	 *
	 */
	public  static GCUBELog logger = new GCUBELog(NodeAccountingListener.class);

	/**
	 * 
	 */
	
	private GCUBEScope scope = null;
	/**
	 * default constructor
	 */
	public NodeAccountingListener(){}
	
	/**
	 * create the nodeListener for the given scope
	 * @param scope
	 */
	public NodeAccountingListener (GCUBEScope scope){
		this.scope = scope;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void onMessage(Message message) {
		try {
			if (message instanceof ObjectMessage) {
				ObjectMessage msg = (ObjectMessage ) message;

				if (msg.getObject() instanceof NodeAccountingMessage)
					((NodeAccountingMessageChecker)ServiceContext.getContext().getMessageCheckerMap().get(scope).get(NodeAccountingMessage.class)).check((NodeAccountingMessage)msg.getObject());
			}
		}
		catch (Exception e) {
			logger.error(e);
			}
		}
}