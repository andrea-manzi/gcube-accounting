package org.gcube.messaging.common.producer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import javax.jms.JMSException;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
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
public class JMSLocalProducer {

	private static long refreshTime = 1800;
	private static long waitingTime = 60;
	
	/** Logger */
	private static Logger logger = LoggerFactory.getLogger(JMSLocalProducer.class);
	

	private static HashMap<String,ScheduledRetriever> brokerMap = new HashMap<String, ScheduledRetriever>();

	/**ActiveMQ Connection factory**/
	private static HashMap<String,ArrayList<ActiveMQConnectionFactory>> connectionFactoryMap = null;
	
	private static HashMap<String,ArrayList<QueueConnection>> queueConnectionMap = null;

	public JMSLocalProducer(String scope){
			try {
			JMSLocalProducer.addScope(scope);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get the QueueConnection list for the given scope
	 * @return the queue Connection list for the given scope
	 */
	public static ArrayList<QueueConnection> getQueueConnection(String scope){
		
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
	public synchronized static void addScope (String scope) throws BrokerNotConfiguredInScopeException, Exception{

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
	 * Return the Monitored scope
	 * @return The Set of Scope to monitor
	 */
	public synchronized static Set<String> getMonitoredScope () 
	{
		return brokerMap.keySet();
	}

	/**
	 * Remove Scope from BrokerMap
	 * @param scope the scope to remove
	 */
	public synchronized static void removeScope (String scope) 
	{
		brokerMap.remove(scope);
	}


	/**
	 * Reload the scope connection
	 * @param scope the scope to reload
	 */
	public synchronized static void reloadConnection(String scope) {

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

	public void run() {

		connectionFactoryMap = new HashMap<String,ArrayList<ActiveMQConnectionFactory>>();
		queueConnectionMap = new HashMap<String, ArrayList<QueueConnection>>();

		
		for (String scope : brokerMap.keySet())	{
			logger.info("Broker Map, scope to monitor: "+scope.toString());
			reloadConnection(scope);
		}
	}	
	


	public static void stopConnections(String scope){

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

