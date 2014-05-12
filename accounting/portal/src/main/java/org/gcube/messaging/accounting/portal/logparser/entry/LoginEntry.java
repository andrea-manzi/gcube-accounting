package org.gcube.messaging.accounting.portal.logparser.entry;


import java.text.ParseException;

import org.gcube.messaging.accounting.portal.logparser.AccessLogParser.EntryType;
import org.gcube.messaging.common.messages.records.BaseRecord;
import org.gcube.messaging.common.messages.records.LoginRecord;

/**
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public class LoginEntry extends LogEntry{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * constructor
	 * @param line line to parse 
	 * @throws ParseException Exception
	 */
	public LoginEntry(String line) throws ParseException{
		super();
		this.line = line;
		entryType = EntryType.Login_To_VRE;
		record = new LoginRecord();
		parse();
		}

	@Override
	public BaseRecord fillRecord() {
		record.setDate(this.getDate());
		((LoginRecord)record).setMessage(this.getMessage().getMessage());
		return record;
	}

}
