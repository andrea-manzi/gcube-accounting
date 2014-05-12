/**
 * 
 */
package org.gcube.messaging.accounting.portal.logparser.entry;

import java.text.ParseException;
import java.util.HashMap;
import java.util.StringTokenizer;

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
	
	
	private String operator;

	private HashMap<String,String> collections  = null;
	private HashMap<String,String> terms = null;
	private StringTokenizer tokenizer = null;
	
	
	
	protected enum SearchAndBrowseTokens {
		collectionName("collectionName"),
		collectionID("collectionID"),
		term("term"),
		value("value"),
		DISTINCT("DISTINCT"),
		Browse_by("Browse by"),
		operator("operator");
		String tokens;
		SearchAndBrowseTokens(String tokens) {this.tokens = tokens;}
		public String toString() {return this.tokens;}
	}
	
	protected enum OperatorType {
		AND("AND"),
		OR("OR"),
		None("None");
		String operator;
		OperatorType(String operator) {this.operator= operator;}
		public String toString() {return this.operator;} 
	}

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
		
		collections= new HashMap<String,String>();
		terms= new HashMap<String,String>();
		
		tokenizer = new StringTokenizer(this.getMessage().getMessage(),Message.messageTokensSeparator);
		
		while(tokenizer.hasMoreElements()){
			String tok = tokenizer.nextToken();
			if (tok.contains(SearchAndBrowseTokens.collectionName.tokens))
			{
				String [] col= Message.getDetails(tok);
				collections.put(col[0],col[1]);
			}else if (tok.contains(SearchAndBrowseTokens.term.tokens))
			{
				String [] term= Message.getDetails(tok);
				terms.put(term[1], term[0]);
			}

			 else if (tok.contains(SearchAndBrowseTokens.operator.tokens))
				operator= Message.getValue(tok);
		}
		
		record.setDate(this.getDate());
		((AdvancedSearchRecord)record).setTerms(terms);
		((AdvancedSearchRecord)record).setCollections(collections);
		((AdvancedSearchRecord)record).setOperator(AdvancedSearchRecord.OperatorType.valueOf(operator));
		return record;
	}

}
