package org.gcube.messaging.accounting.portal.logparser.entry;

import java.text.ParseException;
import org.gcube.messaging.accounting.portal.logparser.AccessLogParser.EntryType;
import org.gcube.messaging.common.messages.records.BaseRecord;
import org.gcube.messaging.common.messages.records.QuickSearchRecord;

/**
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public class QuickSearchEntry extends LogEntry {
	
private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor
	 * @param line the line to parse
	 * @throws ParseException exception
	 */
	public QuickSearchEntry(String line) throws ParseException{
		super();
		this.line = line;
		entryType = EntryType.Quick_Search;
		record = new QuickSearchRecord();
		parse();
	}

	@Override
	public BaseRecord fillRecord() {
		record.setDate(this.getDate());
		((QuickSearchRecord)record).setTerm(this.getMessage().getTerm());
		return record;
	}
}
