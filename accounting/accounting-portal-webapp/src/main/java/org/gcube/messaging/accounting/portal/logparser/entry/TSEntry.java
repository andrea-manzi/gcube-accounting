package org.gcube.messaging.accounting.portal.logparser.entry;

import java.text.ParseException;
import java.util.StringTokenizer;

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
	
	private StringTokenizer tokenizer = null;

	private String tstitle = null;

	
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


	protected enum TSTokens {
		TITLE("TITLE");
		String tokens;
		TSTokens(String tokens) {this.tokens = tokens;}
		public String toString() {return this.tokens;}
	}
	
	
	@Override
	public BaseRecord fillRecord() {
		tokenizer = new StringTokenizer(message.getMessage(),Message.messageTokensSeparator);
		while(tokenizer.hasMoreElements()){
			String tok = tokenizer.nextToken();
			if (tok.contains(TSTokens.TITLE.tokens))
				tstitle = Message.getValue(tok);
			}
		record.setDate(this.getDate());
		((TSRecord)record).setTitle(tstitle);
		((TSRecord)record).setTSsubType(TSRecord.TSSubType.valueOf(this.getEntryType().name()));
		return record;
	}
}