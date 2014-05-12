package org.gcube.messaging.accounting.portal.logparser.entry;

import java.text.ParseException;

import org.gcube.messaging.accounting.portal.logparser.AccessLogParser.EntryType;
import org.gcube.messaging.common.messages.records.BaseRecord;
import org.gcube.messaging.common.messages.records.WarRecord;
/**
 * 
 * @author andrea
 *
 */
public class WarEntry extends LogEntry{
	
private static final long serialVersionUID = -4173528605022856737L;

	
	public WarEntry(String line,EntryType type) throws ParseException{
		super();
		this.line = line;
		entryType = type;
		record = new  WarRecord();
		parse();
	}

	
	@Override
	public BaseRecord fillRecord() {
		record.setDate(this.getDate());
		((WarRecord)record).setSubType(WarRecord.WarSubType.valueOf(this.getEntryType().name()));	
		if (this.getEntryType().compareTo(EntryType.WAR_REMOVED)==0 )
			((WarRecord)record).setWarId(this.getMessage().getWarEntry()[0]);
		else {
			((WarRecord)record).setWarId(this.getMessage().getWarEntry()[0]);
			((WarRecord)record).setAppName(this.getMessage().getWarEntry()[1]);
			((WarRecord)record).setAppVersion(this.getMessage().getWarEntry()[2]);
			((WarRecord)record).setCategory(this.getMessage().getWarEntry()[3]);
			((WarRecord)record).setWarName(this.getMessage().getWarEntry()[4]);
			
		}
		return record;
	}

}
