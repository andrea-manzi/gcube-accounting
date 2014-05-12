package org.gcube.messaging.common.consumer;

import org.gcube.common.core.contexts.GCUBEStatefulPortTypeContext;
import org.gcube.common.core.state.GCUBEWSHome;

/**
 * The resource home
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public class MessagingConsumerHome extends GCUBEWSHome{
		/**
		 * @return Port Type Context
		 */
		public GCUBEStatefulPortTypeContext getPortTypeContext() {
			return MessagingConsumerContext.getContext();
		}
}

