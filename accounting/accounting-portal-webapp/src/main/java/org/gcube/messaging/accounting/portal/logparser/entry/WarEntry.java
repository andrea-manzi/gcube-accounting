package org.gcube.messaging.accounting.portal.logparser.entry;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.gcube.messaging.accounting.portal.logparser.AccessLogParser.EntryType;
import org.gcube.messaging.common.messages.records.BaseRecord;
import org.gcube.messaging.common.messages.records.WarRecord;
import org.gcube.messaging.common.messages.records.WebAppRecord;
import org.gcube.messaging.common.messages.records.WebAppRecord.GHN;
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
	private StringTokenizer tokenizer = null;
	
	//webapp 
	private String []warEntry = new String [5];

	
	protected static final String hlAddresssesSeparator= ";";
	protected static final String hlAddresssesSeparator2= ":";
	

	protected enum WarTokens {
		CATEGORY_NAME("CATEGORY_NAME"),
		ID("ID"),
		NAME("NAME"),
		APPLICATION_NAME("APPLICATION_NAME"),
		APPLICATION_VERSION("APPLICATION_VERSION");
		String tokens;
		WarTokens(String tokens) {this.tokens = tokens;}
		public String toString() {return this.tokens;}
	}
	
	
	protected static ArrayList<GHN> getGHN(String line){
		ArrayList<GHN> ghns = new ArrayList<GHN>();
		String ghnString = line.substring(line.indexOf(LogEntry.parameterValueSeparator)+1).trim();
		StringTokenizer tokenizer = new StringTokenizer(ghnString,hlAddresssesSeparator);
		while(tokenizer.hasMoreElements()){
			String tok = tokenizer.nextToken();
			
			GHN ghn = new  WebAppRecord().new GHN();
			ghn.setGHN_ID(tok.substring(0,tok.indexOf(hlAddresssesSeparator2)));
			ghn.setGHN_NAME(tok.substring(tok.indexOf(hlAddresssesSeparator2)+1));
			ghns.add(ghn);
		}
		return ghns;
		
	} 
	
	@Override
	public BaseRecord fillRecord() {
		
		tokenizer = new StringTokenizer(message.getMessage(),Message.messageTokensSeparator);
		
		while(tokenizer.hasMoreElements()){
			String tok = tokenizer.nextToken();
			if (tok.contains(WarTokens.ID.tokens))
				warEntry[0] = Message.getValue(tok);
			else if (tok.contains(WarTokens.APPLICATION_NAME.tokens))
				warEntry[1] = Message.getValue(tok);
			else if (tok.contains(WarTokens.APPLICATION_VERSION.tokens))
				warEntry[2] = Message.getValue(tok);
			else if (tok.contains(WarTokens.CATEGORY_NAME.tokens))
				warEntry[3] = Message.getValue(tok);
			else if (tok.contains(WarTokens.NAME.tokens))
				warEntry[4] = Message.getValue(tok);
		}
		record.setDate(this.getDate());
		((WarRecord)record).setSubType(WarRecord.WarSubType.valueOf(this.getEntryType().name()));	
		if (this.getEntryType().compareTo(EntryType.WAR_REMOVED)==0 )
			((WarRecord)record).setWarId(warEntry[0]);
		else {
			((WarRecord)record).setWarId(warEntry[0]);
			((WarRecord)record).setAppName(warEntry[1]);
			((WarRecord)record).setAppVersion(warEntry[2]);
			((WarRecord)record).setCategory(warEntry[3]);
			((WarRecord)record).setWarName(warEntry[4]);
			
		}
		return record;
	}

}
