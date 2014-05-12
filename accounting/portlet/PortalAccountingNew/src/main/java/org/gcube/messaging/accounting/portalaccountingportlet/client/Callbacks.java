package org.gcube.messaging.accounting.portalaccountingportlet.client;

import java.util.ArrayList;
import java.util.List;

import org.gcube.messaging.accounting.portalaccountingportlet.client.AccountingCostants.EntryType;
import org.gcube.messaging.accounting.portalaccountingportlet.client.obj.CollectionPair;
import org.gcube.messaging.accounting.portalaccountingportlet.client.obj.ContentPair;
import org.gcube.messaging.accounting.portalaccountingportlet.client.obj.DataObj;
import org.gcube.messaging.accounting.portalaccountingportlet.client.obj.GCUBEUser;
import org.gcube.messaging.accounting.portalaccountingportlet.client.obj.GHNDetail;
import org.gcube.messaging.accounting.portalaccountingportlet.client.obj.Statistics;
import org.gcube.messaging.accounting.portalaccountingportlet.client.obj.TermPair;
import org.gcube.messaging.accounting.portalaccountingportlet.client.obj.WebAppDetail;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class Callbacks {

	public static AsyncCallback<List<Statistics>> getStatisticsCallback = new AsyncCallback<List<Statistics>>(){

		public void onFailure(Throwable caught) {			
			AccountingUI.get().notifyError("Error Calculating Statistics");
		}

		public void onSuccess(List<Statistics> result) {
			//GWT.log(result, null);
			AccountingUI.get().accountingUI.statistics.loadStatistics(result);
		}
	};

	public static AsyncCallback<ArrayList<ArrayList<String>>> listUsersCallback = new AsyncCallback<ArrayList<ArrayList<String>>>(){

		public void onFailure(Throwable caught) {			
		}

		public void onSuccess(ArrayList<ArrayList<String>> results) {
			FiltUserPanel.get().update(results);
		}
	};

	public static AsyncCallback<ArrayList<ArrayList<String>>> listRecordTypesCallback = new AsyncCallback<ArrayList<ArrayList<String>>>(){

		public void onFailure(Throwable caught) {			
		}

		public void onSuccess(ArrayList<ArrayList<String>> results) {
			FiltTypePanel.get().update(results);
		}
	};
	public static AsyncCallback<ArrayList<ArrayList<String>>> listVresCallback = new AsyncCallback<ArrayList<ArrayList<String>>>(){

		public void onFailure(Throwable caught) {			
		}

		public void onSuccess(ArrayList<ArrayList<String>> results) {
			FiltVrePanel.get().update(results);
		}
	};
	public static AsyncCallback<ArrayList<CollectionPair>> listCollectionsDetailsCallback = new AsyncCallback<ArrayList<CollectionPair>> (){

		public void onFailure(Throwable caught) {
			AccountingUI.get().notifyError("Error retrieving Collections Details");	
		}

		public void onSuccess(ArrayList<CollectionPair> result) {
			try {
				//((CollectionPanel)AccountingUI.get().accountingDetail.details.getComponent(AccountingCostants.CollectionPanelID)).loadCollectionsDetails(result);
				//CollectionPanel2.get().loadCollectionsDetails(result);
				AccountingUI.get().accountingDetail.collectionPanel.loadCollectionsDetails(result);
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
			//((TermPanel)AccountingUI.get().accountingDetail.details.getComponent(AccountingCostants.TermsPanelId)).loadTermsDetails(result);
			AccountingUI.get().accountingDetail.termPanel.loadCollectionsDetails(result,false);
		}
	};
	public static AsyncCallback<ContentPair> listContentDetailsCallback = new AsyncCallback<ContentPair> (){

		public void onFailure(Throwable caught) {
			AccountingUI.get().notifyError("Error retrieving Content Details");

		}

		public void onSuccess(ContentPair result) {
			//	((ContentPanel)AccountingUI.get().accountingDetail.details.getComponent(AccountingCostants.ContentPanelId)).loadContent(result);
			DataObj record=new DataObj();
			record.setName(result.getContentName());
			record.setIdentifier(result.getContentId());
			AccountingUI.get().accountingDetail.contentPanel.loadData(record);
		}
	};

	public static AsyncCallback<ArrayList<GCUBEUser>> lisGCUBEAdreesseesCallback = new AsyncCallback<ArrayList<GCUBEUser>> (){

		public void onFailure(Throwable caught) {
			AccountingUI.get().notifyError("Error retrieving GCUBE users Addressees Details");

		}

		public void onSuccess(ArrayList<GCUBEUser> result) {
			//	((GCUBEUsersPanel)AccountingUI.get().accountingDetail.details.getComponent(AccountingCostants.GCUBEUSersAddresseesPanelId)).loadUserDetails(result);
			AccountingUI.get().accountingDetail.gcubeUsersPanel.loadCollectionsDetails(result);
		}
	};

	public static AsyncCallback<DataObj> getDetailsCallback = new AsyncCallback<DataObj> (){
		public void onFailure(Throwable caught) {
			caught.printStackTrace();
			AccountingUI.get().notifyError("Error retrieving  Details");
		}
		public void onSuccess(DataObj result) {
			AccountingUI.get().accountingDetail.loadPanel(result);
			
			if(result.getType().compareTo(EntryType.HLRecord.type)==0 && result.getSUBTYPE()!=null){
				if(result.getSUBTYPE().compareTo(AccountingCostants.HLSubTypeSent)== 0)
					AccountingUI.service.getAddresseesGCUBEUsers(result.getId(), Callbacks.lisGCUBEAdreesseesCallback);
			}
		}
	};

		public static AsyncCallback<String> setStartDateCallback = new AsyncCallback<String> (){

			public void onFailure(Throwable caught) {
				AccountingUI.get().notifyError("Error retrieving Start DB date");

			}

			public void onSuccess(String result) {
				AccountingLeftPanel.get().setDateStart(result);

			}
		};

		public static AsyncCallback<ArrayList<GHNDetail>> getGHNDetailsCallback = new AsyncCallback<ArrayList<GHNDetail>> (){
			public void onFailure(Throwable caught) {
				AccountingUI.get().notifyError("Error retrieving GHN Details");

			}
			public void onSuccess(ArrayList<GHNDetail> result) {
				//((GHNDetailPanel)AccountingUI.get().accountingDetail.details.getComponent(AccountingCostants.GHNDetailPanelId)).loadGHNDetails(result);
				AccountingUI.get().accountingDetail.ghnDetailPanel.loadCollectionsDetails(result);
			}
		};

		public static AsyncCallback<ArrayList<WebAppDetail>> getWebAppDetailsCallback = new AsyncCallback<ArrayList<WebAppDetail>> (){

			public void onFailure(Throwable caught) {
				AccountingUI.get().notifyError("Error retrieving Web App Details");

			}

			public void onSuccess(ArrayList<WebAppDetail> result) {
				//((WebAppDetailPanel)AccountingUI.get().accountingDetail.details.getComponent(AccountingCostants.WebAppDetailPanelId)).loadWebAppDetails(result);
				AccountingUI.get().accountingDetail.webappdetailPanel.loadCollectionsDetails(result);
			}
		};

	}
