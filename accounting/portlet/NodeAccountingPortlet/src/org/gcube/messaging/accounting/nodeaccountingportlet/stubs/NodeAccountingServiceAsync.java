package org.gcube.messaging.accounting.nodeaccountingportlet.stubs;


import java.util.ArrayList;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;



/**
 * 
 * @author Andrea Manzi (CERN)
 *
 */
public interface NodeAccountingServiceAsync {	
	public void export(ArrayList<String[]> record ,String name,AsyncCallback<String> callback);
	public void getStartDate(AsyncCallback<String> callback);
	void getScopes(AsyncCallback<ArrayList<ArrayList<String>>> callback);
	void getRecords(String ghn, String scope, String serviceClass,
			String serviceName, String sortColumn, String sortDir, int start,
			int limit, String[] date, AsyncCallback<String> callback);
	void getGHNs(AsyncCallback<Map<String,ArrayList<String>>> callback);
	void getServices(AsyncCallback<Map<String,ArrayList<String>>> callback);
	void getStatistics(String serviceClass, String serviceName,
			String callerScope, String ghn, String groupBy, String sortColumn,
			String sortDir, String[] dates, AsyncCallback<String> callback);

}
