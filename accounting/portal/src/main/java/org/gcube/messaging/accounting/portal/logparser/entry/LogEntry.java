package org.gcube.messaging.accounting.portal.logparser.entry;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

import org.gcube.messaging.accounting.portal.logparser.AccessLogParser;
import org.gcube.messaging.accounting.portal.logparser.AccessLogParser.EntryType;
import org.gcube.messaging.common.messages.records.BaseRecord;

/**
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public abstract class LogEntry implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected Date date;
	protected String  vre;
	protected BaseRecord  record;
	protected EntryType entryType;;


	protected Message message;
	protected String user;
	protected String line;
	
	protected static final String parameterValueSeparator= "=";
	protected static final String parameterSeparator= "->";
	
	protected static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	protected static SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
	
	protected enum TokensType {
		USER("USER"),
		ENTRY_TYPE("ENTRY_TYPE"),
		MESSAGE("MESSAGE"),
		VRE("VRE");
		String type;
		TokensType(String type) {this.type = type;}
		public String toString() {return this.type;}
	}
	
	/**
	 * parse the line of log 
	 * @throws ParseException ParseException
	 */
	public void parse() throws ParseException {
		StringTokenizer tokenizer = new StringTokenizer(line,AccessLogParser.tokensSeparator);
		  while (tokenizer.hasMoreTokens()) {
		      String token = tokenizer.nextToken();
		      if (token.contains(TokensType.ENTRY_TYPE.type)){}
		      else if(token.contains(TokensType.VRE.type)){
		    	  vre =token.substring(token.indexOf(parameterSeparator)+parameterSeparator.length()+1);
		      }
		      else if(token.contains(TokensType.MESSAGE.type)){
		    	  if ((token.indexOf(parameterSeparator) + parameterSeparator.length()) != token.length()) {
		    		  token = token.substring(token.indexOf(parameterSeparator)+parameterSeparator.length()+1);
		    		  message = new Message(token,entryType);
		    		  message.parse();
		    	  }
		      }
		      else if(token.contains(TokensType.USER.type)){
		    	  user =token.substring(token.indexOf(parameterSeparator)+parameterSeparator.length()+1);
		      }
		      else date = format.parse(token);
		  	
		  }
	}

	/**
	 * get the date
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * set the date
	 * @param date the date
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * get the Associated Record
	 * @return the record
	 */
	public BaseRecord getRecord() {
		return record;
	}

	/**
	 * Set the Associtated record
	 * @param record
	 */
	public void setRecordName(BaseRecord record) {
		this.record = record;
	}

	/**
	 * get the message part of the log
	 * @return the message part
	 */
	public Message getMessage() {
		return message;
	}

	/**
	 * set the message part of the log
	 * @param message the message
	 */
	public void setMessage(Message message) {
		this.message = message;
	}

	/**
	 * get the user part
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * set the user
	 * @param user the user
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * get the VRE
	 * @return the VRE
	 */
	public String getVre() {
		return vre;
	}

	/**
	 * set the VRE
	 * @param vre the VRE
	 */
	public void setVre(String vre) {
		this.vre = vre;
	}
	
	/**
	 * string representation of the log
	 */
	public String toString(){
		return this.getVre() + parameterSeparator+this.getUser() 
		+parameterSeparator+this.getDate() +parameterSeparator+this.getMessage();
			
	}
	
	public EntryType getEntryType() {
		return entryType;
	}

	public void setEntryType(EntryType entryType) {
		this.entryType = entryType;
	}
	
	public abstract BaseRecord fillRecord();
		  
}
