package org.gcube.messaging.accounting.portal.logparser.entry;

import java.text.ParseException;

import org.gcube.messaging.accounting.portal.logparser.AccessLogParser.EntryType;
import org.gcube.messaging.common.messages.records.AnnotationRecord;
import org.gcube.messaging.common.messages.records.BaseRecord;
import org.gcube.messaging.common.producer.GCUBELocalProducer;

/**
 * 
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public class AnnotationEntry  extends LogEntry {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	/**
	 * constructor
	 * @param line the line to parse
	 * @param The EntryType
	 * @throws ParseException exception
	 */
	public AnnotationEntry(String line,EntryType type) throws ParseException{
		super();
		this.line = line;
		record = new AnnotationRecord();
		entryType = type;
		parse();
	}


	@Override
	public BaseRecord fillRecord() {
		//common field
		record.setDate(this.getDate());
		((AnnotationRecord)record).setObjectID(this.getMessage().getAnnotationEntry()[2]);
		((AnnotationRecord)record).setAnnotationName(this.getMessage().getAnnotationEntry()[1]);
		((AnnotationRecord)record).setAnnotationSubType(AnnotationRecord.AnnotationSubType.valueOf(this.getEntryType().name()));
		((AnnotationRecord)record).setAnnotationType("");
		((AnnotationRecord)record).setObjectName("");
		if (this.getEntryType().compareTo(EntryType.Create_Annotation)== 0){
			((AnnotationRecord)record).setAnnotationType(this.getMessage().getAnnotationEntry()[0]);
			((AnnotationRecord)record).setObjectName(this.getMessage().getAnnotationEntry()[3]);
		}
		else if (this.getEntryType().compareTo(EntryType.Edit_Annotation)== 0){
			((AnnotationRecord)record).setAnnotationType(this.getMessage().getAnnotationEntry()[0]);
		}
			
		return record;
	}
}
