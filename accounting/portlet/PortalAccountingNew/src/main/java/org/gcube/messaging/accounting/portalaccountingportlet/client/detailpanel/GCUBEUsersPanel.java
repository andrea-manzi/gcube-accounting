package org.gcube.messaging.accounting.portalaccountingportlet.client.detailpanel;

import java.util.ArrayList;
import java.util.List;

import org.gcube.messaging.accounting.portalaccountingportlet.client.AccountingCostants;
import org.gcube.messaging.accounting.portalaccountingportlet.client.ColumnDefinition;
import org.gcube.messaging.accounting.portalaccountingportlet.client.obj.CollectionPair;
import org.gcube.messaging.accounting.portalaccountingportlet.client.obj.DataObj;
import org.gcube.messaging.accounting.portalaccountingportlet.client.obj.DataObjProperties;
import org.gcube.messaging.accounting.portalaccountingportlet.client.obj.GCUBEUser;

import com.google.gwt.core.client.GWT;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridSelectionModel;

public class GCUBEUsersPanel extends ContentPanel{
	
	private Grid<DataObj> grid;
	private ListStore<DataObj> store;
	private static GCUBEUsersPanel singleton;
	
	public static GCUBEUsersPanel get(){
		return singleton;
	}
	
	public GCUBEUsersPanel(){
		singleton=this;
		this.setTitle("GCUBEAddresseesUsers");
		this.setWidth(280);
		this.setHeight(400);
		this.setId(AccountingCostants.GCUBEUSersAddresseesPanelId);
		DataObjProperties props = GWT.create(DataObjProperties.class);
		store = new ListStore<DataObj>(props.key());
		
		ColumnModel<DataObj> cm = ColumnDefinition.GCUBEUsersModel();
		grid = new Grid<DataObj>(store, cm) {};
		final GridSelectionModel<DataObj> sm = new GridSelectionModel<DataObj>(){};
		grid.setSelectionModel(sm);
		grid.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		
		VerticalLayoutContainer con = new VerticalLayoutContainer();
	    con.setBorders(true);	       
	    con.add(grid, new VerticalLayoutData(1, 1));
	    
	    this.setHeight(400);
		this.setBodyBorder(false);  
		this.add(con);
	}
	

	public void loadCollectionsDetails(ArrayList<GCUBEUser> result){
		List<DataObj> users = getUsers(result);  
		store.clear();
		store.addAll(users);
		store.commitChanges();
		this.show();
	}
	
	private List<DataObj> getUsers(ArrayList<GCUBEUser> result) {
		List<DataObj> list=new ArrayList<DataObj>();		
	
		for (int i = 0; i < result.size(); i++) {
			int index = -1;
			DataObj dataObj=new DataObj();
			dataObj.setName(result.get(i).getName());
			String temp =result.get(i).getVre().substring(1);
			if ((index = temp.indexOf("/")) != -1)				
				dataObj.setVre(temp.substring(index));
			else dataObj.setVre(result.get(i).getVre()); 
			
			list.add(dataObj);
		}
		
		return list;
	}
	
}
