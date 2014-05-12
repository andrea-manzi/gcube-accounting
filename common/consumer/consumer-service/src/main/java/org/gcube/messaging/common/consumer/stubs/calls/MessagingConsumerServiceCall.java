package org.gcube.messaging.common.consumer.stubs.calls;

import java.util.ArrayList;
import java.util.List;

import org.apache.axis.message.addressing.EndpointReferenceType;
import org.gcube.common.core.contexts.GCUBERemotePortTypeContext;
import org.gcube.common.core.contexts.GHNContext;
import org.gcube.common.core.informationsystem.client.AtomicCondition;
import org.gcube.common.core.informationsystem.client.ISClient;
import org.gcube.common.core.informationsystem.client.queries.GCUBERIQuery;
import org.gcube.common.core.resources.GCUBERunningInstance;
import org.gcube.common.core.scope.GCUBEScope;
import org.gcube.common.core.security.GCUBESecurityManager;
import org.gcube.common.core.types.VOID;
import org.gcube.common.core.utils.calls.RICall;
import org.gcube.messaging.common.consumer.stubs.MessagingConsumerPortType;
import org.gcube.messaging.common.consumer.stubs.SendReport;
import org.gcube.messaging.common.consumer.stubs.service.MessagingConsumerServiceAddressingLocator;

/**
 * 
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public class MessagingConsumerServiceCall extends RICall implements MessagingConsumerServiceCallInterface {

	
	private static String accounting="PORTAL_ACCOUNTING";
	private static String system_accounting="SYSTEM_ACCOUNTING";
	private static String monitoring="GHN_MONITORING";
	private static int callTimeout=240000;
	/**
	 * 
	 * @param scope the scope
	 * @param securityManager the security manager
	 * @throws Exception
	 */
	public MessagingConsumerServiceCall(GCUBEScope scope, GCUBESecurityManager[] securityManager)
			throws Exception {
		super(scope, securityManager);
	}

	@Override
	protected String getServiceClass() {
		return "Messaging";
	}

	@Override
	protected String getServiceName() {
		return "Consumer";
	}

	

	protected List<EndpointReferenceType> findPortType(GCUBERIQuery query,String portType,String subscriptiontype) throws Exception {
		ISClient client = GHNContext.getImplementation(ISClient.class);
		List<EndpointReferenceType> eprs = new ArrayList<EndpointReferenceType>();
		for (GCUBERunningInstance instance : client.execute(query, getScopeManager().getScope())) 
			if (instance.getAccessPoint().getEndpoint(portType)!=null && instance.getSpecificData() !=null 
					&& instance.getSpecificData().contains(subscriptiontype) )
				eprs.add(instance.getAccessPoint().getEndpoint(portType));
		return eprs;
	}
	
	@Override
	protected GCUBERIQuery getRIQuery() throws Exception {
		GCUBERIQuery q = GHNContext.getImplementation(ISClient.class).getQuery(GCUBERIQuery.class);
		q.addGenericCondition("//SpecificData");
		q.addAtomicConditions(
				new AtomicCondition("//ServiceName",getServiceName()),
				new AtomicCondition("//ServiceClass",getServiceClass()));
		
		return q;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String queryAccountingDB(final  String query) throws Exception {
		
		final ResultHolder<String> result = new ResultHolder<String>();
		new RICallHandler() {
			public void interact(EndpointReferenceType epr) throws Exception {
				MessagingConsumerPortType PT= new MessagingConsumerServiceAddressingLocator().getMessagingConsumerPortTypePort(epr);
				PT=GCUBERemotePortTypeContext.getProxy(PT, getScopeManager(),callTimeout);
				result.value = PT.queryAccountingDB(query);
			}
			
			protected List<EndpointReferenceType> findInstances() throws Exception {
				return findPortType(getRIQuery(),getPortTypeName(),accounting);
			}
			
		}.run();
		return result.value;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public String querySystemAccountingDB(final  String query) throws Exception {
		
		final ResultHolder<String> result = new ResultHolder<String>();
		new RICallHandler() {
			public void interact(EndpointReferenceType epr) throws Exception {
				MessagingConsumerPortType PT= new MessagingConsumerServiceAddressingLocator().getMessagingConsumerPortTypePort(epr);
				PT=GCUBERemotePortTypeContext.getProxy(PT, getScopeManager(), callTimeout);
				result.value = PT.querySystemAccountingDB(query);
				}
			
			protected List<EndpointReferenceType> findInstances() throws Exception {
				return findPortType(getRIQuery(),getPortTypeName(),system_accounting);
			}
			
		}.run();
		return result.value;
	}
	
	
	
	/**
	 * {@inheritDoc}
	 */
	public String queryMonitoringDB(final String query) throws Exception {
		final ResultHolder<String> result = new ResultHolder<String>();
		new RICallHandler() {
			public void interact(EndpointReferenceType epr) throws Exception {
				MessagingConsumerPortType PT= new MessagingConsumerServiceAddressingLocator().getMessagingConsumerPortTypePort(epr);
				PT=GCUBERemotePortTypeContext.getProxy(PT, getScopeManager(), callTimeout);
				result.value = PT.queryMonitoringDB(query);
			}
			
			protected List<EndpointReferenceType> findInstances() throws Exception {
				return findPortType(getRIQuery(),getPortTypeName(),monitoring);
			}
		}.run();
		return result.value;
	}
	/**
	 * {@inheritDoc}
	 */
	public void sendReport(String date, GCUBEScope scope) throws Exception {
		final SendReport request = new SendReport(date,scope.toString());
		new RICallHandler() {
				public void interact(EndpointReferenceType epr) throws Exception {
					MessagingConsumerPortType PT= new MessagingConsumerServiceAddressingLocator().getMessagingConsumerPortTypePort(epr);
					PT=GCUBERemotePortTypeContext.getProxy(PT, getScopeManager(), callTimeout);
					PT.sendReport(request);
			}
		}.run();
		
	}

	@Override
	public String getPortTypeName() {
		return "gcube/messaging/common/consumer/MessagingConsumer";
	}

	/**
	 * {@inheritDoc}
	 */
	public void backupAccountingDB() throws Exception {
		new RICallHandler() {
			public void interact(EndpointReferenceType epr) throws Exception {
				MessagingConsumerPortType PT= new MessagingConsumerServiceAddressingLocator().getMessagingConsumerPortTypePort(epr);
				PT=GCUBERemotePortTypeContext.getProxy(PT, getScopeManager(), callTimeout);
				PT.backupAccountingDB(new VOID());
			}
		}.run();
		
	}

	/**
	 * {@inheritDoc}
	 */
	public void backupMonitoringDB() throws Exception {
		new RICallHandler() {
			public void interact(EndpointReferenceType epr) throws Exception {
				MessagingConsumerPortType PT= new MessagingConsumerServiceAddressingLocator().getMessagingConsumerPortTypePort(epr);
				PT=GCUBERemotePortTypeContext.getProxy(PT, getScopeManager(), callTimeout);
				PT.backupMonitoringDB(new VOID());
			}
		}.run();
		
	}

}
