package org.gcube.messaging.accounting.portal.logparser.entry;

import java.text.ParseException;
import java.util.StringTokenizer;

import org.gcube.messaging.accounting.portal.logparser.AccessLogParser.EntryType;
import org.gcube.messaging.accounting.portal.logparser.entry.AdvancedSearchEntry.OperatorType;

/**
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public class Message {
	
	
	protected static final String messageTokensSeparator= "|";
	
	private EntryType type;
	private String message;


	protected StringTokenizer tokenizer = null;
	
	
	/**
	 * Message constructor
	 * @param token token
	 * @param type type
	 */
	public Message (String token, EntryType type){
		this.message = token;
		this.type = type;
	}
	/**
	 * parse the message line of log
	 * @throws ParseException ParseException
	 */
	public void parse(LogEntry entry) throws ParseException {
		entry.fillRecord();
		
	}
	
	
		
	protected static  String [] getDetails(String token){
		String [] col = new String [2];
	
		String nameString = token.substring(0,
				token.indexOf(OperatorType.AND.operator));
	
		String idString = token.substring(token.indexOf(OperatorType.AND.operator)+OperatorType.AND.operator.length()+1);
	
		col[0] = idString.substring(idString.indexOf(LogEntry.parameterValueSeparator)+1).trim();
	
		col[1] = nameString.substring(nameString.indexOf(LogEntry.parameterValueSeparator)+1).trim();
	
		return col;
	}

	
	protected static String getValue(String token){
		return token.substring(token.indexOf(LogEntry.parameterValueSeparator)+1).trim();

	}
	
	/**
	 * get the message
	 */
	public String toString (){
		return this.message;
	}


	/**
	 * get the message 
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	
	/**
	 * set teh message
	 * @param message the message
	 */
	public void setMessage(String message) {
		this.message = message;
	}


	
}

