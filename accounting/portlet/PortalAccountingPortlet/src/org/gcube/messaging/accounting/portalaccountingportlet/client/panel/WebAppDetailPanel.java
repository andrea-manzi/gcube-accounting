package org.gcube.messaging.accounting.portalaccountingportlet.client.panel;

import java.util.ArrayList;

import org.gcube.messaging.accounting.portalaccountingportlet.client.AccountingCostants;
import org.gcube.messaging.accounting.portalaccountingportlet.client.ColumnDefinition;
import org.gcube.messaging.accounting.portalaccountingportlet.client.RecordDefinition;
import org.gcube.messaging.accounting.portalaccountingportlet.client.data.WebAppDetail;

import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.Store;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.grid.GridPanel;

/**
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public class WebAppDetailPanel extends Panel {

	GridPanel panel = new GridPanel();
	Store store = null;
	

	public WebAppDetailPanel() {
		this.setTitle("WebApp Details");
		this.setWidth(280);
		this.setHeight(400);
		this.setId(AccountingCostants.WebAppDetailPanelId);

		panel.setColumnModel(ColumnDefinition.WebAppDetailModel());
		panel.setAutoScroll(true);
		panel.setAutoHeight(true);
		panel.setAutoWidth(true);
		Object[][] data = new Object[1][1];
		for (int i =0 ;i< data.length;i++){
			data[i][0]= "";
		}
		MemoryProxy proxy = new MemoryProxy(data);  
		ArrayReader reader = new ArrayReader(RecordDefinition.webappDetailRecord);  
		store = new Store(proxy, reader);
		store.load();
		panel.setStore(store);
		this.add(panel);
		this.show();

	}

	public void loadWebAppDetails(ArrayList<WebAppDetail> result) {

		Object[][] data = getWebApps(result);
		MemoryProxy proxy = new MemoryProxy(data);
		ArrayReader reader = new ArrayReader(RecordDefinition.webappDetailRecord);
		store = new Store(proxy, reader);
		store.load();
		panel.reconfigure(store, ColumnDefinition.WebAppDetailModel());
		this.show();

	}

	private Object[][] getWebApps(ArrayList<WebAppDetail> result) {
		
		Object[][] results = new Object[result.size()][2];
		
		
		for (int i =0 ;i< result.size();i++){
			results[i][0]= result.get(i).getId();
			results[i][1]= result.get(i).getName();
		}
		
		return results;
	}
}