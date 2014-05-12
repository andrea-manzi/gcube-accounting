package org.gcube.messaging.accounting.nodeaccountingportlet.stubs;


import java.util.ArrayList;
import java.util.Map;

import com.google.gwt.user.client.rpc.RemoteService;

/**
 * 
 * @author Andrea Manzi (CERN)
 *
 */

public interface NodeAccountingService extends RemoteService {
	public String getRecords(String ghn,String scope,String serviceClass, String serviceName,
			String sortColumn,String sortDir,int start,int limit,String []date) throws Exception;
	public Map<String,ArrayList<String>> getGHNs() throws Exception;
	public Map<String,ArrayList<String>> getServices() throws Exception;
	public ArrayList<ArrayList<String>> getScopes() throws Exception;
	public String getStartDate() throws Exception;
	public String getStatistics(String serviceClass,String serviceName,String callerScope,String ghn,
			String groupBy,String sortColumn,String sortDir,String []dates ) throws Exception;
	public String export(ArrayList<String[]> records, String name) throws Exception;

}

