package org.gcube.messaging.accounting.nodeaccountingportlet.client;

import org.gcube.portlets.widgets.gcubelivegrid.client.data.BufferedJsonReader;
import org.gcube.portlets.widgets.gcubelivegrid.client.data.BufferedStore;

import com.gwtext.client.core.SortDir;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.SortState;

/**
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public class Stores {
	
	
	public static BufferedStore	getStore(GlobalInfo info){
		return initStore(Costants.servletURL+"?GHN="+info.getSelectedGHN()+"&serviceClass="+info.getSelectedServiceClass()+ "&serviceName="+
				info.getSelectedServiceName()+"&scope="+info.getSelectedScope()+
				"&DateStart="+info.getDates()[0]+"&DateEnd="+info.getDates()[1],RecordDefinition.record);}
	
	
	public static BufferedStore	getStatisticStore(GlobalInfo info){
		return initStatisticStore(Costants.servletStatisticURL+"?GHNName="+info.getSelectedGHN()+"&serviceClass="+info.getSelectedServiceClass()+"&serviceName="+
				info.getSelectedServiceName()+"&callerScope="+info.getSelectedScope()+
				"&DateStart="+info.getDates()[0]+"&DateEnd="+info.getDates()[1]+"&GroupBy="+info.getGrouping(),
				RecordDefinition.getStatisticsRecord(info.getGrouping()));}



	public static BufferedStore initStore(String url,RecordDef recordDef){
		BufferedJsonReader reader;
		reader = new BufferedJsonReader("data",recordDef);		  
		reader.setTotalProperty("totalcount");
		BufferedStore toReturn=new BufferedStore(reader);
		toReturn.setAutoLoad(true);
		toReturn.setBufferSize(500);        
		toReturn.setSortInfo(new SortState("startDate", SortDir.DESC));
		toReturn.setUrl(url);
		return toReturn;
	}
	
	public static BufferedStore initStatisticStore(String url,RecordDef recordDef){
		BufferedJsonReader reader;
		reader = new BufferedJsonReader("data",recordDef);		  
		reader.setTotalProperty("totalcount");
		BufferedStore toReturn=new BufferedStore(reader);
		toReturn.setAutoLoad(false);
		toReturn.setSortInfo(new SortState(recordDef.getFields()[1].getName(), SortDir.DESC));
		toReturn.setBufferSize(500);        
		toReturn.setUrl(url);
		return toReturn;
	}

}
