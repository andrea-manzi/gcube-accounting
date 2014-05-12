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
import com.google.gwt.user.client.rpc.RemoteService;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;


public interface AccountingUIService extends RemoteService {
	public ArrayList<ArrayList<String>> getUsers() throws Exception;
	public String getStartDate() throws Exception;
	public ArrayList<CollectionPair> getCollections(String id) throws Exception;
	public ArrayList<TermPair> getTerms(String id) throws Exception;
	public ArrayList<GHNDetail> getGHNDetail(String id) throws Exception;
	public ArrayList<WebAppDetail> getWebAppDetail(String id) throws Exception;
	public ContentPair getContent(String id) throws Exception;
	public String export(ArrayList<String[]> records, String name) throws Exception;
	public ArrayList<GCUBEUser> getAddresseesGCUBEUsers(String id) throws Exception;
	
	public ArrayList<ArrayList<String>> getRecordTypes() throws Exception;
	public ArrayList<ArrayList<String>> getVres() throws Exception;
	public PagingLoadResult<DataObj> getRecordsForLiveGrid(PagingLoadConfig config, List<String> types,List<String> users,List<String> vres,String []date) throws Exception;
	public List<Statistics> getStatistics(List<String> types, List<String> users,List<String> vres, List<String> groupsBy,String []dates)throws Exception;
	public DataObj getDetails(String id, EntryType recordType) throws Exception;
}

