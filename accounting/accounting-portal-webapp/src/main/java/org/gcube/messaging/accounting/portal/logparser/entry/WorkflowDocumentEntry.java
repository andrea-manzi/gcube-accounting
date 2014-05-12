package org.gcube.messaging.accounting.portal.logparser.entry;

import java.text.ParseException;
import java.util.StringTokenizer;

import org.gcube.messaging.accounting.portal.logparser.AccessLogParser.EntryType;
import org.gcube.messaging.common.messages.records.BaseRecord;
import org.gcube.messaging.common.messages.records.DocumentWorkflowRecord;

/**
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public class WorkflowDocumentEntry extends LogEntry {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1252202428920233522L;
	
	private StringTokenizer tokenizer = null;
	
	//workflowdocument
	private String []workflowDocumentsEntry = new String [3];
		


	public WorkflowDocumentEntry(String line,EntryType type) throws ParseException{
		super();
		this.line = line;
		entryType = type;
		record = new  DocumentWorkflowRecord();
		parse();
	}
	protected enum WorkflowDocumentTokens {
		WorkflowDocuementName("WorkflowDocuementName"),
		WORKFLOWID("WORKFLOWID"),
		STEPS_NO("STEPS_NO"),
		STATUS("STATUS");
		String tokens;
		WorkflowDocumentTokens(String tokens) {this.tokens = tokens;}
		public String toString() {return this.tokens;}
	}

	
	@Override
	public BaseRecord fillRecord() {
		
		tokenizer = new StringTokenizer(message.getMessage(),Message.messageTokensSeparator);
		while(tokenizer.hasMoreElements()){
			String tok = tokenizer.nextToken();
	
			if (tok.contains(WorkflowDocumentTokens.WORKFLOWID.tokens))
				workflowDocumentsEntry[0] = Message.getValue(tok);
			else if (tok.contains(WorkflowDocumentTokens.WorkflowDocuementName.tokens))
				workflowDocumentsEntry[1] = Message.getValue(tok);
			else if (tok.contains(WorkflowDocumentTokens.STATUS.tokens))
				workflowDocumentsEntry[2] = Message.getValue(tok);
			else if (tok.contains(WorkflowDocumentTokens.STEPS_NO.tokens))
				workflowDocumentsEntry[2] = Message.getValue(tok);
		}
		
		record.setDate(this.getDate());
		((DocumentWorkflowRecord)record).setSubType(DocumentWorkflowRecord.WorkflowSubType.valueOf(this.getEntryType().name()));
		((DocumentWorkflowRecord)record).setReportname(workflowDocumentsEntry[1]);
		((DocumentWorkflowRecord)record).setWorkflowid(workflowDocumentsEntry[0]);
			
		if (this.getEntryType().compareTo(EntryType.CREATED_WORKFLOWREPORT_OUTPUT)== 0)
			((DocumentWorkflowRecord)record).setStepsNumber(workflowDocumentsEntry[2]);
		else
			((DocumentWorkflowRecord)record).setStatus(workflowDocumentsEntry[2]);
		
		return record;
	}

}
