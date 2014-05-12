package org.gcube.messaging.accounting.portal.logparser.entry;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import org.gcube.messaging.accounting.portal.logparser.AccessLogParser.EntryType;
import org.gcube.messaging.common.messages.records.HLRecord;
import org.gcube.messaging.common.messages.records.HLRecord.GCUBEUser;
import org.gcube.messaging.common.messages.records.WebAppRecord;
import org.gcube.messaging.common.messages.records.WebAppRecord.GHN;
import org.gcube.messaging.common.messages.records.WebAppRecord.WebApplication;

/**
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public class Message {
	
	
	protected static final String messageTokensSeparator= "|";
	protected static final String hlAddresssesSeparator= ";";
	protected static final String hlAddresssesSeparator2= ":";
	
	private EntryType type;
	private String message;
	
	private String term;
	private String operator;
	private String browseBy;
	

	private boolean isDistinct;
	private HashMap<String,String> collections  = null;
	private HashMap<String,String> terms = null;
	
	private String []content = new String [2];
	//ID, NAME, TYPE
	private String []hlentry = new String [3];  
	//Type, Name, ObjectID,ObjectName
	//Type, Name, ObjectID
	//Name, ObjectID
	private String []annotationEntry = new String [4];
	//workflowdocument
	private String []workflowDocumentsEntry = new String [3];
	//report 
	private String []reportEntry = new String [3];
	//webapp 
	private String []webAppEntry = new String [5];
	//webapp 
	private String []warEntry = new String [5];
	//webapplications
	private ArrayList<GHN> listGHN = new ArrayList<GHN>();
	//webapplications
	private ArrayList<WebApplication> listApplication = new ArrayList<WebApplication>();
	
	//aquamaps
	private String []aquamapsEntry = new String [5];


	private ArrayList<GCUBEUser> addresseeUsers = null;
	
	//ID, NAME
	private String []aisentry = new String [2];
	
	//ID
	private String tstitle;

	protected StringTokenizer tokenizer = null;
	
	
	/**
	 * Message constructor
	 * @param token token
	 * @param type type
	 */
	public Message (String token, EntryType type){
		this.message = token;
		this.type = type;
	}


	protected enum OperatorType {
		AND("AND"),
		OR("OR"),
		None("None");
		String operator;
		OperatorType(String operator) {this.operator= operator;}
		public String toString() {return this.operator;} 
	}

	protected enum RetrieveContentTokens {
		contentID("contentID"),
		contentName("contentName");
		String tokens;
		RetrieveContentTokens(String tokens) {this.tokens = tokens;}
		public String toString() {return this.tokens;}
	}
	
	protected enum HLTokens {
		ID("ID"),
		TYPE("TYPE"),
		ADDRESSEES("ADDRESSEES"),
		NAME("NAME");
		String tokens;
		HLTokens(String tokens) {this.tokens = tokens;}
		public String toString() {return this.tokens;}
	}
	
	protected enum AISTokens {
		ID("ID"),	
		NAME("NAME");
		String tokens;
		AISTokens(String tokens) {this.tokens = tokens;}
		public String toString() {return this.tokens;}
	}
	
	protected enum TSTokens {
		TITLE("TITLE");
		String tokens;
		TSTokens(String tokens) {this.tokens = tokens;}
		public String toString() {return this.tokens;}
	}
	
	protected enum AnnotationTokens {
		annotationName("annotationName"),
		annotationType("annotationType"),
		objectID("objectID"),
		objectName("objectName");
		String tokens;
		AnnotationTokens(String tokens) {this.tokens = tokens;}
		public String toString() {return this.tokens;}
	}
	
	
	protected enum WorkflowDocumentTokens {
		WorkflowDocuementName("WorkflowDocuementName"),
		WORKFLOWID("WORKFLOWID"),
		STEPS_NO("STEPS_NO"),
		STATUS("STATUS");
		String tokens;
		WorkflowDocumentTokens(String tokens) {this.tokens = tokens;}
		public String toString() {return this.tokens;}
	}
	
	protected enum ReportTokens {
		Name("Name"),
		ID("ID"),
		AUTHOR("AUTHOR"),
		MIMETYPE("MIMETYPE"),
		TYPE("TYPE");
		String tokens;
		ReportTokens(String tokens) {this.tokens = tokens;}
		public String toString() {return this.tokens;}
	}
	
	protected enum WebAppTokens {
		APPLICATIONS("APPLICATIONS"),
		ID("ID"),
		NAME("NAME"),
		VERSION("VERSION"),
		GHN_ID("GHN_ID"),
		GHN_NAME("GHN_NAME");

		String tokens;
		WebAppTokens(String tokens) {this.tokens = tokens;}
		public String toString() {return this.tokens;}
	}
	
	protected enum WarTokens {
		CATEGORY_NAME("CATEGORY_NAME"),
		ID("ID"),
		NAME("NAME"),
		APPLICATION_NAME("APPLICATION_NAME"),
		APPLICATION_VERSION("APPLICATION_VERSION");
		String tokens;
		WarTokens(String tokens) {this.tokens = tokens;}
		public String toString() {return this.tokens;}
	}
	
	
	
	protected enum SearchAndBrowseTokens {
		collectionName("collectionName"),
		collectionID("collectionID"),
		term("term"),
		value("value"),
		DISTINCT("DISTINCT"),
		Browse_by("Browse by"),
		operator("operator");
		String tokens;
		SearchAndBrowseTokens(String tokens) {this.tokens = tokens;}
		public String toString() {return this.tokens;}
	}
	
	
	protected enum AquamapsTokens {
		TITLE("TITLE"),
		TYPE("TYPE"),
		ID("ID"),
		HSPEC("HSPEC"),
		SPECIES_COUNT("SPECIES_COUNT"),
		GIS("GIS");
		String tokens;
		AquamapsTokens(String tokens) {this.tokens = tokens;}
		public String toString() {return this.tokens;}
	}
	
	

	/**
	 * parse the message line of log
	 * @throws ParseException ParseException
	 */
	public void parse() throws ParseException {
		switch (type){
			case Simple_Search:
				collections= new HashMap<String,String>();

				StringTokenizer tokenizer = new StringTokenizer(message,messageTokensSeparator);
				while(tokenizer.hasMoreElements()){
					String tok = tokenizer.nextToken();
					if (tok.contains(SearchAndBrowseTokens.collectionName.tokens))
					{
						String [] col= getDetails(tok);
						collections.put(col[0],col[1]);
					}else if (tok.contains(SearchAndBrowseTokens.term.tokens))
						term=  getValue(tok);
				}
				break;
			case Advanced_Search:
			case Browse_Collection:
				collections= new HashMap<String,String>();
				terms= new HashMap<String,String>();
				
				tokenizer = new StringTokenizer(message,messageTokensSeparator);
				while(tokenizer.hasMoreElements()){
					String tok = tokenizer.nextToken();
					if (tok.contains(SearchAndBrowseTokens.collectionName.tokens))
					{
						String [] col= getDetails(tok);
						collections.put(col[0],col[1]);
					}else if (tok.contains(SearchAndBrowseTokens.term.tokens))
					{
						String [] term= getDetails(tok);
						terms.put(term[1], term[0]);
					}
					else if (tok.contains(SearchAndBrowseTokens.Browse_by.tokens))
						browseBy = getValue(tok);
					 else if (tok.contains(SearchAndBrowseTokens.DISTINCT.tokens))
						isDistinct = Boolean.getBoolean(getValue(tok));
					 else if (tok.contains(SearchAndBrowseTokens.operator.tokens))
						operator= getValue(tok);
				}
				break;
			case Retrieve_Content:
				tokenizer = new StringTokenizer(message,messageTokensSeparator);
				while(tokenizer.hasMoreElements()){
					String tok = tokenizer.nextToken();
					if (tok.contains(RetrieveContentTokens.contentID.tokens))
						content[0] = getValue(tok);
					else if (tok.contains(RetrieveContentTokens.contentName.tokens))
						content[1] = getValue(tok);
					}
			break;
			case Quick_Search:
			case Google_Search:
				tokenizer = new StringTokenizer(message,messageTokensSeparator);
				while(tokenizer.hasMoreElements()){
					String tok = tokenizer.nextToken();
					 if (tok.contains(SearchAndBrowseTokens.term.tokens))
							term=  getValue(tok);
				}
			break;		
			case HL_FOLDER_ITEM_CREATED:
			case HL_FOLDER_ITEM_REMOVED:
			case HL_FOLDER_ITEM_IMPORTED:
				tokenizer = new StringTokenizer(message,messageTokensSeparator);
				while(tokenizer.hasMoreElements()){
					String tok = tokenizer.nextToken();
					if (tok.contains(HLTokens.ID.tokens))
						hlentry[0] = getValue(tok);
					else if (tok.contains(HLTokens.NAME.tokens))
						hlentry[1] = getValue(tok);
					else if (tok.contains(HLTokens.TYPE.tokens))
						hlentry[2] = getValue(tok);
					}
			break;	
			case HL_ITEM_SENT:
				tokenizer = new StringTokenizer(message,messageTokensSeparator);
				while(tokenizer.hasMoreElements()){
					String tok = tokenizer.nextToken();
					if (tok.contains(HLTokens.ID.tokens))
						hlentry[0] = getValue(tok);
					else if (tok.contains(HLTokens.NAME.tokens))
						hlentry[1] = getValue(tok);
					else if (tok.contains(HLTokens.TYPE.tokens))
						hlentry[2] = getValue(tok);
					else if (tok.contains(HLTokens.ADDRESSEES.tokens))
						addresseeUsers = getAddresseesUser(tok);
					}
			break;
			case SCRIPT_CREATED:
			case SCRIPT_LAUNCHED:
			case SCRIPT_REMOVED:
				tokenizer = new StringTokenizer(message,messageTokensSeparator);
				while(tokenizer.hasMoreElements()){
					String tok = tokenizer.nextToken();
					if (tok.contains(AISTokens.ID.tokens))
						aisentry[0] = getValue(tok);
					else if (tok.contains(AISTokens.NAME.tokens))
						aisentry[1] = getValue(tok);	
				}
			break;
			case TS_CSV_IMPORTED:
			case TS_CURATION_CLOSED:
			case TS_CURATION_STARTED:
			case TS_TIMESERIES_PUBLISHED:
			case TS_TIMESERIES_SAVED:
				tokenizer = new StringTokenizer(message,messageTokensSeparator);
				while(tokenizer.hasMoreElements()){
					String tok = tokenizer.nextToken();
					if (tok.contains(TSTokens.TITLE.tokens))
						tstitle = getValue(tok);
					}
			break;
			case Create_Annotation:
			case Edit_Annotation:
			case Delete_Annotation:
			{
				tokenizer = new StringTokenizer(message,messageTokensSeparator);
				while(tokenizer.hasMoreElements()){
					String tok = tokenizer.nextToken();
					//Type, Name, ObjectID,ObjectName
					if (tok.contains(AnnotationTokens.annotationType.tokens))
						annotationEntry[0] = getValue(tok);
					else if (tok.contains(AnnotationTokens.annotationName.tokens))
						annotationEntry[1] = getValue(tok);
					else if (tok.contains(AnnotationTokens.objectID.tokens))
						annotationEntry[2] = getValue(tok);
					else if (tok.contains(AnnotationTokens.objectName.tokens))
						annotationEntry[3] = getValue(tok);
				}
			}
			break;
			case CREATED_WORKFLOWREPORT_OUTPUT:
			case DELETED_WORKFLOWREPORT_OUTPUT:
			{
				tokenizer = new StringTokenizer(message,messageTokensSeparator);
				while(tokenizer.hasMoreElements()){
					String tok = tokenizer.nextToken();
			
					if (tok.contains(WorkflowDocumentTokens.WORKFLOWID.tokens))
						workflowDocumentsEntry[0] = getValue(tok);
					else if (tok.contains(WorkflowDocumentTokens.WorkflowDocuementName.tokens))
						workflowDocumentsEntry[1] = getValue(tok);
					else if (tok.contains(WorkflowDocumentTokens.STATUS.tokens))
						workflowDocumentsEntry[2] = getValue(tok);
					else if (tok.contains(WorkflowDocumentTokens.STEPS_NO.tokens))
						workflowDocumentsEntry[2] = getValue(tok);
				}
			}
			break;
			case CREATE_REPORT:
			case OPEN_REPORT:
			{
				tokenizer = new StringTokenizer(message,messageTokensSeparator);
				while(tokenizer.hasMoreElements()){
					String tok = tokenizer.nextToken();
			
					if (tok.contains(ReportTokens.ID.tokens))
						reportEntry[0] = getValue(tok);
					else if (tok.contains(ReportTokens.Name.tokens))
						reportEntry[1] = getValue(tok);
					
				}
			}
			break;
			case OPEN_WORKFLOW_REPORT:
			case SAVE_WORKFLOW_REPORT:
			case CREATE_TEMPLATE:
			case OPEN_TEMPLATE:
			{
				tokenizer = new StringTokenizer(message,messageTokensSeparator);
				while(tokenizer.hasMoreElements()){
					String tok = tokenizer.nextToken();
			
					if (tok.contains(ReportTokens.ID.tokens))
						reportEntry[0] = getValue(tok);
					else if (tok.contains(ReportTokens.Name.tokens))
						reportEntry[1] = getValue(tok);
					else if (tok.contains(ReportTokens.AUTHOR.tokens))
						reportEntry[2] = getValue(tok);
					
				}
			}
			break;		
			case GENERATE_REPORT_OUTPUT:
			{
				tokenizer = new StringTokenizer(message,messageTokensSeparator);
				while(tokenizer.hasMoreElements()){
					String tok = tokenizer.nextToken();
			
					if (tok.contains(ReportTokens.Name.tokens))
						reportEntry[0] = getValue(tok);
					else if (tok.contains(ReportTokens.MIMETYPE.tokens))
						reportEntry[1] = getValue(tok);
					else if (tok.contains(ReportTokens.TYPE.tokens))
						reportEntry[2] = getValue(tok);
					
				}
			}
			break;
			case WEB_APPLICATION_ACTIVATED:
			case WEB_APPLICATION_DEACTIVATED: 
			{
				tokenizer = new StringTokenizer(message,messageTokensSeparator);
				while(tokenizer.hasMoreElements()){
					String tok = tokenizer.nextToken();
			
					if (tok.contains(WebAppTokens.ID.tokens))
						webAppEntry[0] = getValue(tok);
					else if (tok.contains(WebAppTokens.NAME.tokens))
						webAppEntry[1] = getValue(tok);
					else if (tok.contains(WebAppTokens.VERSION.tokens))
						webAppEntry[2] = getValue(tok);
					else if (tok.contains(WebAppTokens.GHN_ID.tokens))
						webAppEntry[3] = getValue(tok);
					else if (tok.contains(WebAppTokens.GHN_NAME.tokens))
						webAppEntry[4] = getValue(tok);
					
				}
			}
			break;	
			case WEB_APPLICATION_DEPLOYED:
			{
				tokenizer = new StringTokenizer(message,messageTokensSeparator);
				while(tokenizer.hasMoreElements()){
					String tok = tokenizer.nextToken();
					if (tok.contains(WebAppTokens.GHN_ID.tokens))
						webAppEntry[0] = getValue(tok);
					else if (tok.contains(WebAppTokens.GHN_NAME.tokens))
						webAppEntry[1] = getValue(tok);
					else if (tok.contains(WebAppTokens.APPLICATIONS.tokens))
						listApplication = getApplications(tok);
				}
			}
			break;
			case WEB_APPLICATION_UNDEPLOYED:
			{
				tokenizer = new StringTokenizer(message,messageTokensSeparator);
				while(tokenizer.hasMoreElements()){
					String tok = tokenizer.nextToken();
					if (tok.contains(WebAppTokens.APPLICATIONS.tokens)){
						listApplication = getApplications(tok);
						listGHN = getGHN(tok);
					}
				}
			}
			break;
			case WAR_REMOVED:
			case WAR_UPDATED:
			case WAR_UPLOADED:
			{
				tokenizer = new StringTokenizer(message,messageTokensSeparator);
				while(tokenizer.hasMoreElements()){
					String tok = tokenizer.nextToken();
					if (tok.contains(WarTokens.ID.tokens))
						warEntry[0] = getValue(tok);
					else if (tok.contains(WarTokens.APPLICATION_NAME.tokens))
						warEntry[1] = getValue(tok);
					else if (tok.contains(WarTokens.APPLICATION_VERSION.tokens))
						warEntry[2] = getValue(tok);
					else if (tok.contains(WarTokens.CATEGORY_NAME.tokens))
						warEntry[3] = getValue(tok);
					else if (tok.contains(WarTokens.NAME.tokens))
						warEntry[4] = getValue(tok);
				}
			}
			break;
			case AQUAMAPSOBJECTGENERATION:
			case AQUAMAPSSAVEDITEM:
			{
				tokenizer = new StringTokenizer(message,messageTokensSeparator);
				while(tokenizer.hasMoreElements()){
					String tok = tokenizer.nextToken();
					
					if (tok.contains(AquamapsTokens.TITLE.tokens))
						aquamapsEntry[0] = getValue(tok);
					else if (tok.contains(AquamapsTokens.TYPE.tokens))
						aquamapsEntry[1] = getValue(tok);
					else if (tok.contains(AquamapsTokens.GIS.tokens))
						aquamapsEntry[2] = getValue(tok);
					else if (tok.contains(AquamapsTokens.SPECIES_COUNT.tokens))
						aquamapsEntry[3] = getValue(tok);
					else if (tok.contains(AquamapsTokens.HSPEC.tokens))
						aquamapsEntry[4] = getValue(tok);
					else if (tok.contains(AquamapsTokens.ID.tokens))
						aquamapsEntry[4] = getValue(tok);
					
				}
			}
			break;
				
			case Generic_Entry:
			case Login_To_VRE:
			case HL_WORKSPACE_CREATED:
			default:
				break;
		}
	}
	
	public String[] getAquamapsEntry() {
		return aquamapsEntry;
	}

	public void setAquamapsEntry(String[] aquamapsEntry) {
		this.aquamapsEntry = aquamapsEntry;
	}

	public String[] getReportEntry() {
		return reportEntry;
	}

	public void setReportEntry(String[] reportEntry) {
		this.reportEntry = reportEntry;
	}

	private ArrayList<GCUBEUser> getAddresseesUser(String line){
		ArrayList<GCUBEUser> users = new ArrayList<GCUBEUser>();
		String userString = line.substring(line.indexOf(LogEntry.parameterValueSeparator)+1).trim();
		StringTokenizer tokenizer = new StringTokenizer(userString,hlAddresssesSeparator);
		while(tokenizer.hasMoreElements()){
			String tok = tokenizer.nextToken();
			
			GCUBEUser user = new  HLRecord().new GCUBEUser();
			user.setUser(tok.substring(0,tok.indexOf(hlAddresssesSeparator2)));
			user.setVre(tok.substring(tok.indexOf(hlAddresssesSeparator2)+1));
			users.add(user);
		}
		return users;
		
	} 
	
	private ArrayList<GHN> getGHN(String line){
		ArrayList<GHN> ghns = new ArrayList<GHN>();
		String ghnString = line.substring(line.indexOf(LogEntry.parameterValueSeparator)+1).trim();
		StringTokenizer tokenizer = new StringTokenizer(ghnString,hlAddresssesSeparator);
		while(tokenizer.hasMoreElements()){
			String tok = tokenizer.nextToken();
			
			GHN ghn = new  WebAppRecord().new GHN();
			ghn.setGHN_ID(tok.substring(0,tok.indexOf(hlAddresssesSeparator2)));
			ghn.setGHN_NAME(tok.substring(tok.indexOf(hlAddresssesSeparator2)+1));
			ghns.add(ghn);
		}
		return ghns;
		
	} 
	
	private ArrayList<WebApplication> getApplications(String line){
		ArrayList<WebApplication> webApps  = new ArrayList<WebApplication>();
		String ghnString = line.substring(line.indexOf(LogEntry.parameterValueSeparator)+1).trim();
		StringTokenizer tokenizer = new StringTokenizer(ghnString,hlAddresssesSeparator);
		while(tokenizer.hasMoreElements()){
			String tok = tokenizer.nextToken();
			
			WebApplication webApp = new  WebAppRecord().new WebApplication();
			if (tok.contains(WebAppTokens.ID.tokens))
				webApp.setWEB_APPLICATION_ID(getValue(tok));
			else if (tok.contains(WebAppTokens.NAME.tokens))
				webApp.setWEB_APPLICATION_NAME(getValue(tok));
			else if (tok.contains(WebAppTokens.VERSION.tokens))
				webApp.setWEB_APPLICATION_NAME(getValue(tok));
			
			webApps.add(webApp);
		}
		return webApps;
		
	} 
		
	private String [] getDetails(String token){
		String [] col = new String [2];
	
		String nameString = token.substring(0,
				token.indexOf(OperatorType.AND.operator));
	
		String idString = token.substring(token.indexOf(OperatorType.AND.operator)+OperatorType.AND.operator.length()+1);
	
		col[0] = idString.substring(idString.indexOf(LogEntry.parameterValueSeparator)+1).trim();
	
		col[1] = nameString.substring(nameString.indexOf(LogEntry.parameterValueSeparator)+1).trim();
	
		return col;
	}

	
	private String getValue(String token){
		return token.substring(token.indexOf(LogEntry.parameterValueSeparator)+1).trim();

	}
	
	public String[] getWorkflowDocumentsEntry() {
		return workflowDocumentsEntry;
	}

	public void setWorkflowDocumentsEntry(String[] workflowDocumentsEntry) {
		this.workflowDocumentsEntry = workflowDocumentsEntry;
	}
	
	/**
	 * get the message
	 */
	public String toString (){
		return this.message;
	}

	/**
	 * get teh collections map
	 * @return the collections map
	 */
	public HashMap<String, String> getCollections() {
		return collections;
	}

	/**
	 * set the collections map
	 * @param collections the collections map
	 */
	public void setCollections(HashMap<String, String> collections) {
		this.collections = collections;
	}

	/**
	 * get the message 
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	
	/**
	 * set teh message
	 * @param message the message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * get the term
	 * @return the term
	 */
	public String getTerm() {
		return term;
	}

	/**
	 * set the term
	 * @param term the term
	 */
	public void setTerm(String term) {
		this.term = term;
	}

	/**
	 * get the terms map
	 * @return the terms map
	 */
	public HashMap<String, String> getTerms() {
		return terms;
	}

	/**
	 * set the terms map
	 * @param terms the terms map
	 */
	public void setTerms(HashMap<String, String> terms) {
		this.terms = terms;
	}

	/**
	 * get the type
	 * @return the type
	 */
	public EntryType getType() {
		return type;
	}

	/**
	 * set the type
	 * @param type
	 */
	public void setType(EntryType type) {
		this.type = type;
	}

	/**
	 * get browse by
	 * @return browse by
	 */
	public String getBrowseBy() {
		return browseBy;
	}

	/**
	 * set the browse by
	 * @param browseBy
	 */
	public void setBrowseBy(String browseBy) {
		this.browseBy = browseBy;
	}

	/**
	 * set the isDistinct
	 * @return
	 */
	public boolean isDistinct() {
		return isDistinct;
	}

	/**
	 * set the isDistinct
	 * @param isDistinct the parameter
	 */
	public void setDistinct(boolean isDistinct) {
		this.isDistinct = isDistinct;
	}

	/**
	 * get the operator 
	 * @return the operator
	 */
	public String getOperator() {
		return operator;
	}

	/**
	 * set the operator
	 * @param operator the operator
	 */
	public void setOperator(String operator) {
		this.operator = operator;
	}
	
	/**
	 * get the content info
	 * @return the content info
	 */
	public String[] getContent() {
		return content;
	}

	/**
	 * set the content info
	 * @param content the conte
	 */
	public void setContent(String[] content) {
		this.content = content;
	}
	
	/**
	 * 
	 * @return hlentry
	 */
	public String[] getHlentry() {
		return hlentry;
	}

	/**
	 * 
	 * @param hlentry
	 */
	public void setHlentry(String[] hlentry) {
		this.hlentry = hlentry;
	}

	/**
	 * 
	 * @return
	 */
	public ArrayList<GCUBEUser> getAddresseeUsers() {
		return addresseeUsers;
	}

	/**
	 * 
	 * @param addresseeUsers
	 */
	public void setAddresseeUsers(ArrayList<GCUBEUser> addresseeUsers) {
		this.addresseeUsers = addresseeUsers;
	}

	/**
	 * 
	 * @return
	 */
	public static String getMessagetokensseparator() {
		return messageTokensSeparator;
	}

	public String[] getAisentry() {
		return aisentry;
	}

	public void setAisentry(String[] aisentry) {
		this.aisentry = aisentry;
	}

	public String getTstitle() {
		return tstitle;
	}

	public void setTstitle(String tstitle) {
		this.tstitle = tstitle;
	}
	
	public String[] getAnnotationEntry() {
		return annotationEntry;
	}

	public void setAnnotationEntry(String[] annotationEntry) {
		this.annotationEntry = annotationEntry;
	}
	
	public String[] getWebAppEntry() {
		return webAppEntry;
	}

	public void setWebAppEntry(String[] webAppEntry) {
		this.webAppEntry = webAppEntry;
	}
	public ArrayList<GHN> getListGHN() {
		return listGHN;
	}

	public void setListGHN(ArrayList<GHN> listGHN) {
		this.listGHN = listGHN;
	}

	public ArrayList<WebApplication> getListApplication() {
		return listApplication;
	}

	public void setListApplication(ArrayList<WebApplication> listApplication) {
		this.listApplication = listApplication;
	}
	
	public String[] getWarEntry() {
		return warEntry;
	}

	public void setWarEntry(String[] warEntry) {
		this.warEntry = warEntry;
	}


	
}

