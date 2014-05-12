package org.gcube.messaging.accounting.nodeaccountingportlet.client;

import java.util.ArrayList;
import java.util.Map;


import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
/**
 * 
 * @author Andrea Manzi (CERN)
 *
 */
public class Callbacks {

	 
	public static AsyncCallback<String> listRecordsCallback = new AsyncCallback<String>(){

		public void onFailure(Throwable caught) {			
			NodeAccountingUI.get().notifyError("Error retrieving records Details");
		}

		public void onSuccess(String result) {
					
			NodeAccountingUI.get().central.grid.getStore().reload();
		}
	};
	
	public static AsyncCallback<String> getStatisticsCallback = new AsyncCallback<String>(){

		public void onFailure(Throwable caught) {			
			NodeAccountingUI.get().notifyError("Error Calculating Statistics");
		}

		public void onSuccess(String result) {
			GWT.log(result, null);
		}
	};
	
	public static AsyncCallback<Map<String,ArrayList<String>>> listGHNsCallback = new AsyncCallback<Map<String,ArrayList<String>>>(){

		public void onFailure(Throwable caught) {	
			NodeAccountingUI.get().hideLoading(NodeAccountingUI.get().tree.ghns.getId());
			NodeAccountingUI.get().notifyError("Error Getting GHN list");
		}

		public void onSuccess(Map<String,ArrayList<String>> results) {
			GHNTree.get().populateTree(results);
			NodeAccountingUI.get().hideLoading(NodeAccountingUI.get().tree.ghns.getId());
		}
	};
	
	public static AsyncCallback<Map<String,ArrayList<String>>> listServicesCallback = new AsyncCallback<Map<String,ArrayList<String>>>(){

		public void onFailure(Throwable caught) {	
			NodeAccountingUI.get().hideLoading(NodeAccountingUI.get().tree.types.getId());
			NodeAccountingUI.get().notifyError("Error Getting Service list");
			
		}

		public void onSuccess(Map<String,ArrayList<String>> results) {
			ServiceTree.get().populateTree(results);
			NodeAccountingUI.get().hideLoading(NodeAccountingUI.get().tree.types.getId());
		}
	};
	
	
	public static AsyncCallback<String> exportCallback = new AsyncCallback<String>(){

		public void onFailure(Throwable caught) {			
			NodeAccountingUI.get().notifyError("Error saving temporary csv");
		}

		public void onSuccess(String result) {
			Window.open(Costants.exportServletURL+"?file="+result,"_self","");
		}
	};
	

	
	public static AsyncCallback<String> setStartDateCallback = new AsyncCallback<String> (){

		public void onFailure(Throwable caught) {
			NodeAccountingUI.get().notifyError("Error retrieving Start DB date");
			
		}

		public void onSuccess(String result) {
			TreeContainer.get().setDateStart(result);
		}
	};
	
	
	public static AsyncCallback<ArrayList<ArrayList<String>>> setScopes = new AsyncCallback<ArrayList<ArrayList<String>>> (){

		public void onFailure(Throwable caught) {
			NodeAccountingUI.get().notifyError("Error retrieving caller scopes");
			
		}

		public void onSuccess(ArrayList<ArrayList<String>> result) {
			TreeContainer.get().cb.updateCB(result);
		}
	};
}
