/**
 * 
 */
package org.gcube.messaging.accounting.portal.logparser.entry;

import java.text.ParseException;

import org.gcube.messaging.accounting.portal.logparser.AccessLogParser.EntryType;
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

	public BrowseCollectionEntry(String line) throws ParseException{
		super();
		this.line = line;
		entryType = EntryType.Browse_Collection;
		record = new BrowseRecord(); 
		parse();
	}

	@Override
	public BaseRecord fillRecord() {
		((BrowseRecord)record).setDate(this.getDate());
		((BrowseRecord)record).setBrowseBy(this.getMessage().getBrowseBy());
		((BrowseRecord)record).setDistinct(this.getMessage().isDistinct());
		((BrowseRecord)record).setCollections(this.getMessage().getCollections());
		return record;
	}

}
