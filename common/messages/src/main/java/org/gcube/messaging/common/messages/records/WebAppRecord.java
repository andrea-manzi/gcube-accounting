package org.gcube.messaging.common.messages.records;

import java.io.Serializable;
import java.util.ArrayList;

public class WebAppRecord extends BaseRecord{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1926339166809817725L;

	public enum WebAppSubType {
		WEB_APPLICATION_DEPLOYED("WEB_APPLICATION_DEPLOYED"),
		WEB_APPLICATION_ACTIVATED("WEB_APPLICATION_ACTIVATED"),
		WEB_APPLICATION_DEACTIVATED("WEB_APPLICATION_DEACTIVATED"),
		WEB_APPLICATION_UNDEPLOYED("WEB_APPLICATION_UNDEPLOYED");
		String type;
		WebAppSubType(String type) {this.type = type;}
		public String toString() {return this.type;}
		
	}
	public WebAppRecord(){}
	
	private WebAppSubType subType;

	private ArrayList<WebApplication> webapplications= new ArrayList<WebApplication>();

	private ArrayList<GHN> ghns= new ArrayList<GHN>();

	public ArrayList<GHN> getGhns() {
		return ghns;
	}

	public void addGhn(GHN ghn) {
		this.ghns.add(ghn);
	}

	public ArrayList<WebApplication> getWebapplications() {
		return webapplications;
	}

	public void addWebapplication(WebApplication webapplication) {
		this.webapplications.add(webapplication);
	}

	public WebAppSubType getSubType() {
		return subType;
	}

	public void setSubType(WebAppSubType subType) {
		this.subType = subType;
	}
	
	public class WebApplication implements Serializable
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String WEB_APPLICATION_ID;
		private String WEB_APPLICATION_NAME;
		private String WEB_APPLICATION_VERSION;
		
		public WebApplication(){}

		public String getWEB_APPLICATION_ID() {
			return WEB_APPLICATION_ID;
		}

		public void setWEB_APPLICATION_ID(String wEB_APPLICATION_ID) {
			WEB_APPLICATION_ID = wEB_APPLICATION_ID;
		}

		public String getWEB_APPLICATION_NAME() {
			return WEB_APPLICATION_NAME;
		}

		public void setWEB_APPLICATION_NAME(String wEB_APPLICATION_NAME) {
			WEB_APPLICATION_NAME = wEB_APPLICATION_NAME;
		}

		public String getWEB_APPLICATION_VERSION() {
			return WEB_APPLICATION_VERSION;
		}

		public void setWEB_APPLICATION_VERSION(String wEB_APPLICATION_VERSION) {
			WEB_APPLICATION_VERSION = wEB_APPLICATION_VERSION;
		}
		
		
	}
	
	public class GHN implements Serializable
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		 
		 
		private String GHN_ID;
		private String GHN_NAME;
		
		public String getGHN_ID() {
			return GHN_ID;
		}

		public void setGHN_ID(String gHN_ID) {
			GHN_ID = gHN_ID;
		}

		public String getGHN_NAME() {
			return GHN_NAME;
		}

		public void setGHN_NAME(String gHN_NAME) {
			GHN_NAME = gHN_NAME;
		}


		
		
	}
	
}
