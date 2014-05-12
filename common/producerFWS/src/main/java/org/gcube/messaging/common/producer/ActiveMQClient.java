package org.gcube.messaging.common.producer;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.gcube.messaging.common.messages.GCUBEMessage;

import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * JMS Client that sends message to ActiveMQ Broker
 * 
 * @author Andrea Manzi( CERN)
 *
 */
public class ActiveMQClient implements Runnable{
	
	/** Logger */
	private static Logger logger = LoggerFactory.getLogger(ActiveMQClient.class);


	
	private static int ackMode;
	private static  boolean transacted;
	private static ActiveMQClient singleton; 
	private String selectorBase = "MessageType";
	private boolean running = true;
	
	private static Integer MAX_ACCOUNTING_QUEUE_SIZE = 1000;
	private static  ConcurrentLinkedQueue<GCUBEMessage> messageForQueue = null;
	
	static Thread t = null;

	static {
		ackMode = Session.AUTO_ACKNOWLEDGE;
		transacted = false;
		singleton = new ActiveMQClient();
		messageForQueue = new ConcurrentLinkedQueue<GCUBEMessage>();
		
		//starting thread
		t = new Thread(singleton);
		t.start();
	}

	/**
	 * no object instantiation possible
	 */
	private ActiveMQClient() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
		    public void run() {
		    	singleton.terminate();
		    	try {
					t.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		});
	}

	

	/**
	 * Sends message to a a QUEUE destination
	 * 	@param message the GCUBEMessage to send
	 */
	public void sendMessageToQueue(GCUBEMessage message) {
		ArrayList<QueueConnection> connections = JMSLocalProducer.getQueueConnection(message.getScope());
		if (connections != null){
			for (QueueConnection connection :connections){
				try {
					QueueSession session = connection.createQueueSession(transacted, ackMode);
					Queue queue = session.createQueue(message.getTopic());
					QueueSender sender = session.createSender(queue);
					sender.setDeliveryMode(DeliveryMode.PERSISTENT);
					ObjectMessage objMsg = session.createObjectMessage();
					objMsg.setObject(message);
					objMsg.setJMSMessageID(createRandomString());
					sender.send(objMsg);
					logger.debug("Message "+message.toString()+ " SENT");
					return;
				} catch (JMSException e) {
					logger.error("Exception sending message to the Broker",e);
					enqueueMessageForQueue(message);
				} catch (Exception e) {
					logger.error("Exception sending message to the Broker",e);
					enqueueMessageForQueue(message);
				}
			}
		}
		JMSLocalProducer.reloadConnection(message.getScope());
	}

	/**
	 * Create a random Long
	 * @return a Random Long-String
	 */
	private  String createRandomString() {
		Random random = new Random(System.currentTimeMillis());
		long randomLong = random.nextLong();
		return Long.toHexString(randomLong);
	}

	public static ActiveMQClient getSingleton() {
		return singleton;
	}

	public void setSingleton(ActiveMQClient singleton) {
		this.singleton = singleton;
	}

	/**
	 * 
	 * enqueue the message
	 * @param message the message
	 */
	private void enqueueMessageForQueue(GCUBEMessage message){
		try {
			synchronized (messageForQueue) {
				if (messageForQueue.size() >=MAX_ACCOUNTING_QUEUE_SIZE){
					logger.error("Reached Maximum queue size, message discarded");
					logger.error(message.toString());
				} else messageForQueue.add(message);	
			}
		}catch  (Exception e) {
			logger.error("Error enqueuing GCUBEMessage : "+ message.toString(),e);
		}

	}
	

	public void run() {
		int sizeQueue = 0;
		while (running){
			
			synchronized(messageForQueue){
				sizeQueue = messageForQueue.size();
			}
		
			
			if (sizeQueue ==0) {
				try {
					Thread.sleep(1000*600);
				} catch (InterruptedException e) 
				{
					e.printStackTrace();
				}
			}
			else {
				if (sizeQueue >0){
					GCUBEMessage message = null;
					synchronized (messageForQueue) 
					{
						 message = messageForQueue.poll();
					}
					this.sendMessageToQueue(message);
					try {
						Thread.sleep(1000*60);
					} catch (InterruptedException e) 
					{
						e.printStackTrace();
					}
				}
				
			}
		}
		
	}
	
	private void terminate() {
		running = false;
	}
	
}

