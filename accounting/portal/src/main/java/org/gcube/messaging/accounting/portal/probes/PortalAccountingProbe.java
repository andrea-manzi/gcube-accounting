package org.gcube.messaging.accounting.portal.probes;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.gcube.common.core.contexts.GHNContext;
import org.gcube.common.core.monitoring.GCUBEMessage;
import org.gcube.common.core.monitoring.GCUBETestProbe;
import org.gcube.common.core.scope.GCUBEScope;
import org.gcube.messaging.accounting.portal.logparser.AccessLogParser;
import org.gcube.messaging.accounting.portal.logparser.entry.LogEntry;
import org.gcube.messaging.common.messages.PortalAccountingMessage;
import org.gcube.messaging.common.messages.records.*;
import org.gcube.messaging.common.producer.ActiveMQClient;
import org.gcube.messaging.common.producer.GCUBELocalProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public class PortalAccountingProbe extends  GCUBETestProbe{
	
	static Logger logger = LoggerFactory.getLogger(ParseFilters.class);


	private String logsLocation = System.getenv("CATALINA_TMPDIR")+File.separator+"accessLogs";

	@SuppressWarnings("unchecked")
	private ArrayList<PortalAccountingMessage<?>> groupAccountingMessages(ArrayList<LogEntry> entries,ParseFilters filter){

		ArrayList<PortalAccountingMessage<?>> messages = new ArrayList<PortalAccountingMessage<?>>();

		HashMap<String,HashMap<Class<? extends BaseRecord>,PortalAccountingMessage<? extends BaseRecord>>> messageMap =
			new HashMap<String,HashMap<Class<? extends BaseRecord>,PortalAccountingMessage<? extends BaseRecord>>> ();

		for (LogEntry entry :entries){
			
			//check if the user is not banned
			if (filter != null && filter.applyFilters(entry.getUser().trim()))
			{
				logger.error("The Log Entry Record belongs to a banned user: " +entry.getUser()+", DISCARDED!");
				continue;
			} 
			
			MapKey<String,String> key = new MapKey<String,String> (entry.getUser(),entry.getVre());
			//check entries for this user
			HashMap<Class<? extends BaseRecord>,PortalAccountingMessage<? extends BaseRecord>> map = null;

			PortalAccountingMessage message;

			if ((map = messageMap.get(key.getKey()))== null)
				map = new HashMap<Class<? extends BaseRecord>,PortalAccountingMessage<? extends BaseRecord>>();

			Class clazz = entry.getRecord().getClass();
			if ((message = (PortalAccountingMessage) map.get(clazz)) == null){
					message = new PortalAccountingMessage();
					message.setUser(entry.getUser());
					message.setVre(entry.getVre());
				}
			message.addRecord(entry.fillRecord());
			map.put(clazz, message);
			messageMap.put(key.getKey(), map);
		}
		for (String key :messageMap.keySet())
			for (PortalAccountingMessage <?>message :messageMap.get(key).values())
				messages.add(message);
		return messages;
	}


	private ArrayList<PortalAccountingMessage<?>> createAccoutingMessages(File input,ParseFilters filter){

		AccessLogParser parser = new AccessLogParser (input.getAbsolutePath());

		try {
			parser.parse();
		} catch (IOException e) {
			logger.error("Error Parsing portal Log file", e);
		} catch (ParseException e) {
			logger.error("Error Parsing portal Log file", e);
		}

		return groupAccountingMessages(parser.getEntryList(),filter);
	}

	private File[] getLogFileToParse() {
		File logsLocationFolder = new File(logsLocation);
		return logsLocationFolder.listFiles(new AccessLogFilter());
	}

	private void renameFile(File file) {
		file.renameTo(new File(file.getAbsolutePath()+".OK"));
	}
	
	@Override
	public void run() throws Exception {
		ParseFilters filter = null;
		File [] files = getLogFileToParse();
		
		logger.debug("Executing Accounting portal probe");
		
		//creating filters
		try {
			filter = new ParseFilters();
		}catch (Exception e){
			logger.error("Error creating banned user filters file");
		}
		
		if (files!= null){
			for (File logFile :files){
				try{	
					
					for (GCUBEScope scope : GCUBELocalProducer.getMonitoredScope())
						for (PortalAccountingMessage <?>message :createAccoutingMessages(logFile,filter)){
							message.setTimeNow();
							message.setScope(scope.toString());
							message.setSourceGHN(GHNContext.getContext().getHostnameAndPort());
							message.createTopicName(scope);
							Thread.sleep(1000);
							this.sendMessage(message);
						}
					
				}
				catch (Exception e){
					logger.error("Error Executing Accounting probe", e);
				}
				finally{
					renameFile(logFile);
				}
			}
		}else logger.warn("Log file  already processed");
		
	}


	/**
	 *{@inheritDoc}
	 */
	public void sendMessage(GCUBEMessage message) {
		ActiveMQClient.getSingleton().sendMessageToQueue(message);
	}
	
	private static String getTodayLogFile() {
		Date date = new Date();
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
	    return "accessLog"+dateFormat.format(date.getTime())+".log";
	}
	
	private class AccessLogFilter implements FilenameFilter{
		public boolean accept(File file, String name) {
			return name.startsWith("accessLog") && !(name.contains("OK")) && (name.compareTo(getTodayLogFile())!=0);
		}

	}

	private class MapKey<USER ,VRE>{
		private USER user;
		private VRE vre;

		MapKey(USER user ,VRE vre){
			this.user = user;
			this.vre= vre;

		}
		public String getKey() {
			return user+"_"+vre;				
		}

	}


}
