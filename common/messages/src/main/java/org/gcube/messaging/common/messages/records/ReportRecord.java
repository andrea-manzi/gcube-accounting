package org.gcube.messaging.common.messages.records;

/**
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public class ReportRecord extends BaseRecord{

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public enum ReportSubType {
		CREATE_REPORT("Create_Report"),
		GENEARATE_REPORT_OUTPUT("Generate_Report_Output"),
		OPEN_REPORT("Open_Report"),
		OPEN_WORKFLOW_REPORT("Open_Workflow_Report"),
		SAVE_WORKFLOW_REPORT("SaveWorkflowLogEntry"),
		CREATE_TEMPLATE("Create_Template"),
		OPEN_TEMPLATE("Open_Template");
		String type;
		ReportSubType(String type) {this.type = type;}
		public String toString() {return this.type;}
	};
	
	private String templateName;
	
	private String templateID;
	
	private String name;
	
	private String mimetype;
	
	private String type;
	
	private String author;
	
	private ReportSubType subType;

	public ReportSubType getSubType() {
		return subType;
	}

	public void setSubType(ReportSubType subType) {
		this.subType = subType;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getTemplateID() {
		return templateID;
	}

	public void setTemplateID(String templateID) {
		this.templateID = templateID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMimetype() {
		return mimetype;
	}

	public void setMimetype(String mimetype) {
		this.mimetype = mimetype;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

}
