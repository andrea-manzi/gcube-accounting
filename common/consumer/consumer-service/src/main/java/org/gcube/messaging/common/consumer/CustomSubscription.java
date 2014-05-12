package org.gcube.messaging.common.consumer;

import javax.jms.JMSException;
import javax.jms.MessageListener;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.gcube.common.core.scope.GCUBEScope;

/**
 * 
 * @author Andrea Manzi(CERN)
 *
 */
@SuppressWarnings("unchecked")
public class CustomSubscription extends BrokerSubscription{

	/**
	 *Default subscription
	 */
	public CustomSubscription(){
		
	}
	
	/**
	 * Start a Subscriber for the specified topic using the given broker
	 * 
	 * @param topicName the topic Name
	 * @param brokerEpr the brokerEpr
	 * @param listener the Listener
	 * @param selector the message selector
	 * @throws Exception 
	 */
	public void setupCustomDurableSubscriber(String topicName, String brokerEpr,MessageListener listener,String... selector) throws Exception {

		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerEpr);
			TopicSession session;
			TopicConnection connection;
			try {
				connection = ((TopicConnectionFactory)connectionFactory).createTopicConnection();
				if (selector.length > 0 )
					connection.setClientID(topicName+selector[0]);
				else 
					connection.setClientID(topicName+listener.getClass());
				connection.start();
				connections.add(connection);
				
				session = connection.createTopicSession(transacted, ackMode);
				Topic topic = session.createTopic(topicName);
				TopicSubscriber consumer = null ;
				if (selector.length > 0 )
					 consumer = session.createDurableSubscriber(topic,topic.getTopicName()+selector[0],selector[0],false);
				else 
					 consumer = session.createDurableSubscriber(topic,topic.getTopicName());
				consumer.setMessageListener(listener);
				
					
			} catch (JMSException e) {
				logger.error("Error creating Durable Subscriber",e);
				throw e;
			}
			catch (Exception e) {
				logger.error("Error creating Durable Subscriber",e);
				throw e;
			}
			logger.info("Started Durable Subscriber for topic: "+topicName);
	}

	@Override
	public void setScope(GCUBEScope scope) {}
	
}
