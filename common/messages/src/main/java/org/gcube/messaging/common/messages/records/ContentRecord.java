package org.gcube.messaging.common.messages.records;

/**
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public class ContentRecord  extends BaseRecord {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * constructor
	 */
	public ContentRecord(){}
	
	private String contentId = "";
	private String contentName = "";
	
	/**
	 * get the content ID
	 * @return the content ID
	 */
	public String getContentId() {
		return contentId;
	}
	/**
	 * set the content ID
	 * @param contentId the content ID
	 */
	public void setContentId(String contentId) {
		this.contentId = contentId;
	}

	/**
	 * get the content name
	 * @return the content name
	 */
	public String getContentName() {
		return contentName;
	}
	/**
	 * set the content name
	 * @param contentName the content name
	 */
	public void setContentName(String contentName) {
		this.contentName = contentName;
	}

}
