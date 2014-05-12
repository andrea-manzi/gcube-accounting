package org.gcube.messaging.accounting.portalaccountingportlet.client.panel;

import java.util.ArrayList;

import org.gcube.messaging.accounting.portalaccountingportlet.client.AccountingCostants;
import org.gcube.messaging.accounting.portalaccountingportlet.client.ColumnDefinition;
import org.gcube.messaging.accounting.portalaccountingportlet.client.RecordDefinition;
import org.gcube.messaging.accounting.portalaccountingportlet.client.data.TermPair;

import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.Store;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.grid.GridPanel;
/**
 * 
 * @author Andrea Manzi (CERN)
 *
 */
public class TermPanel extends Panel{

	GridPanel gridTerms = new GridPanel();
	
	private Store store;

	
	public TermPanel(){
		this.setTitle("Terms");
		this.setWidth(280);
		this.setHeight(400);
		this.setId(AccountingCostants.TermsPanelId);

		gridTerms.setColumnModel(ColumnDefinition.TermsModel());
		gridTerms.setAutoHeight(true);
		gridTerms.setAutoWidth(true);
		gridTerms.setAutoScroll(true);
		Object[][] data = new Object[1][1];
		for (int i =0 ;i< data.length;i++){
			data[i][0]= "";
		}

		MemoryProxy proxy = new MemoryProxy(data);  
		ArrayReader reader = new ArrayReader(RecordDefinition.termRecord);  
		store = new Store(proxy, reader);
		store.load();
		gridTerms.setStore(store);
		this.add(gridTerms);
		this.show();
		
	}

	public void loadTermsDetails(ArrayList<TermPair> result){
		Object[][] data = getTerms(result);  
		MemoryProxy proxy = new MemoryProxy(data);  
		ArrayReader reader = new ArrayReader(RecordDefinition.termRecord);  
		store = new Store(proxy, reader);
		store.load();
		gridTerms.reconfigure(store,ColumnDefinition.TermsModel());
		this.show();
		  
	
	}

	private  Object[][] getTerms(ArrayList<TermPair> result)
	{
		Object[][] results = new Object[result.size()][2] ;

		for (int i =0 ;i< result.size();i++){
			results[i][0]= result.get(i).getTermName();
			results[i][1]= result.get(i).getTermValue();
		}

		return results;
	}
}
