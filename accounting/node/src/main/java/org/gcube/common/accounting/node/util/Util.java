package org.gcube.common.accounting.node.util;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


import org.gcube.common.accounting.node.logparser.LogEntry;
import org.gcube.common.accounting.node.logparser.RIInvocationParser;
import org.gcube.common.accounting.node.persistence.PersistenceAdapter;
import org.gcube.common.accounting.node.persistence.RIAccountingData;
import org.gcube.common.core.scope.GCUBEScope;
import org.gcube.common.core.utils.logging.GCUBELog;


import org.gcube.messaging.common.messages.NodeAccountingMessage;
import org.gcube.messaging.common.messages.records.IntervalRecord;

/**
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public class Util {

	
	public  static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public  static final String DATE_FORMAT_DAY = "yyyy-MM-dd";
	
	public	 static SimpleDateFormat format= new SimpleDateFormat(DATE_FORMAT);
	public	 static SimpleDateFormat format_day= new SimpleDateFormat(DATE_FORMAT_DAY);
	
	GCUBELog logger = null;
	
	public Util (GCUBELog logger){
		this.logger= logger;
	}
	
	protected  ArrayList<NodeAccountingMessage<IntervalRecord>> groupAccountingMessages(ArrayList<LogEntry> entries,DateInterval interval){
		//Message to send
		ArrayList<NodeAccountingMessage<IntervalRecord>> messages = new ArrayList<NodeAccountingMessage<IntervalRecord>>();		
		
		/*Big data structure...contains the Node accounting aggregated information.
			for each scope and for each service we creates a message that contains aggregated IntervalRecord 
		    for each caller IP 
		*/        
		HashMap<GCUBEScope,HashMap<String,HashMap<String,InvocationInfo>>> mapInfo = 
			new HashMap<GCUBEScope,HashMap<String,HashMap<String,InvocationInfo>>> ();
		
		for (LogEntry entry :entries){
			
			HashMap<String,HashMap<String,InvocationInfo>> mapService= null;
			HashMap<String,InvocationInfo> mapIP = null;
			
			InvocationInfo info = null;
			if ((mapService = mapInfo.get(entry.getScope()))== null)
				mapService = new HashMap<String,HashMap<String,InvocationInfo>>();
			if ((mapIP = mapService.get(entry.getInfo().toString()))== null)
				mapIP = new HashMap<String,InvocationInfo>();
			 
			if ((info =  mapIP.get(entry.getInfo().getCallerIP())) == null)
				info = new InvocationInfo();
			info.addInvocation(entry.getInvocationTime());
			info.setTime(entry.getTime());
			info.setIP(entry.getInfo().getCallerIP());
		
			mapIP.put(entry.getInfo().getCallerIP(), info);
			mapService.put(entry.getInfo().toString(), mapIP);
			mapInfo.put(entry.getScope(),mapService);
		}

		for (GCUBEScope scope :mapInfo.keySet()) {
			for (String info :mapInfo.get(scope).keySet()){
				NodeAccountingMessage<IntervalRecord> message = new NodeAccountingMessage<IntervalRecord>();
				message.setCallScope(scope.toString());
				message.setServiceClass(getClazz(info));
				message.setServiceName(getName(info));
				for (String ip:mapInfo.get(scope).get(info).keySet()){
						IntervalRecord record =  new IntervalRecord(interval.getInterval()/1000);//seconds
						record.setStartInterval(interval.getStartDate().getTime());
						record.setEndInterval(interval.getEndDate().getTime());
						record.setAverageInvocationTime(mapInfo.get(scope).get(info).get(ip).getAverageInvocationTime());
						record.setInvocationNumber(mapInfo.get(scope).get(info).get(ip).getInvocationCount());
						record.setIP(ip);
						message.setTime(mapInfo.get(scope).get(info).get(ip).getTime());
						message.setRecord(ip,record);		
				}
				messages.add(message);	
			}
		}
		return messages;
	}
	
	/**
	 * generateDateIntervals	
	 * @param adapter
	 * @param interval  
	 * @return
	 */
	public ArrayList<DateInterval> generateDateIntervals(PersistenceAdapter adapter,Long interval){
		ArrayList<DateInterval> intervals = new ArrayList<DateInterval>();
		//using ms interval
		long msinterval = interval*1000;
		Calendar start = null;
		if (( start = adapter.getLastUpdate()) == null) {	
			intervals.add(new DateInterval(msinterval));
		}else {
			//counting number of intervals to process
			long starts = start.getTimeInMillis();
			long ends = Calendar.getInstance().getTimeInMillis();
			long nInt= (ends-starts)/msinterval;
			if (nInt == 0) nInt=1;
			//long lastInterval= (ends-starts)%msinterval;
				
			long temps = starts;
			Calendar starttmp = Calendar.getInstance();
			Calendar endtmp = Calendar.getInstance();
			for (int i = 0;i<nInt;i++){
				starttmp.setTime(new Date(temps));
				endtmp.setTime(new Date(temps+msinterval));
				intervals.add(new DateInterval(starttmp,endtmp,msinterval));
				temps=temps+msinterval;
			}
		}
		for (DateInterval inter : intervals){
			logger.debug("Generated Date Interval from " +inter.getStartDateAsString()+ 
					" to " + inter.getEndDateAsString());
		}
		
		return intervals;
		
	}
	
	/**
	 * 
	 * @param files
	 * @param inputIntervals
	 * @return
	 */
	public HashMap<File, ArrayList<DateInterval> > matchIntervalsWithLogFiles(File [] files, ArrayList<DateInterval> inputIntervals){
		
		HashMap<File, ArrayList<DateInterval>> map = new HashMap<File, ArrayList<DateInterval> >();
		
		if (files == null || files.length == 0){
			logger.debug("No Log Files to Parse");
			return map;
		}
		
		for (DateInterval interval : inputIntervals) {
			ArrayList<DateInterval> targetIntervals = null;
			//check if the interval is today
			File fileInput = null;
			if ((interval.getStartDateAsShortString().compareTo(format_day.format(Calendar.getInstance().getTime())))== 0)
			{
				
				//check if teh day logger exists
				for (File file :files){
					if (file.getAbsolutePath().endsWith("access.log")){
						fileInput= file;
						break;
					}
				}
				
			}else //past day
			{
				for (File file :files){
					if (file.getAbsolutePath().endsWith("access.log."+interval.getStartDateAsShortString())){
						fileInput= file;
						break;
					}
				}
				if (fileInput == null){
					//it can be that the log has not been splitted yet, so checking the access.log
					//check if teh day logger exists
					for (File file :files){
						if (file.getAbsolutePath().endsWith("access.log")){
							fileInput= file;
							break;
						}
					}
				}
			}
			if (fileInput == null){
				logger.debug("NO log file found for the interval "+interval.getStartDateAsString()+"/"+interval.getEndDateAsString());
				continue;
			}
			//assign the interval to the file
			if ((targetIntervals = map.get(fileInput))== null){
				targetIntervals= new ArrayList<DateInterval> ();
			}
			logger.debug("Adding interval "+interval.getStartDateAsString()+"/"+interval.getEndDateAsString()+
					" to file "+fileInput.getAbsolutePath());
			targetIntervals.add(interval);
			map.put(fileInput,targetIntervals);
			
		}
		
		return map;
	}
	
	private ArrayList<NodeAccountingMessage<IntervalRecord>> filterMessage ( ArrayList<NodeAccountingMessage<IntervalRecord>> messages, String serviceName, String serviceClass,String scope){
		
		if (messages == null) return new  ArrayList<NodeAccountingMessage<IntervalRecord>> ();
		ArrayList<NodeAccountingMessage<IntervalRecord>> msgs = new ArrayList<NodeAccountingMessage<IntervalRecord>>();
		for (NodeAccountingMessage<IntervalRecord> msg :messages){
			if ((msg.getCallScope().compareTo(scope) == 0) &&
			 (msg.getServiceClass().compareTo(serviceClass) == 0) &&
			 (msg.getServiceName().compareTo(serviceName) == 0))
			{
				msgs.add(msg);
				
			}
		}
		return msgs;
	}
	
	private RIAccountingData updateDataFromMessage(NodeAccountingMessage<IntervalRecord> message,RIAccountingData data ) 
	{
		
		if (message == null) return data;
		Double tempInvocationNumber = new Double(0.0);
		ArrayList<Double> tempInvocationTimeArray = new ArrayList<Double>();
		try {
			for (IntervalRecord record :message.getRecords().values()){
				//adding total number of calls
				data.addCalls(record.getInvocationNumber());
				//adding avg number of calls
				data.getTopCallerData().addCallerInfo(record.getIP(),record.getInvocationNumber());
				
				tempInvocationNumber+=record.getInvocationNumber();
				tempInvocationTimeArray.add(record.getAverageInvocationTime());
				}	
			Double sumInvocationTime = new Double(0.0);
			for (Double number : tempInvocationTimeArray)
				sumInvocationTime+=number;
				
			data.getAvgCallsNumber().addStatistics(tempInvocationNumber);
			//adding avg invocation time				
			if (tempInvocationTimeArray.size() != 0)
				data.getAvgInvocationTime().addStatistics(sumInvocationTime/tempInvocationTimeArray.size());
			else
				data.getAvgInvocationTime().addStatistics(new Double(0.0));
		}catch (Exception e ){
			logger.error("ERROR updating Accounting info from Message" , e);
		}
		return data;
	}
	
	
	public  void updateAccoutingInfo( ArrayList<NodeAccountingMessage<IntervalRecord>> messages ,PersistenceAdapter adapter,DateInterval interval) throws Exception{
	
			for ( String service : adapter.getMap().keySet()){
				
				HashMap<String,RIAccountingData> map = adapter.getMap().get(service);
				
				for (String scope : map.keySet()){
					try {
						RIAccountingData data = map.get(scope);
						
						ArrayList<NodeAccountingMessage<IntervalRecord>> msgs = filterMessage(messages,data.getServiceName(), data.getServiceClass(), scope);
						
						if (msgs.size() >0)
						{
							for (NodeAccountingMessage<IntervalRecord> message : msgs)
								data = updateDataFromMessage(message,data);
							
						}else {
							//adding avg number of calls
							data.getAvgCallsNumber().addStatistics(new Double(0));
							//in case of 0 invocations, the same invocation time as before is reported
							data.getAvgInvocationTime().addStatistics(data.getAvgInvocationTime().getStatistics(new Long(0)));
							//fake caller
							data.getTopCallerData().addCallerInfo("N/A",new Long(0));
						}
						if (messages != null)messages.removeAll(msgs);
						map.put(scope, data);
					}catch (Exception e){
						logger.error("ERROR updating Accounting info for service "+service +" in scope "+scope, e);
						
					}
				}
				adapter.getMap().put(service,map);
			}
			
			if (messages != null){
				//checking if some service has received calls from vre scopes, or new services has been deployed
				for (NodeAccountingMessage<IntervalRecord> message : messages){
					String service = adapter.new MapKey(message.getServiceClass(),message.getServiceName()).getKey();
					try {
						
						HashMap<String,RIAccountingData> map = null;
						if ((map = adapter.getMap().get(service)) ==null) 
							map = new HashMap<String, RIAccountingData>();
						RIAccountingData data = new RIAccountingData(adapter.getInterval());
						data.setServiceClass(message.getServiceClass());
						data.setServiceName(message.getServiceName());
						
						data = updateDataFromMessage(message,data);
						map.put(message.getCallScope(), data);
						adapter.getMap().put(service,map);
					}catch (Exception e){
						logger.error("ERROR updating Accounting info for service "+service +" in scope "+message.getCallScope(), e);
					}
				}
			
			}
			adapter.setLastUpdate(interval.getEndDate());
			adapter.store();
		
		
		
	}

	public ArrayList<NodeAccountingMessage<IntervalRecord>> createAccountingInfo(File input,DateInterval interval) throws IOException,Exception {
		
		logger.debug("Parsing Node Log file"+input.getAbsolutePath());
		RIInvocationParser parser = null;
		try {
			parser = new RIInvocationParser (input.getAbsolutePath(),interval);
			parser.parse();
		} catch (IOException e) {
			logger.error("Error Parsing Node Log file", e);
			throw e;
		} catch (ParseException e) {
			logger.error("Error Parsing Node Log file", e);
			throw e;
		}

		if (parser.getEntryList().size() ==0){
			logger.debug("No data to parse in the given interval");
			return null;
		}
		//setStatistic informations
		return groupAccountingMessages(parser.getEntryList(),interval);

	}

	
	public  static String getClazz(String line){
		return  line.substring(0,line.indexOf("_"));
	}
	
	public static  String getName(String line){
		return  line.substring(line.indexOf("_")+1);
	}

	
	private class InvocationInfo{
		private long invocationCount;
		private double averageInvocationTime;
		private String time;
		private String IP;


		public InvocationInfo(){}

		public double getAverageInvocationTime() {
			return averageInvocationTime/invocationCount;
		}

		public long getInvocationCount() {
			return invocationCount;
		}

		public void addInvocation(Double invocationTime){
			invocationCount++;
			averageInvocationTime+=invocationTime.doubleValue(); 

		}
		
		public String getTime() {
			return time;
		}


		public void setTime(String time) {
			this.time = time;
		}
		
		public String getIP() {
			return this.IP;
		}


		public void setIP(String iP) {
			this.IP = iP;
		}

	}
}
