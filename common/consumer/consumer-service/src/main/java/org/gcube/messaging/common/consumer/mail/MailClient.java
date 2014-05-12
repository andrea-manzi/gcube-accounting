package org.gcube.messaging.common.consumer.mail;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.io.File;
import org.gcube.common.core.scope.GCUBEScope;
import org.gcube.common.core.utils.handlers.GCUBEScheduledHandler;
import org.gcube.common.core.utils.handlers.lifetime.State;
import org.gcube.common.core.utils.logging.GCUBELog;
import org.gcube.messaging.common.consumer.Constants;
import org.gcube.messaging.common.consumer.ServiceContext;
import org.gcube.messaging.common.consumer.notifier.Notification;

/**
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public class MailClient {

	static final String subject ="gCube Monitoring";
	
	GCUBELog logger = new GCUBELog(MailClient.class);
	
	Map<String,MailRecipientHelper> scopeMap= null;
	
	String mailFile = null;
	
	MailRecipientHelper parser = null;
		
	boolean daylySummary = false;
	

	/**
	 * constructor
	 *
	 */
	public MailClient () {
		scopeMap = Collections.synchronizedMap( new HashMap<String,MailRecipientHelper>());
		
	}
	/**
	 * Initialization
	 *
	 */
	public void initialize() {

		
		daylySummary= (Boolean)ServiceContext.getContext().getProperty("MailSummary", false);
		if (daylySummary) {
			final Calendar midnight = Calendar.getInstance();
			midnight.set(Calendar.HOUR_OF_DAY, 23);
			midnight.set(Calendar.MINUTE, 59);
			
			new Thread() { 
				public void run() { 
					while(true) { 
						try { Thread.sleep(20000); 
							if (Calendar.getInstance().before(midnight)) {
								try { Thread.sleep(30*60000);}
								catch (Exception e){} }
							else {new MailSummary().execute(); return; }
						}
						catch (Exception e){}
					}
				}
			}.start();

		}

	}
	



	/**
	 * Add information to mail Client for the given scope
	 * @param scope the scope 
	 * @throws Exception Exception
	 */
	public void addScope(GCUBEScope scope) throws Exception {
		MailRecipientResource resource = new MailRecipientResource(scope);
		parser = new MailRecipientHelper();
		resource.setHandled(parser);
		GCUBEScheduledHandler scheduler = new GCUBEScheduledHandler();
		scheduler.setScheduled(resource);
		scheduler.setInterval(3600);//one hour
		scheduler.run();
		
		while(resource.getState().equals(State.Running.INSTANCE))
			Thread.sleep(1000);
		
		scopeMap.put(scope.toString(),parser);

	}

	
	
	
	public Map<String, MailRecipientHelper> getScopeMap() {
		return scopeMap;
	}
	public void setScopeMap(Map<String, MailRecipientHelper> scopeMap) {
		this.scopeMap = scopeMap;
	}
	/**
	 * Send mail summary
	 * @param message the message to send
	 * @param groupName the group to send the message to
	 */
	public void sendMailSummary(String message,String groupName){
		new MailMessage(getSummaryMailForScope(groupName),message,subject).sendMail();
		
	}

	/**
	 * 
	 * Send a mail notification
	 * @param not the notification
	 * @param groupName the group to send the message to 
	 */
	public void sendMailNotification(String message,Notification not,String groupName,String domain){
		String subject = MailClient.subject + " " + "- Type: "+ not.getType() + " - GHN: "+ not.getSourceGHN(); 
		new MailMessage(getNotificationMailForScopeAndDomain(groupName,domain),message,subject).sendMail();
	
	}
	
	
	/**
	 * 
	 * @param scope
	 * @param domain
	 * @return
	 */
	public ArrayList<String> getNotificationMailForScopeAndDomain(String scope,String domain) 
	{
		
		ArrayList<String> recipients = new ArrayList<String>();
		try {
			String query ="SELECT DISTINCT USER.email FROM USER INNER JOIN USERDOMAIN on USERDOMAIN.email=USER.email AND USERDOMAIN.Name='"+domain+"' " +
						"WHERE (USER.Admin="+true+" OR USER.ReceiveNotification="+true+") AND USER.Scope='"+scope+"';";
		
			ResultSet result =ServiceContext.getContext().getMonitoringManager().query(query);
			while (result.next())
				recipients.add(result.getString("email"));
		}
		catch (Exception e){
			logger.error("Error retrieving mail recipients ",e);
		}
		return recipients;
	}
	
	/**
	 * 
	 * @param scope
	 * @param domain
	 * @return
	 */
	public ArrayList<String> getSummaryMailForScope(String scope) 
	{
		ArrayList<String> recipients = new ArrayList<String>();
		try {
			String query ="SELECT DISTINCT USER.email FROM USER WHERE (USER.Admin="+true+" OR USER.ReceiveSummary="+true+") AND USER.Scope='"+scope+"';";
		
			ResultSet result =ServiceContext.getContext().getMonitoringManager().query(query);
			while (result.next())
				recipients.add(result.getString("email"));
		}
		catch (Exception e){
			
			logger.error("Error retrieving mail recipients ",e);
		}
		return recipients;
	}
	
}

