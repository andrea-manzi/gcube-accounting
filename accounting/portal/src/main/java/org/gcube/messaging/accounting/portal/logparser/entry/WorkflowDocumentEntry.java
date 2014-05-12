package org.gcube.messaging.accounting.portal.logparser.entry;

import java.text.ParseException;

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

	public WorkflowDocumentEntry(String line,EntryType type) throws ParseException{
		super();
		this.line = line;
		entryType = type;
		record = new  DocumentWorkflowRecord();
		parse();
	}

	
	@Override
	public BaseRecord fillRecord() {
		
		record.setDate(this.getDate());
		((DocumentWorkflowRecord)record).setSubType(DocumentWorkflowRecord.WorkflowSubType.valueOf(this.getEntryType().name()));
		((DocumentWorkflowRecord)record).setReportname(this.getMessage().getWorkflowDocumentsEntry()[1]);
		((DocumentWorkflowRecord)record).setWorkflowid(this.getMessage().getWorkflowDocumentsEntry()[0]);
			
		if (this.getEntryType().compareTo(EntryType.CREATED_WORKFLOWREPORT_OUTPUT)== 0)
			((DocumentWorkflowRecord)record).setStepsNumber(this.getMessage().getWorkflowDocumentsEntry()[2]);
		else
			((DocumentWorkflowRecord)record).setStatus(this.getMessage().getWorkflowDocumentsEntry()[2]);
		
		return record;
	}

}
