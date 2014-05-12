package org.gcube.messaging.accounting.portalaccountingportlet.server;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import net.sf.csv4j.CSVWriter;

import org.gcube.application.framework.core.session.ASLSession;
import org.gcube.application.framework.core.session.SessionManager;
import org.gcube.common.core.scope.GCUBEScope;
import org.gcube.common.core.utils.logging.GCUBEClientLog;
import org.gcube.messaging.accounting.portalaccountingportlet.client.AccountingCostants.EntryType;
import org.gcube.messaging.accounting.portalaccountingportlet.client.data.CollectionPair;
import org.gcube.messaging.accounting.portalaccountingportlet.client.data.ContentPair;
import org.gcube.messaging.accounting.portalaccountingportlet.client.data.GCUBEUser;
import org.gcube.messaging.accounting.portalaccountingportlet.client.data.GHNDetail;
import org.gcube.messaging.accounting.portalaccountingportlet.client.data.TermPair;
import org.gcube.messaging.accounting.portalaccountingportlet.client.data.WebAppDetail;
import org.gcube.messaging.accounting.portalaccountingportlet.client.interfaces.AccountingUIService;
import org.gcube.messaging.common.consumerlibrary.impl.ConsumerLibrary;
import org.gcube.messaging.common.consumerlibrary.query.PortalAccountingQuery;
import org.gcube.portal.custom.scopemanager.scopehelper.ScopeHelper;


import com.google.gwt.user.server.rpc.RemoteServiceServlet;


/**
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public class AccountingUI  extends RemoteServiceServlet implements AccountingUIService {

	/** Logger */
	private static GCUBEClientLog logger = new GCUBEClientLog(AccountingUI.class);

	private static final long serialVersionUID = 1L;

	/**
	 * ConsumerLibrary
	 */
	private static ConsumerLibrary library = null;


	/**
	 * Class constructor
	 */
	public AccountingUI() {
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

	private ConsumerLibrary getConsumerLibrary() throws Exception{
		if (library == null){
			try {
				library = new ConsumerLibrary(getASLsession().getScope().getInfrastructure());
			}catch (Exception e){
				logger.error("Error getting ASL session, returning default", e);
				library = new ConsumerLibrary(GCUBEScope.getScope("/d4science.research-infrastructures.eu"));	
				//library = new ConsumerLibrary(GCUBEScope.getScope("/gcube"),
				//		"pcd4science3.cern.ch",
				//		"8080"
				//		);
			}
		} 
		return library;
	} 


	public ArrayList<ArrayList<String>> getUsers() throws Exception{
		PortalAccountingQuery query = null;
		try {
			query = getConsumerLibrary().getQuery(PortalAccountingQuery.class);	
			query.setQuery("SELECT DISTINCT user FROM PORTALACCOUNTING " +
					"WHERE type='"+EntryType.LoginRecord+"' ORDER BY user");
			query.query();
		}  catch (Exception e) {
			logger.error(e);
		}
		try {
			return query.getResultsAsArray();
		} catch (Exception e) {
			logger.error(e);
			throw e;
		} 
	}
	
	

	public String getRecordsByTypeAndUserJson(String type,String user,String sortColumn,String sortDir,int start,int limit,String []date) throws Exception{
		PortalAccountingQuery query = null;
		
		try {
			query = getConsumerLibrary().getQuery(PortalAccountingQuery.class);			
			query.setLimitClause(start, limit);	
			query.setOrderClause(sortColumn + " " +sortDir);

		}  catch (Exception e) {
			logger.error(e);
		}
		try {
			if (type.compareTo("")==0 && user.compareTo("") ==0)
			{
				query.setQuery("SELECT * FROM PORTALACCOUNTING WHERE "
						+selectDateFilter(date,""));
				return query.query();
			}
			else if (type.compareTo("")==0 && user.compareTo("") !=0 ) 
			{
				query.setQuery("SELECT * FROM PORTALACCOUNTING WHERE " +
						"user='"+user+"'"+ selectDateFilter(date,"AND"));
				return query.query();
			}
			else if (type.compareTo("")!=0 && user.compareTo("") ==0 ) 
			{
				return query.queryByType(type, date);
			}
			else return query.queryByUser(type, user,date);

		} catch (Exception e) {
			logger.error(e);
			throw e;
		} 
	}

	public ArrayList<CollectionPair> getCollections(String id) throws Exception{
		PortalAccountingQuery query = null;
		ArrayList<CollectionPair> collections= new ArrayList<CollectionPair>();
		try {
			query = getConsumerLibrary().getQuery(PortalAccountingQuery.class);	
			HashMap<String,String> temp = query.getCollections(id);

			for (String key: temp.keySet()){
				CollectionPair pair = new CollectionPair();
				pair.setId(key);
				pair.setName(temp.get(key));
				collections.add(pair);
			}
		}  catch (Exception e) {
			logger.error(e);
		}
		try {
			return collections;
		} catch (Exception e) {
			logger.error(e);
			throw e;
		} 
	}

	public ArrayList<WebAppDetail> getWebAppDetail(String id) throws Exception{
		PortalAccountingQuery query = null;
		ArrayList<WebAppDetail> webapps= new ArrayList<WebAppDetail>();
		try {
			query = getConsumerLibrary().getQuery(PortalAccountingQuery.class);	
			HashMap<String,String> temp = query.getWebAppDetails(id);
			for (String key: temp.keySet()){
				WebAppDetail webapp = new WebAppDetail();
				webapp.setId(key);
				webapp.setName(temp.get(key));
				webapps.add(webapp);
			}
		}  catch (Exception e) {
			logger.error(e);
		}
		try {
			return webapps;
		} catch (Exception e) {
			logger.error(e);
			throw e;
		} 
	}
	
	public ArrayList<GHNDetail> getGHNDetail(String id) throws Exception{
		PortalAccountingQuery query = null;
		ArrayList<GHNDetail> ghns= new ArrayList<GHNDetail>();
		try {
			query = getConsumerLibrary().getQuery(PortalAccountingQuery.class);	
			HashMap<String,String> temp = query.getGHNDetails(id);
			for (String key: temp.keySet()){
				GHNDetail pair = new GHNDetail();
				pair.setGhnID(key);
				pair.setGhnName(temp.get(key));
				ghns.add(pair);
			}
		}  catch (Exception e) {
			logger.error(e);
		}
		try {
			return ghns;
		} catch (Exception e) {
			logger.error(e);
			throw e;
		} 
	}
	
	public ArrayList<TermPair> getTerms(String id) throws Exception{
		PortalAccountingQuery query = null;
		ArrayList<TermPair> terms= new ArrayList<TermPair>();
		try {
			query = getConsumerLibrary().getQuery(PortalAccountingQuery.class);	
			HashMap<String,String> temp = query.getTerms(id);

			for (String key: temp.keySet()){
				TermPair pair = new TermPair();
				pair.setTermName(key);
				pair.setTermValue(temp.get(key));
				terms.add(pair);
			}
		}  catch (Exception e) {
			logger.error(e);
		}
		try {
			return terms;
		} catch (Exception e) {
			logger.error(e);
			throw e;
		} 
	}

	public ContentPair getContent(String id) throws Exception {
		PortalAccountingQuery query = null;
		ContentPair pair= new ContentPair();
		try {
			query = getConsumerLibrary().getQuery(PortalAccountingQuery.class);	
			String[] content = query.getContent(id);
			pair.setContentId(content[0]);
			pair.setContentName(content[1]);

		}  catch (Exception e) {
			logger.error(e);
		}
		try {
			return pair;
		} catch (Exception e) {
			logger.error(e);
			throw e;
		} 
	}
	
	public ArrayList<GCUBEUser> getAddresseesGCUBEUsers(String id) throws Exception {
		PortalAccountingQuery query = null;
		
		ArrayList<GCUBEUser>  list= new ArrayList<GCUBEUser>();
		try {
			query = getConsumerLibrary().getQuery(PortalAccountingQuery.class);	
			HashMap<String, String > map= query.getAddresseesGCUBEUsers(id);
			for (String name:map.keySet()) {
				GCUBEUser us = new GCUBEUser();
				us.setName(name);
				us.setVre(map.get(name));
				list.add(us);
			}

		}  catch (Exception e) {
			logger.error(e);
		}
		try {
			return list;
		} catch (Exception e) {
			logger.error(e);
			throw e;
		} 
	}

	public String getStatistics(String type, String user, String groupBy,String sortColumn,String sortDir,String []dates)throws Exception {
		PortalAccountingQuery query = null;
		String result;
		try {
			query = getConsumerLibrary().getQuery(PortalAccountingQuery.class);	
			query.setOrderClause(sortColumn + " " +sortDir);
		}  catch (Exception e) {
			logger.error(e);
		}
		try {
			if (user.compareTo("")==0)
				result =query.countByTypeAndUserWithGrouping(type, groupBy,dates);
			else result =query.countByTypeAndUserWithGrouping(type, groupBy,dates,user );
		} catch (Exception e) {
			logger.error(e);
			throw e;
		} 
		return result;

	}

	public String export(ArrayList<String[]> records,String name)throws Exception {
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

	private String selectDateFilter(String[] date,String delimiter) {
		String ret= "";
		switch (date.length)
		{
		case 0:
			break;
		case 1: 
			ret= delimiter +" date='"+date[0]+"'";
			break;
		case 2:
			ret = delimiter +" date BETWEEN '"+date[0]+"' AND '"+date[1]+"'";
			break;

		}
		return ret;
	}

	public String getStartDate() throws Exception {
		PortalAccountingQuery query = null;
		try {
			query = getConsumerLibrary().getQuery(PortalAccountingQuery.class);	
			query.setQuery("SELECT DISTINCT date FROM PORTALACCOUNTING ORDER BY date ASC LIMIT 1,1");
			query.query();
		}  catch (Exception e) {
			logger.error(e);
		}
		try {
			return query.getResultsAsArray().get(1).get(0);
		} catch (Exception e) {
			logger.error(e);
			throw e;
		} 
	}
}