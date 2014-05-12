package org.gcube.messaging.common.consumer.notifier;

import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.mail.MessagingException;

import org.gcube.common.core.utils.logging.GCUBELog;
import org.gcube.messaging.common.consumer.ServiceContext;
import org.gcube.messaging.common.consumer.ghn.GHNNotification;
import org.gcube.messaging.common.consumer.ri.RINotification;

import java.net.InetAddress;
/**
 * Notifier class
 * @author Andrea Manzi(CERN)
 *
 */
public class Notifier implements Runnable{

	/**
	 * logger
	 */
	public  static GCUBELog logger = new GCUBELog(Notifier.class);
	
	private static  ConcurrentLinkedQueue<Notification> notificationQueue = null;

	private boolean notifiyByMail = false;

	/**
	 * Constructor
	 * @param notifyByMail if notif by mail or not
	 */
	public Notifier(boolean notifyByMail)
	{
		notifiyByMail= notifyByMail;
		notificationQueue = new  ConcurrentLinkedQueue<Notification>();


	}
	/**
	 * 
	 * enqueue the notification
	 * @param notification the notification
	 */
	public void enqueue(Notification notification){
		try {
			synchronized (notificationQueue) {
				notificationQueue.add(notification);	
			}
		}catch  (Exception e) {
			logger.error("Error enqueuing Notification with Message: "+ notification.getMessage(),e);
		}

	}

	private void sendGHNNotificationMail(Notification not) throws IOException, MessagingException {
	
		String messageTosend = "***** gCube Monitoring *****\n\n"
			+ "Type: " +((GHNNotification)not).getType()+"\n" 
			+ "GHN: " +((GHNNotification)not).getSourceGHN()+"\n" 
			+ "Address: "+ InetAddress.getByName(((GHNNotification)not).getSourceGHN().substring(0,((GHNNotification)not).getSourceGHN().indexOf(':'))) + "\n"
			+ not.getMessage() +"\n\n" 
			+ "Date/Time: " + not.getTime() + "\n\n";
		
		String domain = getDomain(not.getSourceGHN());
		ServiceContext.getContext().getMailClient().sendMailNotification(messageTosend,not,not.getScope().toString(),domain);
	}
	
	private void sendRINotificationMail(Notification not) throws IOException, MessagingException {

		String messageTosend = "***** gCube Monitoring *****\n\n"
			+ "Type: " +((RINotification)not).getType()+"\n" 
			+ "GHN: " +((RINotification)not).getSourceGHN()+"\n" 
			+ "Address: "+ InetAddress.getByName(((RINotification)not).getSourceGHN().substring(0,((RINotification)not).getSourceGHN().indexOf(':'))) + "\n"
			+ "ServiceClass: " + ((RINotification)not).getServiceClass()+"\n"
			+ "ServiceName: " + ((RINotification)not).getServiceName()+"\n"
			+ not.getMessage() +"\n\n" 
			+ "Date/Time: " + not.getTime() + "\n\n";

		String domain = getDomain(not.getSourceGHN());
		ServiceContext.getContext().getMailClient().sendMailNotification(messageTosend,not,not.getScope().toString(),domain);
	}


	
	private String getDomain(String ghnName){
		try {
			String temp = ghnName.substring(ghnName.indexOf(".")+1);
			return temp.substring(0,temp.indexOf(":"));
		}catch (Exception e){
			logger.error("Error extracting domain from ghn "+ghnName);
			return "";
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void run () {
		int size = 0;
		while (true){
			synchronized(notificationQueue){
				size = notificationQueue.size();
			}
			if (size ==0){
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) 
				{
					e.printStackTrace();
				}
			}
			else {
				Notification not = null;
				synchronized (notificationQueue) 
				{
					not = notificationQueue.poll();
				}
				this.sendNotification(not);
			}
		}

	}


	private void sendNotification(Notification not){
		if (notifiyByMail)
		{
			if(not instanceof GHNNotification){
				logger.debug("Sending GHN Notification Mail");
				try {
					this.sendGHNNotificationMail(not);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (MessagingException e) {
					e.printStackTrace();
				}
			} else if(not instanceof RINotification){
				logger.debug("Sending RI Notification Mail");
				try {
					this.sendRINotificationMail(not);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (MessagingException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
