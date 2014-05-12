package org.gcube.messaging.accounting.portalaccountingportlet.client.interfaces;


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

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;



public interface AccountingUIServiceAsync {	
public void getUsers(AsyncCallback<ArrayList<ArrayList<String>>> callback) ;
	public void getCollections(String id,AsyncCallback<ArrayList<CollectionPair>> callback);
	public void getTerms(String id, AsyncCallback<ArrayList<TermPair>> callback);
	public void getContent(String id, AsyncCallback<ContentPair> callback);
	public void getAddresseesGCUBEUsers(String id, AsyncCallback<ArrayList<GCUBEUser>>  callback);
	public void export(ArrayList<String[]> record ,String name,AsyncCallback<String> callback);
	public void getStartDate(AsyncCallback<String> callback);
	public void getGHNDetail(String id, AsyncCallback<ArrayList<GHNDetail>> callback);
	public void getWebAppDetail(String id,
			AsyncCallback<ArrayList<WebAppDetail>> callback);

	public void getRecordTypes(AsyncCallback<ArrayList<ArrayList<String>>> callback); 
	public void getVres(AsyncCallback<ArrayList<ArrayList<String>>> callback); 
	public void getRecordsForLiveGrid(PagingLoadConfig config, List<String> types,List<String> users,List<String> vres,String []date,
			AsyncCallback<PagingLoadResult<DataObj>> callback); 
	public void getStatistics(List<String> types, List<String> users,List<String> vres, List<String> groupsBy,String []dates,
			AsyncCallback<List<Statistics>> callback);
	public void getDetails(String id, EntryType recordType,
			AsyncCallback<DataObj> callback);
}
