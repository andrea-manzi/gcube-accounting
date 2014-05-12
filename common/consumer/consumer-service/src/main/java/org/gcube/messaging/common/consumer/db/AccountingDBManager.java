package org.gcube.messaging.common.consumer.db;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.gcube.common.core.contexts.GHNContext;
import org.gcube.common.core.utils.logging.GCUBELog;
import org.gcube.messaging.common.consumer.Constants;
import org.gcube.messaging.common.consumer.ServiceContext;
import org.gcube.messaging.common.messages.NodeAccountingMessage;
import org.gcube.messaging.common.messages.PortalAccountingMessage;
import org.gcube.messaging.common.messages.records.*;
import org.gcube.messaging.common.messages.records.AquamapsRecord.AquamapsSubType;
import org.gcube.messaging.common.messages.records.DocumentWorkflowRecord.WorkflowSubType;
import org.gcube.messaging.common.messages.records.HLRecord.GCUBEUser;
import org.gcube.messaging.common.messages.records.HLRecord.HLSubType;
import org.gcube.messaging.common.messages.records.ReportRecord.ReportSubType;
import org.gcube.messaging.common.messages.records.StatisticalManagerRecord.StatisticalManagerSubType;
import org.gcube.messaging.common.messages.records.WarRecord.WarSubType;
import org.gcube.messaging.common.messages.records.WebAppRecord.GHN;
import org.gcube.messaging.common.messages.records.WebAppRecord.WebApplication;


/**
 *  
 *  @author Andrea Manzi (CERN)
 *
 */
public class AccountingDBManager extends DBManager implements Runnable {

	GCUBELog logger = new GCUBELog(AccountingDBManager.class);

	protected static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	protected static SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
	

	/**
	 * Default constructor
	 */
	public AccountingDBManager() {

		dbFileBaseFolder =(String)ServiceContext.getContext().getPersistenceRoot().getAbsolutePath() + File.separator + "AccountingDB"; 
		dbName = "accounting";
		dbFileName =  dbFileBaseFolder +File.separator+ dbName+".db";

		backupFolder = new File ((System.getenv("HOME") + File.separator + "AccountingDBBackup"));

		queriesFile = new File(GHNContext.getContext().getLocation() + File.separator +
				(String)ServiceContext.getContext().getProperty(Constants.CONFIGDIR_JNDI_NAME)+ File.separator +
				(String)ServiceContext.getContext().getProperty(Constants.ACCOUNTINGDBFILE_JNDI_NAME, true));
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

		String testQuery = "SELECT LIMIT 1 1 * FROM NODEINVOCATION";
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
	 * Store Interval record on RI invocation
	 * @param interval the interval
	 * @param record the record
	 * @param message the node Accounting message
	 */
	public void storeNodeAccoutingInfo(IntervalRecord record,NodeAccountingMessage<IntervalRecord> message){
		logger.debug("INSERT the following RECORD: "+record.toString()+" INTO DB");
		String startTime = format.format(record.getStartInterval());
		String endTIme = format.format(record.getEndInterval());
		String ghn = message.getSourceGHN();
		//workaround to make the consumer correctly created records from old gCore based messages
		if (ghn == null) 
			ghn = message.getServiceName() + ":8080";
		try {
			String expression = "INSERT INTO NODEACCOUNTING VALUES ('"+
					ghn+"','"
					+message.getServiceClass()+"','"
					+message.getServiceName()+"','"
					+message.getCallScope()+"','"
					+startTime+"','"
					+endTIme+"','"
					+record.getInterval()+"','"
					+record.getInvocationNumber()+"','"
					+record.getAverageInvocationTime()+"','"
					+record.getIP()+"');";
			//logger.debug(expression);
			this.update(expression);
		} catch (Exception e) {
			logger.error("Error updating DB", e);
			e.printStackTrace();
		}

	}

	/**
	 * Store a generic portal message 
	 * @param message the message
	 * @param record the base record
	 */
	public synchronized void storePortalMessage(PortalAccountingMessage<?> message ,BaseRecord record){
		logger.debug("INSERT the following RECORD: "+record.toString()+" INTO DB");
		String dateTime = format.format(record.getDate());
		String date= dateTime.substring(0,  dateTime.indexOf(" "));
		String time = dateTime.substring(dateTime.indexOf(" ")+1);
		String id =  UUID.randomUUID().toString();
		try {
			String expression = "INSERT INTO PORTALACCOUNTING VALUES ('"+
					id+"','"
					+message.getUser()+"','"
					+message.getVre()+"','"
					+date+"','"
					+time+"','"
					+record.getClass().getSimpleName()+"');";
			this.update(expression);
		} catch (Exception e) {
			logger.error("Error updating DB", e);
			e.printStackTrace();
		}

		//update the other tables
		if (record instanceof AdvancedSearchRecord)
			this.storeRecord(id,(AdvancedSearchRecord)record);
		else if (record instanceof SimpleSearchRecord)
			this.storeRecord(id,(SimpleSearchRecord)record);
		else if (record instanceof BrowseRecord)
			this.storeRecord(id,(BrowseRecord)record);
		else if (record instanceof LoginRecord)
			this.storeRecord(id,(LoginRecord)record);
		else if (record instanceof GenericRecord)
			this.storeRecord(id,(GenericRecord)record);
		else if (record instanceof ContentRecord)
			this.storeRecord(id,(ContentRecord)record);
		else if (record instanceof QuickSearchRecord)
			this.storeRecord(id,(QuickSearchRecord)record);
		else if (record instanceof  GoogleSearchRecord)
			this.storeRecord(id,(GoogleSearchRecord)record);
		else if (record instanceof  HLRecord)
			this.storeRecord(id,(HLRecord)record);
		else if (record instanceof  AISRecord)
			this.storeRecord(id,(AISRecord)record);
		else if (record instanceof  TSRecord)
			this.storeRecord(id,(TSRecord)record);
		else if (record instanceof  AnnotationRecord)
			this.storeRecord(id,(AnnotationRecord)record);
		else if (record instanceof  DocumentWorkflowRecord)
			this.storeRecord(id,(DocumentWorkflowRecord)record);
		else if (record instanceof  WebAppRecord)
			this.storeRecord(id,(WebAppRecord)record);
		else if (record instanceof  WarRecord)
			this.storeRecord(id,(WarRecord)record);
		else if (record instanceof  ReportRecord)
			this.storeRecord(id,(ReportRecord)record);
		else if (record instanceof  AquamapsRecord)
			this.storeRecord(id,(AquamapsRecord)record);
		else if (record instanceof  StatisticalManagerRecord)
			this.storeRecord(id,(StatisticalManagerRecord)record);
		


	}

	private synchronized void storeRecord(String id, AdvancedSearchRecord record){

		for (String collectionid : record.getCollections().keySet()){
			try {
				String expression = "INSERT INTO COLLECTION VALUES ('"+
						id+"','"
						+collectionid+"','"
						+record.getCollections().get(collectionid)+"');";
				this.update(expression);
			} catch (Exception e) {
				logger.error("Error updating DB", e);
			}
		}

		for (String term : record.getTerms().keySet()){
			try {
				String expression = "INSERT INTO TERM VALUES ('"+
						id+"','"
						+term+"','"
						+record.getTerms().get(term)+"');";
				this.update(expression);
			} catch (Exception e) {
				logger.error("Error updating DB", e);
			}
		}
		try {
			String expression = "INSERT INTO ADVANCEDSEARCH VALUES ('"+
					id+"','"
					+record.getOperator()+"');";
			this.update(expression);
		} catch (Exception e) {
			logger.error("Error updating DB", e);
		}
	} 

	private synchronized void storeRecord(String id, SimpleSearchRecord record){

		for (String collectionid : record.getCollections().keySet()){
			try {
				String expression = "INSERT INTO COLLECTION VALUES ('"+
						id+"','"
						+collectionid+"','"
						+record.getCollections().get(collectionid)+"');";
				this.update(expression);
			} catch (Exception e) {
				logger.error("Error updating DB", e);
			}
		}

		try {
			String expression = "INSERT INTO SIMPLESEARCH VALUES ('"+
					id+"','"
					+record.getTerm()+"');";
			this.update(expression);
		} catch (Exception e) {
			logger.error("Error updating DB", e);
		}
	} 

	private synchronized void storeRecord(String id, BrowseRecord record){

		for (String collectionid : record.getCollections().keySet()){
			try {
				String expression = "INSERT INTO COLLECTION VALUES ('"+
						id+"','"
						+collectionid+"','"
						+record.getCollections().get(collectionid)+"');";
				this.update(expression);
			} catch (Exception e) {
				logger.error("Error updating DB", e);
			}
		}

		try {
			String expression = "INSERT INTO BROWSE VALUES ('"+
					id+"','"
					+record.getBrowseBy()+"','"
					+(record.isDistinct()?1:0)+"');";
			this.update(expression);
		} catch (Exception e) {
			logger.error("Error updating DB", e);
		}
	} 

	private synchronized void storeRecord(String id, LoginRecord record){

		try {
			String expression = "INSERT INTO LOGIN VALUES ('"+
					id+"','"
					+record.getMessage()+"');";
			this.update(expression);
		} catch (Exception e) {
			logger.error("Error updating DB", e);
		}
	} 

	private synchronized void storeRecord(String id, GenericRecord record){

		try {
			String expression = "INSERT INTO GENERIC VALUES ('"+
					id+"','"
					+record.getMessage()+"');";
			this.update(expression);
		} catch (Exception e) {
			logger.error("Error updating DB", e);
		}
	} 

	private synchronized void storeRecord(String id, ContentRecord record){

		try {
			String expression = "INSERT INTO CONTENT VALUES ('"+
					id+"','"
					+record.getContentId()+"','"
					+record.getContentName()+"');";
			this.update(expression);
		} catch (Exception e) {
			logger.error("Error updating DB", e);
		}
	}

	private synchronized void storeRecord(String id, GoogleSearchRecord record){

		try {
			String expression = "INSERT INTO GOOGLESEARCH VALUES ('"+
					id+"','"
					+record.getTerm()+"');";
			this.update(expression);
		} catch (Exception e) {
			logger.error("Error updating DB", e);
		}
	}

	private synchronized void storeRecord(String id, QuickSearchRecord record){

		try {
			String expression = "INSERT INTO QUICKSEARCH VALUES ('"+
					id+"','"
					+record.getTerm()+"');";
			this.update(expression);
		} catch (Exception e) {
			logger.error("Error updating DB", e);
		}
	}

	private synchronized void storeRecord(String id, TSRecord record){

		try {
			String expression = "INSERT INTO TS VALUES ('"+
					id+"','"
					+record.getTitle()+"','"
					+record.getTSsubType().name()+"');";
			this.update(expression);
		} catch (Exception e) {
			logger.error("Error updating DB", e);
		}
	}

	private synchronized void storeRecord(String id, AISRecord record){

		try {
			String expression = "INSERT INTO AIS VALUES ('"+
					id+"','"
					+record.getID()+"','"
					+record.getName()+"','"
					+record.getAISsubType().name()+"');";
			this.update(expression);
		} catch (Exception e) {
			logger.error("Error updating DB", e);
		}
	}

	private synchronized void storeRecord(String id, AnnotationRecord record){

		try {
			String expression = "INSERT INTO ANNOTATION VALUES ('"+
					id+"','"
					+record.getAnnotationName()+"','"
					+record.getAnnotationSubType().name()+"','"
					+record.getAnnotationType()+"');";
			this.update(expression);
		} catch (Exception e) {
			logger.error("Error updating DB", e);
		}

		try {
			String expression = "INSERT INTO CONTENT VALUES ('"+
					id+"','"
					+record.getObjectID()+"','"
					+record.getObjectName()+"');";
			this.update(expression);
		} catch (Exception e) {
			logger.error("Error updating DB", e);
		}
	}

	private synchronized void storeRecord(String id, HLRecord record){

		try {
			if (record.getHLsubType().compareTo(HLSubType.HL_WORKSPACE_CREATED)==0)
			{
				String expression = "INSERT INTO HL VALUES ('"+
						id+"','"
						+"NULL','"
						+"NULL','"
						+"NULL','"
						+record.getHLsubType().name()+"');";
				this.update(expression);
			}
			else 
			{
				String expression = "INSERT INTO HL VALUES ('"+
						id+"','"
						+record.getID()+"','"
						+record.getName()+"','"
						+record.getType()+"','"
						+record.getHLsubType().name()+"');";
				this.update(expression);

				if(record.getHLsubType().compareTo(HLSubType.HL_ITEM_SENT)==0)
				{
					for (GCUBEUser user : record.getAddresseesUsers()){
						String expr = "INSERT INTO GCUBEUSERSADDRESSEES VALUES ('"+
								id+"','"
								+user.getUser()+"','"
								+user.getVre()+"');";
						this.update(expr);
					}
				}		
			}
		} catch (Exception e) {
			logger.error("Error updating DB", e);
		}
	}

	private synchronized void storeRecord(String id, ReportRecord record){

		try {
			String expression = "";
			if (!(record.getSubType().compareTo(ReportSubType.GENEARATE_REPORT_OUTPUT)==0))	
			{
				expression = "INSERT INTO REPORT VALUES ('"+
						id+"','"
						+record.getSubType().name()+"','"
						+record.getTemplateID()+"','"
						+record.getTemplateName()+"','"
						+((record.getSubType().compareTo(ReportSubType.CREATE_REPORT)==0||
						record.getSubType().compareTo(ReportSubType.OPEN_REPORT)==0)?"NULL','":record.getAuthor()+"','")
						+"NULL','"
						+"NULL','"
						+"NULL');";

			}
			else 
			{
				expression = "INSERT INTO REPORT VALUES ('"+
						id+"','"
						+record.getSubType().name()+"','"
						+"NULL','"
						+"NULL','"
						+"NULL','"
						+record.getName()+"','"
						+record.getMimetype()+"','"
						+record.getType()+"');";

			}
			this.update(expression);
		}catch (Exception e) {
			logger.error("Error updating DB", e);
		}
	}


	private synchronized void storeRecord(String id, DocumentWorkflowRecord record){

		try {
			String 	expression = "INSERT INTO DOCUMENTWORKFLOW VALUES ('"+
					id+"','"
					+record.getSubType().name()+"','"
					+record.getWorkflowid()+"','"
					+record.getReportname()+"','"
					+((record.getSubType().compareTo(WorkflowSubType.CREATED_WORKFLOWREPORT_OUTPUT)==0)?"NULL','":record.getStepsNumber()+"','")
					+((record.getSubType().compareTo(WorkflowSubType.DELETED_WORKFLOWREPORT_OUTPUT)==0)?"NULL');":record.getStatus()+"');");
			this.update(expression);
		}catch (Exception e) {
			logger.error("Error updating DB", e);
		}

	}

	private synchronized void storeRecord(String id, WebAppRecord record){

		try {
			String 	expression = "INSERT INTO WEBAPP VALUES ('"+
					id+"','"
					+record.getSubType().name()+"');";
			this.update(expression);

			for (GHN ghn : record.getGhns()){
				String expr = "INSERT INTO GHNDETAIL VALUES ('"+
						id+"','"
						+ghn.getGHN_ID()+"','"
						+ghn.getGHN_NAME()+"');";
				this.update(expr);
			}

			for (WebApplication webApp : record.getWebapplications()){
				String expr = "INSERT INTO WEBAPPDETAIL VALUES ('"+
						id+"','"
						+webApp.getWEB_APPLICATION_ID()+"','"
						+webApp.getWEB_APPLICATION_NAME()+"','"
						+webApp.getWEB_APPLICATION_VERSION()+"');";
				this.update(expr);
			}

		}catch (Exception e) {
			logger.error("Error updating DB", e);
		}

	}
	
	private synchronized void storeRecord(String id, WarRecord record){

		try {
			String 	expression = "INSERT INTO WAR VALUES ('"+
					id+"','"
					+record.getSubType().name()+"','"
					+record.getWarId()+"','"
					+((record.getSubType().compareTo(WarSubType.WAR_REMOVED)==0)?"NULL','":record.getWarName()+"','")
					+((record.getSubType().compareTo(WarSubType.WAR_REMOVED)==0)?"NULL','":record.getAppName()+"','")
					+((record.getSubType().compareTo(WarSubType.WAR_REMOVED)==0)?"NULL','":record.getAppVersion()+"','")
					+((record.getSubType().compareTo(WarSubType.WAR_REMOVED)==0)?"NULL');":record.getCategory()+"');");
			this.update(expression);
		}catch (Exception e) {
			logger.error("Error updating DB", e);
		}

	}
	
	private synchronized void storeRecord(String id, AquamapsRecord record){

		try {
			String 	expression = "INSERT INTO AQUAMAPS VALUES ('"+
					id+"','"
					+record.getTitle()+"','"
					+record.getType()+"','"
					+record.getAquamapsSubtype().name()+"','"
					+record.getSpeciesCount()+"','"
					+record.isGis()+"','"
					+((record.getAquamapsSubtype().compareTo(AquamapsSubType.AquamapsSavedItem)==0)?"NULL','":record.getHspecId()+"','")
					+((record.getAquamapsSubtype().compareTo(AquamapsSubType.AquamapsGeneration)==0)?"NULL');":record.getObjectID()+"');");
			this.update(expression);
		}catch (Exception e) {
			logger.error("Error updating DB", e);
		}

	}
	
	
	private synchronized void storeRecord(String id, StatisticalManagerRecord record){
		
		String 	expression ="";		
		
		try {
			if ((record.getType().compareTo(StatisticalManagerSubType.STATISTICALMANAGER_EXECUTION)==0))	{
			
				 	expression = "INSERT INTO SM VALUES ('"+
					id+"','"
					+record.getType().name()+"','"
					+"NULL','"
					+"NULL','"
					+record.getAlgorithmName()+"','"
					+record.getExecutionOutcome()+"','"
					+record.getExecutionSecondsTime()+"');";
			}else {
				 	expression = "INSERT INTO SM VALUES ('"+
						id+"','"
						+record.getType().name()+"','"
						+record.getFileName()+"','"
						+record.getFileType()+"','"
						+"NULL','"
						+"NULL','"
						+"NULL');";
			}
				
			this.update(expression);
		}catch (Exception e) {
			logger.error("Error updating DB", e);
		}

	}



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
