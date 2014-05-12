package org.gcube.messaging.accounting.portalaccountingportlet.client;

/**
 * 
 * @author Andrea Manzi (CERN)
 *
 */
public class GlobalInfo {
	
	public static AccountingCostants.EntryType DefaultRecord=AccountingCostants.EntryType.Empty;
	private static AccountingCostants.EntryType selectedRecord = DefaultRecord;
	private static String selectedUSer = "";
	private static String startdate = "";
	private static String enddate = "";
	private static String grouping = "";
	
	public static String[] getDates(){
		return new String[] { getStartdate(),getEnddate()};
	}
	
	public static String getGrouping() {
		return grouping;
	}
	public static void setGrouping(String grouping) {
		GlobalInfo.grouping = grouping;
	}
	public static String getStartdate() {
		return startdate;
	}
	public static void setStartdate(String startdate) {
		GlobalInfo.startdate = startdate;
	}
	public static String getEnddate() {
		return enddate;
	}
	public static void setEnddate(String enddate) {
		GlobalInfo.enddate = enddate;
	}
	public static AccountingCostants.EntryType getSelectedRecord() {
		return selectedRecord;
	}
	public static void setSelectedRecord(AccountingCostants.EntryType selectedRecord) {
		GlobalInfo.selectedRecord = selectedRecord;
	}
	public static String getSelectedUSer() {
		return selectedUSer;
	}
	public static void setSelectedUSer(String selectedUSer) {
		GlobalInfo.selectedUSer = selectedUSer;
	}
	

}

