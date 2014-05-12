package org.gcube.common.accounting.node.probes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import org.gcube.common.accounting.node.persistence.PersistenceAdapter;
import org.gcube.common.accounting.node.util.DateInterval;
import org.gcube.common.accounting.node.util.Util;
import org.gcube.common.core.contexts.GHNContext;
import org.gcube.common.core.contexts.GHNContext.Status;
import org.gcube.common.core.monitoring.GCUBETestProbe;
import org.gcube.common.core.scope.GCUBEScope;
import org.gcube.messaging.common.messages.GCUBEMessage;
import org.gcube.messaging.common.messages.NodeAccountingMessage;
import org.gcube.messaging.common.messages.records.IntervalRecord;
import org.gcube.messaging.common.producer.ActiveMQClient;
import org.gcube.messaging.common.producer.GCUBELocalProducer;


/**
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public class NodeAccountingProbe  extends  GCUBETestProbe{

	private String logsLocation = System.getenv("GLOBUS_LOCATION")+File.separator+"logs";

	private PersistenceAdapter persistenceAdapter = null;

	private Util util= null;

	private Long interval =  null;
	
	private boolean updateIS = true;

	private Properties properties= new Properties();

	private class AccessLogFilter implements FilenameFilter{
		public boolean accept(File file, String name) {
			return (name.startsWith("access.log")); 
		}

	}

	private File [] getLogFileToParse() {
		File logsLocationFolder = new File(logsLocation);
		return logsLocationFolder.listFiles(new AccessLogFilter());
	}

	/**
	 * {@inheritDoc}
	 */
	public void run() throws Exception {

		try  {
			//read configuration
			properties.load(new FileInputStream(
					System.getenv("GLOBUS_LOCATION")+File.separator+"config"+
					File.separator+"NodeAccounting.properties")) ;

			this.setInterval(Long.valueOf((String)properties.get("PROBING_INTERVAL")));

			updateIS = Boolean.valueOf((String)properties.get("PUBLISH_ON_IS"));

			if (GHNContext.getContext().getStatus().compareTo(Status.CERTIFIED) !=0) {
				this.setInterval(40);
				logger.debug("GHN not YET Ready, retrying in 40 seconds");
				return;
			}

			util= new Util(logger);

			//moving the interval to seconds:
			interval =  this.getInterval()/1000;
			
			persistenceAdapter= new PersistenceAdapter(logger, interval);
			persistenceAdapter.load();

			//check if the update has to be performed
			if (persistenceAdapter.alreadyUpdated()){
				logger.debug("Node Accounting info already updated, skip to next execution");
				return;

			}
		} catch (Exception e){
			logger.debug("Error on initialization",e);
			throw e;
		}

		HashMap<DateInterval,ArrayList<NodeAccountingMessage<IntervalRecord>>> messages =
			new HashMap<DateInterval,ArrayList<NodeAccountingMessage<IntervalRecord>>>();

		ArrayList<DateInterval> intervals = util.generateDateIntervals(persistenceAdapter,interval);
		//check the last update and calculate update intervals
		HashMap<File,ArrayList<DateInterval>> map= util.matchIntervalsWithLogFiles(
				getLogFileToParse(),intervals);

		if (map.size()== 0) {
			logger.warn("No log File to parse");
			return;
		}
		//generating accounting info and messages 
		for (File file : map.keySet()) {

			for (DateInterval interval : map.get(file)) {
				try {
					logger.debug("Trying to aggregate logs from "+interval.getStartDateAsString() + 
							" to " +interval.getEndDateAsString() + " from File "+ file);
					//put messages into Hash
					messages.put(interval,util.createAccountingInfo(file, interval));

					if (messages.get(interval)!= null ) {
						if (messages.get(interval).size() != 0 ) {
							for (GCUBEScope scope : GCUBELocalProducer.getMonitoredScope()){
								//sending messages
								for (NodeAccountingMessage<IntervalRecord> message :messages.get(interval)){
									message.setScope(scope.toString());
									message.setSourceGHN(GHNContext.getContext().getHostnameAndPort());
									message.createTopicName(scope.toString());
									this.sendMessage(message);
								}
							}
						}
						else {logger.debug("No messages to send"); continue;}
					}else {logger.debug("No messages to send"); continue;}
				}
				catch (IOException e){
					logger.error("Error Reading log file:"+ file, e);
				}
				catch (Exception e){
					logger.error("Error Executing Node Accounting probe on logFile:"+file , e);
				}
			}
		}
		//update info on DB

		for (DateInterval interval :intervals) {
			ArrayList<NodeAccountingMessage<IntervalRecord>> message = messages.get(interval);
			try {
				util.updateAccoutingInfo(message, persistenceAdapter,interval);
			}
			catch (Exception e){
				logger.error("Error Updating Accounting info", e);
			}
		}
		//updating profiles if configured
		if (updateIS){
			for (String ri :persistenceAdapter.getMap().keySet()){
				try {
					RIProfileUpdater updater = new RIProfileUpdater(persistenceAdapter.getMap().get(ri));
					updater.run();
				}

				catch (Exception e){
					logger.error("Error Updating RIProfile", e);
				}
			}
		}
	}


	/**
	 * {@inheritDoc}
	 */
	public void sendMessage(GCUBEMessage message) {
		ActiveMQClient.getSingleton().sendMessageToQueue(message);
	}

	@Override
	public void sendMessage(org.gcube.common.core.monitoring.GCUBEMessage arg0) {
		// TODO Auto-generated method stub
		
	}


}
