package org.gcube.messaging.common.consumer;

import java.io.IOException;

import javax.jms.JMSException;
import javax.jms.TopicConnection;

import org.gcube.common.core.faults.GCUBEFault;
import org.gcube.common.core.scope.GCUBEScope;
import org.gcube.common.core.scope.GCUBEScope.MalformedScopeExpressionException;
import org.gcube.common.core.types.VOID;
import org.gcube.common.core.utils.logging.GCUBELog;
import org.gcube.messaging.common.consumer.mail.MailSummary;
import org.gcube.messaging.common.consumer.stubs.SendReport;


/**
 * 
 * Models the Consumer Monitor
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public class MessagingConsumer {

	/**
	 * The Logger
	 */
	public  static GCUBELog logger = new GCUBELog(MessagingConsumer.class);

	/**
	 * Default Constructor
	 *
	 */
	public MessagingConsumer() {}



	/**
	 * Send a Report given the date
	 * 
	 * @param report The report Type defined in the wsdl
	 * @return VOID coid type
	 * @throws GCUBEFault base fault
	 */
	public VOID sendReport(SendReport report) throws GCUBEFault {
		String date = report.getDate();
		String scope = report.getScope();

		try {
			MailSummary.sendReport(GCUBEScope.getScope(scope),date);
		} catch (MalformedScopeExpressionException e) {
			logger.error(e);
			throw ServiceContext.getContext().getDefaultException("Error sending report",e).toFault();
		} catch (IOException e) {

			logger.error(e);
			throw ServiceContext.getContext().getDefaultException("Error sending report",e).toFault();
		}
		return new VOID(); 

	}



	/**
	 * query the MontoringDB 
	 * 
	 * @param query a mysqlQuery
	 * @return json result
	 * @throws GCUBEFault base fault
	 */
	public String queryMonitoringDB(String query) throws GCUBEFault {

		String json ="";

		try {
			json = ServiceContext.getContext().getMonitoringManager().queryJSON(query);
		} catch (Exception e) {
			logger.error(e);
			throw ServiceContext.getContext().getDefaultException("Error executing query",e).toFault();
		}
		return json;

	}

	/**
	 * query the AccountingDB 
	 * 
	 * @param query a mysqlQuery
	 * @return json result
	 * @throws GCUBEFault base fault
	 */
	public String queryAccountingDB(String query) throws GCUBEFault {

		String json ="";

		try {
			json = ServiceContext.getContext().getAccountingManager().queryJSON(query);
		} catch (Exception e) {
			logger.error(e);
			throw ServiceContext.getContext().getDefaultException("Error executing query",e).toFault();
		}
		return json;

	}
	
	/**
	 * query the SystemAccountingDB 
	 * 
	 * @param query a mysqlQuery
	 * @return JSon result
	 * @throws GCUBEFault base fault
	 */
	public String querySystemAccountingDB(String query) throws GCUBEFault {

		String json ="";

		try {
			json = ServiceContext.getContext().getAccountingSystemManager().queryJSON(query);
		} catch (Exception e) {
			logger.error(e);
			throw ServiceContext.getContext().getDefaultException("Error executing query",e).toFault();
		}
		return json;

	}


	/**
	 * backupDB 
	 * 
	 * @throws GCUBEFault base fault
	 */
	public void backupMonitoringDB(VOID void1) throws GCUBEFault {

		try {
			ServiceContext.getContext().getMonitoringManager().backup();

		} catch (Exception e) {
			logger.error(e);
			throw ServiceContext.getContext().getDefaultException("Error during DB backup",e).toFault();
		}

	}

	/**
	 * backupDB 
	 * 
	 * @throws GCUBEFault base fault
	 */
	public void backupAccountingDB(VOID void1) throws GCUBEFault {

		try {
			ServiceContext.getContext().getAccountingManager().backup();

		} catch (Exception e) {
			logger.error(e);
			throw ServiceContext.getContext().getDefaultException("Error during DB backup",e).toFault();
		}

	}




	/**
	 * 
	 * 
	 * @author Andrea Manzi(CERN)
	 *
	 */
	public static class ReloadDurableSubscribers implements Runnable{

		public void run() {
			while (true) {
				try {
					Thread.sleep(60*35*1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				for (BrokerSubscription <?>connection: ServiceContext.getSubscriptionsList())
				{
					for (Object con :connection.getConnections()) {
						try {
							((TopicConnection)con).stop();
						} catch (JMSException e) {
							logger.debug("Exception stopping the connection",e);
						}
						try {
							((TopicConnection)con).start();
						} catch (JMSException e) {
							logger.debug("Exception starting the connection",e);
						}
					}

				}
			}
		}
	}
}

