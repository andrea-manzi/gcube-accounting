package org.gcube.messaging.common.producer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.jms.JMSException;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.gcube.common.core.contexts.GHNContext;
import org.gcube.common.core.monitoring.GCUBETestProbe;
import org.gcube.common.core.monitoring.LocalMonitor;
import org.gcube.common.core.scope.GCUBEScope;
import org.gcube.common.messaging.endpoints.BrokerEndpoints;
import org.gcube.common.messaging.endpoints.ScheduledRetriever;
import org.gcube.common.scope.api.ScopeProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Implementation of a Local Monitor
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public class GCUBELocalProducer extends LocalMonitor{

	private static long refreshTime = 1800;
	private static long waitingTime = 60;
	
	/** Logger */
	private static Logger logger = LoggerFactory.getLogger(GCUBELocalProducer.class);


	private static HashMap<GCUBEScope,ScheduledRetriever> brokerMap = new HashMap<GCUBEScope, ScheduledRetriever>();

	/**ActiveMQ Connection factory**/
	private static HashMap<GCUBEScope,ArrayList<ActiveMQConnectionFactory>> connectionFactoryMap = null;
	
	private static HashMap<GCUBEScope,ArrayList<QueueConnection>> queueConnectionMap = null;

	static Properties resources = new Properties();
	public GCUBELocalProducer(){
		
		
		try {
			GCUBELocalProducer.addScope(GCUBEScope.getScope("/"+GHNContext.getContext().getGHN().getInfrastructure()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * {@inheritDoc} 
	 */
	@Override
	public void loadProbes(Map<String,Class<? extends GCUBETestProbe>> map) {

		try {
			Properties query= new Properties();
			query.load(GCUBELocalProducer.class.getResourceAsStream("/probes.properties"));

			for (Object prop : query.keySet()) {
				try {
					map.put((String) prop, (Class<? extends GCUBETestProbe>) Class.forName((String)query.get(prop)));
				} catch (Exception e){
					logger.error("Error loading probe class",e);
				}
			}
		}
		catch (Exception e) {
			logger.error("Error loading probes configuration",e);
		}
	}



	/**
	 * Get the QueueConnection list for the given scope
	 * @return the queue Connection list for the given scope
	 */
	public static ArrayList<QueueConnection> getQueueConnection(GCUBEScope scope){
		
		ArrayList<QueueConnection> connection = queueConnectionMap.get(scope);
		if( connection == null ) {
			logger.debug("CONNECTION MAP NULL");
		}
		return connection;
	}

	/**
	 * Add the Scope to the monitoredMap
	 * @return the scope to add
	 * @throws BrokerNotConfiguredInScopeException 
	 * @throws Exception 
	 */
	public synchronized static void addScope (GCUBEScope scope) throws BrokerNotConfiguredInScopeException, Exception{

		if (!(brokerMap.containsKey(scope)))
		{
			ScopeProvider.instance.set(scope.toString());

			ScheduledRetriever ret = BrokerEndpoints.getRetriever(waitingTime, refreshTime);

			if (ret.getFailoverEndpoint()!= null) 
			{

				brokerMap.put(scope, ret);
			}
			else throw new BrokerNotConfiguredInScopeException();

		}
	}

	/**
	 * retrun the Monitored scope
	 * @return The Set of Scope to monitor
	 */
	public synchronized static Set<GCUBEScope> getMonitoredScope () 
	{
		return brokerMap.keySet();
	}

	/**
	 * Remove Scope from BrokerMap
	 * @param scope the scope to remove
	 */
	public synchronized static void removeScope (GCUBEScope scope) 
	{
		brokerMap.remove(scope);
	}


	/**
	 * Reload the scope connection
	 * @param scope the scope to reload
	 */
	public synchronized static void reloadConnection(GCUBEScope scope) {

		//close current connections:
		stopConnections(scope);
		
		ArrayList<ActiveMQConnectionFactory> factoryList = new ArrayList<ActiveMQConnectionFactory>();

		ArrayList<QueueConnection> queueConnectionList = new ArrayList<QueueConnection>();

		logger.debug("Reload JMS connections");
		
		ScheduledRetriever ret= brokerMap.get(scope);
		
			try {
				logger.info("JMS failover endpoint: "+ret.getFailoverEndpoint()+" for scope: "+scope.toString());
				
				ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(ret.getFailoverEndpoint());
				factoryList.add(factory);
				QueueConnection queueCon = ((QueueConnectionFactory)factory).createQueueConnection();
				queueConnectionList.add(queueCon);
				queueCon.start();

			} catch (JMSException e1) {
				logger.error("Error creating Topic Connection",e1);
			} catch (Exception e) {
				logger.error("Error creating Topic Connection",e);
			}
		
		queueConnectionMap.put(scope,queueConnectionList );
		connectionFactoryMap.put(scope, factoryList);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void run() {

		connectionFactoryMap = new HashMap<GCUBEScope,ArrayList<ActiveMQConnectionFactory>>();

		queueConnectionMap = new HashMap<GCUBEScope, ArrayList<QueueConnection>>();

		
		for (GCUBEScope scope : brokerMap.keySet())	{
			logger.info("Broker Map, scope to monitor: "+scope.toString());
			reloadConnection(scope);
		}

		if (implementationMap==null) {
			implementationMap = Collections.synchronizedMap(new HashMap<String,Class<? extends GCUBETestProbe>>());
			loadProbes(implementationMap);
		}	

		for (Class<? extends GCUBETestProbe> classProbe :implementationMap.values())
		{
			try {
				GCUBETestProbe probe = classProbe.newInstance();
				probe.setInterval(interval);
				logger.info("Initializing " + classProbe + " Probe");
				probe.execute();
			} catch (Exception e) {
				logger.error("Error Initializing probe "+classProbe,e);	
			}
		}
	}	

	/**
	 * Check if the given scope belongs to start scopes
	 * @param scope the scope to check
	 * @return true/false
	 */
	public static boolean checkStartScope(GCUBEScope scope) {
		boolean isStartScope = false;
		for (GCUBEScope scopeStart:GHNContext.getContext().getStartScopes())
		{
			if (scope.equals(scopeStart) || scope.isInfrastructure())
				isStartScope = true;
		}
		return isStartScope;
	}


	private static void stopConnections(GCUBEScope scope){

		if (queueConnectionMap != null  && queueConnectionMap.get(scope) != null) {
			//close current connections:
			for (QueueConnection con :queueConnectionMap.get(scope)){
				try {
					con.stop();
					con.close();
				}
				catch (JMSException e ){
					logger.error("Error stopping queueConnections",e);
				}
			}
		}
	}

}

