package org.gcube.messaging.accounting.portal.logparser.entry;

import java.text.ParseException;

import org.gcube.messaging.accounting.portal.logparser.AccessLogParser.EntryType;
import org.gcube.messaging.common.messages.records.AISRecord;
import org.gcube.messaging.common.messages.records.BaseRecord;

/**
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public class AISEntry  extends LogEntry {

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
	public AISEntry(String line,EntryType type) throws ParseException{
		super();
		this.line = line;
		record = new AISRecord();
		entryType=type;
		parse();
	}


	@Override
	public BaseRecord fillRecord() {
		((AISRecord)record).setDate(this.getDate());
		((AISRecord)record).setID(this.getMessage().getAisentry()[0]);
		((AISRecord)record).setName(this.getMessage().getAisentry()[1]);
		((AISRecord)record).setAISsubType(AISRecord.AISSubType.valueOf(this.getEntryType().name()));
		return record;
	}
}