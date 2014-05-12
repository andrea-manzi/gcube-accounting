package org.gcube.messaging.accounting.nodeaccountingportlet.server;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import net.sf.csv4j.CSVWriter;

import org.gcube.application.framework.core.session.ASLSession;
import org.gcube.application.framework.core.session.SessionManager;
import org.gcube.messaging.accounting.nodeaccountingportlet.stubs.NodeAccountingService;
import org.gcube.messaging.common.consumerlibrary.ConsumerCL;
import org.gcube.messaging.common.consumerlibrary.proxies.Proxies;
import org.gcube.messaging.common.consumerlibrary.query.NodeAccountingQuery;
import org.gcube.portal.custom.scopemanager.scopehelper.ScopeHelper;
import org.gcube.common.scope.impl.ScopeBean;
import org.gcube.common.scope.impl.ScopeBean.Type;
import org.gcube.common.scope.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */

public class NodeAccountingServiceImpl extends RemoteServiceServlet implements
NodeAccountingService {



	/** Logger */
	private static Logger logger = LoggerFactory.getLogger(NodeAccountingServiceImpl.class);

	private static final long serialVersionUID = 1L;

	/**
	 * ConsumerLibrary
	 */
	private  ConsumerCL library = null;


	/**
	 * Class constructor
	 */
	public NodeAccountingServiceImpl() {
		try {
			super.init();
		} catch (ServletException e) {
			logger.error("Servlet failed to initialize");
		}
	}

	/**
	 * Get the ASL session
	 * 
	 * @return the ASL session
	 */

	private ASLSession getASLsession() throws Exception{
		HttpSession httpSession = this.getThreadLocalRequest().getSession();
		String username =  httpSession.getAttribute(ScopeHelper.USERNAME_ATTRIBUTE).toString();
		ASLSession session = SessionManager.getInstance().getASLSession(httpSession.getId(), username);
		return session;
	}

	/**
	 * Get the the consumerLibrary
	 * 
	 * @return the consumerLibrary
	 */
	
	private String getInfraScope(String scope){
		ScopeBean sc = new ScopeBean(scope);
		while (!sc.is(Type.INFRASTRUCTURE))
			sc= sc.enclosingScope();
		return sc.toString();
		
	}

	private ConsumerCL getConsumerLibrary() throws Exception{
		if (library == null){
			try {
				ScopeProvider.instance.set(getInfraScope( getASLsession().getScope()));
				library = Proxies.consumerService().withTimeout(1, TimeUnit.MINUTES).build();
			}catch (Exception e){
				logger.error("Error getting ASL session, returning default", e);
				ScopeProvider.instance.set("/d4science.research-infrastructures.eu");
				library = Proxies.consumerService().withTimeout(1, TimeUnit.MINUTES).build();
			}
		} 
		return library;
	} 
	public String export(ArrayList<String[]> records, String name) throws IOException{
		String file = System.getProperty("java.io.tmpdir")
		+java.io.File.separator+name+System.currentTimeMillis()+".csv";
		FileWriter fileWriter= new FileWriter(file); 
		CSVWriter writer = new CSVWriter(fileWriter);
		for (String[] record : records){
			writer.writeLine(record);
		}
		fileWriter.flush();
		fileWriter.close();
		return file;

	}



	public String getStartDate() throws Exception {
		NodeAccountingQuery query = null;
		try {
			query = getConsumerLibrary().getQuery(NodeAccountingQuery.class,library);	
			query.setQuery("SELECT DISTINCT startDate FROM NODEACCOUNTING ORDER BY startDate ASC LIMIT 1,1");
			query.query();
		}  catch (Exception e) {
			logger.error(e.getMessage());
		}
		try {
			return query.getResultsAsArray().get(1).get(0);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		} 
	}


	public Map<String,ArrayList<String>> getGHNs() throws Exception {
		NodeAccountingQuery query = null;
		try {
			query = getConsumerLibrary().getQuery(NodeAccountingQuery.class,library);	
			return query.getGHNs();
		}catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		} 
	}

	public String getRecords(String ghn, String scope, String serviceClass,
			String serviceName, String sortColumn, String sortDir, int start,
			int limit, String[] date) throws Exception {
		NodeAccountingQuery query = null;
		try {
			query = getConsumerLibrary().getQuery(NodeAccountingQuery.class,library);	 
			query.setLimitClause(start, limit);	
			query.setOrderClause(sortColumn + " " +sortDir);

			query.setQuery("SELECT * FROM NODEACCOUNTING "+selectDateFilter(date,"WHERE")
					+((ghn.compareTo("")==0)?"":" AND GHNName ='"+ghn+"'")
					+((scope.compareTo("")==0)?"":" AND callerScope ='"+scope+"'")
					+((serviceClass.compareTo("")==0)?"":" AND ServiceClass ='"+serviceClass+"'")
					+((serviceName.compareTo("")==0)?"":" AND ServiceName ='"+serviceName+"'"));
		}  catch (Exception e) {
			logger.error(e.getMessage());
		}
		try {
			return query.query();
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		} 
	}


	public ArrayList<ArrayList<String>> getScopes() throws Exception {
		NodeAccountingQuery query = null;
		ArrayList<ArrayList<String>> results = null;
		try {
			query = getConsumerLibrary().getQuery(NodeAccountingQuery.class,library);	
			query.getScopes();
			return query.getResultsAsArray();
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}

	}

	public Map<String,ArrayList<String>> getServices() throws Exception {
		NodeAccountingQuery query = null;
		try {
			query = getConsumerLibrary().getQuery(NodeAccountingQuery.class,library);	
			return query.getServices();
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}

	}

	private String selectDateFilter(String[] date,String delimiter) {
		String ret= "";
		switch (date.length)
		{
		case 0:
			break;
		case 1: 
			ret= delimiter +" endDate='"+date[0]+"'";
			break;
		case 2:
			ret = delimiter +" startDate >= '"+date[0]+"' AND endDate <= '"+date[1]+"'";
			break;

		}
		return ret;
	}

	public String getStatistics(String serviceClass, String serviceName,
			String callerScope, String ghn, String groupBy, String sortColumn,
			String sortDir, String[] dates) throws Exception {
		NodeAccountingQuery query = null;
		try {
			query = getConsumerLibrary().getQuery(NodeAccountingQuery.class,library);	 
			query.setOrderClause(sortColumn + " " +sortDir);
			if (groupBy.compareTo("Date")==0)
					groupBy="DATE(startDate)";
			query.getInvocationPerInterval(serviceClass, serviceName, ghn, dates[0], dates[1], callerScope, groupBy);
		}  catch (Exception e) {
			logger.error(e.getMessage());
		}
		try {
			return query.getResults();
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		} 
	}

}
