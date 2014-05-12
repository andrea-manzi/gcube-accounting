package org.gcube.messaging.accounting.portalaccountingportlet.client.obj;

import java.io.Serializable;

public class ContentPair implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String contentName;
	private String contentId;
	public String getContentName() {
		return contentName;
	}
	public void setContentName(String contentName) {
		this.contentName = contentName;
	}
	public String getContentId() {
		return contentId;
	}
	public void setContentId(String contentId) {
		this.contentId = contentId;
	}
}
