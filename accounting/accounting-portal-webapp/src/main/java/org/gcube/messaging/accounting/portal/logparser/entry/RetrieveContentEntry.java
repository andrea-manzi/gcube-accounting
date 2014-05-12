package org.gcube.messaging.accounting.portal.logparser.entry;

import java.text.ParseException;
import java.util.StringTokenizer;

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
	
	private StringTokenizer tokenizer = null;
	
	private String []content = new String [2];
	


	public RetrieveContentEntry(String line) throws ParseException{
		super();
		this.line = line;
		entryType = EntryType.Retrieve_Content;
		record = new ContentRecord();
		parse();
	}

	
	protected enum RetrieveContentTokens {
		contentID("contentID"),
		contentName("contentName");
		String tokens;
		RetrieveContentTokens(String tokens) {this.tokens = tokens;}
		public String toString() {return this.tokens;}
	}
	

	
	@Override
	public BaseRecord fillRecord() {
		tokenizer = new StringTokenizer(this.getMessage().getMessage(),Message.messageTokensSeparator);
		while(tokenizer.hasMoreElements()){
			String tok = tokenizer.nextToken();
			if (tok.contains(RetrieveContentTokens.contentID.tokens))
				content[0] = Message.getValue(tok);
			else if (tok.contains(RetrieveContentTokens.contentName.tokens))
				content[1] = Message.getValue(tok);
		}
		((ContentRecord)record).setDate(this.getDate());
		((ContentRecord)record).setContentId(content[0]);
		((ContentRecord)record).setContentName(content[1]);
		return record;
	}

}