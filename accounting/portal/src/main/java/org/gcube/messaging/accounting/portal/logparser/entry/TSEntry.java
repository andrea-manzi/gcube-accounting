package org.gcube.messaging.accounting.portal.logparser.entry;

import java.text.ParseException;

import org.gcube.messaging.accounting.portal.logparser.AccessLogParser.EntryType;
import org.gcube.messaging.common.messages.records.BaseRecord;
import org.gcube.messaging.common.messages.records.TSRecord;

/**
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public class TSEntry  extends LogEntry {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	/**
	 * constructor
	 * @param line the line to parse
	 * @param The EntryType
	 * @throws ParseException exception
	 */
	public TSEntry(String line,EntryType type) throws ParseException{
		super();
		this.line = line;
		entryType = type;
		record = new TSRecord();
		parse();
	}


	@Override
	public BaseRecord fillRecord() {
		record.setDate(this.getDate());
		((TSRecord)record).setTitle(this.getMessage().getTstitle());
		((TSRecord)record).setTSsubType(TSRecord.TSSubType.valueOf(this.getEntryType().name()));
		return record;
	}
}