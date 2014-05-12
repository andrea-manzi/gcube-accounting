package org.gcube.messaging.common.consumer;

import org.gcube.common.core.contexts.GCUBEServiceContext;
import org.gcube.common.core.contexts.GCUBEStatefulPortTypeContext;
import org.gcube.messaging.common.consumer.ServiceContext;

/**
 * The consumer Context 
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public class MessagingConsumerContext extends GCUBEStatefulPortTypeContext{
	/**
	 * PORT TYPE NAME
	 */ 
	private static final String PORTTYPE_NAME = "gcube/messaging/common/consumer/MessagingConsumer";

	/**
	 * NAMESPACE
	 */ 
	private static final String NAMESPACE ="http://gcube-system.org/namespaces/messaging/common/consumer";

	
	private MessagingConsumerContext(){}

	/**
	 * Singleton object
	 */
	protected static final MessagingConsumerContext singleton = new MessagingConsumerContext();

	/**
	 * @return the context
	 */
	public static MessagingConsumerContext getContext() {
		return singleton;
	}

	/**
	 * @return PORT TYPE NAME
	 */
	public String getJNDIName() {
		return PORTTYPE_NAME;
	}

	/**
	 * @inheritDoc
	 * @return Namespace
	 */
	@Override
	public String getNamespace() {
		return NAMESPACE;
	}

	/**
	 * @inheritDoc
	 * @return Service Context
	 */
	@Override
	public GCUBEServiceContext getServiceContext() {
		return ServiceContext.getContext();
	}

}
