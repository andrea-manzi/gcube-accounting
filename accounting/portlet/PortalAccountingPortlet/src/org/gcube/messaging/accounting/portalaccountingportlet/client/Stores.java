package org.gcube.messaging.accounting.portalaccountingportlet.client;

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
	
	
	public static BufferedStore	getStore(AccountingCostants.EntryType type,String user,String []date){
		return initStore(AccountingCostants.servletURL+"?Record="+type+"&User="+user+
				"&DateStart="+date[0]+"&DateEnd="+date[1],RecordDefinition.getRecordDef(type));}
	
	public static BufferedStore	getStatisticStore(AccountingCostants.EntryType type,String user,String group,String [] date){
		return initStatisticStore(AccountingCostants.servletStatisticURL+"?Record="+type+"&User="+user+
				"&DateStart="+date[0]+"&DateEnd="+date[1]+"&GroupBy="+group,
				RecordDefinition.getStatisticsRecord(group));}



	public static BufferedStore initStore(String url,RecordDef recordDef){
		BufferedJsonReader reader;
		reader = new BufferedJsonReader("data",recordDef);		  
		reader.setTotalProperty("totalcount");
		BufferedStore toReturn=new BufferedStore(reader);
		toReturn.setAutoLoad(true);
		toReturn.setBufferSize(500);        
		toReturn.setSortInfo(new SortState("date", SortDir.DESC));
		toReturn.setUrl(url);
		return toReturn;
	}
	
	public static BufferedStore initStatisticStore(String url,RecordDef recordDef){
		BufferedJsonReader reader;
		reader = new BufferedJsonReader("data",recordDef);		  
		reader.setTotalProperty("totalcount");
		BufferedStore toReturn=new BufferedStore(reader);
		toReturn.setAutoLoad(false);
		toReturn.setSortInfo(new SortState(recordDef.getFields()[0].getName(), SortDir.DESC));
		toReturn.setBufferSize(500);        
		toReturn.setUrl(url);
		return toReturn;
	}

}
