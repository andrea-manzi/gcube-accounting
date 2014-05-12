/**
 * 
 */
package org.gcube.messaging.accounting.portal.logparser.entry;

import java.text.ParseException;

import org.gcube.messaging.accounting.portal.logparser.AccessLogParser.EntryType;
import org.gcube.messaging.common.messages.records.BaseRecord;
import org.gcube.messaging.common.messages.records.SimpleSearchRecord;

/**
 * @author Andrea Manzi(CERN)
 *
 */
public class SimpleSearchEntry extends LogEntry{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * constructor
	 * @param line the line to parse
	 * @throws ParseException  exception
	 */
	public SimpleSearchEntry(String line) throws ParseException {
		super();
		this.line = line;
		entryType = EntryType.Simple_Search;
		record = new SimpleSearchRecord();
		parse();
	}

	@Override
	public BaseRecord fillRecord() {
		record.setDate(this.getDate());
		((SimpleSearchRecord)record).setCollections(this.getMessage().getCollections());
		((SimpleSearchRecord)record).setTerm(this.getMessage().getTerm());
		return record;
	}
}
