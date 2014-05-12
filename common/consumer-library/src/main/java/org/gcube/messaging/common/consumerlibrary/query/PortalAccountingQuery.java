package org.gcube.messaging.common.consumerlibrary.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.gcube.messaging.common.consumerlibrary.impl.Constants;
import org.gcube.messaging.common.consumerlibrary.json.JSONArray;
import org.gcube.messaging.common.consumerlibrary.json.JSONException;
import org.gcube.messaging.common.consumerlibrary.json.JSONObject;
import org.gcube.messaging.common.messages.PortalAccountingMessage;
import org.gcube.messaging.common.messages.records.*;
import org.gcube.messaging.common.messages.records.AISRecord.AISSubType;
import org.gcube.messaging.common.messages.records.AdvancedSearchRecord.OperatorType;
import org.gcube.messaging.common.messages.records.AnnotationRecord.AnnotationSubType;
import org.gcube.messaging.common.messages.records.DocumentWorkflowRecord.WorkflowSubType;
import org.gcube.messaging.common.messages.records.HLRecord.HLSubType;
import org.gcube.messaging.common.messages.records.ReportRecord.ReportSubType;
import org.gcube.messaging.common.messages.records.TSRecord.TSSubType;
import org.gcube.messaging.common.messages.records.WebAppRecord.WebAppSubType;
import org.gcube.messaging.common.messages.records.WarRecord.WarSubType;
import org.gcube.messaging.common.messages.records.AquamapsRecord.AquamapsSubType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public class PortalAccountingQuery extends AccountingQuery{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * queryByType
	 * @param <TYPE> the TYPE of Record to retrieve
	 * @param type the class of results
	 * @return a String representing the result in Json;
	 * @throws Exception Exception
	 */
	public <TYPE extends BaseRecord> String queryByType (Class<TYPE> type) throws  Exception {
		this.setQuery(selectQuery(type));
		query();
		return this.getResults();	
	}

	/**
	 * queryByType
	 * @param type the class of results
	 * @param date
	 * @return a String representing the result in Json;
	 * @throws Exception Exception
	 */
	public  String queryByType (String type,String []date) throws Exception {
		this.setQuery(selectQuery(type)+ selectDateFiler(date," AND"));
		query();
		return this.getResults();	
	}
	
	
	
	private String selectDateFiler(String[] date,String delimiter) {
		String ret= "";
		switch (date.length)
		{
			case 0:
				break;
			case 1: 
				ret= delimiter +" date='"+date[0]+"'";
				break;
			case 2:
				ret =delimiter +" date BETWEEN '"+date[0]+"' AND '"+date[1]+"'";
				break;
			
		}
		return ret;
	}

	/**
	 * queryByUser
	 * @param <TYPE> the TYPE of Record to retrieve
	 * @param type the class of results
	 * @param user the user  
	 * @return a String representing the result in Json;
	 * @throws Exception Exception
	 */
	public <TYPE extends BaseRecord>  String queryByUser (Class<TYPE> type, String user, String ... scope ) throws  Exception {
		this.setQuery(selectQuery(type) + " AND user='"+user+"'"
				+(scope.length>0?" AND vre='"+scope[0].toString()+"'":""));
		query();
		return this.getResults();
	}
	
	/**
	 * queryByUser
	 * @param type the TYPE of Record to retrieve
	 * @param type the class of results
	 * @param user the user  
	 * @param date
	 * @return a String representing the result in Json;
	 * @throws Exception Exception
	 */
	public  String queryByUser (String type, String user, String []date,String ... scope ) throws  Exception {
		this.setQuery(selectQuery(type) + " AND user='"+user+"'" + selectDateFiler(date," AND")
				+(scope.length>0?" AND vre='"+scope[0].toString()+"'":""));
		query();
		return this.getResults();
	}

	/**
	 * countByType
	 * @param <TYPE> the TYPE of Record to retrieve
	 * @param type the class of results
	 * @param scope the optional scope 
	 * @return the number of records for the given type ( and scope)
	 * @throws Exception Exception
	 */
	public <TYPE extends BaseRecord> Long countByType (Class<TYPE> type,String ... scope) throws Exception {
		this.setQuery(PredefinedQueries.COUNTQUERY.replace(Constants.TYPE, type.getSimpleName())
				+(scope.length>0?" AND vre='"+scope[0].toString()+"'":""));
		query();
		Long count = null ;
		try {
			count = toJSON().getJSONObject(0).getLong(Constants.COUNT);
		}catch (EmptyResultException e){
			throw e;
		}
		return count;
	}

	/**
	 * countByUser
	 * @param <TYPE> the TYPE of Record to retrieve
	 * @param type the class of results
	 * @param user the user
	 * @param scope the optional scope 
	 * @return the number of records for the given type ( and scope)
	 * @throws EmptyResultException Exception
	 * @throws Exception Exception
	 */
	public <TYPE extends BaseRecord> Long countByUser (Class<TYPE> type,String user,String ... scope) throws  Exception,EmptyResultException{
		this.setQuery(PredefinedQueries.COUNTQUERY.replace(Constants.TYPE, type.getSimpleName()) +" AND user='"+user+"'"
				+(scope.length>0?" AND vre='"+scope[0].toString()+"'":""));
		query();
		Long count = null ;
		try {
			count = toJSON().getJSONObject(0).getLong(Constants.COUNT);
		}catch (EmptyResultException e){
			throw e;
		}
		return count;
	}

	
	/**
	 * countByType
	 * @param type the class of results
	 * @param scope the optional scope 
	 * @return the number of records for the given type ( and scope)
	 * @throws Exception Exception
	 */
	public Long countByType (String type,String ... scope) throws Exception {
		this.setQuery(PredefinedQueries.COUNTQUERY.replace(Constants.TYPE, type)
				+(scope.length>0?" AND vre='"+scope[0].toString()+"'":""));
		query();
		Long count = null ;
		try {
			count = toJSON().getJSONObject(0).getLong(Constants.COUNT);
		}catch (EmptyResultException e){
			throw e;
		}
		return count;
	}

	/**
	 * countByUser
	 * @param type the class of results
	 * @param user the user
	 * @param scope the optional scope 
	 * @return the number of records for the given type ( and scope)
	 * @throws EmptyResultException Exception
	 * @throws Exception Exception
	 */
	public  Long countByUser (String type,String user,String ... scope) throws  Exception,EmptyResultException{
		this.setQuery(PredefinedQueries.COUNTQUERY.replace(Constants.TYPE, type) +" AND user='"+user+"'"
				+(scope.length>0?" AND vre='"+scope[0].toString()+"'":""));
		query();
		Long count = null ;
		try {
			count = toJSON().getJSONObject(0).getLong(Constants.COUNT);
		}catch (EmptyResultException e){
			throw e;
		}
		return count;
	}
	

	/**
	 * countByTypeAndUserWithScopeGrouping
	 * @param type the class of results
	 * @param groupBy groupBy
	 * @param user the user
	 * @param scope the optional scope
	 * @param dates the optional scope  
	 * @return the number of records for the given type ( and scope)
	 * @throws EmptyResultException Exception
	 * @throws Exception Exception
	 */
	public  String countByTypeAndUserWithGrouping (String type,String groupBy,String []dates, String ...user ) throws  Exception,EmptyResultException{
		String and = "";
		String and1 = "";
		
		if (type.compareTo("")!=0)
			and=" AND " ;
		if (user.length >0)
			and1="AND";
		this.setQuery(PredefinedQueries.COUNTWITHGROUPQUERY.replace(Constants.GROUPBYCOUNT, groupBy) + 
				" WHERE " + selectDateFiler(dates,"")+ and+
				((type.compareTo("")!=0)?" type='"+type+"'":"")+ and1 +
				(user.length>0?" user='"+user[0].toString()+"'":""));
		this.setGroupClause(groupBy);	
		query();
		return this.getResults();
	}
	
	
	/**
	 * getCollections
	 * @param id the message id
	 * @return HashMap<String, String> collection map (id, name)
	 * @throws EmptyResultException exception
	 * @throws Exception exception
	 */
	public HashMap<String, String>  getCollections(String id) throws EmptyResultException, Exception {
		HashMap<String, String> collections =  new HashMap<String, String>();
		setQuery(PredefinedQueries.COLLECTIONQUERY.replace(Constants.ID, id))	;
		query();
		JSONArray tmp = toJSON();
		for (int i = 0; i< tmp.length();i++)
			collections.put(tmp.getJSONObject(i).getString(Constants.IDENTIFIER),tmp.getJSONObject(i).getString(Constants.NAME));
		return collections;

	}

	/**
	 * getTerms
	 * @param id the message id
	 * @return HashMap<String, String> term map (name, value)
	 * @throws EmptyResultException exception
	 * @throws Exception exception
	 */
	public HashMap<String, String> getTerms(String id) throws EmptyResultException, Exception {
		HashMap<String, String> terms =  new HashMap<String, String>();
		setQuery(PredefinedQueries.TERMQUERY.replace(Constants.ID, id))	;
		query();
		JSONArray tmp = toJSON();
		for (int i = 0; i< tmp.length();i++)
			terms.put(tmp.getJSONObject(i).getString(Constants.NAME),tmp.getJSONObject(i).getString(Constants.VALUE));
		return terms;


	}
	
	/**
	 * getWebAppDetails
	 * @param id the message id
	 * @return HashMap<String, String> ghn map (id, name)
	 * @throws EmptyResultException exception
	 * @throws Exception exception
	 */
	public HashMap<String, String> getWebAppDetails(String id) throws EmptyResultException, Exception {
		HashMap<String, String> terms =  new HashMap<String, String>();
		setQuery(PredefinedQueries.WEBAPPDETAILQUERY.replace(Constants.ID, id))	;
		query();
		JSONArray tmp = toJSON();
		for (int i = 0; i< tmp.length();i++)
			terms.put(tmp.getJSONObject(i).getString(Constants.WEBAPPID),tmp.getJSONObject(i).getString(Constants.WEBAPPNAME));
		return terms;
	}
	
	/**
	 * getGHNDetails
	 * @param id the message id
	 * @return HashMap<String, String> ghn map (id, name)
	 * @throws EmptyResultException exception
	 * @throws Exception exception
	 */
	public HashMap<String, String> getGHNDetails(String id) throws EmptyResultException, Exception {
		HashMap<String, String> terms =  new HashMap<String, String>();
		setQuery(PredefinedQueries.GHNDETAILQUERY.replace(Constants.ID, id))	;
		query();
		JSONArray tmp = toJSON();
		for (int i = 0; i< tmp.length();i++)
			terms.put(tmp.getJSONObject(i).getString(Constants.GHNID),tmp.getJSONObject(i).getString(Constants.GHNNAME));
		return terms;
	}

	/**
	 * getAddressesGCUBEUsers
	 * @param id the message id
	 * @return results user, vre 
	 * @throws EmptyResultException exception
	 * @throws Exception exception
	 */
	public HashMap<String, String> getAddresseesGCUBEUsers(String id) throws EmptyResultException, Exception {
		HashMap<String, String> gcubeAddresses =  new HashMap<String, String>();
		setQuery(PredefinedQueries.ADDRESSESSQUERY.replace(Constants.ID, id))	;
		query();
		JSONArray tmp = toJSON();
		for (int i = 0; i< tmp.length();i++) {
			gcubeAddresses.put(tmp.getJSONObject(i).getString(Constants.NAME_UPPERCASE),
					tmp.getJSONObject(i).getString(Constants.VRE_UPPERCASE));
		}
		return gcubeAddresses;

	}
	
	/**
	 * getContent
	 * @param id the message id
	 * @return results contentid, contentName 
	 * @throws EmptyResultException exception
	 * @throws Exception exception
	 */
	public String[] getContent(String id) throws EmptyResultException, Exception {
		String [] results = new String [2];
		this.setQuery(PredefinedQueries.CONTENTNAME.replace(Constants.ID, id))	;
		query();
		JSONArray tmp = toJSON();
		results[0] = tmp.getJSONObject(0).getString(Constants.IDENTIFIER);
		results[1] = tmp.getJSONObject(0).getString(Constants.NAME);
		return results;

	}


	/**
	 * get the results as ArrayList<PortalAccountingMessage<TYPE>>
	 * @param <TYPE>
	 * @param type the type
	 * @return ArrayList<PortalAccountingMessage<TYPE>>
	 * @throws Exception exception
	 * @throws EmptyResultException exception
	 */
	public <TYPE extends BaseRecord> ArrayList<PortalAccountingMessage<TYPE>> getResultsAsMessage(Class<TYPE> type) throws Exception,EmptyResultException {
		ArrayList<PortalAccountingMessage<TYPE>> list = new ArrayList<PortalAccountingMessage<TYPE>>();
		
		JSONArray tmp = toJSON();
		for (int i = 0 ;i<tmp.length();i++){
			PortalAccountingMessage<TYPE> message = new PortalAccountingMessage<TYPE>();
			message.setUser(tmp.getJSONObject(i).getString(Constants.USER));
			message.setVre( tmp.getJSONObject(i).getString(Constants.VRE));
			message.setTime(tmp.getJSONObject(i).getString(Constants.TIME));
			message.setId(tmp.getJSONObject(i).getString(Constants.QUERYID));

			TYPE record = type.newInstance();
			record.setDate(dateFormat.parse(tmp.getJSONObject(i).getString(Constants.DATE)));
			populateRecord(tmp.getJSONObject(i),record);
			message.addRecord(record);
			list.add(message);
		}
		return list;
	}

	private <TYPE extends BaseRecord> String selectQuery(Class<TYPE> type){
		if (type.equals(LoginRecord.class))
			return PredefinedQueries.LOGINQUERY;
		else if (type.equals(BrowseRecord.class))
			return PredefinedQueries.BROWSEQUERY;
		else if (type.equals(AdvancedSearchRecord.class))
			return PredefinedQueries.ADVANCEDSEARCHQUERY;
		else if (type.equals(SimpleSearchRecord.class))
			return PredefinedQueries.SIMPLESEARCHQUERY;
		else if (type.equals(ContentRecord.class))
			return PredefinedQueries.CONTENTQUERY;
		else if (type.equals(GenericRecord.class))
			return PredefinedQueries.GENERICQUERY;
		else if (type.equals(GoogleSearchRecord.class))
			return PredefinedQueries.GOOGLESEARCHQUERY;
		else if (type.equals(QuickSearchRecord.class))
			return PredefinedQueries.QUICKSEARCHQUERY;
		else if (type.equals(HLRecord.class))
			return PredefinedQueries.HLQUERY;
		else if (type.equals(AISRecord.class))
			return PredefinedQueries.AISQUERY;
		else if (type.equals(TSRecord.class))
			return PredefinedQueries.TSQUERY;
		else if (type.equals(AnnotationRecord.class))
			return PredefinedQueries.ANNOTATIONQUERY;
		else if (type.equals(ReportRecord.class))
			return PredefinedQueries.REPORTQUERY;
		else if (type.equals(DocumentWorkflowRecord.class))
			return PredefinedQueries.WORKFLOWDOCUMENTQUERY;
		else if (type.equals(WebAppRecord.class))
			return PredefinedQueries.WEBAPPQUERY;
		else if (type.equals(WarRecord.class))
			return PredefinedQueries.WARQUERY;
		else if (type.equals(AquamapsRecord.class))
			return PredefinedQueries.AQUAMAPSQUERY;
		else if (type.equals(StatisticalManagerRecord.class))
			return PredefinedQueries.SMQUERY;
		else return "";

	}

	private String selectQuery(String type){
		logger.debug("QUERY TYPE: "+ type);
		if (type.equals("LoginRecord"))
			return PredefinedQueries.LOGINQUERY;
		else if (type.equals("BrowseRecord"))
			return PredefinedQueries.BROWSEQUERY;
		else if (type.equals("AdvancedSearchRecord"))
			return PredefinedQueries.ADVANCEDSEARCHQUERY;
		else if (type.equals("SimpleSearchRecord"))
			return PredefinedQueries.SIMPLESEARCHQUERY;
		else if (type.equals("ContentRecord"))
			return PredefinedQueries.CONTENTQUERY;
		else if (type.equals("GenericRecord"))
			return PredefinedQueries.GENERICQUERY;
		else if (type.equals("GoogleSearchRecord"))
			return PredefinedQueries.GOOGLESEARCHQUERY;
		else if (type.equals("QuickSearchRecord"))
			return PredefinedQueries.QUICKSEARCHQUERY;
		else if (type.equals("HLRecord"))
			return PredefinedQueries.HLQUERY;
		else if (type.equals("AISRecord"))
			return PredefinedQueries.AISQUERY;
		else if (type.equals("TSRecord"))
			return PredefinedQueries.TSQUERY;
		else if (type.equals("AnnotationRecord"))
			return PredefinedQueries.ANNOTATIONQUERY;
		else if (type.equals("WebAppRecord"))
			return PredefinedQueries.WEBAPPQUERY;
		else if (type.equals("WarRecord"))
			return PredefinedQueries.WARQUERY;
		else if (type.equals("ReportRecord"))
			return PredefinedQueries.REPORTQUERY;
		else if (type.equals("DocumentWorkflowRecord"))
			return PredefinedQueries.WORKFLOWDOCUMENTQUERY;
		else if (type.equals("AquamapsRecord"))
			return PredefinedQueries.AQUAMAPSQUERY;
		else if (type.equals("StatisticalManagerRecord"))
			return PredefinedQueries.SMQUERY;
		else return "";

	}

	private <TYPE extends BaseRecord> TYPE populateRecord (JSONObject obj,TYPE record ) throws JSONException{
		if (record instanceof LoginRecord)
			((LoginRecord)record).setMessage(obj.getString(Constants.MESSAGE));
		else if (record instanceof ContentRecord){
			((ContentRecord)record).setContentId(obj.getString(Constants.IDENTIFIER));
			((ContentRecord)record).setContentName(obj.getString(Constants.NAME));
		}
		else if (record instanceof BrowseRecord){
			((BrowseRecord)record).setBrowseBy(obj.getString(Constants.BROWSEBY));
			((BrowseRecord)record).setBrowseBy(obj.getString(Constants.ISDISTINCT));
		}
		else if (record instanceof AdvancedSearchRecord)
			((AdvancedSearchRecord)record).setOperator(OperatorType.valueOf(obj.getString(Constants.OPERATOR)));
		else if (record instanceof SimpleSearchRecord)
			((SimpleSearchRecord)record).setTerm(obj.getString(Constants.TERMVALUE));
		else if (record instanceof QuickSearchRecord)
			((QuickSearchRecord)record).setTerm(obj.getString(Constants.TERMVALUE));
		else if (record instanceof GoogleSearchRecord)
			((GoogleSearchRecord)record).setTerm(obj.getString(Constants.TERMVALUE));
		else if (record instanceof HLRecord) {
			((HLRecord)record).setHLsubType(HLSubType.valueOf(obj.getString(Constants.SUBTYPE)));
			((HLRecord)record).setID(obj.getString(Constants.IDENTIFIER_UPPERCASE));
			((HLRecord)record).setType(obj.getString(Constants.HL_TYPE));
			((HLRecord)record).setName(obj.getString(Constants.NAME_UPPERCASE));
		}	
		else if (record instanceof AISRecord) {
			((AISRecord)record).setAISsubType(AISSubType.valueOf(obj.getString(Constants.SUBTYPE)));
			((AISRecord)record).setID(obj.getString(Constants.IDENTIFIER_UPPERCASE));
			((AISRecord)record).setName((obj.getString(Constants.NAME_UPPERCASE)));
		}
		else if (record instanceof TSRecord) {
			((TSRecord)record).setTSsubType(TSSubType.valueOf(obj.getString(Constants.SUBTYPE)));
			((TSRecord)record).setTitle(obj.getString(Constants.TITLE));
			
		}
		else if (record instanceof WebAppRecord) {
			((WebAppRecord)record).setSubType(WebAppSubType.valueOf(obj.getString(Constants.SUBTYPE)));
		} 
		else if (record instanceof WarRecord) {
			((WarRecord)record).setSubType(WarSubType.valueOf(obj.getString(Constants.SUBTYPE)));
			((WarRecord)record).setWarId(obj.getString(Constants.WARID));
			((WarRecord)record).setWarName(obj.getString(Constants.WARNAME));
			((WarRecord)record).setAppName(obj.getString(Constants.WEBAPPNAME));
			((WarRecord)record).setAppVersion(obj.getString(Constants.WEBAPPVERSION));
			((WarRecord)record).setCategory(obj.getString(Constants.CATEGORY));
		} 
		else if (record instanceof AnnotationRecord) {
			((AnnotationRecord)record).setAnnotationSubType((AnnotationSubType.valueOf(obj.getString(Constants.ACTION))));
			((AnnotationRecord)record).setAnnotationName(obj.getString(Constants.NAME));
			((AnnotationRecord)record).setAnnotationType(obj.getString(Constants.SUBTYPE));		
		}
		else if (record instanceof ReportRecord) {
			((ReportRecord)record).setAuthor(obj.getString(Constants.AUTHOR));
			((ReportRecord)record).setMimetype(obj.getString(Constants.MIMETYPE));
			((ReportRecord)record).setName(obj.getString(Constants.NAME));
			((ReportRecord)record).setSubType(ReportSubType.valueOf(obj.getString(Constants.SUBTYPE)));
			((ReportRecord)record).setTemplateID(obj.getString(Constants.TEMPLATEID));
			((ReportRecord)record).setTemplateName(obj.getString(Constants.TEMPLATENAME));
			((ReportRecord)record).setType(obj.getString(Constants.TYPE));
		} else if (record instanceof DocumentWorkflowRecord) {
			((DocumentWorkflowRecord)record).setSubType(WorkflowSubType.valueOf(obj.getString(Constants.SUBTYPE)));
			((DocumentWorkflowRecord)record).setReportname(obj.getString(Constants.REPORTNAME));
			((DocumentWorkflowRecord)record).setStatus(obj.getString(Constants.STATUS));
			((DocumentWorkflowRecord)record).setStepsNumber(obj.getString(Constants.STEPNUMBER));
			((DocumentWorkflowRecord)record).setWorkflowid(obj.getString(Constants.WORKFLOWID));
			
		} else if (record instanceof AquamapsRecord) {
			((AquamapsRecord)record).setAquamapsSubtype(AquamapsSubType.valueOf(obj.getString(Constants.SUBTYPE)));
			((AquamapsRecord)record).setGis(Boolean.parseBoolean(obj.getString(Constants.GIS)));
			((AquamapsRecord)record).setHspecId(obj.getString(Constants.HSPECID));
			((AquamapsRecord)record).setObjectID(obj.getString(Constants.OBJECTID));
			((AquamapsRecord)record).setSpeciesCount(Long.parseLong(obj.getString(Constants.SPECIESCOUNT)));
			((AquamapsRecord)record).setTitle(obj.getString(Constants.TITLE));
			((AquamapsRecord)record).setType(obj.getString(Constants.AQUAMAPSTYPE));
			
		}
			return record;
	}

	
}
