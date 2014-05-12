package org.gcube.messaging.accounting.portal.logparser.entry;

import java.text.ParseException;

import org.gcube.messaging.accounting.portal.logparser.AccessLogParser.EntryType;
import org.gcube.messaging.common.messages.records.BaseRecord;
import org.gcube.messaging.common.messages.records.ContentRecord;

/**
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public class RetrieveContentEntry extends LogEntry {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RetrieveContentEntry(String line) throws ParseException{
		super();
		this.line = line;
		entryType = EntryType.Retrieve_Content;
		record = new ContentRecord();
		parse();
	}

	@Override
	public BaseRecord fillRecord() {
		((ContentRecord)record).setDate(this.getDate());
		((ContentRecord)record).setContentId(this.getMessage().getContent()[0]);
		((ContentRecord)record).setContentName(this.getMessage().getContent()[1]);
		return record;
	}

}