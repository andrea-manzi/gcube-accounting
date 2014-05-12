package org.gcube.messaging.common.consumerlibrary.plugins;

import static org.gcube.messaging.common.consumerlibrary.impl.Constants.*;

import javax.xml.ws.EndpointReference;

import static org.gcube.common.clients.stubs.jaxws.StubFactory.*;

import org.gcube.common.clients.config.ProxyConfig;
import org.gcube.common.clients.delegates.ProxyDelegate;
import org.gcube.messaging.common.consumerlibrary.ConsumerCL;
import org.gcube.messaging.common.consumerlibrary.fws.MessagingServiceJAXWSStubs;
import org.gcube.messaging.common.consumerlibrary.impl.Constants;

/**
 * 
 * @author andrea
 *
 */
public class ConsumerServicePlugin extends AbstractPlugin<MessagingServiceJAXWSStubs,ConsumerCL> {

	public ConsumerServicePlugin() {
		super(Constants.PORT_TYPE_NAME);
	}
	
	
	
	public MessagingServiceJAXWSStubs resolve(EndpointReference address,ProxyConfig<?,?> config) throws Exception {
		return stubFor(consumer).at(address);
	}
	
	public ConsumerCL newProxy(ProxyDelegate<MessagingServiceJAXWSStubs> delegate) {
		return new ConsumerCL(delegate);
	}

	public Exception convert(Exception fault, ProxyConfig<?, ?> arg1) {
			return fault;
	}
}