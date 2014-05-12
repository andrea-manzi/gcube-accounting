package org.gcube.messaging.accounting.portal.logparser.entry;

import java.text.ParseException;
import java.util.StringTokenizer;

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

	private StringTokenizer tokenizer = null;

	//ID, NAME
	private String []aisentry = new String [2];
		
		
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

	
	protected enum AISTokens {
		ID("ID"),	
		NAME("NAME");
		String tokens;
		AISTokens(String tokens) {this.tokens = tokens;}
		public String toString() {return this.tokens;}
	}
	


	@Override
	public BaseRecord fillRecord() {
		
		tokenizer = new StringTokenizer(message.getMessage(),Message.messageTokensSeparator);
		while(tokenizer.hasMoreElements()){
			String tok = tokenizer.nextToken();
			if (tok.contains(AISTokens.ID.tokens))
				aisentry[0] = Message.getValue(tok);
			else if (tok.contains(AISTokens.NAME.tokens))
				aisentry[1] = Message.getValue(tok);	
		}
		
		((AISRecord)record).setDate(this.getDate());
		((AISRecord)record).setID(aisentry[0]);
		((AISRecord)record).setName(aisentry[1]);
		((AISRecord)record).setAISsubType(AISRecord.AISSubType.valueOf(this.getEntryType().name()));
		return record;
	}
}