package org.gcube.messaging.common.consumerlibrary.plugins;

import org.gcube.common.clients.fw.plugin.Plugin;
import org.gcube.messaging.common.consumerlibrary.impl.Constants;

	
/**
 * 
 * @author andrea
 *
 * @param <S>
 * @param <P>
 */
public abstract class AbstractPlugin<S,P> implements Plugin<S,P> {

	public final String name;
	
	public AbstractPlugin(String name) {
		this.name=name;
	}
	

	public String serviceClass() {
		return Constants.SERVICE_CLASS;
	}
	
	public String serviceName() {
		return Constants.SERVICE_NAME;
	}
	
	
	public String namespace() {
		return Constants.NAMESPACE;
	}
	
	public String name() {
		return Constants.PORT_TYPE_NAME;
	}
	
}

