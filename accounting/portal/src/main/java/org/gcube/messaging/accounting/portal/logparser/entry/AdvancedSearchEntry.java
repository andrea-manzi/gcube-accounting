/**
 * 
 */
package org.gcube.messaging.accounting.portal.logparser.entry;

import java.text.ParseException;

import org.gcube.messaging.accounting.portal.logparser.AccessLogParser.EntryType;
import org.gcube.messaging.common.messages.records.AdvancedSearchRecord;
import org.gcube.messaging.common.messages.records.BaseRecord;



/**
 * @author Andrea Manzi(CERN)
 *
 */
public class AdvancedSearchEntry extends LogEntry {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * constructor
	 * @param line the line to parse
	 * @throws ParseException ParseException
	 */
	public AdvancedSearchEntry(String line) throws ParseException{
		super();
		this.line = line;
		record= new AdvancedSearchRecord();
		entryType=EntryType.Advanced_Search;
		parse();
	}

	@Override
	public BaseRecord fillRecord() {
		
		record.setDate(this.getDate());
		((AdvancedSearchRecord)record).setTerms(this.getMessage().getTerms());
		((AdvancedSearchRecord)record).setCollections(this.getMessage().getCollections());
		((AdvancedSearchRecord)record).setOperator(AdvancedSearchRecord.OperatorType.valueOf(this.getMessage().getOperator()));
		return record;
	}

}
