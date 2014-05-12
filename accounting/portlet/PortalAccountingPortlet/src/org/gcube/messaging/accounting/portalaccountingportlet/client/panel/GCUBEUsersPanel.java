package org.gcube.messaging.accounting.portalaccountingportlet.client.panel;

import java.util.ArrayList;

import org.gcube.messaging.accounting.portalaccountingportlet.client.AccountingCostants;
import org.gcube.messaging.accounting.portalaccountingportlet.client.ColumnDefinition;
import org.gcube.messaging.accounting.portalaccountingportlet.client.RecordDefinition;
import org.gcube.messaging.accounting.portalaccountingportlet.client.data.GCUBEUser;

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
public class GCUBEUsersPanel extends Panel {

	GridPanel panel = new GridPanel();
	Store store = null;
	

	public GCUBEUsersPanel() {
		this.setTitle("GCUBEAddresseesUsers");
		this.setWidth(280);
		this.setHeight(400);
		this.setId(AccountingCostants.GCUBEUSersAddresseesPanelId);

		panel.setColumnModel(ColumnDefinition.GCUBEUsersModel());
		panel.setAutoScroll(true);
		panel.setAutoHeight(true);
		panel.setAutoWidth(true);
		Object[][] data = new Object[1][1];
		for (int i =0 ;i< data.length;i++){
			data[i][0]= "";
		}
		MemoryProxy proxy = new MemoryProxy(data);  
		ArrayReader reader = new ArrayReader(RecordDefinition.userRecord);  
		store = new Store(proxy, reader);
		store.load();
		panel.setStore(store);
		this.add(panel);
		this.show();

	}

	public void loadUserDetails(ArrayList<GCUBEUser> result) {

		Object[][] data = getUsers(result);
		MemoryProxy proxy = new MemoryProxy(data);
		ArrayReader reader = new ArrayReader(RecordDefinition.userRecord);
		store = new Store(proxy, reader);
		store.load();
		panel.reconfigure(store, ColumnDefinition.GCUBEUsersModel());
		this.show();

	}

	private Object[][] getUsers(ArrayList<GCUBEUser> result) {
		
		Object[][] results = new Object[result.size()][2];
		
		
		for (int i = 0; i < result.size(); i++) {
			int index = -1;
			String temp =result.get(i).getVre().substring(1);
			if ((index = temp.indexOf("/")) != -1)
				results[i][1] =temp.substring(index);
			else results[i][1] = result.get(i).getVre();
			
			results[i][0] = result.get(i).getName();
			
		}
		
		return results;
	}
}
