package org.gcube.messaging.common.consumer.mail;

import org.gcube.common.core.contexts.GHNContext;
import org.gcube.common.core.informationsystem.client.AtomicCondition;
import org.gcube.common.core.informationsystem.client.ISClient;
import org.gcube.common.core.informationsystem.client.queries.GCUBEGenericResourceQuery;
import org.gcube.common.core.resources.GCUBEGenericResource;
import org.gcube.common.core.resources.GCUBEResource;
import org.gcube.common.core.scope.GCUBEScope;
import org.gcube.common.core.utils.handlers.GCUBEHandler;
import org.gcube.common.core.utils.handlers.lifetime.State;
import org.gcube.common.core.utils.logging.GCUBELog;

/**
 * The Mail Recipients resource
 * 
 * @author Andrea Manzi
 *
 */
public class MailRecipientResource extends GCUBEHandler<MailRecipientHelper>{
	
	GCUBEGenericResource profile = null;
	GCUBEScope scope = null;
	ISClient client = null;
	GCUBEGenericResourceQuery query = null;
	
	private static String monitoringConfiguration = "MONITORING_CONFIGURATION"; 
	
	/** Object logger */
	protected final GCUBELog logger=new GCUBELog(MailRecipientResource.class);
	
	
	
	/**
	 *  The constructor
	 * @param scope the scope
	 */
	public MailRecipientResource(GCUBEScope scope) throws Exception{
		this.scope = scope;
		client = GHNContext.getImplementation(ISClient.class);
		query =  client.getQuery(GCUBEGenericResourceQuery.class);
		query.addAtomicConditions(new AtomicCondition("/Resource/Profile/SecondaryType",monitoringConfiguration));
		
	}
	
	/**
	 * 
	 */
	@Override
	public void run()throws Exception
	{
		setState(State.Running.INSTANCE);
		logger.debug("Parsing IS generic Resource");
		String profile =get();
		if (profile != null)
			this.getHandled().parse(get());
		setState(State.Done.INSTANCE);
	}
	
	/**
	 *  Get the resource Profile body
	 * @return The String Representation of the profile body
	 * @throws Exception  Exception
	 */
	public String get() throws Exception{
		try {
			this.profile = client.execute(query,scope ).get(0);
		} catch (Exception e){
			logger.debug("Error getting IS generic Resource");
			return null;
		}
		return this.profile.getBody();
	}
	
	/**
	 * The Resource profile
	 * @return the resource profile
	 */
	public  GCUBEResource getResource() {
		return this.getResource();
	}
	
}
