package org.gcube.messaging.accounting.portalaccountingportlet.client;

import java.util.ArrayList;
import java.util.List;

import org.gcube.messaging.accounting.portalaccountingportlet.client.AccountingCostants.EntryType;

public class GlobalInfo {
	
	public static AccountingCostants.EntryType DefaultRecord=AccountingCostants.EntryType.Empty;
	private static AccountingCostants.EntryType selectedRecord=DefaultRecord; //grid record
	private static List<String> selectedTypes=new ArrayList<String>();
	private static List<String> selectedUsers=new ArrayList<String>();
	private static List<String> selectedVres=new ArrayList<String>();
	
	private static List<String> selectedGroupsBy=new ArrayList<String>();
		
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

	public static List<String> getSelectedTypes() {
		return selectedTypes;
	}

	public static void setSelectedTypes(List<String> selectedTypes) {
		GlobalInfo.selectedTypes = selectedTypes;
	}

	public static List<String> getSelectedUsers() {
		return selectedUsers;
	}

	public static void setSelectedUsers(List<String> selectedUsers) {
		GlobalInfo.selectedUsers = selectedUsers;
	}

	public static List<String> getSelectedVres() {
		return selectedVres;
	}

	public static void setSelectedVres(List<String> selectedVres) {
		GlobalInfo.selectedVres = selectedVres;
	}

	public static List<String> getSelectedGroupsBy() {
		return selectedGroupsBy;
	}

	public static void setSelectedGroupsBy(List<String> selectedGroupsBy) {
		GlobalInfo.selectedGroupsBy = selectedGroupsBy;
	}
	
}

