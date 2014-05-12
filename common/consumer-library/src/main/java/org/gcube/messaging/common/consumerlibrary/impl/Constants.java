package org.gcube.messaging.common.consumerlibrary.impl;

import javax.xml.namespace.QName;

import org.gcube.common.clients.stubs.jaxws.GCoreService;
import org.gcube.messaging.common.consumerlibrary.fws.MessagingServiceJAXWSStubs;

import static org.gcube.common.clients.stubs.jaxws.GCoreServiceBuilder.*;

/**
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public interface Constants {
	/**  * LIMIT  */
	public final static String LIMIT = "LIMIT";
	/**  * LIMIT  */
	public final static String ORDERBY = "ORDER BY";
	/**  * LIMIT  */
	public final static String GROUPBY = "GROUP BY";
	/**  * LIMIT  */
	public final static String FLOAT = "FLOAT";
	/**  * LIMIT  */
	public final static String BIGINT = "BIGINT";
	/**  * LIMIT  */
	public final static String DECIMAL = "DECIMAL (3,2)";
	/**  * LIMIT  */
	public final static String UNSIGNED = "UNSIGNED";
	/**  * LIMIT  */
	public final static String IDENTIFIER = "identifier";
	/**  * LIMIT  */
	public final static String NAME = "name";
	/**  * LIMIT  */
	public final static String VALUE = "value";
	/**  * LIMIT  */
	public final static String ID = "***ID***";
	
	public final static String TYPE = "***TYPE***";
	/**  * LIMIT  */
	public final static String USER = "user";
	/**  * LIMIT  */
	public final static String VRE = "vre";
	/**  * LIMIT  */
	public final static String TIME = "time";
	/**  * LIMIT  */
	public final static String QUERYID = "Id";
	/**  * LIMIT  */
	public final static String DATE = "date";
	/**  * LIMIT  */
	public final static String MESSAGE = "message";
	/**  * LIMIT  */
	public final static String BROWSEBY = "browseBy";
	/**  * LIMIT  */
	public final static String ISDISTINCT = "isDistinct";
	/**  * LIMIT  */
	public final static String TERMVALUE = "termValue";
	/**  * LIMIT  */
	public final static String OPERATOR = "operator";
	/**  * LIMIT  */
	public final static String COUNT = "COUNT";
	/**  * GROUPBY  */
	public final static String GROUPBYCOUNT = "***GROUPBY***";
	/**  * IDENTIFIER_UPPERCASE  */
	public final static String IDENTIFIER_UPPERCASE = "IDENTIFIER"; 
	/**  * NAME_UPPERCASE  */
	public final static String NAME_UPPERCASE = "NAME";
	/**  * SUBTYPE  */
	public final static String SUBTYPE = "SUBTYPE";
	/**  * HL_TYPE  */
	public final static String HL_TYPE = "HL_TYPE";
	/**  	TITLE  */
	public final static String TITLE = "TITLE";
	/**  	VRE  */
	public final static String VRE_UPPERCASE = "VRE";
	/**  	ACTION  */
	public static final String ACTION = "ACTION";
	
	public static final String AUTHOR = "AUTHOR";
	
	public static final String MIMETYPE = "MIMETYPE";
	
	public static final String TEMPLATEID = "TEMPLATEID";
	
	public static final String TEMPLATENAME = "TEMPLATENAME";
	
	public static final String REPORTNAME = "REPORTNAME";
	
	public static final String STATUS = "STATUS";
	
	public static final String STEPNUMBER = "STEPNUMBER";
	
	public static final String WORKFLOWID = "WORKFLOWID";
	
	public static final String HSPECID = "HSPECID";
	
	public static final String GIS = "GIS";
	
	public static final String OBJECTID = "OBJECTID";
	
	public static final String SPECIESCOUNT = "SPECIESCOUNT";
	
	public static final String AQUAMAPSTYPE = "AQUAMAPSTYPE";
	
	public static final String WEBAPPID = "WEBAPPID";
	
	public static final String WEBAPPNAME = "WEBAPPNAME";
	
	public static final String WEBAPPVERSION = "WEBAPPVERSION";
	
	public static final String GHNID = "GHNID";
	
	public static final String GHNNAME = "GHNNAME";
	
	public static final String WARID = "WARID";
	
	public static final String WARNAME = "WARNAME";
	
	public static final String CATEGORY = "CATEGORY";
	
	public static final String SERVICE_CLASS = "Messaging";
	
	public static final String SERVICE_NAME = "Consumer";
	
	public static final String NAMESPACE = "http://gcube-system.org/namespaces/messaging/common/consumer";

	public static final String PORT_TYPE_NAME = "gcube/messaging/common/consumer/MessagingConsumer";;
	

	public static final String serviceNS = "http://gcube-system.org/namespaces/messaging/common/consumer/service";
	  
	  
	public static final String serviceLocalName = "MessagingConsumerService";
	
	public static final QName serviceName = new QName(serviceNS,serviceLocalName);
	 
	public static final String porttypeNS = "http://gcube-system.org/namespaces/messaging/common/consumer";
	
	static final String porttypeLocalName = "MessagingConsumerPortType";
	  
	public static final String service_class="Messaging";
	
	public static final String service_name="Consumer";
	  
   
	public static final GCoreService<MessagingServiceJAXWSStubs> consumer = service().withName(serviceName)
            .coordinates(service_class,service_name)
           .andInterface(MessagingServiceJAXWSStubs.class); 

}

