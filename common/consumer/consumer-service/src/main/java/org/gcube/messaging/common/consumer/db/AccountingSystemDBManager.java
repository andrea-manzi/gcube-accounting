package org.gcube.messaging.common.consumer.db;

import java.io.File;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.UUID;


import org.gcube.common.core.contexts.GHNContext;
import org.gcube.common.core.utils.logging.GCUBELog;
import org.gcube.messaging.common.consumer.Constants;
import org.gcube.messaging.common.consumer.ServiceContext;
import org.gcube.messaging.common.messages.SystemAccountingMessage;
import org.gcube.messaging.common.messages.util.SQLType;

/**
 *  
 *  @author Andrea Manzi (CERN)
 *
 */
public class AccountingSystemDBManager extends DBManager implements Runnable {

	GCUBELog logger = new GCUBELog(AccountingSystemDBManager.class);

	protected static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	protected static SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);

	/**
	 * Default constructor
	 */
	public AccountingSystemDBManager() {

		dbFileBaseFolder =(String)ServiceContext.getContext().getPersistenceRoot().getAbsolutePath() + File.separator + "AccountingSystemDB"; 
		dbName = "system_accounting";
		dbFileName =  dbFileBaseFolder +File.separator+ dbName+".db";

		backupFolder = new File ((System.getenv("HOME") + File.separator + "AccountingSystemDBBackup"));

		queriesFile = new File(GHNContext.getContext().getLocation() + File.separator +
				(String)ServiceContext.getContext().getProperty(Constants.CONFIGDIR_JNDI_NAME)+ File.separator +
				(String)ServiceContext.getContext().getProperty(Constants.ACCOUNTINGSYTEMDBFILE_JNDI_NAME, true));
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

	@Override
	public void open() throws ClassNotFoundException, SQLException, Exception {

		if(connection==null){
			if (ServiceContext.getContext().getUseEmbeddedDB())
				connectToEmbeddedDB();	
			else connectToMySql();
		}

		String testQuery = "SELECT LIMIT 1 1 * FROM ACCOUNTINGSYTEMTYPE";
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
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
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


	private boolean isTableOnDB(String tableName){
		String TABLE_NAME = "TABLE_NAME";
		String[] TABLE_TYPES = {"TABLE"};
		try {
			java.sql.DatabaseMetaData  dbmd = connection.getMetaData();
			ResultSet tables = dbmd.getTables(null, null, null, TABLE_TYPES);
			while (tables.next()) {
				if(tables.getString(TABLE_NAME).compareTo(tableName)==0)
					return true;
			}
		}catch (SQLException e){
			logger.error("Error getting database Information",e);
		}
		return false;
	}


	private String checkType(SQLType type){
		if (type.getName().compareTo("VARCHAR")==0)
			return "VARCHAR(256)";
		else return type.getName();
	}
	
	/**
	 * Store a system accounting message
	 * 
	 * @param message
	 */
	public synchronized void storeSystemAccoutingInfo(SystemAccountingMessage message) {

		String type = message.getMessageType();
	  
		//checking the message type and if the related table already exists
		if (!isTableOnDB(type)){
			try {
				logger.debug("CREATING the following table: "+type+" INTO DB");
				String create = "CREATE TABLE "+type+" ("
				+"id VARCHAR(64) NOT NULL,"
				+"scope VARCHAR(128) NOT NULL,"
				+"serviceClass VARCHAR(64),"
	            +"serviceName VARCHAR(128),"
	            +"sourceGHN VARCHAR(128) NOT NULL,"
	            +"datetime DATETIME  NOT NULL,";
				for (String key: message.getFieldMap().keySet()){
					create+=key
					+" "+checkType(message.getFieldMap().get(key).getSqlType())+",";
				}
				create =create.substring(0, create.length()-1);
				create+=");";
				logger.debug("Expression: "+create);
				
				this.update(create);
			} catch (Exception e) {
				logger.error("Error Creating table into DB", e);
			}
		}
		
		//perform insert on the table
		logger.debug("INSERT the following msg: "+message.toString()+" INTO DB");
		
		String id =  UUID.randomUUID().toString();
		
		try {
			String expression = "INSERT INTO "+type+" VALUES ('"+
			id+"','"
			+message.getScope()+"','"
			+message.getServiceClass()+"','"
			+message.getServiceName()+"','"
			+message.getSourceGHN()+"','"
			+message.getTime()+"','";
			for (String key: message.getFieldMap().keySet()){
				expression+=message.getFieldMap().get(key).getValue()+"','";
			}
			expression =expression.substring(0, expression.length()-2);
			expression+=");";
			logger.debug("Expression: "+expression);
			this.update(expression);
		} catch (Exception e) {
			logger.error("Error updating DB", e);
		}
		
	} 
}
