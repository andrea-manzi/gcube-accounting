package org.gcube.messaging.common.messages.records;

import org.gcube.messaging.common.messages.records.HLRecord.HLSubType;

/**
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public class AnnotationRecord extends BaseRecord {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String annotationType;
	private String annotationName;
	private String objectID;
	private String objectName;
	private AnnotationSubType annotationSubType;


	/**
	 * 
	 * @author Andrea Manzi(CERN)
	 *
	 */
	public enum AnnotationSubType {
		Create_Annotation("Create_Annotation"),
		Edit_Annotation("Edit_Annotation"),
		Delete_Annotation("Delete_Annotation");
		String type;
		AnnotationSubType(String type) {this.type = type;}
		public String toString() {return this.type;}
	};
	
	/**
	 * Get the Annotation type (TEXT, ASSOCIATION, BINARY)
	 * @return the annotion type
	 */
	public String getAnnotationType() {
		return annotationType;
	}

	/**
	 * Set the Annotation type
	 * @param annotationType
	 */
	public void setAnnotationType(String annotationType) {
		this.annotationType = annotationType;
	}

	/**
	 * Get the annotation Name
	 * @return the annotation name
	 */
	public String getAnnotationName() {
		return annotationName;
	}

	/**
	 * Set the annotation Name
	 * @param annotationName
	 */
	public void setAnnotationName(String annotationName) {
		this.annotationName = annotationName;
	}

	/**
	 * get the Annotated object ID 
	 * @return the Object ID
	 */
	public String getObjectID() {
		return objectID;
	}

	/**
	 * Set the object id
	 * @param objectID
	 */
	public void setObjectID(String objectID) {
		this.objectID = objectID;
	}

	/**
	 * Get the object name
	 * @return the object name
	 */
	public String getObjectName() {
		return objectName;
	}

	/**
	 * Set the object name
	 * @param objectName
	 */
	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	/**
	 * get the AnnotationSubType 
	 * @return the AnnotationSubType
	 */
	public AnnotationSubType getAnnotationSubType() {
		return annotationSubType;
	}

	/**
	 * Set the AnnotationSubType
	 * @param annotationSubType
	 */
	public void setAnnotationSubType(AnnotationSubType annotationSubType) {
		this.annotationSubType = annotationSubType;
	}

	
	
}
