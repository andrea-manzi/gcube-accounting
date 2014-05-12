package org.gcube.messaging.common.messages.records;


/**
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public class DocumentWorkflowRecord extends BaseRecord{

	private String reportname;
	
	private String workflowid;
	
	private String status;
	
	private String stepsNumber;
	
	private WorkflowSubType subType;
	
	public WorkflowSubType getSubType() {
		return subType;
	}

	public void setSubType(WorkflowSubType subType) {
		this.subType = subType;
	}

	public String getReportname() {
		return reportname;
	}

	public void setReportname(String reportname) {
		this.reportname = reportname;
	}

	public String getWorkflowid() {
		return workflowid;
	}

	public void setWorkflowid(String workflowid) {
		this.workflowid = workflowid;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStepsNumber() {
		return stepsNumber;
	}

	public void setStepsNumber(String stepsNumber) {
		this.stepsNumber = stepsNumber;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -6395075326965193477L;

	public enum WorkflowSubType {
		CREATED_WORKFLOWREPORT_OUTPUT("Created_WorkflowReport_Output"),
		DELETED_WORKFLOWREPORT_OUTPUT("Deleted_WorkflowReport_Output");		
		String type;
		WorkflowSubType(String type) {this.type = type;}
		public String toString() {return this.type;}
	};
}
