package org.gcube.messaging.accounting.portal.logparser.entry;

import java.text.ParseException;

import org.gcube.messaging.accounting.portal.logparser.AccessLogParser.EntryType;
import org.gcube.messaging.common.messages.records.BaseRecord;
import org.gcube.messaging.common.messages.records.HLRecord;


/**
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public class HLEntry  extends LogEntry {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	/**
	 * constructor
	 * @param line the line to parse
	 * @param The EntryType
	 * @throws ParseException exception
	 */
	public HLEntry(String line,EntryType type) throws ParseException{
		super();
		this.line = line;
		entryType = type;
		record = new HLRecord();
		parse();
	}


	@Override
	public BaseRecord fillRecord() {
	
		record.setDate(this.getDate());
		((HLRecord)record).setHLsubType(HLRecord.HLSubType.valueOf(this.getEntryType().name()));
		if (this.getEntryType().compareTo(EntryType.HL_WORKSPACE_CREATED)!= 0){
			((HLRecord)record).setID(this.getMessage().getHlentry()[0]);
			((HLRecord)record).setName(this.getMessage().getHlentry()[1]);
			((HLRecord)record).setType(this.getMessage().getHlentry()[2]);
			if (this.getEntryType().compareTo(EntryType.HL_ITEM_SENT)== 0) {
				((HLRecord)record).setAddresseesUsers(this.getMessage().getAddresseeUsers());
			}
		}
		return record;
	}
}