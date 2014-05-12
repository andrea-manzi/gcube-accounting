package org.gcube.messaging.accounting.portal.logparser.entry;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.gcube.messaging.accounting.portal.logparser.AccessLogParser.EntryType;
import org.gcube.messaging.common.messages.records.BaseRecord;
import org.gcube.messaging.common.messages.records.HLRecord;
import org.gcube.messaging.common.messages.records.HLRecord.GCUBEUser;


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
	
	protected static final String hlAddresssesSeparator= ";";
	protected static final String hlAddresssesSeparator2= ":";
	
	private StringTokenizer tokenizer = null;
	
	private String []hlentry = new String [3];  
	
	private ArrayList<GCUBEUser> addresseeUsers = null;


	
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
	
	protected enum HLTokens {
		ID("ID"),
		TYPE("TYPE"),
		ADDRESSEES("ADDRESSEES"),
		NAME("NAME");
		String tokens;
		HLTokens(String tokens) {this.tokens = tokens;}
		public String toString() {return this.tokens;}
	}
	
	protected static ArrayList<GCUBEUser> getAddresseesUser(String line){
		
		
		ArrayList<GCUBEUser> users = new ArrayList<GCUBEUser>();
		String userString = line.substring(line.indexOf(LogEntry.parameterValueSeparator)+1).trim();
		StringTokenizer tokenizer = new StringTokenizer(userString,hlAddresssesSeparator);
		while(tokenizer.hasMoreElements()){
			String tok = tokenizer.nextToken();
			
			GCUBEUser user = new  HLRecord().new GCUBEUser();
			user.setUser(tok.substring(0,tok.indexOf(hlAddresssesSeparator2)));
			user.setVre(tok.substring(tok.indexOf(hlAddresssesSeparator2)+1));
			users.add(user);
		}
		return users;
		
	} 
	


	@Override
	public BaseRecord fillRecord() {
		
		tokenizer = new StringTokenizer(message.getMessage(),Message.messageTokensSeparator);
		while(tokenizer.hasMoreElements()){
			String tok = tokenizer.nextToken();
			if (tok.contains(HLTokens.ID.tokens))
				hlentry[0] = Message.getValue(tok);
			else if (tok.contains(HLTokens.NAME.tokens))
				hlentry[1] = Message.getValue(tok);
			else if (tok.contains(HLTokens.TYPE.tokens))
				hlentry[2] = Message.getValue(tok);
			else if (tok.contains(HLTokens.ADDRESSEES.tokens))
				addresseeUsers = HLEntry.getAddresseesUser(tok);
			}
		
	
		record.setDate(this.getDate());
		((HLRecord)record).setHLsubType(HLRecord.HLSubType.valueOf(this.getEntryType().name()));
		if (this.getEntryType().compareTo(EntryType.HL_WORKSPACE_CREATED)!= 0){
			((HLRecord)record).setID(hlentry[0]);
			((HLRecord)record).setName(hlentry[1]);
			((HLRecord)record).setType(hlentry[2]);
			if (this.getEntryType().compareTo(EntryType.HL_ITEM_SENT)== 0) {
				((HLRecord)record).setAddresseesUsers(addresseeUsers);
			}
		}
		return record;
	}
}