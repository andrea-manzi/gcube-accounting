package org.gcube.messaging.accounting.portalaccountingportlet.server;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import net.sf.csv4j.CSVWriter;

import org.gcube.application.framework.core.session.ASLSession;
import org.gcube.application.framework.core.session.SessionManager;
import org.gcube.common.scope.api.ScopeProvider;
import org.gcube.messaging.accounting.portalaccountingportlet.client.AccountingCostants.EntryType;
import org.gcube.messaging.accounting.portalaccountingportlet.client.interfaces.AccountingUIService;
import org.gcube.messaging.accounting.portalaccountingportlet.client.obj.CollectionPair;
import org.gcube.messaging.accounting.portalaccountingportlet.client.obj.ContentPair;
import org.gcube.messaging.accounting.portalaccountingportlet.client.obj.Data;
import org.gcube.messaging.accounting.portalaccountingportlet.client.obj.DataObj;
import org.gcube.messaging.accounting.portalaccountingportlet.client.obj.GCUBEUser;
import org.gcube.messaging.accounting.portalaccountingportlet.client.obj.GHNDetail;
import org.gcube.messaging.accounting.portalaccountingportlet.client.obj.Statistics;
import org.gcube.messaging.accounting.portalaccountingportlet.client.obj.StatisticsData;
import org.gcube.messaging.accounting.portalaccountingportlet.client.obj.TermPair;
import org.gcube.messaging.accounting.portalaccountingportlet.client.obj.WebAppDetail;
import org.gcube.messaging.common.consumerlibrary.ConsumerCL;
import org.gcube.messaging.common.consumerlibrary.proxies.Proxies;
import org.gcube.messaging.common.consumerlibrary.query.PortalAccountingQuery;
import org.gcube.messaging.common.consumerlibrary.query.PredefinedQueries;
import org.gcube.portal.custom.scopemanager.scopehelper.ScopeHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.SortInfo;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoadResultBean;



public class AccountingUI  extends RemoteServiceServlet implements AccountingUIService {

	/** Logger */
	private static Logger logger = LoggerFactory.getLogger(AccountingUI.class);

	private static final long serialVersionUID = 1L;

	/**
	 * ConsumerLibrary
	 */
	private ConsumerCL library = null;


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

	private ConsumerCL getConsumerLibrary() throws Exception{
			try {
				ScopeProvider.instance.set(getASLsession().getScope());
				library = Proxies.consumerService().withTimeout(1, TimeUnit.MINUTES).build();
				
			}catch (Exception e){
				logger.error("Error getting ASL session, returning default", e);
				ScopeProvider.instance.set("/gcube");
				library = Proxies.consumerService().withTimeout(1, TimeUnit.MINUTES).build();
				
			}
		
		return library;
	} 


	public ArrayList<ArrayList<String>> getUsers() throws Exception{
		PortalAccountingQuery query = null;
		try {
			query = getConsumerLibrary().getQuery(PortalAccountingQuery.class,library);	
			query.setQuery("SELECT DISTINCT user FROM PORTALACCOUNTING " +
					"WHERE type='"+EntryType.LoginRecord+"' ORDER BY user");
			query.query();
		}  catch (Exception e) {
			logger.error(e.getMessage());
		}
		try {
			return query.getResultsAsArray();
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		} 
	}

	public ArrayList<ArrayList<String>> getRecordTypes() throws Exception{
		PortalAccountingQuery query = null;
		try {
			query = getConsumerLibrary().getQuery(PortalAccountingQuery.class,library);	
			query.setQuery("SELECT DISTINCT type FROM PORTALACCOUNTING " +
					" ORDER BY type");
			query.query();
		}  catch (Exception e) {
			logger.error(e.getMessage());
		}
		try {
			return query.getResultsAsArray();
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		} 
	}
	public ArrayList<ArrayList<String>> getVres() throws Exception{
		PortalAccountingQuery query = null;
		try {
			query = getConsumerLibrary().getQuery(PortalAccountingQuery.class,library);	
			query.setQuery("SELECT DISTINCT vre FROM PORTALACCOUNTING " +
					" ORDER BY vre");
			query.query();
		}  catch (Exception e) {
			logger.error(e.getMessage());
		}
		try {
			return query.getResultsAsArray();
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		} 
	}

	public int getNumOfRecords(String query) throws Exception{
		String result = null;
		try {
			PortalAccountingQuery queryGetNumOfRecords = null;
			queryGetNumOfRecords=getConsumerLibrary().getQuery(PortalAccountingQuery.class,library);
			queryGetNumOfRecords.setQuery(query);
			result = queryGetNumOfRecords.query();
		}  catch (Exception e) {
			logger.error(e.getMessage());
		}

		if(result==null)return 0;
		//the results generally are in json format 
		// this type of result will be sth like : 
		// "{"data":[{"COUNT(*)":"5366"}],"total_count":1}"
		//so we must extract the first group number
		try {
			String number="";
			boolean nextNotDigitOut=false;
			for (int i = 0; i < result.length(); i++) {
				if (Character.isDigit(result.charAt(i))) {
					// found a digit
					number=number+result.charAt(i);
					nextNotDigitOut=true; //take only the first group of numbers
				}
				else if (nextNotDigitOut)break;
			}
			int num = Integer.valueOf(number);
			return num;
		}  catch (Exception e) {
			logger.error(e.getMessage());
			return 0;
		}
	}


	public PagingLoadResult<DataObj> getRecordsForLiveGrid(PagingLoadConfig config, List<String> types,List<String> users,List<String> vres,String []date) throws Exception{
		PortalAccountingQuery query = null;
		int startRecord = config.getOffset();  
		int limit=config.getLimit();
		int total_limit;

		String sortColumn=null;
		String sortDir=null;
		for(SortInfo tmp:config.getSortInfo()){ //list contains only one SortInfo obj
			sortColumn=tmp.getSortField();
			sortDir=tmp.getSortDir().name();
			break; 
		}		
		if(sortColumn==null || sortDir==null){
			sortColumn="date";
			sortDir=SortDir.DESC.toString();
		}
		
		String tmp=" ",tmp2=" ",tmp3=" ";
		for(String a:users)	tmp=tmp+a+", ";
		for(String a:types)	tmp2=tmp2+a+", ";
		for(String a:vres)	tmp3=tmp3+a+", ";
		System.out.println("\n.... filtering ...."+"\n"+
				"sortColumn="+sortColumn+" - sortDir="+sortDir+"\n"+
				"dateStart="+date[0]+" - dateEnd="+date[1]+"\n"+
				"users:\n"+tmp+"\ntypes:\n"+tmp2+"\nvres:\n"+tmp3+"\n");

				
		String res=null;		
		try {
			query = getConsumerLibrary().getQuery(PortalAccountingQuery.class,library);			
			query.setLimitClause(startRecord, limit);	
			query.setOrderClause(sortColumn + " " +sortDir);
		}  catch (Exception e) {
			logger.error(e.getMessage());
		}

		try {
			String filterOfUser="";
			if(users.size()!=0)filterOfUser=selectTypeOrUserOrVreFilter(users,"user")+" AND";
			String filterOfType="";
			if(types.size()!=0)filterOfType=selectTypeOrUserOrVreFilter(types,"type")+" AND";
			String filterOfVre="";
			if(vres.size()!=0)filterOfVre=selectTypeOrUserOrVreFilter(vres,"vre")+" AND";

			String queryString = "FROM PORTALACCOUNTING WHERE "+
					filterOfUser+
					filterOfType+
					filterOfVre+
					selectDateFilter(date,"");

			total_limit = getNumOfRecords("SELECT COUNT(*) "+queryString);				
			queryString = "SELECT * "+queryString;
			query.setQuery(queryString);
			res= query.query();


		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		} 

		if(res==null){
			System.out.println("\nquery result .... null!! \n");
			return null;
		}

		List<DataObj> list=new ArrayList<DataObj>();
		Gson gson=new GsonBuilder().serializeNulls().create();

		Data data=gson.fromJson(res,Data.class);
		if(data.getData()!=null){
			System.out.println("\n.... query result .... "+data.total_count+"\n"+res+"\n");
			for(int i=0;i<data.getData().size();i++){
				list.add(data.getData().get(i));
			}
		}
		else System.out.println("\nquery result ....data.getData()=null\n");

		System.out.println("\n\nPagingLoadConfig ...\nconfig.getOffset()="+startRecord+" - config.getLimit()="+limit+"\ntotal_limit="+total_limit);
		return new PagingLoadResultBean<DataObj>(list, total_limit, startRecord);
	}

	public String selectTypeOrUserOrVreFilter(List<String> objs,String fieldName){
		String selectFilter="";
		int i=1;
		for(String obj:objs){
			selectFilter=selectFilter+fieldName+"='"+obj+"'";
			if(i<objs.size())selectFilter=selectFilter+" OR ";
			i++;
		}
		return "("+selectFilter+")";
	}
	
	
	

	public ArrayList<CollectionPair> getCollections(String id) throws Exception{
		PortalAccountingQuery query = null;
		ArrayList<CollectionPair> collections= new ArrayList<CollectionPair>();
		try {
			query = getConsumerLibrary().getQuery(PortalAccountingQuery.class,library);	
			HashMap<String,String> temp = query.getCollections(id);

			for (String key: temp.keySet()){
				CollectionPair pair = new CollectionPair();
				pair.setId(key);
				pair.setName(temp.get(key));
				collections.add(pair);
			}
		}  catch (Exception e) {
			logger.error(e.getMessage());
		}
		try {
			return collections;
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		} 
	}

	public ArrayList<WebAppDetail> getWebAppDetail(String id) throws Exception{
		PortalAccountingQuery query = null;
		ArrayList<WebAppDetail> webapps= new ArrayList<WebAppDetail>();
		try {
			query = getConsumerLibrary().getQuery(PortalAccountingQuery.class,library);	
			HashMap<String,String> temp = query.getWebAppDetails(id);
			for (String key: temp.keySet()){
				WebAppDetail webapp = new WebAppDetail();
				webapp.setId(key);
				webapp.setName(temp.get(key));
				webapps.add(webapp);
			}
		}  catch (Exception e) {
			logger.error(e.getMessage());
		}
		try {
			return webapps;
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		} 
	}

	public ArrayList<GHNDetail> getGHNDetail(String id) throws Exception{
		PortalAccountingQuery query = null;
		ArrayList<GHNDetail> ghns= new ArrayList<GHNDetail>();
		try {
			query = getConsumerLibrary().getQuery(PortalAccountingQuery.class,library);	
			HashMap<String,String> temp = query.getGHNDetails(id);
			for (String key: temp.keySet()){
				GHNDetail pair = new GHNDetail();
				pair.setGhnID(key);
				pair.setGhnName(temp.get(key));
				ghns.add(pair);
			}
		}  catch (Exception e) {
			logger.error(e.getMessage());
		}
		try {
			return ghns;
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		} 
	}

	public ArrayList<TermPair> getTerms(String id) throws Exception{
		PortalAccountingQuery query = null;
		ArrayList<TermPair> terms= new ArrayList<TermPair>();
		try {
			query = getConsumerLibrary().getQuery(PortalAccountingQuery.class,library);	
			HashMap<String,String> temp = query.getTerms(id);

			for (String key: temp.keySet()){
				TermPair pair = new TermPair();
				pair.setTermName(key);
				pair.setTermValue(temp.get(key));
				terms.add(pair);
			}
		}  catch (Exception e) {
			logger.error(e.getMessage());
		}
		try {
			return terms;
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		} 
	}

	public ContentPair getContent(String id) throws Exception {
		PortalAccountingQuery query = null;
		ContentPair pair= new ContentPair();
		try {
			query = getConsumerLibrary().getQuery(PortalAccountingQuery.class,library);	
			String[] content = query.getContent(id);
			pair.setContentId(content[0]);
			pair.setContentName(content[1]);

		}  catch (Exception e) {
			logger.error(e.getMessage());
		}
		try {
			return pair;
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		} 
	}

	public ArrayList<GCUBEUser> getAddresseesGCUBEUsers(String id) throws Exception {
		PortalAccountingQuery query = null;

		ArrayList<GCUBEUser>  list= new ArrayList<GCUBEUser>();
		try {
			query = getConsumerLibrary().getQuery(PortalAccountingQuery.class,library);	
			HashMap<String, String > map= query.getAddresseesGCUBEUsers(id);
			for (String name:map.keySet()) {
				GCUBEUser us = new GCUBEUser();
				us.setName(name);
				us.setVre(map.get(name));
				list.add(us);
			}

		}  catch (Exception e) {
			logger.error(e.getMessage());
		}
		try {
			return list;
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		} 
	}

	public DataObj getDetails(String id, EntryType recordType) throws Exception{
		PortalAccountingQuery query = getConsumerLibrary().getQuery(PortalAccountingQuery.class,library);		
		DataObj returnObj=null;
		String res=null;
		String stringQuery=null;
		
		if(recordType.compareTo(EntryType.BrowseRecord)==0 ){
			//browse call .. 
			stringQuery=PredefinedQueries.BROWSEQUERY +
					" AND BROWSE.id='"+id+"'";
		}
		else if(	recordType.compareTo(EntryType.AdvancedSearchRecord)==0){
			//AdvancedSearchRecord call .. 
			stringQuery=PredefinedQueries.ADVANCEDSEARCHQUERY +
					" AND ADVANCEDSEARCH.id='"+id+"'";
		}
		else if (recordType.compareTo(EntryType.SimpleSearchRecord)==0){
			//SimpleSearchRecord call .. 
			stringQuery=PredefinedQueries.SIMPLESEARCHQUERY +
					" AND SIMPLESEARCH.id='"+id+"'";
		}				
		else if (recordType.compareTo(EntryType.ContentRecord)==0 ){
			//not needed, there is another specific method for retrieving this type
		}
		else if (recordType.compareTo(EntryType.GoogleSearchRecord)==0 ){
			//GoogleSearchRecord
			stringQuery=PredefinedQueries.GOOGLESEARCHQUERY +
					" AND GOOGLESEARCH.id='"+id+"'";
		}
		else if (recordType.compareTo(EntryType.QuickSearchRecord)==0 ){
			//QuickSearchRecord
			stringQuery=PredefinedQueries.QUICKSEARCHQUERY +
					" AND QUICKSEARCH.id='"+id+"'";
		}
		else if (recordType.compareTo(EntryType.HLRecord)==0 ){
			//HL call
			stringQuery=PredefinedQueries.HLQUERY +
					" AND HL.id='"+id+"'";
		}
		else if (recordType.compareTo(EntryType.TSRecord)==0 ){
			//TSRecord
			stringQuery=PredefinedQueries.TSQUERY +
					" AND TS.id='"+id+"'";
		}
		else if (recordType.compareTo(EntryType.AISRecord)==0 ){
			//AISRecord
			stringQuery=PredefinedQueries.AISQUERY +
					" AND AIS.id='"+id+"'";
		}
		else if (recordType.compareTo(EntryType.AnnotationRecord)==0 ){
			//annotation call
			stringQuery=PredefinedQueries.ANNOTATIONQUERY +
					" AND ANNOTATION.id='"+id+"'";
		}
		else if (recordType.compareTo(EntryType.DocumentWorkflowRecord)==0 ){
			//DocumentWorkflowRecord call
			stringQuery=PredefinedQueries.WORKFLOWDOCUMENTQUERY +
					" AND DOCUMENTWORKFLOW.id='"+id+"'";
		}
		else if (recordType.compareTo(EntryType.ReportRecord)==0 ){
			//ReportRecord call
			stringQuery=PredefinedQueries.REPORTQUERY +
					" AND REPORT.id='"+id+"'";
		}
		else if (recordType.compareTo(EntryType.WebAppRecord)==0 ) {
			//not needed, there is another specific method for retrieving this type
		}
		else if (recordType.compareTo(EntryType.WarRecord)==0 ){
			//WarRecord call
			stringQuery=PredefinedQueries.WARQUERY +
					" AND WAR.id='"+id+"'";
		}
		else if (recordType.compareTo(EntryType.AquamapsRecord)==0 ){
			//AquamapsRecord call
			stringQuery=PredefinedQueries.AQUAMAPSQUERY +
					" AND AQUAMAPS.id='"+id+"'";
		}
		else if (recordType.compareTo(EntryType.GenericRecord)==0 ){
			//GenericRecord call
			stringQuery=PredefinedQueries.GENERICQUERY +
					" AND GENERIC.id='"+id+"'";
		}
		else if (recordType.compareTo(EntryType.StatisticalManagerRecord)==0 ){
			//SM call
			stringQuery=PredefinedQueries.SMQUERY +
					" AND SM.id='"+id+"'";
		}
		
		try {
			query.setQuery(stringQuery);
			res= query.query();
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		} 

		if(res==null){
			System.out.println("\ngetDetails - query result .... null!! \n");
			return null;
		}

		Gson gson=new GsonBuilder().serializeNulls().create();
		Data data=gson.fromJson(res,Data.class);
		if(data.getData()!=null){
			System.out.println("\n.... query result .... \n"+res+"\n");
			returnObj=data.getData().get(0);
		}
		else System.out.println("\nquery result ....data.getData()=null\n");
		return returnObj;
	}
	
	public List<Statistics> getStatistics(List<String> types, List<String> users,List<String> vres, List<String> groupsBy,String []dates)throws Exception {
		String res=null	;
		PortalAccountingQuery query = null;
		
		try {
			query = getConsumerLibrary().getQuery(PortalAccountingQuery.class,library);			
			//query.setOrderClause(sortColumn + " " +sortDir);
		}  catch (Exception e) {
			logger.error(e.getMessage());
		}

		try {
			//where's 
			String filterOfUser="";
			if(users.size()!=0)filterOfUser=selectTypeOrUserOrVreFilter(users,"user")+" AND";
			String filterOfType="";
			if(types.size()!=0)filterOfType=selectTypeOrUserOrVreFilter(types,"type")+" AND";
			String filterOfVre="";
			if(vres.size()!=0)filterOfVre=selectTypeOrUserOrVreFilter(vres,"vre")+" AND";		
			//groups by & select
			String grouping="";	
			String select="";
			if(groupsBy.size()!=0){
				grouping=groupByMulti(groupsBy);
				select=", ";
				int num=1;
				for(String tmp:groupsBy){
					select=select+tmp+" ";
					if(num<groupsBy.size())select=select+", ";
					num++;
				}
			}			
						
			String queryString = "SELECT COUNT(Id) AS CNT "+
					select+
					"FROM PORTALACCOUNTING WHERE "+
					filterOfUser+
					filterOfType+
					filterOfVre+
					selectDateFilter(dates,"")+
					grouping +
					" ORDER BY CNT DESC";

			System.out.println("getStatistics - query:\n"+queryString+"\n");
			query.setQuery(queryString);
			res= query.query();

		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}

		if(res==null){
			System.out.println("\nquery result .... null!! \n");
			return null;
		}
		System.out.println("getStatistics - query_result:\n"+res+"\n");
		
		List<Statistics> list=new ArrayList<Statistics>();
		Gson gson=new GsonBuilder().serializeNulls().create();
		StatisticsData data=gson.fromJson(res,StatisticsData.class);
		if(data.getData()!=null){
			for(int i=0;i<data.getData().size();i++){
				list.add(data.getData().get(i));
			}
		}
		else System.out.println("\nStatistics query result ....data.getData()=null\n");

		return list;
	}
	
	public String groupByMulti(List<String> groupsBy){
		String grouping="GROUP BY ";
		int i=1;
		for(String obj:groupsBy){
			grouping=grouping+obj;
			if(i<groupsBy.size())grouping=grouping+", ";
			i++;
		}
		return grouping+" ";
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
			ret= " " + delimiter +" date='"+date[0]+"' ";
			break;
		case 2:
			ret = " " + delimiter +" date BETWEEN '"+date[0]+"' AND '"+date[1]+"' ";
			break;

		}
		return ret;
	}

	public String getStartDate() throws Exception {
		PortalAccountingQuery query = null;
		try {
			query = getConsumerLibrary().getQuery(PortalAccountingQuery.class,library);	
			query.setQuery("SELECT DISTINCT date FROM PORTALACCOUNTING ORDER BY date ASC LIMIT 1,1");
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
}