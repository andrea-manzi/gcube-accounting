package org.gcube.messaging.common.consumerlibrary.fws;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;

import org.gcube.common.clients.stubs.jaxws.JAXWSUtils.Empty;

import static org.gcube.messaging.common.consumerlibrary.impl.Constants.*;


@WebService(name=porttypeLocalName,targetNamespace=porttypeNS)
public interface MessagingServiceJAXWSStubs {
	
	@SOAPBinding(parameterStyle=ParameterStyle.BARE)
	public Empty backupMonitoringDB(Empty empty);
	
	@SOAPBinding(parameterStyle=ParameterStyle.BARE)
	public Empty backupAccountingDB(Empty epmy);
	
	@SOAPBinding(parameterStyle=ParameterStyle.BARE)
	public String queryMonitoringDB(String s);
	
	@SOAPBinding(parameterStyle=ParameterStyle.BARE)
	public String queryAccountingDB(String s);
	
	
	@SOAPBinding(parameterStyle=ParameterStyle.BARE)
	public String querySystemAccountingDB(String s);

}


