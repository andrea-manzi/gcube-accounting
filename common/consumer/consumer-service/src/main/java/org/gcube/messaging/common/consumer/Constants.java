package org.gcube.messaging.common.consumer;

public class Constants {

	/** Name of the port-type in the JNDI configuration of the service. */
	public static final String JNDI_NAME="gcube/messaging/common/consumer";
	
	/** Name of the AccountingDBFile in the JNDI configuration of the service. */
	public static final String ACCOUNTINGDBFILE_JNDI_NAME="AccountingDBFile";
	
	/** Name of the MonitoringDBFile in the JNDI configuration of the service. */
	public static final String MONITORINGDBFILE_JNDI_NAME="MonitoringDBFile";
	
	/** Name of the AccountingSystemDBFile in the JNDI configuration of the service. */
	public static final String ACCOUNTINGSYTEMDBFILE_JNDI_NAME="AccountingSystemDBFile";
	
	/** Name of the MAILRECIPIENTS_JNDI_NAME in the JNDI configuration of the service. */
	public static final String MAILRECIPIENTS_JNDI_NAME="MailRecipients";
	
	/** Name of the CONFIGDIR_JNDI_NAME in the JNDI configuration of the service. */
	public static final String CONFIGDIR_JNDI_NAME="configDir";
	
	/** Name of the CONFIGDIR_JNDI_NAME in the JNDI configuration of the service. */
	public static final String NOTIFYBYMAIL_JNDI_NAME="NotifiybyMail";
	
	/** Name of theUSEEMBEDDEDBROKER_JNDI_NAME in the JNDI configuration of the service. */
	public static final String USEEMBEDDEDBROKER_JNDI_NAME ="UseEmbeddedBroker";
	
	
	/** Name of the UseEmbeddedDB in the JNDI configuration of the service. */
	public static final String SUBSCRIPTIONSMAPPING_JNDI_NAME ="SubscriptionsMapping";
	
	public static final String MESSAGESSUBSCRIPTIONS_JNDI_NAME ="Subscriptions";
	/** Name of the UseEmbeddedDB in the JNDI configuration of the service. */
	
	public static final String USEEMBEDDEDDB_JNDI_NAME ="UseEmbeddedDB";
	
	/** Name of the DBUser in the JNDI configuration of the service. */
	public static final String DBUSER_JNDI_NAME ="DBUser";
	
	/** Name of the DBPass in the JNDI configuration of the service. */
	public static final String DBPASS_JNDI_NAME ="DBPass";
	
	/** Name of the DBName in the JNDI configuration of the service. */
	public static final String DBHOST_JNDI_NAME ="DBHost";
	
	/** Name of the DBNport in the JNDI configuration of the service. */
	public static final String DBPORT_JNDI_NAME ="DBPort";
	
	/** Name of the MaxDBConnection in the JNDI configuration of the service. */
	public static final String MAXDBCONNECTIONS_JNDI_NAME ="MaxDBConnections";
	
	/** Name of the MailTemplatesFile in the JNDI configuration of the service. */
	public static final String MAIL_TEMPLATE_FILE_JNDI_NAME ="MailTemplatesFile";
	
	/** Name of the connectToUserManagementDB in the JNDI configuration of the service. */
	public static final String CONNECT_TO_USERMANAGEMENT_DB_JNDI_NAME ="connectToUserManagementDB";
	
	/**Name of the endpointWaitingTime in the JNDI configuration of the service */
	public static final String MESSAGING_ENDPOINT_WAITING_TIME = "endpointWaitingTime";
	
	/** Name of the endpointRefreshTime in the JNDI configuration of the service. */
	public static final String MESSAGING_ENDPOINT_REFRESH_TIME = "endpointRefreshTime";
	
	
	
	public enum NotificationSubType {
		GHN_READY("GHN_READY"),
		GHN_SHUTDOWN("GHN_SHUTDOWN"),
		SCOPE_ADDED("SCOPE_ADDED"),
		SCOPE_REMOVED("SCOPE_REMOVED");
		String type;
		NotificationSubType(String type) {this.type = type;}
		public String toString() {return this.type;}
	};

	public static enum MailTemplateToken {
		OVERLOAD("***OVERLOAD***"),
		SPACE("***SPACE***"),
		INFO("***INFO***");
		String type;
		MailTemplateToken(String type) {this.type = type;}
		public String toString() {return this.type;}
	}
	
}

