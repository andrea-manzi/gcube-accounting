package org.gcube.messaging.accounting.portal.logparser.entry;

import java.text.ParseException;
import java.util.StringTokenizer;

import org.gcube.messaging.accounting.portal.logparser.AccessLogParser.EntryType;
import org.gcube.messaging.common.messages.records.BaseRecord;
import org.gcube.messaging.common.messages.records.StatisticalManagerRecord;

/**
 * 
 * @author andrea
 *
 */
public class StatisticalManagerEntry extends LogEntry {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//sm
	private String []smEntry = new String [5];

	
	protected enum SMTokens {
		ALGORITHM_NAME("algorithmName"),
		EXECUTION_OUTCOME("executionOutcome"),
		EXECUTION_TIME("executionSecondsTime"),
		FILE_NAME("fileName"),
		FILE_TYPE("fileType");
		String tokens;
		SMTokens(String tokens) {this.tokens = tokens;}
		public String toString() {return this.tokens;}
	}
	
	/**
	 * constructor
	 * @param line the line to parse
	 * @param The EntryType
	 * @throws ParseException exception
	 */
	public StatisticalManagerEntry(String line,EntryType type) throws ParseException{
		super();
		this.line = line;
		entryType = type;
		record = new StatisticalManagerRecord();
		parse();
	}


	@Override
	public BaseRecord fillRecord() {
		
	tokenizer = new StringTokenizer(message.getMessage(),Message.messageTokensSeparator);
		
		while(tokenizer.hasMoreElements()){
			String tok = tokenizer.nextToken();
			if (tok.contains(SMTokens.ALGORITHM_NAME.tokens))
				smEntry[0] = Message.getValue(tok);
			else if (tok.contains(SMTokens.EXECUTION_OUTCOME.tokens))
				smEntry[1] = Message.getValue(tok);
			else if (tok.contains(SMTokens.EXECUTION_TIME.tokens))
				smEntry[2] = Message.getValue(tok);
			else if (tok.contains(SMTokens.FILE_NAME.tokens))
				smEntry[3] = Message.getValue(tok);
			else if (tok.contains(SMTokens.FILE_TYPE.tokens))
				smEntry[4] = Message.getValue(tok);
		}
		
		record.setDate(this.getDate());
		
		((StatisticalManagerRecord)record).setType(StatisticalManagerRecord.StatisticalManagerSubType.valueOf(this.getEntryType().name()));
	
		if (((StatisticalManagerRecord)record).getType().compareTo(StatisticalManagerRecord.StatisticalManagerSubType.STATISTICALMANAGER_EXECUTION) == 0)
		{
			((StatisticalManagerRecord)record).setAlgorithmName(smEntry[0]);
			((StatisticalManagerRecord)record).setExecutionOutcome(smEntry[1]);
			((StatisticalManagerRecord)record).setExecutionSecondsTime(Long.parseLong(smEntry[2]));
		}
		else {
			((StatisticalManagerRecord)record).setFileName(smEntry[3]);
			((StatisticalManagerRecord)record).setFileType(smEntry[4]);
		}
		return record;
	}
	
}
