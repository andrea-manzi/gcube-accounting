package org.gcube.messaging.accounting.portalaccountingportlet.client;

import java.util.ArrayList;

import org.gcube.messaging.accounting.portalaccountingportlet.client.data.CollectionPair;
import org.gcube.messaging.accounting.portalaccountingportlet.client.data.ContentPair;
import org.gcube.messaging.accounting.portalaccountingportlet.client.data.GCUBEUser;
import org.gcube.messaging.accounting.portalaccountingportlet.client.data.GHNDetail;
import org.gcube.messaging.accounting.portalaccountingportlet.client.data.TermPair;
import org.gcube.messaging.accounting.portalaccountingportlet.client.data.WebAppDetail;
import org.gcube.messaging.accounting.portalaccountingportlet.client.panel.CollectionPanel;
import org.gcube.messaging.accounting.portalaccountingportlet.client.panel.ContentPanel;
import org.gcube.messaging.accounting.portalaccountingportlet.client.panel.GCUBEUsersPanel;
import org.gcube.messaging.accounting.portalaccountingportlet.client.panel.GHNDetailPanel;
import org.gcube.messaging.accounting.portalaccountingportlet.client.panel.TermPanel;
import org.gcube.messaging.accounting.portalaccountingportlet.client.panel.WebAppDetailPanel;

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
			AccountingUI.get().notifyError("Error retrieving records Details");
			AccountingUI.get().hideLoading(AccountingUI.get().accountingUI.grid.getId());
		}

		public void onSuccess(String result) {
					
			AccountingUI.get().accountingUI.grid.getStore().reload();
			AccountingUI.get().hideLoading(AccountingUI.get().accountingUI.grid.getId());
		}
	};
	
	public static AsyncCallback<String> getStatisticsCallback = new AsyncCallback<String>(){

		public void onFailure(Throwable caught) {			
			AccountingUI.get().notifyError("Error Calculating Statistics");
		}

		public void onSuccess(String result) {
			GWT.log(result, null);
		}
	};
	
	public static AsyncCallback<ArrayList<ArrayList<String>>> listUsersCallback = new AsyncCallback<ArrayList<ArrayList<String>>>(){

		public void onFailure(Throwable caught) {			
		}

		public void onSuccess(ArrayList<ArrayList<String>> results) {
			AccountingUserTree.get().populateTree(results);
		}
	};
	
	public static AsyncCallback<ArrayList<CollectionPair>> listCollectionsDetailsCallback = new AsyncCallback<ArrayList<CollectionPair>> (){

		public void onFailure(Throwable caught) {
			AccountingUI.get().notifyError("Error retrieving Collections Details");	
		}

		public void onSuccess(ArrayList<CollectionPair> result) {
			try {
				((CollectionPanel)AccountingUI.get().accountingDetail.details.getComponent(AccountingCostants.CollectionPanelID)).loadCollectionsDetails(result);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}

		
	};
	
	public static AsyncCallback<String> exportCallback = new AsyncCallback<String>(){

		public void onFailure(Throwable caught) {			
			AccountingUI.get().notifyError("Error saving temporary csv");
		}

		public void onSuccess(String result) {
			Window.open(AccountingCostants.exportServletURL+"?file="+result,"_self","");
		}
	};
	
	public static AsyncCallback<ArrayList<TermPair>> listTermsDetailsCallback = new AsyncCallback<ArrayList<TermPair>> (){

		public void onFailure(Throwable caught) {
			AccountingUI.get().notifyError("Error retrieving Terms Details");
			
		}

		public void onSuccess(ArrayList<TermPair> result) {
			((TermPanel)AccountingUI.get().accountingDetail.details.getComponent(AccountingCostants.TermsPanelId)).loadTermsDetails(result);
			
		}
	};
	public static AsyncCallback<ContentPair> listContentDetailsCallback = new AsyncCallback<ContentPair> (){

		public void onFailure(Throwable caught) {
			AccountingUI.get().notifyError("Error retrieving Content Details");
			
		}

		public void onSuccess(ContentPair result) {
			((ContentPanel)AccountingUI.get().accountingDetail.details.getComponent(AccountingCostants.ContentPanelId)).loadContent(result);
			
		}
	};
	
	public static AsyncCallback<ArrayList<GCUBEUser>> lisGCUBEAdreesseesCallback = new AsyncCallback<ArrayList<GCUBEUser>> (){

		public void onFailure(Throwable caught) {
			AccountingUI.get().notifyError("Error retrieving GCUBE users Addressees Details");
			
		}

		public void onSuccess(ArrayList<GCUBEUser> result) {
			((GCUBEUsersPanel)AccountingUI.get().accountingDetail.details.getComponent(AccountingCostants.GCUBEUSersAddresseesPanelId)).loadUserDetails(result);
			
		}
	};
	
	
	public static AsyncCallback<String> setStartDateCallback = new AsyncCallback<String> (){

		public void onFailure(Throwable caught) {
			AccountingUI.get().notifyError("Error retrieving Start DB date");
			
		}

		public void onSuccess(String result) {
			AccountingTreeContainer.get().setDateStart(result);
			
		}
	};
	
	public static AsyncCallback<ArrayList<GHNDetail>> getGHNDetailsCallback = new AsyncCallback<ArrayList<GHNDetail>> (){

		public void onFailure(Throwable caught) {
			AccountingUI.get().notifyError("Error retrieving GHN Details");
			
		}

		public void onSuccess(ArrayList<GHNDetail> result) {
			((GHNDetailPanel)AccountingUI.get().accountingDetail.details.getComponent(AccountingCostants.GHNDetailPanelId)).loadGHNDetails(result);
				
		}
	};
	
	public static AsyncCallback<ArrayList<WebAppDetail>> getWebAppDetailsCallback = new AsyncCallback<ArrayList<WebAppDetail>> (){

		public void onFailure(Throwable caught) {
			AccountingUI.get().notifyError("Error retrieving Web App Details");
			
		}

		public void onSuccess(ArrayList<WebAppDetail> result) {
			((WebAppDetailPanel)AccountingUI.get().accountingDetail.details.getComponent(AccountingCostants.WebAppDetailPanelId)).loadWebAppDetails(result);
				
		}
	};
	
}
