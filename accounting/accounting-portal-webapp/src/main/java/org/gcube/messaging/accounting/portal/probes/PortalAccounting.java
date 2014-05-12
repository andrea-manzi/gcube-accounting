package org.gcube.messaging.accounting.portal.probes;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

import org.gcube.messaging.accounting.portal.logparser.AccessLogParser;
import org.gcube.messaging.accounting.portal.logparser.entry.LogEntry;
import org.gcube.messaging.common.messages.PortalAccountingMessage;
import org.gcube.messaging.common.messages.records.BaseRecord;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PortalAccounting {
	
	static Logger logger = LoggerFactory.getLogger(PortalAccounting.class);
	
	
	public static String logsLocation = System.getenv("CATALINA_TMPDIR")+File.separator+"accessLogs";

	@SuppressWarnings("unchecked")
	public static ArrayList<PortalAccountingMessage<?>> groupAccountingMessages(ArrayList<LogEntry> entries,ParseFilters filter){

		ArrayList<PortalAccountingMessage<?>> messages = new ArrayList<PortalAccountingMessage<?>>();
		
		PortalAccounting p = new PortalAccounting();

		HashMap<String,HashMap<Class<? extends BaseRecord>,PortalAccountingMessage<? extends BaseRecord>>> messageMap =
			new HashMap<String,HashMap<Class<? extends BaseRecord>,PortalAccountingMessage<? extends BaseRecord>>> ();

		for (LogEntry entry :entries){
			
			//check if the user is not banned
			if (filter != null && filter.applyFilters(entry.getUser().trim()))
			{
				logger.error("The Log Entry Record belongs to a banned user: " +entry.getUser()+", DISCARDED!");
				continue;
			} 
			
			
			MapKey<String,String> key = p.new MapKey<String,String> (entry.getUser(),entry.getVre());
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


	public static ArrayList<PortalAccountingMessage<?>> createAccoutingMessages(File input,ParseFilters filter){

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
