package org.gcube.messaging.common.consumer;

import org.gcube.common.core.scope.GCUBEScope;
import org.gcube.messaging.common.messages.GCUBEMessage;

/**
 *  
 * @author Andrea Manzi(CERN) 
 *
 * @param <MESSAGE>
 */
public abstract class MessageChecker<MESSAGE extends GCUBEMessage>   {

	protected GCUBEScope scope = null;
	
	/**
	 * create a messageChecker for the given scope
	 * @param scope the scope
	 */
	public MessageChecker(GCUBEScope scope ){
		this.scope = scope;
	}
	
	/**
	 * Check the incoming message
	 * @param msg the message
	 * @throws Exception exception
	 */
	public abstract void check(MESSAGE msg) throws Exception ;
}
