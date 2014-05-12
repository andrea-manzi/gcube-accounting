package org.gcube.messaging.accounting.portalaccountingportlet.client.panel;

import java.util.ArrayList;

import org.gcube.messaging.accounting.portalaccountingportlet.client.AccountingCostants;
import org.gcube.messaging.accounting.portalaccountingportlet.client.ColumnDefinition;
import org.gcube.messaging.accounting.portalaccountingportlet.client.RecordDefinition;
import org.gcube.messaging.accounting.portalaccountingportlet.client.data.CollectionPair;

import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.Store;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.grid.GridPanel;

/**
 * 
 * @author Andrea Manzi (CERN)
 *
 */
public class CollectionPanel extends Panel{
	
	GridPanel collectionTerms = new GridPanel();
	
	private Store store;
	
	public TextField field =new TextField("Operator", "Operator", 100);
	
	public CollectionPanel(){
		this.setTitle("Collections");
		this.setWidth(280);
		this.setHeight(400);
		this.setId(AccountingCostants.CollectionPanelID);
		
		field.setLabel("Operator");
		field.setGrow(true);
		field.disable();
		field.hide();
		collectionTerms.setColumnModel(ColumnDefinition.CollectionsModel());
		collectionTerms.setAutoScroll(true);
		collectionTerms.setAutoHeight(true);
		collectionTerms.setAutoWidth(true);
		Object[][] data = new Object[1][1];
		for (int i =0 ;i< data.length;i++){
			data[i][0]= "";
		}
		MemoryProxy proxy = new MemoryProxy(data);  
		ArrayReader reader = new ArrayReader(RecordDefinition.collectionsRecord);  
		store = new Store(proxy, reader);
		store.load();
		collectionTerms.setStore(store);
		this.add(collectionTerms);
		this.add(field);
		this.show();
	}
	

	public void loadCollectionsDetails(ArrayList<CollectionPair> result){
		Object[][] data = getCollections(result);  
		MemoryProxy proxy = new MemoryProxy(data);  
		ArrayReader reader = new ArrayReader(RecordDefinition.collectionsRecord);  
		store = new Store(proxy, reader);
		store.load();
		collectionTerms.reconfigure(store,ColumnDefinition.CollectionsModel());
		this.show();
	}
	
	private  Object[][] getCollections(ArrayList<CollectionPair> result)
	{
		Object[][] results = new Object[result.size()][2] ;

		for (int i =0 ;i< result.size();i++){
			results[i][0]= result.get(i).getId();
			results[i][1]= result.get(i).getName();
		}

		return results;
	}
	
	public void addOperator(String operator){
		field.setValue(operator);
		field.enable();	
		field.show();
	}
	
}
