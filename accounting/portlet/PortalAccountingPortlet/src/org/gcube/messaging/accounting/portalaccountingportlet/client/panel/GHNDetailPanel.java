package org.gcube.messaging.accounting.portalaccountingportlet.client.panel;

import java.util.ArrayList;

import org.gcube.messaging.accounting.portalaccountingportlet.client.AccountingCostants;
import org.gcube.messaging.accounting.portalaccountingportlet.client.ColumnDefinition;
import org.gcube.messaging.accounting.portalaccountingportlet.client.RecordDefinition;
import org.gcube.messaging.accounting.portalaccountingportlet.client.data.GHNDetail;

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
public class GHNDetailPanel extends Panel {

	GridPanel panel = new GridPanel();
	Store store = null;
	

	public GHNDetailPanel() {
		this.setTitle("GHN Details");
		this.setWidth(280);
		this.setHeight(400);
		this.setId(AccountingCostants.GHNDetailPanelId);

		panel.setColumnModel(ColumnDefinition.GHNDetailModel());
		panel.setAutoScroll(true);
		panel.setAutoHeight(true);
		panel.setAutoWidth(true);
		Object[][] data = new Object[1][1];
		for (int i =0 ;i< data.length;i++){
			data[i][0]= "";
		}
		MemoryProxy proxy = new MemoryProxy(data);  
		ArrayReader reader = new ArrayReader(RecordDefinition.ghnRecord);  
		store = new Store(proxy, reader);
		store.load();
		panel.setStore(store);
		this.add(panel);
		this.show();

	}

	public void loadGHNDetails(ArrayList<GHNDetail> result) {

		Object[][] data = getGHNs(result);
		MemoryProxy proxy = new MemoryProxy(data);
		ArrayReader reader = new ArrayReader(RecordDefinition.ghnRecord);
		store = new Store(proxy, reader);
		store.load();
		panel.reconfigure(store, ColumnDefinition.GHNDetailModel());
		this.show();

	}

	private Object[][] getGHNs(ArrayList<GHNDetail> result) {
		
		Object[][] results = new Object[result.size()][2];
		
		
		for (int i =0 ;i< result.size();i++){
			results[i][0]= result.get(i).getGhnID();
			results[i][1]= result.get(i).getGhnName();
		}
		
		return results;
	}
}
