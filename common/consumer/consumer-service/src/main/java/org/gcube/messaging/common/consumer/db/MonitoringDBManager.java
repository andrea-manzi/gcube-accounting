package org.gcube.messaging.common.consumer.db;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.gcube.common.core.contexts.GHNContext;
import org.gcube.common.core.utils.logging.GCUBELog;
import org.gcube.messaging.common.consumer.Constants;
import org.gcube.messaging.common.consumer.ServiceContext;
import org.gcube.messaging.common.consumer.Constants.NotificationSubType;
import org.gcube.messaging.common.consumer.mail.MailRecipientHelper.MonitorUser;
import org.gcube.messaging.common.consumer.notifier.Notification;
import org.gcube.messaging.common.consumer.ri.RINotification;
import org.gcube.messaging.common.messages.GHNMessage;
import org.gcube.messaging.common.messages.RIMessage;

/**
 * @author Andrea Manzi(CERN)
 * 
 */
public class MonitoringDBManager extends DBManager implements Runnable {

	GCUBELog logger = new GCUBELog(MonitoringDBManager.class);

	/**
	 * Constructor
	 */
	public MonitoringDBManager() {

		dbFileBaseFolder =(String)ServiceContext.getContext().getPersistenceRoot().getAbsolutePath() + File.separator + "MonitoringDB";
		dbName = "monitoring";
		dbFileName =  dbFileBaseFolder +File.separator+ dbName +".db";
		backupFolder = new File ((System.getenv("HOME") + File.separator + "MonitoringDBBackup"));

		queriesFile = new File(GHNContext.getContext().getLocation() + File.separator +
				(String)ServiceContext.getContext().getProperty(Constants.CONFIGDIR_JNDI_NAME)+ File.separator +
				(String)ServiceContext.getContext().getProperty(Constants.MONITORINGDBFILE_JNDI_NAME, true));
		connection = null;
		//if use enbeddedDB starts automatic backup thread
		if (ServiceContext.getContext().getUseEmbeddedDB()){
			Thread t = new Thread(this);
			t.start();
		}
		poolManager = new PoolManager(dbName);
	}
	
	@Override
	protected void connectToMySql() throws Exception{
		  connection = poolManager.getInternalDBConnection();
		  connection.setAutoCommit(true);
		  
	}

	/**
	 * {@inheritDoc}
	 */
	public  synchronized void open() throws ClassNotFoundException, SQLException, Exception {
		if(connection==null){
			if (ServiceContext.getContext().getUseEmbeddedDB())
				connectToEmbeddedDB();
			else connectToMySql();
		}

		String testQuery = "SELECT LIMIT 1 1 * FROM GHNMessage";
		BaseConsumer baseConsumer = new BaseConsumer(){
			public void consume(ResultSet resultset) throws Exception {}

		};
		try {
			queryAndConsume(testQuery,baseConsumer);
		}
		catch (Exception e){
			try {
				createDB();
			}
			catch (Exception e1){
				throw e1;
			}
		}
	}

	/**
	 * Insert a user on the DB
	 * @param  user the user to add
	 */
	public void insertUser(MonitorUser user) {
		logger.debug("INSERT the following user "+user+" INTO DB");
		try {
			String expression = "INSERT INTO USER VALUES ('"+
			user.getMail()+"','"
			+user.getName()+"','"
			+user.getScope()+"','"
			+(user.isNotify()?1:0)+"','"
			+(user.isReceiveSummary()?1:0)+"','"
			+(user.isAdmin()?1:0)+"');";

			this.update(expression);
		} catch (Exception e) {
			logger.error("Error updating DB", e);
		}
	}

	/**
	 * 
	 * @return the domains
	 */
	public ResultSet getDomains() {
		String query  ="SELECT DISTINCT Name FROM  DOMAIN"; 
		logger.debug(query);
		ResultSet result = null;
		try {
	
			result = this.query(query);
		} catch (Exception e) {
			logger.error("Error querying  DB", e);
		}
		return result;
	}

	/**
	 * Insert a domain on the DB
	 * @param  domain the domain to add
	 */
	public void insertDomain(String domain,String siteName) {
		logger.debug("INSERT the following domain "+domain+" INTO DB");
		try {
			String expression = "INSERT INTO DOMAIN VALUES ('"
				+domain+"','"
				+siteName+"');";
			this.update(expression);
		} catch (Exception e) {
			logger.error("Error updating DB", e);
		}
	}

	/**
	 * Associate a user to a domain
	 * @param  userMail the userMail to add
	 * @param  domain the domain to add
	 */
	public void associateUserAndDomain(String userMail,String domain,String scope) {
		logger.debug("INSERT the following domain "+domain+" INTO DB");
		try {
			String expression = "INSERT INTO USERDOMAIN VALUES ('"
				+userMail+"','"
				+domain+"','"
				+scope+"');";
			this.update(expression);
		} catch (Exception e) {
			logger.error("Error updating DB", e);
		}
	}


	/**
	 * clean the given table
	 * @param tableName
	 */
	public void cleanMonitoringConf(String scope) {
		logger.debug("Cleaning Monitoring conf for scope " +scope);
		try {
			connection.setAutoCommit(false);
			String query1 = "DELETE FROM USER WHERE Scope='"+scope+"';";
			String query2 = "DELETE FROM USERDOMAIN WHERE Scope='"+scope+"';";
			
			Statement statement = connection.createStatement();
			Statement statement2 = connection.createStatement();
			statement.executeUpdate(query1);
			statement2.executeUpdate(query2);
			connection.commit();
			statement.close();
			statement2.close();

		} catch (Exception e) {
			try {
				connection.rollback();
			} catch (SQLException error) {
				logger.error("Error updating DB", e);
				e.printStackTrace();
			}
		}
		finally{
			try {
				connection.setAutoCommit(true);
			} catch (SQLException e) {
				logger.error("Error setting connection Autocommit", e);

			}
		}
	}



	/**
	 * Insert a GHNMessage on the DB
	 * @param message the Message
	 */
	public void InsertGHNMessage(GHNMessage<?> message) {
		logger.debug("INSERT the following GHN message "+message+" INTO DB");
		String date = message.getTime().substring(0,  message.getTime().indexOf(" "));
		String time = message.getTime().substring(message.getTime().indexOf(" ")+1);
		try {
			String expression = "INSERT INTO GHNMessage (GHNName,testType,description,result,scope,date,time) VALUES ('"+
			message.getSourceGHN()+"','"
			+message.getTest().getType()+"','"
			+message.getTest().getDescription()+"','"
			+(((message.getTest().getTestResult()!= null)?message.getTest().getTestResult().toString():""))+"','"
			+message.getScope()+"','"
			+date+"','"
			+time+"');";

			this.update(expression);
		} catch (Exception e) {
			logger.error("Error updating DB", e);
		}
	}

	/**
	 * Insert a RIMessage
	 * @param message message
	 */
	public void InsertRIMessage(RIMessage<?> message) {
		logger.debug("INSERT the following RI message: "+message+" INTO DB");
		String date = message.getTime().substring(0,  message.getTime().indexOf(" "));
		String time = message.getTime().substring(message.getTime().indexOf(" ")+1);

		try {
			String expression = "INSERT INTO RIMessage (ServiceName,ServiceClass,GHNName,testType,description,result,scope,date,time) VALUES ('"+
			message.getServiceName()+"','"
			+message.getServiceClass()+"','"
			+message.getSourceGHN()+"','"
			+message.getTest().getType()+"','"
			+message.getTest().getDescription()+"','"
			+(((message.getTest().getTestResult()!= null)?message.getTest().getTestResult().toString():""))+"','"
			+message.getScope()+"','"
			+date+"','"
			+time+"');";

			this.update(expression);
		} catch (Exception e) {
			logger.error("Error updating DB", e);
		}
	}
	/**
	 * insert a Notification on the DB
	 * @param notification the notification
	 */
	public void insertNotification(Notification not) {

		logger.debug("INSERT the following Notification : "+not+" INTO DB");
		String date = not.getTime().substring(0,  not.getTime().indexOf(" "));
		String time = not.getTime().substring(not.getTime().indexOf(" ")+1);

		try {
			String expression = "INSERT INTO NOTIFICATION (GHNName,ServiceName,ServiceClass,testType,testSubType,scope,message,date,time) VALUES ('"+
			not.getSourceGHN()+"','"
			+((not instanceof RINotification)?((RINotification)not).getServiceName()+"','":"NULL','")
			+((not instanceof RINotification)?((RINotification)not).getServiceClass()+"','":"NULL','")
			+not.getType()+"','"
			+getSubType(not)+"','"
			+not.getScope().toString()+"','"
			+not.getMessage()+"','"
			+date+"','"
			+time+"');";

			this.update(expression);
		} catch (Exception e) {
			logger.error("Error updating DB", e);
		}
	}

	private String getSubType(Notification not){
		if(not.getType() != "NOTIFICATION")
			return not.getType();
		else if (not.getMessage().contains("GHN Ready")) return NotificationSubType.GHN_READY.name();
		else if (not.getMessage().contains("GHN shutdown")) return NotificationSubType.GHN_SHUTDOWN.name();
		else if (not.getMessage().contains("Added")) return NotificationSubType.SCOPE_ADDED.name();
		else if (not.getMessage().contains("Removed")) return NotificationSubType.SCOPE_REMOVED.name();
		else return""; 
	}

	/**
	 * {@inheritDoc}
	 */
	public void run() {
		do {
			try {
				Thread.sleep(backupIntervalMS);
				this.backup();
			} catch (InterruptedException e) {
				logger.error("Unable to sleep", e);
			} catch (Exception e) {
				logger.error("Unable to backup", e);
			}
		} while (! Thread.interrupted());

	} 
}
