package org.gcube.messaging.accounting.portalaccountingportlet.client;

import org.gcube.messaging.accounting.portalaccountingportlet.client.AccountingCostants.EntryType;

import com.gwtext.client.data.BooleanFieldDef;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.IntegerFieldDef;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.StringFieldDef;


/**
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public class RecordDefinition {

	public static RecordDef getRecordDef(EntryType type){
		if (type.compareTo(EntryType.LoginRecord)==0)
			return loginRecord;
		else if (type.compareTo(EntryType.BrowseRecord)==0)
			return browseRecord;
		else if (type.compareTo(EntryType.AdvancedSearchRecord)==0)
			return advancedSearchRecord;
		else if (type.compareTo(EntryType.SimpleSearchRecord)==0)
			return simpleSearchRecord;
		else if (type.compareTo(EntryType.ContentRecord)==0)
			return contentRecord;
		else if (type.compareTo(EntryType.GoogleSearchRecord)==0)
			return simpleSearchRecord;
		else if (type.compareTo(EntryType.QuickSearchRecord)==0)
			return simpleSearchRecord;
		else if (type.compareTo(EntryType.GenericRecord)==0)
			return loginRecord;
		else if (type.compareTo(EntryType.TSRecord)==0)
			return tsRecord;
		else if (type.compareTo(EntryType.AISRecord)==0)
			return aisRecord;
		else if (type.compareTo(EntryType.HLRecord)==0)
			return hlRecord;
		else if (type.compareTo(EntryType.AnnotationRecord)==0)
			return annotationRecord;
		else if (type.compareTo(EntryType.Empty)==0)
			return loginRecord;
		else if (type.compareTo(EntryType.DocumentWorkflowRecord)==0)
			return documentWorkflowRecord;
		else if (type.compareTo(EntryType.ReportRecord)==0)
			return reportRecord;
		else if (type.compareTo(EntryType.WebAppRecord)==0)
			return webappRecord;
		else if (type.compareTo(EntryType.WarRecord)==0)
			return warRecord;
		else if (type.compareTo(EntryType.AquamapsRecord)==0)
			return aquamapsRecord;
		
		else return null;
		
			
	}

	
	public static RecordDef loginRecord=new RecordDef(
		new FieldDef[]{
				new StringFieldDef("user"),
				new StringFieldDef("vre"),
				new StringFieldDef("date"),
				new StringFieldDef("time"),
				new StringFieldDef("type"),
				new StringFieldDef("Id")	
	});
	
	
	public static RecordDef webappRecord=new RecordDef(
			new FieldDef[]{
					new StringFieldDef("user"),
					new StringFieldDef("vre"),
					new StringFieldDef("date"),
					new StringFieldDef("time"),
					new StringFieldDef("type"),
					new StringFieldDef("Id"),
					new StringFieldDef("NAME"),
					new StringFieldDef("ACTION"),
					new StringFieldDef("SUBTYPE"),
		});
	
	public static RecordDef warRecord=new RecordDef(
			new FieldDef[]{
					new StringFieldDef("user"),
					new StringFieldDef("vre"),
					new StringFieldDef("date"),
					new StringFieldDef("time"),
					new StringFieldDef("type"),
					new StringFieldDef("Id"),
					new StringFieldDef("SUBTYPE"),
					new StringFieldDef("WARID"),
					new StringFieldDef("WARNAME"),
					new StringFieldDef("WEBAPPNAME"),
					new StringFieldDef("WEBAPPVERSION"),
					new StringFieldDef("CATEGORY"),
		});
	
	public static RecordDef reportRecord=new RecordDef(
		new FieldDef[]{
				new StringFieldDef("user"),
				new StringFieldDef("vre"),
				new StringFieldDef("date"),
				new StringFieldDef("time"),
				new StringFieldDef("type"),
				new StringFieldDef("Id"),
				new StringFieldDef("SUBTYPE"),
				new StringFieldDef("TEMPLATEID"),
				new StringFieldDef("TEMPLATENAME"),
				new StringFieldDef("AUTHOR"),
				new StringFieldDef("NAME"),
				new StringFieldDef("MIMETYPE"),
				new StringFieldDef("TYPE")
				
				
	});
	
	public static RecordDef documentWorkflowRecord=new RecordDef(
		new FieldDef[]{
				new StringFieldDef("user"),
				new StringFieldDef("vre"),
				new StringFieldDef("date"),
				new StringFieldDef("time"),
				new StringFieldDef("type"),
				new StringFieldDef("Id"),
				new StringFieldDef("SUBTYPE"),
				new StringFieldDef("WORKFLOWID"),
				new StringFieldDef("REPORTNAME"),
				new StringFieldDef("STATUS"),
				new StringFieldDef("STEPNUMBER")
				
	});
	public static RecordDef browseRecord=new RecordDef(
			new FieldDef[]{
					new StringFieldDef("user"),
					new StringFieldDef("vre"),
					new StringFieldDef("date"),
					new StringFieldDef("time"),
					new StringFieldDef("Id"),
					new StringFieldDef("type"),
					new StringFieldDef("browseBy"),
					new BooleanFieldDef("isDistinct")
		});
	
	public static RecordDef advancedSearchRecord=new RecordDef(
			new FieldDef[]{
					new StringFieldDef("user"),
					new StringFieldDef("vre"),
					new StringFieldDef("date"),
					new StringFieldDef("time"),
					new StringFieldDef("Id"),
					new StringFieldDef("type"),
					new StringFieldDef("operator")
		});
	
	public static RecordDef simpleSearchRecord=new RecordDef(
			new FieldDef[]{
					new StringFieldDef("user"),
					new StringFieldDef("vre"),
					new StringFieldDef("date"),
					new StringFieldDef("time"),
					new StringFieldDef("Id"),
					new StringFieldDef("type"),
					new StringFieldDef("termValue")
		});
	public static RecordDef contentRecord=new RecordDef(
			new FieldDef[]{
					new StringFieldDef("user"),
					new StringFieldDef("vre"),
					new StringFieldDef("date"),
					new StringFieldDef("time"),
					new StringFieldDef("Id"),
					new StringFieldDef("type"),
					new StringFieldDef("identifier"),
					new StringFieldDef("name")
		});
	
	public static RecordDef aisRecord=new RecordDef(
			new FieldDef[]{
					new StringFieldDef("user"),
					new StringFieldDef("vre"),
					new StringFieldDef("date"),
					new StringFieldDef("time"),
					new StringFieldDef("Id"),
					new StringFieldDef("type"),
					new StringFieldDef("IDENTIFIER"),
					new StringFieldDef("NAME"),
					new StringFieldDef("SUBTYPE")
		});
		
	public static RecordDef hlRecord=new RecordDef(
			new FieldDef[]{
					new StringFieldDef("user"),
					new StringFieldDef("vre"),
					new StringFieldDef("date"),
					new StringFieldDef("time"),
					new StringFieldDef("Id"),
					new StringFieldDef("type"),
					new StringFieldDef("IDENTIFIER"),
					new StringFieldDef("NAME"),
					new StringFieldDef("TYPE"),
					new StringFieldDef("SUBTYPE"),
		});
	
	public static RecordDef tsRecord=new RecordDef(
			new FieldDef[]{
					new StringFieldDef("user"),
					new StringFieldDef("vre"),
					new StringFieldDef("date"),
					new StringFieldDef("time"),
					new StringFieldDef("Id"),
					new StringFieldDef("type"),
					new StringFieldDef("SUBTYPE"),
					new StringFieldDef("TITLE"),
					
						});
	
	public static RecordDef annotationRecord=new RecordDef(
			new FieldDef[]{
					new StringFieldDef("user"),
					new StringFieldDef("vre"),
					new StringFieldDef("date"),
					new StringFieldDef("time"),
					new StringFieldDef("Id"),
					new StringFieldDef("type"),
					new StringFieldDef("SUBTYPE"),
					new StringFieldDef("ACTION"),
					new StringFieldDef("NAME"),
						});
	
	public static RecordDef aquamapsRecord=new RecordDef(
			new FieldDef[]{
					new StringFieldDef("user"),
					new StringFieldDef("vre"),
					new StringFieldDef("date"),
					new StringFieldDef("time"),
					new StringFieldDef("Id"),
					new StringFieldDef("type"),
					new StringFieldDef("SUBTYPE"),
					new StringFieldDef("TITLE"),
					new StringFieldDef("AQUAMAPSTYPE"),
					new StringFieldDef("SPECIESCOUNT"),
					new StringFieldDef("GIS"),
					new StringFieldDef("HSPECID"),
					new StringFieldDef("OBJECTID"),
						});

	public static RecordDef ghnRecord = new RecordDef(
			new FieldDef[]{
					new StringFieldDef("ghnid"),
					new StringFieldDef("ghnname")
			});
	
	public static RecordDef webappDetailRecord = new RecordDef(
			new FieldDef[]{
					new StringFieldDef("webappid"),
					new StringFieldDef("webappname"),
			});


	public static RecordDef collectionsRecord = new RecordDef(
		new FieldDef[]{
				new StringFieldDef("id"),
				new StringFieldDef("name")
		});
	
	public static RecordDef userRecord = new RecordDef(
			new FieldDef[]{
					new StringFieldDef("name"),
					new StringFieldDef("vre")
			});
	
	
	public static RecordDef termRecord = new RecordDef(
			new FieldDef[]{
					new StringFieldDef("name"),
					new StringFieldDef("value")
			});
	
	
	public static RecordDef getStatisticsRecord(String group){
		return new  RecordDef(
				new FieldDef[]{
						new IntegerFieldDef("CNT"),
						new StringFieldDef(group)

				});
	}

}
