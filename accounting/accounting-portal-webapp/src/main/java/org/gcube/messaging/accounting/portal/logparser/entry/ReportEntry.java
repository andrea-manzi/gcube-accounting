package org.gcube.messaging.accounting.portal.logparser.entry;

import java.text.ParseException;
import java.util.StringTokenizer;

import org.gcube.messaging.accounting.portal.logparser.AccessLogParser.EntryType;
import org.gcube.messaging.common.messages.records.BaseRecord;
import org.gcube.messaging.common.messages.records.ReportRecord;

/**
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public class ReportEntry extends LogEntry {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3436449743388602935L;
	
	private StringTokenizer tokenizer = null;
	
	//report 
	private String []reportEntry = new String [3];
		


	public ReportEntry(String line,EntryType type) throws ParseException{
		super();
		this.line = line;
		entryType = type;
		record = new  ReportRecord();
		parse();
	}
	
	protected enum ReportTokens {
		Name("Name"),
		ID("ID"),
		AUTHOR("AUTHOR"),
		MIMETYPE("MIMETYPE"),
		TYPE("TYPE");
		String tokens;
		ReportTokens(String tokens) {this.tokens = tokens;}
		public String toString() {return this.tokens;}
	}
	

	
	@Override
	public BaseRecord fillRecord() {

		tokenizer = new StringTokenizer(message.getMessage(),Message.messageTokensSeparator);
		

		switch (this.getEntryType()){

			case CREATE_REPORT:
			case OPEN_REPORT:
			{
				while(tokenizer.hasMoreElements()){
					String tok = tokenizer.nextToken();
			
					if (tok.contains(ReportTokens.ID.tokens))
						reportEntry[0] = Message.getValue(tok);
					else if (tok.contains(ReportTokens.Name.tokens))
						reportEntry[1] = Message.getValue(tok);
					
				}
			}
			break;
			case OPEN_WORKFLOW_REPORT:
			case SAVE_WORKFLOW_REPORT:
			case CREATE_TEMPLATE:
			case OPEN_TEMPLATE:
			{
				while(tokenizer.hasMoreElements()){
					String tok = tokenizer.nextToken();
			
					if (tok.contains(ReportTokens.ID.tokens))
						reportEntry[0] = Message.getValue(tok);
					else if (tok.contains(ReportTokens.Name.tokens))
						reportEntry[1] = Message.getValue(tok);
					else if (tok.contains(ReportTokens.AUTHOR.tokens))
						reportEntry[2] = Message.getValue(tok);
					
				}
			}
				
				
			case GENERATE_REPORT_OUTPUT:
			{
				while(tokenizer.hasMoreElements()){
					String tok = tokenizer.nextToken();
			
					if (tok.contains(ReportTokens.Name.tokens))
						reportEntry[0] = Message.getValue(tok);
					else if (tok.contains(ReportTokens.MIMETYPE.tokens))
						reportEntry[1] = Message.getValue(tok);
					else if (tok.contains(ReportTokens.TYPE.tokens))
						reportEntry[2] = Message.getValue(tok);
					
				}
			}
		}
				
			
			
		
		record.setDate(this.getDate());
		((ReportRecord)record).setSubType(ReportRecord.ReportSubType.valueOf(this.getEntryType().name()));

		if (this.getEntryType().compareTo(EntryType.GENERATE_REPORT_OUTPUT)==0) {
			((ReportRecord)record).setName(reportEntry[0]);
			((ReportRecord)record).setMimetype(reportEntry[1]);
			((ReportRecord)record).setType(reportEntry[2]);
		}
		else  {
			((ReportRecord)record).setTemplateID(reportEntry[0]);
			((ReportRecord)record).setTemplateName(reportEntry[1]);
			if (this.getEntryType().compareTo(EntryType.SAVE_WORKFLOW_REPORT )== 0 || this.getEntryType().compareTo(EntryType.OPEN_WORKFLOW_REPORT )== 0) 
				((ReportRecord)record).setAuthor(reportEntry[2]);
		}		
		return record;
	}

}
