package org.gcube.messaging.accounting.portal.logparser.entry;

import java.text.ParseException;

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

	public ReportEntry(String line,EntryType type) throws ParseException{
		super();
		this.line = line;
		entryType = type;
		record = new  ReportRecord();
		parse();
	}
	
	@Override
	public BaseRecord fillRecord() {
		record.setDate(this.getDate());
		((ReportRecord)record).setSubType(ReportRecord.ReportSubType.valueOf(this.getEntryType().name()));

		if (this.getEntryType().compareTo(EntryType.GENERATE_REPORT_OUTPUT)==0) {
			((ReportRecord)record).setName(this.getMessage().getReportEntry()[0]);
			((ReportRecord)record).setMimetype(this.getMessage().getReportEntry()[1]);
			((ReportRecord)record).setType(this.getMessage().getReportEntry()[2]);
		}
		else  {
			((ReportRecord)record).setTemplateID(this.getMessage().getReportEntry()[0]);
			((ReportRecord)record).setTemplateName(this.getMessage().getReportEntry()[1]);
			if (this.getEntryType().compareTo(EntryType.SAVE_WORKFLOW_REPORT )== 0 || this.getEntryType().compareTo(EntryType.OPEN_WORKFLOW_REPORT )== 0) 
				((ReportRecord)record).setAuthor(this.getMessage().getReportEntry()[2]);
		}		
		return record;
	}

}
