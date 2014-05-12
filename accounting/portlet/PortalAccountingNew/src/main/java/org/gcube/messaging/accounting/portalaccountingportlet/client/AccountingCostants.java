package org.gcube.messaging.accounting.portalaccountingportlet.client;

import com.google.gwt.core.client.GWT;



public class AccountingCostants {

	public static final String 	servletURL = GWT.getModuleBaseURL() + "QueryServlet";
	public static final String 	servletStatisticURL = GWT.getModuleBaseURL() + "StatisticsServlet";
	public static final String 	exportServletURL = GWT.getModuleBaseURL() + "ExportServlet";
	

	public static final String AnnotationPanelId="AnnotationPanelId";
	public static final String CollectionPanelID="CollectionPanelId";
	public static final String TermsPanelId="TermsPanelId";
	public static final String BrowsePanelId="BrowsePanelId";
	public static final String ContentPanelId="ContentPanelId";
	public static final String TSPanelId="TSPanelId";
	public static final String AISPanelId="AISPanelId";
	public static final String HLPanelId="HLPanelId";
	public static final String GCUBEUSersAddresseesPanelId="GCUBEUSersAddresseesPanelId";
	public static final String GHNDetailPanelId="GHNDetailPanelId";
	public static final String WorkflowDocumentPanelId="WorkflowDocumentPanelId";
	public static final String ReportGeneratorPanelId="ReportGeneratorPanelId";
	public static final String TemplateGeneratorPanelId="TemplateGeneratorPanelId";
	public static final String AquamapsPanelId = "AquamapsPanelId";
	public static final String WebAppDetailPanelId = "WebAppDetailPanelId";
	public static final String WebAppPanelId = "WebAppPanelId";
	public static final String WarPanelId = "WarPanelId";
	public static final String SMPanelId = "SMPanelId";
	
	public static final String DIVID="UIDiv";
	public static final String START="start";
	public static final String LIMIT="limit";
	public static final String DefaultGrouping="vre";
	public static final String HLSubTypeSent="HL_ITEM_SENT";
	public static final String GENERATE_REPORT_OUTPUT="Generate_Report_Output";


	
	public  enum  Colors {
			red("red"),
			blue("blue"),
			green("green"),
			silver("silver"),
			aqua("aqua"),
			black("black"),
			fuchsia("fuchsia"),
			gray("gray"),
			lime("lime"),
			maroon("maroon"),
			navy("navy"),
			olive("olive"),
			purple("purple"),
			teal("teal"),
			white("white"),
			yellow("yellow");
			String type;
			Colors(String type) {this.type = type;}
			};

	
	public enum EntryType {
		LoginRecord("LoginRecord"),
		BrowseRecord("BrowseRecord"),
		AdvancedSearchRecord("AdvancedSearchRecord"),
		SimpleSearchRecord("SimpleSearchRecord"),
		ContentRecord("ContentRecord"),
		GoogleSearchRecord("GoogleSearchRecord"),
		QuickSearchRecord("QuickSearchRecord"),
		HLRecord("HLRecord"),
		TSRecord("TSRecord"),
		AISRecord("AISRecord"),
		AnnotationRecord("AnnotationRecord"),
		DocumentWorkflowRecord("DocumentWorkflowRecord"),
		ReportRecord("ReportRecord"),
		WebAppRecord("WebAppRecord"),
		WarRecord("WarRecord"),
		AquamapsRecord("AquamapsRecord"),
		GenericRecord("GenericRecord"),
		StatisticalManagerRecord("StatisticalManagerRecord"),
		Empty("");
		String type;
		EntryType(String type) {this.type = type;}
		public String toString() {return this.type;}
	};
}
