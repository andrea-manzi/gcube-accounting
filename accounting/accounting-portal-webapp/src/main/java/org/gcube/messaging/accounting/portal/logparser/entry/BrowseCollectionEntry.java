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
import org.gcube.messaging.common.messages.records.BrowseRecord;


/**
 * @author Andrea 
 *
 */
public class BrowseCollectionEntry extends LogEntry {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String browseBy;
	private boolean isDistinct;
	private HashMap<String,String> collections  = null;
	private HashMap<String,String> terms = null;
	private StringTokenizer tokenizer = null;

	public BrowseCollectionEntry(String line) throws ParseException{
		super();
		this.line = line;
		entryType = EntryType.Browse_Collection;
		record = new BrowseRecord(); 
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
			else if (tok.contains(SearchAndBrowseTokens.Browse_by.tokens))
				browseBy =Message.getValue(tok);
	
		}
		
		((BrowseRecord)record).setDate(this.getDate());
		((BrowseRecord)record).setBrowseBy(browseBy);
		((BrowseRecord)record).setDistinct(isDistinct);
		((BrowseRecord)record).setCollections(collections);
		return record;
	}

}
