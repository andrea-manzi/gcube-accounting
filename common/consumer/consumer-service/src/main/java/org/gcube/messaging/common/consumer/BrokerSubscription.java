package org.gcube.messaging.common.consumer;

import java.util.ArrayList;

import javax.jms.Connection;
import javax.jms.ExceptionListener;
import javax.jms.InvalidClientIDException;
import javax.jms.JMSException;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.gcube.common.core.scope.GCUBEScope;
import org.gcube.common.core.utils.logging.GCUBELog;
import org.gcube.common.messaging.endpoints.BrokerEndpoints;
import org.gcube.common.messaging.endpoints.ScheduledRetriever;
import org.gcube.common.scope.api.ScopeProvider;

/**
 * 
 * @author Andrea Manzi(CERN)
 * 
 * @param <LISTENER>
 */
public abstract class BrokerSubscription<LISTENER extends MessageListener>
extends Thread implements ExceptionListener {

	protected TopicCouple couple;
	protected ArrayList<String> messageSelectors = new ArrayList<String>();
	protected LISTENER listener;
	protected static GCUBELog logger = new GCUBELog(BrokerSubscription.class);
	protected ArrayList<Connection> connections = new ArrayList<Connection>();
	protected static boolean transacted = false;
	protected static int ackMode = Session.AUTO_ACKNOWLEDGE;
	protected boolean queue = false;

	/**
	 * set the scope
	 * 
	 * @param scope
	 *            the scope to set
	 */
	public abstract void setScope(GCUBEScope scope);

	/**
	 * subscribe
	 * 
	 * @throws Exception
	 */
	public void subscribe() throws Exception {
		if (queue) {
			setupQueueSubscription();
		} else if (messageSelectors.size() == 0)
			setupDurableSubscribers();
		else
			for (String selector : messageSelectors) {
				setupDurableSubscribers(selector);
			}
	}

	/**
	 * Get the list of connections
	 * 
	 * @return the list of connections
	 */
	public ArrayList<Connection> getConnections() {
		return connections;
	}

	/**
	 * Set the list of connections
	 * 
	 * @param connections
	 *            the list of connections
	 */
	public void setConnections(ArrayList<Connection> connections) {
		this.connections = connections;
	}

	/**
	 * 
	 * @throws Exception
	 */
	public void setupDurableSubscribers(String ... selector) throws Exception{

		ActiveMQConnectionFactory connectionFactory = null;

		if (ServiceContext.getContext().getUseEmbeddedBroker()){
			connectionFactory = 
					new ActiveMQConnectionFactory("tcp://localhost:61616");
			Thread.sleep(10000);
		} 
		else {

			ScopeProvider.instance.set(this.getCouple().getScope().toString());

			ScheduledRetriever retriever = BrokerEndpoints.getRetriever(ServiceContext.getContext().getEdnpointWaitingTime(), ServiceContext.getContext().getEndpointRefreshTime());

			if (retriever.getEndpoints().size()!=0) {

				for (String endpoint : retriever.getEndpoints()) { 

					connectionFactory = 
							new ActiveMQConnectionFactory(endpoint);

					TopicSession session;
					TopicConnection connection;

					try {
						connection = ((TopicConnectionFactory)connectionFactory).createTopicConnection();
						if (selector.length > 0 )
							connection.setClientID(this.getCouple().getTopicName()+selector[0]);
						else 
							connection.setClientID(this.getCouple().getTopicName()+listener.getClass());
						connection.start();
						connection.setExceptionListener(this);

						session = connection.createTopicSession(transacted, ackMode);
						Topic topic = session.createTopic(this.getCouple().getTopicName());
						TopicSubscriber consumer = null ;

						if (selector.length > 0 )
							consumer = session.createDurableSubscriber(topic,topic.getTopicName()+selector[0],selector[0],false);
						else 
							consumer = session.createDurableSubscriber(topic,topic.getTopicName());

						consumer.setMessageListener(listener);
						connections.add(connection);

					} catch (JMSException e) {
						logger.error("Error creating Durable Subscriber",e);
						throw e;
					}
					catch (Exception e) {
						logger.error("Error creating Durable Subscriber",e);
						throw e;
					}
				}
				
				logger.info("Started Durable Subscriber for topic: "+this.getCouple().getTopicName());
			
			} else logger.error("Impossible to setup Durable Subscriber, Broker epr not specified for the scope: "+this.getCouple().getScope().toString());

		} 
	

		}

		/**
		 * setupQueueSubscription
		 * 
		 * @throws Exception
		 */
		public void setupQueueSubscription() throws Exception {

			ScopeProvider.instance.set(this.getCouple().getScope().toString());

			ScheduledRetriever retriever = BrokerEndpoints.getRetriever(ServiceContext.getContext().getEdnpointWaitingTime(), ServiceContext.getContext().getEndpointRefreshTime());

			
			ActiveMQConnectionFactory connectionFactory = null;
			
			if (retriever.getEndpoints().size()!=0) {

				for (String endpoint : retriever.getEndpoints()) { 

					connectionFactory = 
							new ActiveMQConnectionFactory(endpoint);



				QueueConnection connection;
				QueueSession session;

				try {
					connection = ((QueueConnectionFactory) connectionFactory)
							.createQueueConnection();
					connection.setClientID(this.getCouple().getTopicName());
					connection.start();
					connection.setExceptionListener(this);

					session = connection.createQueueSession(transacted, ackMode);
					Queue queue = session.createQueue(this.getCouple()
							.getTopicName());
					QueueReceiver consumer = null;

					consumer = session.createReceiver(queue);

					consumer.setMessageListener(listener);
					connections.add(connection);

				} catch (JMSException e) {
					logger.error("Error creating Queue Receiver", e);
					throw e;
				} catch (Exception e) {
					logger.error("Error creating Queue Receiver", e);
					throw e;
				}
				}
				logger.info("Started Queue receveiver for topic: "
						+ this.getCouple().getTopicName());

			} else
				logger.error("Impossible to setup Queue Receiver, Broker epr not specified for the scope: "
						+ this.getCouple().getScope().toString());


		}

		@Override
		public void run() {

			try {
				// reset previous connections;
				for (Connection connection : connections) {
					connection.stop();
					connection.close();
				}
				connections.clear();
			} catch (JMSException e) {
				logger.debug("Exception stopping the connection", e);
				connections.clear();
			}

			while (true) {
				try {
					this.subscribe();
					return;
				} catch (InvalidClientIDException ex) {
					logger.error("Subscription has not been reset", ex);
					return;

				} catch (Exception e) {
					logger.error("Error on subscription", e);
					try {
						Thread.sleep(6000 * 2);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
			}
		}

		/**
		 * {@inheritDoc}
		 */
		public void onException(JMSException exce) {

			logger.error(exce.getMessage());
			logger.error(exce);
			this.run();
		}

		/**
		 * default constructor
		 */
		public BrokerSubscription() {
		}

		/**
		 * get the topic info
		 * 
		 * @return the topic info
		 */
		public TopicCouple getCouple() {
			return couple;
		}

		/**
		 * set the topic info
		 * 
		 * @param couple
		 *            the topic info
		 */
		public void setCouple(TopicCouple couple) {
			this.couple = couple;
		}

		/**
		 * Get the listener associated to the subscription
		 * 
		 * @return the listener
		 */
		public LISTENER getListener() {
			return listener;
		}

		/**
		 * set the listener associated to the connection
		 * 
		 * @param listener
		 *            the listener
		 */
		public void setListener(LISTENER listener) {
			this.listener = listener;
		}

		/**
		 * Get the message Selectors for this subscription
		 * 
		 * @return the message selectors
		 */
		public ArrayList<String> getMessageSelectors() {
			return messageSelectors;
		}

		/**
		 * Set the message selectors for this subscription
		 * 
		 * @param messageSelectors
		 *            the message selector
		 */
		public void setMessageSelectors(ArrayList<String> messageSelectors) {
			this.messageSelectors = messageSelectors;
		}

		/**
		 * topic couple
		 * 
		 * @author Andrea Manzi(CERN)
		 * 
		 */
		public class TopicCouple {
			GCUBEScope scope;
			String topicName;

			/**
			 * get the scope
			 * 
			 * @return the scope
			 */
			public GCUBEScope getScope() {
				return scope;
			}

			/**
			 * set the scope
			 * 
			 * @param scope
			 *            the scope
			 */
			public void setScope(GCUBEScope scope) {
				this.scope = scope;
			}

			/**
			 * get the topic name
			 * 
			 * @return the topic name
			 */
			public String getTopicName() {
				return topicName;
			}

			/**
			 * set the topic name
			 * 
			 * @param topicName
			 *            the topic name
			 */
			public void setTopicName(String topicName) {
				this.topicName = topicName;
			}

		}

	}
