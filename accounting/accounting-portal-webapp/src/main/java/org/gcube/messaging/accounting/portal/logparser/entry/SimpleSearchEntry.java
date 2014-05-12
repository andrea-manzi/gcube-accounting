/**
 * 
 */
package org.gcube.messaging.accounting.portal.logparser.entry;

import java.text.ParseException;
import java.util.HashMap;
import java.util.StringTokenizer;

import org.gcube.messaging.accounting.portal.logparser.AccessLogParser.EntryType;
import org.gcube.messaging.accounting.portal.logparser.entry.AdvancedSearchEntry.SearchAndBrowseTokens;
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
	
	private HashMap<String,String> collections  = null;
	private String term = null;


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
		collections= new HashMap<String,String>();

		StringTokenizer tokenizer = new StringTokenizer(this.getMessage().getMessage(),Message.messageTokensSeparator);
		while(tokenizer.hasMoreElements()){
			String tok = tokenizer.nextToken();
			if (tok.contains(SearchAndBrowseTokens.collectionName.tokens))
			{
				String [] col= Message.getDetails(tok);
				collections.put(col[0],col[1]);
			}else if (tok.contains(SearchAndBrowseTokens.term.tokens))
				term=  Message.getValue(tok);
		}
		record.setDate(this.getDate());
		((SimpleSearchRecord)record).setCollections(collections);
		((SimpleSearchRecord)record).setTerm(term);
		return record;
	}
}
