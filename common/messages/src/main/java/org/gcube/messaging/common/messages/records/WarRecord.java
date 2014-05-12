package org.gcube.messaging.common.messages.records;

/**
 * 
 * @author andrea
 *
 */
public class WarRecord extends BaseRecord{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1926339166809817725L;

	public enum WarSubType {
		WAR_REMOVED("WAR_REMOVED"),
		WAR_UPDATED("WAR_UPDATED"),
		WAR_UPLOADED("WAR_UPLOADED");
		String type;
		WarSubType(String type) {this.type = type;}
		public String toString() {return this.type;}
		
	}
	public WarRecord(){}
	
	
	private String warId;
	
	private String warName;
	
	private String category;
	
	private String appName;
	
	private String appVersion;

	private WarSubType subType;
	
	
	public String getWarId() {
		return warId;
	}
	public void setWarId(String warId) {
		this.warId = warId;
	}
	public String getWarName() {
		return warName;
	}
	public void setWarName(String warName) {
		this.warName = warName;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getAppVersion() {
		return appVersion;
	}
	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}


	public WarSubType getSubType() {
		return subType;
	}

	public void setSubType(WarSubType subType) {
		this.subType = subType;
		
	}
	
}
	