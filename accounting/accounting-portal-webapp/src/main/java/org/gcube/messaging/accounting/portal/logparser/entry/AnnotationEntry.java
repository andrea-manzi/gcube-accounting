package org.gcube.messaging.accounting.portal.logparser.entry;

import java.text.ParseException;
import java.util.StringTokenizer;

import org.gcube.messaging.accounting.portal.logparser.AccessLogParser.EntryType;
import org.gcube.messaging.common.messages.records.AnnotationRecord;
import org.gcube.messaging.common.messages.records.BaseRecord;


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
	
	private StringTokenizer tokenizer = null;


	//Type, Name, ObjectID,ObjectName
	//Type, Name, ObjectID
	//Name, ObjectID
	private String []annotationEntry = new String [4];
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
	
	protected enum AnnotationTokens {
		annotationName("annotationName"),
		annotationType("annotationType"),
		objectID("objectID"),
		objectName("objectName");
		String tokens;
		AnnotationTokens(String tokens) {this.tokens = tokens;}
		public String toString() {return this.tokens;}
	}
	


	@Override
	public BaseRecord fillRecord() {
		
		tokenizer = new StringTokenizer(message.getMessage(),Message.messageTokensSeparator);
		while(tokenizer.hasMoreElements()){
			String tok = tokenizer.nextToken();
			//Type, Name, ObjectID,ObjectName
			if (tok.contains(AnnotationTokens.annotationType.tokens))
				annotationEntry[0] = Message.getValue(tok);
			else if (tok.contains(AnnotationTokens.annotationName.tokens))
				annotationEntry[1] = Message.getValue(tok);
			else if (tok.contains(AnnotationTokens.objectID.tokens))
				annotationEntry[2] = Message.getValue(tok);
			else if (tok.contains(AnnotationTokens.objectName.tokens))
				annotationEntry[3] = Message.getValue(tok);
		}
		//common field
		record.setDate(this.getDate());
		((AnnotationRecord)record).setObjectID(annotationEntry[2]);
		((AnnotationRecord)record).setAnnotationName(annotationEntry[1]);
		((AnnotationRecord)record).setAnnotationSubType(AnnotationRecord.AnnotationSubType.valueOf(this.getEntryType().name()));
		((AnnotationRecord)record).setAnnotationType("");
		((AnnotationRecord)record).setObjectName("");
		if (this.getEntryType().compareTo(EntryType.Create_Annotation)== 0){
			((AnnotationRecord)record).setAnnotationType(annotationEntry[0]);
			((AnnotationRecord)record).setObjectName(annotationEntry[3]);
		}
		else if (this.getEntryType().compareTo(EntryType.Edit_Annotation)== 0){
			((AnnotationRecord)record).setAnnotationType(annotationEntry[0]);
		}
			
		return record;
	}
}
