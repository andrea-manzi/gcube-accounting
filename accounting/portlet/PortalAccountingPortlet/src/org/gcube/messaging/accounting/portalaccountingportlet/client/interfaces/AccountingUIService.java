package org.gcube.messaging.accounting.portalaccountingportlet.client.interfaces;


import java.util.ArrayList;

import org.gcube.messaging.accounting.portalaccountingportlet.client.data.CollectionPair;
import org.gcube.messaging.accounting.portalaccountingportlet.client.data.ContentPair;
import org.gcube.messaging.accounting.portalaccountingportlet.client.data.GCUBEUser;
import org.gcube.messaging.accounting.portalaccountingportlet.client.data.GHNDetail;
import org.gcube.messaging.accounting.portalaccountingportlet.client.data.TermPair;
import org.gcube.messaging.accounting.portalaccountingportlet.client.data.WebAppDetail;

import com.google.gwt.user.client.rpc.RemoteService;

/**
 * 
 * @author Andrea Manzi (CERN)
 *
 */

public interface AccountingUIService extends RemoteService {
	public String getRecordsByTypeAndUserJson(String type,String user,String sortColumn,String sortDir,int start,int limit,String []date) throws Exception;
	public ArrayList<ArrayList<String>> getUsers() throws Exception;
	public String getStartDate() throws Exception;
	public ArrayList<CollectionPair> getCollections(String id) throws Exception;
	public ArrayList<TermPair> getTerms(String id) throws Exception;
	public ArrayList<GHNDetail> getGHNDetail(String id) throws Exception;
	public ArrayList<WebAppDetail> getWebAppDetail(String id) throws Exception;
	public ContentPair getContent(String id) throws Exception;
	public String getStatistics(String type, String user,String groupBy,String sortColumn,String sortDir,String []dates ) throws Exception;
	public String export(ArrayList<String[]> records, String name) throws Exception;
	public ArrayList<GCUBEUser> getAddresseesGCUBEUsers(String id) throws Exception;
}

