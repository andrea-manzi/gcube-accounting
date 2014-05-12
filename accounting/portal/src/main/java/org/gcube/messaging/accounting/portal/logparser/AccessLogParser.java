package org.gcube.messaging.accounting.portal.logparser;

import java.io.BufferedReader;
import java.io.File;

import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.gcube.messaging.accounting.portal.logparser.entry.*;

/**
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public class AccessLogParser {

	public static String tokensSeparator= ",";
	
	private String fileName;
	
	public  AccessLogParser(){}
	
	public  AccessLogParser(String fileName){
		this.fileName = fileName;
	}
	
	private ArrayList<LogEntry> entryList = new ArrayList<LogEntry>(); 
	/**
	 * 
	 * @author Andrea Manzi(CERN)
	 *
	 */	

	public enum EntryType {
		Login_To_VRE("Login_To_VRE"),
		Generic_Entry("Generic_Entry"),
		Simple_Search("Simple_Search"),
		Advanced_Search("Advanced_Search"),
		Browse_Collection("Browse_Collection"),
		Retrieve_Content("Retrieve_Content"),
		Quick_Search("Quick_Search"),
		Google_Search("Google_Search"),
		HL_FOLDER_ITEM_CREATED("HL_FOLDER_ITEM_CREATED"),
		HL_FOLDER_ITEM_REMOVED("HL_FOLDER_ITEM_REMOVED"),
		HL_FOLDER_ITEM_IMPORTED("HL_FOLDER_ITEM_IMPORTED"),
		HL_ITEM_SENT("HL_ITEM_SENT"),
		HL_WORKSPACE_CREATED("HL_WORKSPACE_CREATED"),
		SCRIPT_CREATED("SCRIPT_CREATED"),
		SCRIPT_REMOVED("SCRIPT_REMOVED"),
		SCRIPT_LAUNCHED ("SCRIPT_LAUNCHED"),
		TS_CSV_IMPORTED("TS_CSV_IMPORTED"),
		TS_CURATION_STARTED("TS_CURATION_STARTED"),
		TS_CURATION_CLOSED("TS_CURATION_CLOSED"),
		TS_TIMESERIES_SAVED("TS_TIMESERIES_SAVED"),
		TS_TIMESERIES_PUBLISHED("TS_TIMESERIES_PUBLISHED"),
		Create_Annotation("Create_Annotation"),
		Edit_Annotation("Edit_Annotation"),
		Delete_Annotation("Delete_Annotation"),
		CREATED_WORKFLOWREPORT_OUTPUT("Created_WorkflowReport_Output"),
		DELETED_WORKFLOWREPORT_OUTPUT("Deleted_WorkflowReport_Output"),
		CREATE_REPORT("Create_Report"),
		GENERATE_REPORT_OUTPUT("Generate_Report_Output"),
		OPEN_REPORT("Open_Report"),
		OPEN_WORKFLOW_REPORT("Open_Workflow_Report"),
		SAVE_WORKFLOW_REPORT("SaveWorkflowLogEntry"),
		CREATE_TEMPLATE("Create_Template"),
		OPEN_TEMPLATE("Open_Template"),
		WEB_APPLICATION_DEPLOYED("WEB_APPLICATION_DEPLOYED"),
		WEB_APPLICATION_ACTIVATED("WEB_APPLICATION_ACTIVATED"),
		WEB_APPLICATION_DEACTIVATED("WEB_APPLICATION_DEACTIVATED"),
		WEB_APPLICATION_UNDEPLOYED("WEB_APPLICATION_UNDEPLOYED"),
		WAR_UPLOADED("WAR_UPLOADED"),
		WAR_UPDATED("WAR_UPDATED"),
		WAR_REMOVED("WAR_REMOVED"),
		AQUAMAPSOBJECTGENERATION("AquaMapsObjectGenerationLogEntry"),
		AQUAMAPSSAVEDITEM("SavedAquaMapsItemLogEntry");
		String type;
		EntryType(String type) {this.type = type;}
		public String toString() {return this.type;}
	};
	
	public void parse() throws IOException, ParseException {
		String line;
		BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)));
	      while ((line = reader.readLine()) != null) {
	    	  LogEntry entry=parseLine(line);
	    	  if (entry!=null)
	    		  entryList.add(entry);
	      }
	}
	
	private LogEntry parseLine(String line) throws ParseException{
		LogEntry entry = null;
		StringTokenizer tokenizer =  new StringTokenizer(line, tokensSeparator);
		  while (tokenizer.hasMoreTokens()) {
		      String token = tokenizer.nextToken();
		      //Search
		      if (token.contains(EntryType.Login_To_VRE.type)) 
		    	  entry = new LoginEntry(line);
		      else if (token.contains(EntryType.Generic_Entry.type))
		    	  entry = new GenericEntry(line);
		      else if (token.contains(EntryType.Simple_Search.type))
		    	  entry = new SimpleSearchEntry(line);
		      else if (token.contains(EntryType.Advanced_Search.type))
		    	  entry = new AdvancedSearchEntry(line);
		      else if (token.contains(EntryType.Browse_Collection.type))
		    	  entry = new BrowseCollectionEntry(line);
		      else if (token.contains(EntryType.Retrieve_Content.type))
		    	  entry = new RetrieveContentEntry(line);
		      else if (token.contains(EntryType.Quick_Search.type))
		    	  entry = new QuickSearchEntry(line);
		      else if (token.contains(EntryType.Google_Search.type))
		    	  entry = new GoogleSearchEntry(line);
		      //HL
		      else if (token.contains(EntryType.HL_FOLDER_ITEM_CREATED.type))
		    	  entry = new HLEntry(line,EntryType.HL_FOLDER_ITEM_CREATED);
		      else if (token.contains(EntryType.HL_FOLDER_ITEM_REMOVED.type))
			    	entry = new HLEntry(line,EntryType.HL_FOLDER_ITEM_REMOVED);
		      else if (token.contains(EntryType.HL_FOLDER_ITEM_IMPORTED.type))
			    	entry = new HLEntry(line,EntryType.HL_FOLDER_ITEM_IMPORTED);
		      else if (token.contains(EntryType.HL_ITEM_SENT.type))
			    	entry = new HLEntry(line,EntryType.HL_ITEM_SENT);
		      else if (token.contains(EntryType.HL_WORKSPACE_CREATED.type))
			    	entry = new HLEntry(line,EntryType.HL_WORKSPACE_CREATED);		    		     
		      //TS
		      else if (token.contains(EntryType.TS_CSV_IMPORTED.type))
			    	entry = new TSEntry(line,EntryType.TS_CSV_IMPORTED);
		      else if (token.contains(EntryType.TS_CURATION_STARTED.type))
			    	entry = new TSEntry(line,EntryType.TS_CURATION_STARTED);
		      else if (token.contains(EntryType.TS_CURATION_CLOSED.type))
			    	entry = new TSEntry(line,EntryType.TS_CURATION_CLOSED);
		      else if (token.contains(EntryType.TS_TIMESERIES_SAVED.type))
			    	entry = new TSEntry(line,EntryType.TS_TIMESERIES_SAVED);
		      else if (token.contains(EntryType.TS_TIMESERIES_PUBLISHED.type))
			    	entry = new TSEntry(line,EntryType.TS_TIMESERIES_PUBLISHED);
		      
		      //AIS
		      else if (token.contains(EntryType.SCRIPT_CREATED.type))
			    	entry = new AISEntry(line,EntryType.SCRIPT_CREATED);
		      else if (token.contains(EntryType.SCRIPT_REMOVED.type))
			    	entry = new AISEntry(line,EntryType.SCRIPT_REMOVED);
		      else if (token.contains(EntryType.SCRIPT_LAUNCHED.type))
			    	entry = new AISEntry(line,EntryType.SCRIPT_LAUNCHED);
		      
		      //Annotation
		      else if (token.contains(EntryType.Create_Annotation.type))
			    	entry = new AnnotationEntry(line,EntryType.Create_Annotation);
		      else if (token.contains(EntryType.Edit_Annotation.type))
			    	entry = new AnnotationEntry(line,EntryType.Edit_Annotation);
		      else if (token.contains(EntryType.Delete_Annotation.type))
			    	entry = new AnnotationEntry(line,EntryType.Delete_Annotation);
		      
		      //documentWorkflow
		      else if (token.contains(EntryType.CREATED_WORKFLOWREPORT_OUTPUT.type))
			    	entry = new WorkflowDocumentEntry(line,EntryType.CREATED_WORKFLOWREPORT_OUTPUT);
		      else if (token.contains(EntryType.DELETED_WORKFLOWREPORT_OUTPUT.type))
			    	entry = new WorkflowDocumentEntry(line,EntryType.DELETED_WORKFLOWREPORT_OUTPUT);
		    
		      //Report and template
		       
		      else if (token.contains(EntryType.CREATE_REPORT.type))
			    	entry = new ReportEntry(line,EntryType.CREATE_REPORT);
		      else if (token.contains(EntryType.GENERATE_REPORT_OUTPUT.type))
			    	entry = new ReportEntry(line,EntryType.GENERATE_REPORT_OUTPUT);
		      else if (token.contains(EntryType.OPEN_REPORT.type))
			    	entry = new ReportEntry(line,EntryType.OPEN_REPORT); 
		      else if (token.contains(EntryType.OPEN_WORKFLOW_REPORT.type))
			    	entry = new ReportEntry(line,EntryType.OPEN_WORKFLOW_REPORT);
		      else if (token.contains(EntryType.SAVE_WORKFLOW_REPORT.type))
			    	entry = new ReportEntry(line,EntryType.SAVE_WORKFLOW_REPORT);
		      else if (token.contains(EntryType.CREATE_TEMPLATE.type))
			    	entry = new ReportEntry(line,EntryType.CREATE_TEMPLATE);
		      else if (token.contains(EntryType.OPEN_TEMPLATE.type))
			    	entry = new ReportEntry(line,EntryType.OPEN_TEMPLATE);
		      //webApp Management
		      else if (token.contains(EntryType.WEB_APPLICATION_DEPLOYED.type))
			    	entry = new WebAppEntry(line,EntryType.WEB_APPLICATION_DEPLOYED);
		      else if (token.contains(EntryType.WEB_APPLICATION_ACTIVATED.type))
			    	entry = new WebAppEntry(line,EntryType.WEB_APPLICATION_ACTIVATED);
		      else if (token.contains(EntryType.WEB_APPLICATION_DEACTIVATED.type))
			    	entry = new WebAppEntry(line,EntryType.WEB_APPLICATION_DEACTIVATED);
		      else if (token.contains(EntryType.WEB_APPLICATION_UNDEPLOYED.type))
			    	entry = new WebAppEntry(line,EntryType.WEB_APPLICATION_UNDEPLOYED);
		      
		      //War Management
		      else if (token.contains(EntryType.WAR_REMOVED.type))
			    	entry = new WarEntry(line,EntryType.WAR_REMOVED);
		      else if (token.contains(EntryType.WAR_UPDATED.type))
			    	entry = new WarEntry(line,EntryType.WAR_UPDATED);
		      else if (token.contains(EntryType.WAR_UPLOADED.type))
			    	entry = new WarEntry(line,EntryType.WAR_UPLOADED);
		      
		      //Aquamaps
		      else if (token.contains(EntryType.AQUAMAPSOBJECTGENERATION.type))
			    	entry = new AquamapsEntry(line,EntryType.AQUAMAPSOBJECTGENERATION);
		      else if (token.contains(EntryType.AQUAMAPSSAVEDITEM.type))
			    	entry = new AquamapsEntry(line,EntryType.AQUAMAPSSAVEDITEM);
		      
		  }
		  return entry;
	}
	
	public static void main (String[] args) {
		AccessLogParser parser = new AccessLogParser(args[0]);
		ArrayList<LogEntry> entryList = new ArrayList<LogEntry>(); 
		try {
			parser.parse();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		entryList = parser.getEntryList();
		for (LogEntry entry :entryList){
			if (entry  instanceof AdvancedSearchEntry)
			{
			
				Message message =entry.getMessage(); 
					
				for (String name :entry.getMessage().getCollections().values())
					System.out.println(name);
				for (String name :entry.getMessage().getTerms().values())
					System.out.println(name);
			}
					
		}
			
	}

	/**
	 *  get the entryList
	 * @return the entryList
	 */
	public ArrayList<LogEntry> getEntryList() {
		return entryList;
	}

	/**
	 * set the entrylist
	 * @param entryList
	 */
	public void setEntryList(ArrayList<LogEntry> entryList) {
		this.entryList = entryList;
	}
}
