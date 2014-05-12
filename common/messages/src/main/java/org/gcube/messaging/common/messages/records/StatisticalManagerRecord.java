package org.gcube.messaging.common.messages.records;

/**
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public class StatisticalManagerRecord extends BaseRecord{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	private String algorithmName;
	private String executionOutcome;
	private long executionSecondsTime;
	private StatisticalManagerSubType SMSubType;
	private String fileName;
	private String fileType;

	public enum StatisticalManagerSubType {
		STATISTICALMANAGER_EXECUTION("StatisticalManager_Execution"),
		STATISTICALMANAGER_IMPORT("StatisticalManager_Import");
		String type;
		StatisticalManagerSubType(String type) {this.type = type;}
		public String toString() {return this.type;}
	};
	
	public StatisticalManagerSubType getType() {
		return SMSubType;
	}
	public void setType(StatisticalManagerSubType type) {
		this.SMSubType = type;
	}
	public String getAlgorithmName() {
		return algorithmName;
	}


	public void setAlgorithmName(String algorithmName) {
		this.algorithmName = algorithmName;
	}


	public String getExecutionOutcome() {
		return executionOutcome;
	}


	public void setExecutionOutcome(String executionOutcome) {
		this.executionOutcome = executionOutcome;
	}


	public long getExecutionSecondsTime() {
		return executionSecondsTime;
	}


	public void setExecutionSecondsTime(long executionSecondsTime) {
		this.executionSecondsTime = executionSecondsTime;
	}

	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	
}