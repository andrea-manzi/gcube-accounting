package org.gcube.messaging.common.consumerlibrary.proxies;



import org.gcube.common.clients.fw.builders.StatelessBuilder;
import org.gcube.common.clients.fw.builders.StatelessBuilderImpl;
import org.gcube.common.clients.fw.queries.StatelessQuery;
import org.gcube.messaging.common.consumerlibrary.ConsumerCL;
import org.gcube.messaging.common.consumerlibrary.fws.MessagingServiceJAXWSStubs;
import org.gcube.messaging.common.consumerlibrary.plugins.ConsumerServicePlugin;

public class Proxies {
	
	private static final ConsumerServicePlugin plugin = new ConsumerServicePlugin();
	 
	public static StatelessBuilder<ConsumerCL> consumerService() {
	    return new StatelessBuilderImpl<MessagingServiceJAXWSStubs,ConsumerCL>(plugin);
	}
}
