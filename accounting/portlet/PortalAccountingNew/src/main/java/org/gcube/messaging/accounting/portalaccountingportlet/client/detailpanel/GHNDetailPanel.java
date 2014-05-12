package org.gcube.messaging.accounting.portalaccountingportlet.client.detailpanel;

import java.util.ArrayList;
import java.util.List;

import org.gcube.messaging.accounting.portalaccountingportlet.client.AccountingCostants;
import org.gcube.messaging.accounting.portalaccountingportlet.client.ColumnDefinition;
import org.gcube.messaging.accounting.portalaccountingportlet.client.obj.CollectionPair;
import org.gcube.messaging.accounting.portalaccountingportlet.client.obj.DataObj;
import org.gcube.messaging.accounting.portalaccountingportlet.client.obj.DataObjProperties;
import org.gcube.messaging.accounting.portalaccountingportlet.client.obj.GCUBEUser;
import org.gcube.messaging.accounting.portalaccountingportlet.client.obj.GHNDetail;

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

public class GHNDetailPanel extends ContentPanel{
	
	private Grid<DataObj> grid;
	private ListStore<DataObj> store;
	private static GHNDetailPanel singleton;
	
	public static GHNDetailPanel get(){
		return singleton;
	}
	
	public GHNDetailPanel(){
		singleton=this;
		this.setTitle("GHN Details");
		this.setWidth(280);
		this.setHeight(400);
		this.setId(AccountingCostants.GHNDetailPanelId);
		
		DataObjProperties props = GWT.create(DataObjProperties.class);
		store = new ListStore<DataObj>(props.key());
		
		ColumnModel<DataObj> cm = ColumnDefinition.GHNDetailModel();
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
	

	public void loadCollectionsDetails(ArrayList<GHNDetail> result){
		List<DataObj> ghns = getGHNS(result);  
		store.clear();
		store.addAll(ghns);
		store.commitChanges();
		this.show();
	}
	
	private List<DataObj> getGHNS (ArrayList<GHNDetail>  result) {
		List<DataObj> list=new ArrayList<DataObj>();		
			
		for (int i =0 ;i< result.size();i++){
			DataObj dataObj=new DataObj();
			dataObj.setGhnid(result.get(i).getGhnID());
			dataObj.setGhnname(result.get(i).getGhnName());
			list.add(dataObj);
		}	
		return list;
	}
	
}
