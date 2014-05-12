package org.gcube.messaging.accounting.portal.probes;

import java.io.File;
import java.io.FilenameFilter;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.gcube.messaging.common.messages.GCUBEMessage;
import org.gcube.messaging.common.messages.PortalAccountingMessage;
import org.gcube.messaging.common.producer.ActiveMQClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.gcube.common.portal.PortalContext;

public class PortalAccountingProbe implements Runnable{

	static Logger logger = LoggerFactory.getLogger(PortalAccountingProbe.class);
	
	public void run() {
		 
		ParseFilters filter = null;
		File [] files = getLogFileToParse();
		
		logger.debug("Executing Accounting portal probe");
		
		//creating filters
		try {
			filter = new ParseFilters();
		}catch (Exception e){
			logger.error("Error creating banned user filters file");
		}
		
		String scope ="/"+PortalContext.getConfiguration().getInfrastructureName();
		
		if (files!= null){
			for (File logFile :files){
				try{	
					
					for (PortalAccountingMessage <?>message :PortalAccounting.createAccoutingMessages(logFile,filter)){
						message.setTimeNow();
						message.setScope(scope);
						message.setSourceGHN(InetAddress.getLocalHost().getHostName());
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
	
	private File[] getLogFileToParse() {
		File logsLocationFolder = new File(PortalAccounting.logsLocation);
		return logsLocationFolder.listFiles(new AccessLogFilter());
	}

	private void renameFile(File file) {
		file.renameTo(new File(file.getAbsolutePath()+".OK"));
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

}
