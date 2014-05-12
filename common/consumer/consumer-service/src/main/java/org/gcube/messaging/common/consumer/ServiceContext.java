package org.gcube.messaging.common.consumer;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.jms.JMSException;
import javax.jms.TopicConnection;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.activemq.broker.BrokerService;
import org.apache.xerces.dom.DocumentImpl;
import org.gcube.common.core.contexts.GCUBEServiceContext;
import org.gcube.common.core.contexts.GHNContext;
import org.gcube.common.core.contexts.GCUBEServiceContext.Status;
import org.gcube.common.core.resources.GCUBEResource;
import org.gcube.common.core.resources.GCUBEResource.ResourceConsumer;
import org.gcube.common.core.resources.GCUBEResource.ResourceTopic;
import org.gcube.common.core.scope.GCUBEScope;
import org.gcube.common.core.scope.GCUBEScopeNotSupportedException;
import org.gcube.common.core.utils.logging.GCUBELog;
import org.gcube.messaging.accounting.system.SystemAccounting;
import org.gcube.messaging.accounting.system.SystemAccountingFactory;
import org.gcube.messaging.common.consumer.accounting.NodeAccountingListener;
import org.gcube.messaging.common.consumer.accounting.NodeAccountingMessageChecker;
import org.gcube.messaging.common.consumer.accounting.PortalAccountingMessageChecker;
import org.gcube.messaging.common.consumer.accounting.SystemAccountingMessageChecker;
import org.gcube.messaging.common.consumer.db.AccountingDBManager;
import org.gcube.messaging.common.consumer.db.AccountingSystemDBManager;
import org.gcube.messaging.common.consumer.db.MonitoringDBManager;
import org.gcube.messaging.common.consumer.ghn.GHNMessageChecker;
import org.gcube.messaging.common.consumer.mail.MailClient;
import org.gcube.messaging.common.consumer.mail.MailTemplateParser;
import org.gcube.messaging.common.consumer.notifier.Notifier;
import org.gcube.messaging.common.consumer.ri.RIMessageChecker;
import org.gcube.messaging.common.consumer.webserver.WebServer;
import org.gcube.messaging.common.messages.GCUBEMessage;
import org.gcube.messaging.common.messages.GHNMessage;
import org.gcube.messaging.common.messages.NodeAccountingMessage;
import org.gcube.messaging.common.messages.PortalAccountingMessage;
import org.gcube.messaging.common.messages.RIMessage;
import org.gcube.messaging.common.messages.SystemAccountingMessage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 * The service context
 * @author Andrea Manzi(CERN)
 *
 */
public class ServiceContext extends GCUBEServiceContext {

	GCUBELog logger = new GCUBELog(ServiceContext.class);

	static Properties resources = new Properties();

	private  MonitoringDBManager monitoringManager = null;
	private  AccountingDBManager accountingManager = null;
	private  AccountingSystemDBManager accountingSystemManager = null;

	private  Notifier notifier = null;
	private  MailClient mailClient = null;
	private  MailTemplateParser mailTemplateParser = null;
	private  Boolean notifyByMail = false;
	private  Boolean useEmbeddedBroker= false;
	private  Boolean useEmbeddedDB= false;
	private  String dbuser= "";
	private  String dbpass= "";
	private  String dbhost="";
	private  Long dbport=null;
	private  Long maxDBConnections=null;

	private  Long ednpointWaitingTime=null;
	private  Long endpointRefreshTime=null;



	private  WebServer server = null;
	private  HashMap<GCUBEScope,HashMap<Class<? extends  GCUBEMessage>,MessageChecker<?>>> messageCheckerMap = null;

	private  BrokerService broker = null;

	private ArrayList<String> messagesubscriptionsToload;

	private Map<String,Class<? extends BrokerSubscription<?>>> messagesubscriptions;

	private static ArrayList<BrokerSubscription<?>> subscriptionsList;


	private File httpServerBasePath;
	private int httpServerPort;

	protected static ServiceContext cache = new ServiceContext();

	/** Creates an instance. */
	private ServiceContext(){}
	/**
	 * Returns the single context instance.
	 * @return the instance.
	 */
	public static ServiceContext getContext() {return cache;}

	/**
	 * get the notifier
	 * @return the notifier
	 */
	public  Notifier getNotifier() {
		return cache.notifier;
	}

	/**
	 * set the notifier
	 * @param notifier the notifier
	 */
	public  void setNotifier(Notifier notifier) {
		cache.notifier = notifier;
	}

	/**
	 * get the MonitoringDB manager
	 * @return the MonitoringDB manager
	 */
	public  MonitoringDBManager getMonitoringManager() {
		return cache.monitoringManager;
	}

	/**
	 * set the MonitoringDB manager
	 * @param manager the DB manager
	 */
	public  void setMonitoringManager(MonitoringDBManager manager) {
		cache.monitoringManager = manager;
	}

	/**
	 * get the accountingManager manager
	 * @return the accountingManager manager
	 */
	public  AccountingDBManager getAccountingManager() {
		return cache.accountingManager;
	}

	/**
	 * set the accountingManager manager
	 * @param manager the accountingManager manager
	 */
	public  void setAccountingManager(AccountingDBManager manager) {
		cache.accountingManager = manager;
	}
	/** 
	 * get the Accounting System Manager
	 * @return the Accounting System Manager
	 */

	public AccountingSystemDBManager getAccountingSystemManager() {
		return accountingSystemManager;
	}

	/**
	 * Set the Accounting System Manager
	 * @param accountingSystemManager manager
	 */
	public void setAccountingSystemManager(
			AccountingSystemDBManager accountingSystemManager) {
		this.accountingSystemManager = accountingSystemManager;
	}

	
	public Long getEdnpointWaitingTime() {
		return ednpointWaitingTime;
	}
	public void setEdnpointWaitingTime(Long ednpointWaitingTime) {
		this.ednpointWaitingTime = ednpointWaitingTime;
	}
	public Long getEndpointRefreshTime() {
		return endpointRefreshTime;
	}
	public void setEndpointRefreshTime(Long endpointRefreshTime) {
		this.endpointRefreshTime = endpointRefreshTime;
	}

	private void createAndRegisterTopicName(GCUBEScope Scope) {

		for (String subscription: messagesubscriptionsToload)
		{
			BrokerSubscription <?>sub = null;
			try {
				sub = messagesubscriptions.get(subscription).newInstance();
				sub.setScope(Scope);
				sub.subscribe();
			} catch (Exception e) {
				logger.fatal("Unable to Start Subscription",e);
				continue;
			}
			subscriptionsList.add(sub);

		}

		//update RIProfile with SUBSCRIPTIONS handled
		this.getInstance().setSpecificData(generateSpecificDataXML(messagesubscriptionsToload));
		this.setStatus(Status.UPDATED);


	}

	private String generateSpecificDataXML(ArrayList<String> messagesubscriptions )  {
		StringWriter xml = new StringWriter();
		try {
			Document xmlReport= new DocumentImpl();
			Element rootReport = xmlReport.createElement("Subscriptions");
			for (String sub: messagesubscriptions){
				Element e = null;
				e = xmlReport.createElementNS(null, "Subscription");
				e.appendChild(xmlReport.createTextNode(sub));
				rootReport.appendChild(e);
			}
			xmlReport.appendChild(rootReport);

			Result stream = new StreamResult(xml);
			Source source = new DOMSource(xmlReport);
			Transformer xformer = TransformerFactory.newInstance().newTransformer();
			xformer.setOutputProperty(OutputKeys.INDENT, "yes");
			xformer.transform(source, stream);
			logger.debug("RISpecific data generated: "+ xml.toString());
		}catch (Exception e){
			e.printStackTrace();
			logger.error("Unable to generate RI Specific data information", e);
		}
		return xml.toString();

	}



	private  void getInput() throws RuntimeException, Exception{

		messagesubscriptions = new HashMap<String, Class<? extends BrokerSubscription<?>>>();

		messagesubscriptionsToload = new ArrayList<String>();

		//getting subscription mapping file

		File subscriptionMappingFile = new File(GHNContext.getContext().getLocation() + File.separator +
				(String)ServiceContext.getContext().getProperty(Constants.CONFIGDIR_JNDI_NAME)+ File.separator +
				(String)ServiceContext.getContext().getProperty(Constants.SUBSCRIPTIONSMAPPING_JNDI_NAME, true));

		try {
			resources.load(new FileReader(subscriptionMappingFile));


			for (Object prop : resources.keySet()) {
				logger.debug("reading key: "+(String) prop);
				messagesubscriptions.put((String) prop, (Class<? extends BrokerSubscription<?>>) Class.forName((String)resources.get(prop)));
			}
		} catch (IOException e) {	
			logger.error(e);
		} catch (ClassNotFoundException e) {
			logger.error(e);
		}

		httpServerBasePath =new File(getPersistenceRoot().getAbsolutePath() +File.separator+  (String) getProperty("httpServerBasePath", true));
		logger.debug("HTTP Server Base path = " + httpServerBasePath.getAbsolutePath());
		if(!httpServerBasePath.exists())
			httpServerBasePath.mkdirs();
		httpServerPort = Integer.parseInt((String)this.getProperty("httpServerPort",true));
		logger.debug("HTTP Server port = " + httpServerPort);

		try {
			String[] tokens= ((String)getProperty(Constants.MESSAGESSUBSCRIPTIONS_JNDI_NAME,true)).split(",");
			for(int i = 0;i < tokens.length;i++)
			{
				logger.debug("Found Subscription: "+tokens[i]);
				messagesubscriptionsToload.add(tokens[i].trim());
			}
		}catch (RuntimeException e) {
			logger.debug("No Subscription Configuration found to use");
		} 
		setNotifyByMail((Boolean)getProperty(Constants.NOTIFYBYMAIL_JNDI_NAME));
		setUseEmbeddedBroker((Boolean)getProperty(Constants.USEEMBEDDEDBROKER_JNDI_NAME));
		setDbpass((String)getProperty(Constants.DBPASS_JNDI_NAME));
		setDbuser((String)getProperty(Constants.DBUSER_JNDI_NAME));
		setDbhost((String)getProperty(Constants.DBHOST_JNDI_NAME));
		setDbport((Long)getProperty(Constants.DBPORT_JNDI_NAME));
		setMaxDBConnections((Long)getProperty(Constants.MAXDBCONNECTIONS_JNDI_NAME));
		setUseEmbeddedDB((Boolean)getProperty(Constants.USEEMBEDDEDDB_JNDI_NAME));
		setEdnpointWaitingTime((Long)getProperty(Constants.MESSAGING_ENDPOINT_WAITING_TIME));
		setEndpointRefreshTime((Long)getProperty(Constants.MESSAGING_ENDPOINT_REFRESH_TIME));



		//loading mail templates
		setMailTemplateParser(
				new MailTemplateParser(
						GHNContext.getContext().getLocation() + File.separator + (String)ServiceContext.getContext().getProperty(Constants.CONFIGDIR_JNDI_NAME)+ 
						File.separator +
						(String)ServiceContext.getContext().getProperty(Constants.MAIL_TEMPLATE_FILE_JNDI_NAME, true)));
		//parsing the mail templates
		this.getMailTemplateParser().parseMailTemplates();
	}

	/**
	 * get the notify by mail parameter value
	 * @return notify by mail parameter value
	 */
	public Boolean getNotifyByMail() {
		return notifyByMail;
	}
	/**
	 * set the notify by mail parameter value
	 * @param notifyByMail get the notify by mail parameter value
	 */
	public void setNotifyByMail(Boolean notifyByMail) {
		this.notifyByMail = notifyByMail;
	}
	/** {@inheritDoc} */
	public String getJNDIName() {return Constants.JNDI_NAME;}


	private void startWebServer() {

		try {

			String webServerClass = (String) getProperty("webServerClass", true);
			Class cls = Class.forName(webServerClass);
			cache.server = (WebServer) cls.newInstance();
			cache.server.initDefaults(httpServerBasePath.getAbsolutePath(),httpServerPort);
			cache.server.startServer();
		} catch (Exception e) {
			logger.fatal("Unable to Start Web Server",e);
			return;
		}

	}

	/**
	 *  
	 *   * @author  Andrea Manzi(CERN)
	 *
	 */
	private class RIResourceConsumer extends ResourceConsumer{
		/**
		 * {@inheritDoc}
		 */
		protected void onAddScope(GCUBEResource.AddScopeEvent event) {

			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			for (GCUBEScope scope: event.getPayload())
			{
				try {
					if (scope.getServiceMap() !=null && 
							scope.getServiceMap().getEndpoints(GHNContext.MSGBROKER) != null) {
						getMailClient().addScope(scope);
						//creates MessageChecker for the scope
						messageCheckerMap.put(scope, createMessageCheckerMap(scope));
						createAndRegisterTopicName(scope);
					}
				}catch (GCUBEScopeNotSupportedException e) {
					logger.error("Error adding Mail configuration for the given scope: " +scope,e);
				}catch (Exception e) {
					logger.error("Error adding Mail configuration for the given scope: " +scope,e);
				}
			}
		}
	}

	private HashMap<Class<? extends GCUBEMessage>,MessageChecker<?>> createMessageCheckerMap(GCUBEScope scope){
		HashMap<Class<? extends GCUBEMessage>, MessageChecker<?>> map = new HashMap<Class<? extends GCUBEMessage>,MessageChecker<?>>();
		map.put(GHNMessage.class, new GHNMessageChecker(scope));
		map.put(RIMessage.class, new RIMessageChecker(scope));
		map.put(NodeAccountingMessage.class, new NodeAccountingMessageChecker(scope));
		map.put(PortalAccountingMessage.class, new PortalAccountingMessageChecker(scope));
		map.put(SystemAccountingMessage.class, new SystemAccountingMessageChecker(scope));
		return map;
	}

	/**
	 * get the Message checker map
	 * @return the Message checker map
	 */
	public HashMap<GCUBEScope, HashMap<Class<? extends GCUBEMessage>, MessageChecker<?>>> getMessageCheckerMap() {
		return messageCheckerMap;
	}

	/**
	 * set the message checker map
	 * @param messageCheckerMap
	 */
	public void setMessageCheckerMap(
			HashMap<GCUBEScope, HashMap<Class<? extends GCUBEMessage>, MessageChecker<?>>> messageCheckerMap) {
		this.messageCheckerMap = messageCheckerMap;
	}

	private boolean createCustomSubscribers() 

	{	
		//checking customTopics to subscribe for
		String brokerEpr = null;
		String[] customTopics = null;

		try {
			customTopics = ((String)getProperty("CustomTopics",true)).split(",");	 
		}catch (RuntimeException e) {
			logger.debug("No Custom topics available");
			return false ;
		}
		try {
			brokerEpr = (String)getProperty("CustomBrokerURL",true);
		}catch (RuntimeException e) {
			logger.debug("No Custom Broker URL to use");
			return false ;
		}

		for (int idx=0; idx<customTopics.length;idx++)
		{

			logger.debug("Trying to Subscribe to Custom topic: " + customTopics[idx]);
			CustomSubscription subscription = new CustomSubscription();
			try {
				subscription.setupCustomDurableSubscriber(customTopics[idx],brokerEpr,new org.gcube.messaging.common.consumer.ri.RIListener());
				subscriptionsList.add(subscription);
			} catch (Exception e) {
				logger.error("Error creating Durable Subscriber",e);
			}

			CustomSubscription subscription2 = new CustomSubscription();
			try {
				subscription2.setupCustomDurableSubscriber(customTopics[idx],brokerEpr,new org.gcube.messaging.common.consumer.ghn.GHNListener());
				subscriptionsList.add(subscription2);
			} catch (Exception e) {
				logger.error("Error creating Durable Subscriber",e);
			}
			CustomSubscription subscription3 = new CustomSubscription();
			try {
				subscription3.setupCustomDurableSubscriber(customTopics[idx],brokerEpr,new org.gcube.messaging.common.consumer.accounting.PortalAccountingListener());
				subscriptionsList.add(subscription3);
			} catch (Exception e) {
				logger.error("Error creating Durable Subscriber",e);
			}
			CustomSubscription subscription4 = new CustomSubscription();
			try {
				subscription4.setupCustomDurableSubscriber(customTopics[idx],brokerEpr,new NodeAccountingListener());
				subscriptionsList.add(subscription4);
			} catch (Exception e) {
				logger.error("Error creating Durable Subscriber",e);
			}
		}

		//in this situation we have to also add mail configuration for each starts scope
		for (GCUBEScope scope: getStartScopes())
			try {
				getMailClient().addScope(scope);
			} catch (Exception e) {
				logger.error("Error adding Mail configuration for scope: " + scope,e);
			}

		return true;
	}

	@Override 
	protected void onInitialisation() throws Exception {

		subscriptionsList = new ArrayList<BrokerSubscription<?>>();

		messageCheckerMap = new HashMap<GCUBEScope,HashMap<Class<? extends GCUBEMessage>,MessageChecker<?>>>();

		getInput();

		if (getUseEmbeddedBroker()){
			try {
				startEmbeddedBroker();
			} catch (RuntimeException e) {
				logger.debug(e);
			} catch (Exception e) {
				logger.debug(e);
			}
		}
		//open DBs
		setAccountingManager(new AccountingDBManager());
		getAccountingManager().open();
		setMonitoringManager(new MonitoringDBManager());
		getMonitoringManager().open();
		setAccountingSystemManager(new AccountingSystemDBManager());
		getAccountingSystemManager().open();

		setNotifier(new Notifier(getNotifyByMail().booleanValue()));
		Thread t = new Thread(getNotifier());
		t.start();
		MailClient client = new MailClient();
		client.initialize();
		setMailClient( client);

		startWebServer();

		if (createCustomSubscribers()) {}
		else getInstance().subscribeResourceEvents(new RIResourceConsumer(), ResourceTopic.ADDSCOPE);

	}


	@Override 
	protected void onReady() throws Exception {
		SystemAccounting acc = null;
		try {
			acc = SystemAccountingFactory.getSystemAccountingInstance(); 
			String type = "MessagingConsumerType";
			HashMap<String,Object> parameters = new HashMap<String,Object>();
			parameters.put("restarted","true");
			parameters.put("timestamp", new Timestamp(System.currentTimeMillis()));
			acc.sendSystemAccountingMessage(this, type, parameters);
		}catch (Exception e){
			logger.error("Error sending system accounting message",e);
		}
	}


	@Override 
	protected void onShutdown()  throws Exception { 
		try {
			stopDurableSubscriber();
			getAccountingManager().close();
			getMonitoringManager().close();
			if (broker != null)
				broker.stop();
		}catch (Exception e){
			logger.error("ERROR shutting down the service",e);
		}
	}
	/**
	 * get the mail client
	 * @return the mail client
	 */
	public MailClient getMailClient() {
		return cache.mailClient;
	}
	/**
	 * set the mail client
	 * @param mailClient the mail client
	 */
	public void setMailClient(MailClient mailClient) {
		cache.mailClient = mailClient;
	}

	/**
	 * get the embedded broker parameter
	 * @return the broker parameter
	 */
	public Boolean getUseEmbeddedBroker() {
		return cache.useEmbeddedBroker;
	}
	/**
	 * set the use embedded broker
	 * @param useEmbeddedBroker use embedded broker
	 */
	public void setUseEmbeddedBroker(Boolean useEmbeddedBroker) {
		cache.useEmbeddedBroker = useEmbeddedBroker;
	}

	private void startEmbeddedBroker() throws RuntimeException, Exception{
		//		This message broker is embedded, it can be used to forward messages
		broker = new BrokerService();
		broker.setPersistent(true);
		broker.setUseJmx(false);
		broker.addConnector("tcp://localhost:61616");
		broker.start();
		logger.debug("Starting Embedded Broker");
	}
	/**
	 * get the Web server 
	 * @return the Web server
	 */
	public WebServer getServer() {
		return server;
	}
	/**
	 * ser the Web Server
	 * @param server the wewb server
	 */
	public void setServer(WebServer server) {
		this.server = server;
	}
	/**
	 * ger web server port
	 * @return the web server port
	 */
	public int getHttpServerPort() {
		return httpServerPort;
	}
	/**
	 * set teh web server port
	 * @param httpServerPort the web server port
	 */
	public void setHttpServerPort(int httpServerPort) {
		this.httpServerPort = httpServerPort;
	}

	/**
	 * Close Subscription to broker
	 * 
	 * @throws JMSException
	 */
	public void stopDurableSubscriber() {
		for (BrokerSubscription <?>sub : subscriptionsList) 
			for (Object connection: sub.getConnections()) {
				try {
					((TopicConnection)connection).close();
				} catch (JMSException e) 
				{logger.error("Error closing the connection to the broker",e);				}
			}

	}

	/**
	 * get the list of subscriptions
	 * @return list of subscriptions
	 */
	public static ArrayList<BrokerSubscription<?>> getSubscriptionsList() {
		return subscriptionsList;
	}

	/**
	 * set the array of broker subscriptions
	 * @param subscriptionsList the subscription list
	 */
	public static void setSubscriptionsList(
			ArrayList<BrokerSubscription<?>> subscriptionsList) {
		ServiceContext.subscriptionsList = subscriptionsList;
	}

	/**
	 * get the value of property UseEmbeddedDB
	 * @return the value of the property
	 */
	public Boolean getUseEmbeddedDB() {
		return useEmbeddedDB;
	}

	/**
	 * set the value of property UseEmbeddedDB
	 * @param useEmbeddedDB the value to set
	 */
	public void setUseEmbeddedDB(Boolean useEmbeddedDB) {
		this.useEmbeddedDB = useEmbeddedDB;
	}

	/**
	 * Set the value of the property DB user
	 * @return the dbUSer value
	 */
	public String getDbuser() {
		return dbuser;
	}

	/**
	 * set the DBuser value
	 * @param dbuser DB user
	 */
	public void setDbuser(String dbuser) {
		this.dbuser = dbuser;
	}

	/**
	 * get the DB pass value
	 * @return the DB pass
	 */
	public String getDbpass() {
		return dbpass;
	}

	/**
	 * Set the DB pass value
	 * @param dbpass the DB pass
	 */
	public void setDbpass(String dbpass) {
		this.dbpass = dbpass;
	}

	/**
	 * The mail template parser
	 * 
	 * @return the mail template parser
	 */
	public MailTemplateParser getMailTemplateParser() {
		return mailTemplateParser;
	}
	/**
	 * set the mail template parser
	 * @param mailTemplateParser the mail template parser
	 */
	public void setMailTemplateParser(MailTemplateParser mailTemplateParser) {
		this.mailTemplateParser = mailTemplateParser;
	}

	/**
	 * check if the consumer is configured to connect to usermanagement ws
	 * 
	 * @return true false 
	 */
	public boolean connectToUsermanagementDB() {
		return (Boolean)getProperty(Constants.CONNECT_TO_USERMANAGEMENT_DB_JNDI_NAME);
	}

	public String getDbhost() {
		return dbhost;
	}
	public void setDbhost(String dbhost) {
		this.dbhost = dbhost;
	}
	public Long getDbport() {
		return dbport;
	}
	public void setDbport(Long dbport) {
		this.dbport = dbport;
	}
	public Long getMaxDBConnections() {
		return maxDBConnections;
	}
	public void setMaxDBConnections(Long maxDBConnections) {
		this.maxDBConnections = maxDBConnections;
	}

}