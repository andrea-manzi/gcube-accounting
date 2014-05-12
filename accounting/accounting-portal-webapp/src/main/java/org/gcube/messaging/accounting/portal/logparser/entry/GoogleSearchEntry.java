package org.gcube.messaging.accounting.portal.logparser.entry;

import java.text.ParseException;
import java.util.StringTokenizer;

import org.gcube.messaging.accounting.portal.logparser.AccessLogParser.EntryType;
import org.gcube.messaging.accounting.portal.logparser.entry.AdvancedSearchEntry.SearchAndBrowseTokens;
import org.gcube.messaging.common.messages.records.BaseRecord;
import org.gcube.messaging.common.messages.records.GoogleSearchRecord;

/**
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public class GoogleSearchEntry extends LogEntry {
	
	/**

	 */
	private static final long serialVersionUID = 1L;

	private StringTokenizer tokenizer = null;
	
	private String term;

	
	/**
	 * contructor
	 * @param line the line to parse
	 * @throws ParseException exception
	 */
	public GoogleSearchEntry(String line) throws ParseException{
		super();
		this.line = line;
		entryType = EntryType.Google_Search;
		record = new GoogleSearchRecord();
		parse();
	}

	@Override
	public BaseRecord fillRecord() {
		tokenizer = new StringTokenizer(message.getMessage(),Message.messageTokensSeparator);
		while(tokenizer.hasMoreElements()){
			String tok = tokenizer.nextToken();
			 if (tok.contains(SearchAndBrowseTokens.term.tokens))
					term=  Message.getValue(tok);
		}
		record.setDate(this.getDate());
		((GoogleSearchRecord)record).setTerm(term);
		return record;
	}

	
}