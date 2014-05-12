package org.gcube.messaging.common.consumer.mail;

import java.io.File;
import java.io.StringBufferInputStream;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.rpc.ServiceException;

import org.gcube.common.core.utils.logging.GCUBELog;
import org.gcube.messaging.common.consumer.ServiceContext;
import org.gcube.messaging.common.consumer.liferay.LiferayClient;
import org.gcube.vomanagement.usermanagement.ws.UserModel;
import org.gcube.vomanagement.usermanagement.ws.UserModelCustomAttrsMapEntry;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 
 * Mail recipient file parser
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public class MailRecipientHelper {

	private Document domDocument = null;
	private Long diskQuotaThreshold = null;
	private Long virtualMemoryThreshold = null;
	private Double cpuLoadThreshold = null;

	public  static GCUBELog logger = new GCUBELog(MailRecipientHelper.class);

	/**
	 * constructor
	 * @param file teh file
	 * @throws Exception exception
	 */
	public MailRecipientHelper (File file) throws Exception {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;

		try {
			builder = factory.newDocumentBuilder();
			domDocument = builder.parse(file);
		} catch (Exception e) {
			throw e;
		}

	}

	/**
	 * constructor
	 * @param file the file
	 * @throws Exception exception
	 */
	public MailRecipientHelper () throws Exception {}



	/**
	 * @param file file
	 * @throws Exception exception
	 */
	public void parse(String file) throws Exception{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		String scope ="";

		try {
			builder = factory.newDocumentBuilder();
			domDocument = builder.parse(new StringBufferInputStream(file));
		} catch (Exception e) {
			throw e;
		}
		Element root = domDocument.getDocumentElement();
		try {
			scope = root.getAttributes().getNamedItem("name").getNodeValue();

			//clean scope conf 
			ServiceContext.getContext().getMonitoringManager().cleanMonitoringConf(scope);

			NodeList nodes = root.getChildNodes();
			for(int j=0; j<nodes.getLength(); j++){
				Node node = nodes.item(j);
				if (node  instanceof Element) {
					if (!ServiceContext.getContext().connectToUsermanagementDB() &&((Element) node).getTagName().compareTo("NotificationConfiguration")==0 ) {
						NodeList domains = node.getChildNodes();
						for(int i=0; i<domains.getLength(); i++){
							Node domain = domains.item(i);
							if (domain  instanceof Element) {
								if (((Element) domain).getTagName().compareTo("Domain")==0 ) {
									String domainName = domain.getAttributes().getNamedItem("name").getNodeValue();
									String siteName = domain.getAttributes().getNamedItem("site").getNodeValue();
									NodeList users = domain.getChildNodes();
									// add domain to table
									ServiceContext.getContext().getMonitoringManager().insertDomain(domainName, siteName);

									for(int k=0; k<users.getLength(); k++){
										Node user = users.item(k);
										if (user  instanceof Element) {
											if (((Element) user).getTagName().compareTo("User")==0 ) {
												String name = user.getAttributes().getNamedItem("name").getNodeValue();
												String email = user.getAttributes().getNamedItem("email").getNodeValue();
												boolean notify = Boolean.parseBoolean(user.getAttributes().getNamedItem("notify").getNodeValue());
												boolean receiveSummary = Boolean.parseBoolean(user.getAttributes().getNamedItem("receiveSummary").getNodeValue());
												boolean admin = Boolean.parseBoolean(user.getAttributes().getNamedItem("admin").getNodeValue());

												// adduser to table
												ServiceContext.getContext().getMonitoringManager().insertUser(
														new MonitorUser(name, email,scope, notify, receiveSummary, admin));

												//link user domain
												ServiceContext.getContext().getMonitoringManager().associateUserAndDomain(email, domainName,scope);
											}
										}
									}
								}
							}
						}

					}
					else if  (((Element) node).getTagName().compareTo("CPULoad")==0 ) {
						logger.debug(node.getFirstChild().getNodeValue());
						cpuLoadThreshold = Double.parseDouble(node.getFirstChild().getNodeValue());
					}
					else  if (((Element) node).getTagName().compareTo("DiskQuota")==0 ) {
						logger.debug(node.getFirstChild().getNodeValue());
						diskQuotaThreshold = Long.parseLong(node.getFirstChild().getNodeValue());
					}
					else  if (((Element) node).getTagName().compareTo("VirtualMemory")==0 ) {
						logger.debug(node.getFirstChild().getNodeValue());
						virtualMemoryThreshold = Long.parseLong(node.getFirstChild().getNodeValue());
					}
				}
			}



		}catch (Exception e) {
			throw e;
		}

		//the consumer is configured to contact the user management ws
		try {
			if (ServiceContext.getContext().connectToUsermanagementDB()){
				updateUserinfo(scope);
			}

		}catch (Exception e) {
			logger.error("Error Getting user info from Usermanagement WS",e);
			throw e;
		}


	}


	private void updateUserinfo(String scope) throws ServiceException, Exception {

		LiferayClient client = new LiferayClient();

		for (UserModel user :client.getUsersWithRole(LiferayClient.siteManagerRoleName))

		{
			String siteName = "";
			ArrayList<String>  domains = null;
			boolean notify = false;
			boolean receiveSummary = false;
			for(int i = 0;i <user.getCustomAttrsMap().getEntry().length; i++){
				UserModelCustomAttrsMapEntry entry =user.getCustomAttrsMap().getEntry(i);
				if (entry.getKey().compareTo(LiferayClient.siteDomainsCustomAttributeName)==0)
					if (entry.getValue()!= null) domains= getDomains(entry.getValue());
				else if (entry.getKey().compareTo(LiferayClient.receiveNotificationCustomAttributeName)==0)
					if (entry.getValue()!= null) notify = Boolean.parseBoolean(entry.getValue());
				else if (entry.getKey().compareTo(LiferayClient.receiveSummaryCustomAttributeName)==0)
						if (entry.getValue()!= null) receiveSummary = Boolean.parseBoolean(entry.getValue());
				else if (entry.getKey().compareTo(LiferayClient.siteNameCustomAttributeName)==0)
						if (entry.getValue()!= null) siteName = entry.getValue();
			}
			if ( domains == null || siteName.compareTo("")==0) {
				logger.warn("The information related to user " + user.getScreenName() + " are not available");
				return;
			}
				
			for (String domain: domains) {
				ServiceContext.getContext().getMonitoringManager().insertDomain(domain, siteName);										
				ServiceContext.getContext().getMonitoringManager().associateUserAndDomain(user.getScreenName(), domain,scope);
			}
			// adduser to table
			ServiceContext.getContext().getMonitoringManager().insertUser(
					new MonitorUser(user.getScreenName(), user.getEmail(),scope, notify, receiveSummary, false));
		}

		
		for (UserModel user :client.getUsersWithRole(LiferayClient.infraManagerRoleName))
		{
			boolean notify = false;
			boolean receiveSummary = false;
			for(int i = 0;i <user.getCustomAttrsMap().getEntry().length; i++){
				UserModelCustomAttrsMapEntry entry =user.getCustomAttrsMap().getEntry(i);
				if (entry.getKey().compareTo(LiferayClient.receiveNotificationCustomAttributeName)==0)
					if (entry.getValue()!= null) notify = Boolean.parseBoolean(entry.getValue());
				else if (entry.getKey().compareTo(LiferayClient.receiveSummaryCustomAttributeName)==0)
						if (entry.getValue()!= null) receiveSummary = Boolean.parseBoolean(entry.getValue());
			}
			//getting domains
			ResultSet domains = ServiceContext.getContext().getMonitoringManager().getDomains();
			//adding infrastructure managers to each domain
			while(domains.next())
					ServiceContext.getContext().getMonitoringManager().associateUserAndDomain(user.getScreenName(), domains.getString("Name"),scope);
			// adduser to table
			ServiceContext.getContext().getMonitoringManager().insertUser( 
					new MonitorUser(user.getScreenName(), user.getEmail(),scope, notify, receiveSummary, true));
			
			}
	}
	

	private ArrayList<String> getDomains(String value) {
		ArrayList<String> domains = new ArrayList<String>();
		StringTokenizer token = new StringTokenizer(value,",");
		while( token.hasMoreElements())
			domains.add(token.nextToken());
		return domains;
	}

	/**
	 * get diskQuota
	 * @return the disk quota
	 */
	public Long getDiskQuotaThreshold() {
		return diskQuotaThreshold;
	}

	/**
	 * set disk quota
	 * @param diskQuotaThreshold
	 */
	public  void setDiskQuotaThreshold(Long diskQuotaThreshold) {
		this.diskQuotaThreshold = diskQuotaThreshold;
	}

	/**
	 * get virtual memory
	 * @return the virtual memory
	 */
	public  Long getVirtualMemoryThreshold() {
		return virtualMemoryThreshold;
	}

	/**
	 * set teh virtual memory
	 * @param virtualMemoryThreshold
	 */
	public  void setVirtualMemoryThreshold(Long virtualMemoryThreshold) {
		this.virtualMemoryThreshold = virtualMemoryThreshold;
	}

	/**
	 * set the CPUload
	 * @return
	 */
	public  Double getCpuLoadThreshold() {
		return cpuLoadThreshold;
	}

	/**
	 * set the CPU load
	 * @param cpuLoadThreshold
	 */
	public  void setCpuLoadThreshold(Double cpuLoadThreshold) {
		this.cpuLoadThreshold = cpuLoadThreshold;
	}



	/**
	 * Monitor Users
	 * @author Andrea Manzi(CERN)
	 *
	 */
	public class MonitorUser {
		private String mail;
		private String name;
		private String scope;
		private boolean receiveSummary;
		private boolean notify;
		private boolean admin;


		/**
		 * Monitor user
		 * @param name the name
		 * @param mail the mail
		 * @param notify notify?
		 * @param receiveSummary receiveSummary?
		 */
		public MonitorUser(String name,String mail,String scope,boolean notify,boolean receiveSummary,boolean admin){
			this.name = name;
			this.mail = mail;
			this.receiveSummary = receiveSummary;
			this.notify = notify;
			this.admin = admin;
			this.scope = scope;
		}

		/**
		 * get the mail
		 * @return the mail
		 */
		public String getMail() {
			return mail;
		}
		/**
		 * set the mail 
		 * @param mail the mail
		 */
		public void setMail(String mail) {
			this.mail = mail;
		}

		/**
		 * get the name
		 * @return the name
		 */
		public String getName() {
			return name;
		}
		/**
		 * set the name
		 * @param name teh name
		 */
		public void setName(String name) {
			this.name = name;
		}
		/**
		 * isReceiveSummary
		 * @return isReceiveSummary
		 */
		public boolean isReceiveSummary() {
			return receiveSummary;
		}
		public String getScope() {
			return scope;
		}

		public void setScope(String scope) {
			this.scope = scope;
		}

		/**
		 * isReceiveSummary
		 * @param receiveSummary isReceiveSummary
		 */
		public void setReceiveSummary(boolean receiveSummary) {
			this.receiveSummary = receiveSummary;
		}
		/**
		 * isNotify
		 * @return isNotify
		 */
		public boolean isNotify() {
			return notify;
		}
		/**
		 * isNotify
		 * @param notify isNotify
		 */
		public void setNotify(boolean notify) {
			this.notify = notify;
		}

		/**
		 * @return isAdmin
		 */
		public boolean isAdmin() {
			return admin;
		}
		/**
		 * setAdmin
		 * @param admin setAdmin
		 */
		public void setAdmin(boolean admin) {
			this.admin = admin;
		}


	}
}
