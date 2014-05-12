package org.gcube.messaging.accounting.portal.logparser.entry;

import java.text.ParseException;

import org.gcube.messaging.accounting.portal.logparser.AccessLogParser.EntryType;
import org.gcube.messaging.common.messages.records.BaseRecord;
import org.gcube.messaging.common.messages.records.GoogleSearchRecord;

/**
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public class GoogleSearchEntry extends LogEntry {
	
	/**

	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * contructor
	 * @param line the line to parse
	 * @throws ParseException exception
	 */
	public GoogleSearchEntry(String line) throws ParseException{
		super();
		this.line = line;
		entryType = EntryType.Google_Search;
		record = new GoogleSearchRecord();
		parse();
	}

	@Override
	public BaseRecord fillRecord() {
		record.setDate(this.getDate());
		((GoogleSearchRecord)record).setTerm(this.getMessage().getTerm());
		return record;
	}

	
}