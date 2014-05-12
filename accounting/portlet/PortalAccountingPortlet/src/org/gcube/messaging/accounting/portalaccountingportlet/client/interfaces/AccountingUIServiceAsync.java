package org.gcube.messaging.accounting.portalaccountingportlet.client.interfaces;


import java.util.ArrayList;


import org.gcube.messaging.accounting.portalaccountingportlet.client.data.CollectionPair;
import org.gcube.messaging.accounting.portalaccountingportlet.client.data.ContentPair;
import org.gcube.messaging.accounting.portalaccountingportlet.client.data.GCUBEUser;
import org.gcube.messaging.accounting.portalaccountingportlet.client.data.GHNDetail;
import org.gcube.messaging.accounting.portalaccountingportlet.client.data.TermPair;
import org.gcube.messaging.accounting.portalaccountingportlet.client.data.WebAppDetail;

import com.google.gwt.user.client.rpc.AsyncCallback;



/**
 * 
 * @author Andrea Manzi (CERN)
 *
 */
public interface AccountingUIServiceAsync {	
	public void getRecordsByTypeAndUserJson(String type,String user,String sortColumn,String sortDir,int start,int limit,String[] date,AsyncCallback<String> callback);
	public void getUsers(AsyncCallback<ArrayList<ArrayList<String>>> callback) ;
	public void getCollections(String id,AsyncCallback<ArrayList<CollectionPair>> callback);
	public void getTerms(String id, AsyncCallback<ArrayList<TermPair>> callback);
	public void getContent(String id, AsyncCallback<ContentPair> callback);
	public void getAddresseesGCUBEUsers(String id, AsyncCallback<ArrayList<GCUBEUser>>  callback);
	public void getStatistics(String type, String user, String groupBy,String sortColumn,String sortDir,String [] dates,
			AsyncCallback<String> callback) ;
	public void export(ArrayList<String[]> record ,String name,AsyncCallback<String> callback);
	public void getStartDate(AsyncCallback<String> callback);
	public void getGHNDetail(String id, AsyncCallback<ArrayList<GHNDetail>> callback);
	public void getWebAppDetail(String id,
			AsyncCallback<ArrayList<WebAppDetail>> callback);

}
