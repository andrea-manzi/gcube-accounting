package org.gcube.common.accounting.node.logparser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import org.gcube.common.accounting.node.util.DateInterval;
import org.gcube.common.accounting.node.util.Util;

/**
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public class RIInvocationParser {

	private String fileName;

	private static String invalidScope = "INVALID";

	private DateInterval interval;

	public  RIInvocationParser(){}

	public  RIInvocationParser(String fileName){
		this.fileName = fileName;
		this.interval = new DateInterval(new Long(0));
	}

	public  RIInvocationParser(String fileName,DateInterval interval){
		this.fileName = fileName;
		this.interval = interval;
	}

	private ArrayList<LogEntry> entryList = new ArrayList<LogEntry>();

	/**
	 * 
	 * @author Andrea Manzi(CERN)
	 *
	 */
	public enum EntryType {
		ENDCALL("END CALL"),
		STARTCALL("START CALL");
		String type;
		EntryType(String type) {this.type = type;}
		public String toString() {return this.type;}
	};

	/**
	 * 
	 * @throws IOException
	 * @throws ParseException
	 */
	public void parse() throws IOException, ParseException {
		String line;
		LogEntry entry;
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(new File(fileName)));
			while ((line = reader.readLine()) != null) {
				if ((entry = parseLine(line))!=null)
					entryList.add(entry);
			}
		}
		finally{
			reader.close();
		}

	}

	private LogEntry parseLine(String line) throws ParseException{
		LogEntry entry = null;
		if (line.contains(EntryType.ENDCALL.type) && !(line.contains(invalidScope))) {
			String time = line.substring(0,line.indexOf(","));
			if (this.interval.contains(Util.format.parse(time)))
				entry = new LogEntry(EntryType.ENDCALL,line.substring(line.indexOf(EntryType.ENDCALL.type)),time);
		}
		return entry;
	}

	/**
	 * get EntryList
	 * @return EntryList
	 */
	public ArrayList<LogEntry> getEntryList() {
		return entryList;
	}

	/** 
	 * set EntryList
	 * @param entryList entrylist
	 */
	public void setEntryList(ArrayList<LogEntry> entryList) {
		this.entryList = entryList;
	}

}
