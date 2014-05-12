package org.gcube.messaging.accounting.portal.logparser.entry;


import java.text.ParseException;

import org.gcube.messaging.accounting.portal.logparser.AccessLogParser.EntryType;
import org.gcube.messaging.common.messages.records.BaseRecord;
import org.gcube.messaging.common.messages.records.GenericRecord;


/**
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public class GenericEntry extends LogEntry {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GenericEntry(String line) throws ParseException{
		super();
		this.line = line;
		entryType = EntryType.Generic_Entry;
		record = new GenericRecord();
		parse();
	}

	@Override
	public BaseRecord fillRecord() {
		((GenericRecord)record).setDate(this.getDate());
		((GenericRecord)record).setMessage(this.getMessage().getMessage());
		return record;
	}

}
