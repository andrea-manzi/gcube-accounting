package org.gcube.messaging.accounting.portalaccountingportlet.client.detailpanel;

import java.util.ArrayList;
import java.util.List;

import org.gcube.messaging.accounting.portalaccountingportlet.client.AccountingCostants;
import org.gcube.messaging.accounting.portalaccountingportlet.client.AccountingUI;
import org.gcube.messaging.accounting.portalaccountingportlet.client.ColumnDefinition;
import org.gcube.messaging.accounting.portalaccountingportlet.client.obj.CollectionPair;
import org.gcube.messaging.accounting.portalaccountingportlet.client.obj.DataObj;
import org.gcube.messaging.accounting.portalaccountingportlet.client.obj.DataObjProperties;
import org.gcube.messaging.accounting.portalaccountingportlet.client.obj.GCUBEUser;
import org.gcube.messaging.accounting.portalaccountingportlet.client.obj.GHNDetail;
import org.gcube.messaging.accounting.portalaccountingportlet.client.obj.TermPair;

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

public class TermPanel extends ContentPanel{
	
	private Grid<DataObj> grid;
	private ListStore<DataObj> store;
	private static TermPanel singleton;
	
	public static TermPanel get(){
		return singleton;
	}
	
	public TermPanel(){
		singleton=this;
		this.setTitle("Terms");
		this.setWidth(280);
		this.setHeight(400);
		this.setId(AccountingCostants.TermsPanelId);
		
		DataObjProperties props = GWT.create(DataObjProperties.class);
		store = new ListStore<DataObj>(props.key());
		
		ColumnModel<DataObj> cm = ColumnDefinition.TermsModel2();
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
	

	public void loadCollectionsDetails(ArrayList<TermPair> result,boolean hideName){
		List<DataObj> terms = getTerms(result);  
		store.clear();
		store.addAll(terms);
		store.commitChanges();
		if(hideName){
			ColumnModel<DataObj> cm = ColumnDefinition.TermsModel1();
			grid.reconfigure(store, cm);
		}
		else{
			ColumnModel<DataObj> cm = ColumnDefinition.TermsModel2();
			grid.reconfigure(store, cm);
		}
		this.show();
	}
	
	private List<DataObj> getTerms (ArrayList<TermPair> result) {
		List<DataObj> list=new ArrayList<DataObj>();		

		for (int i =0 ;i< result.size();i++){
			DataObj dataObj = new DataObj();
			dataObj.setName(result.get(i).getTermName());
			dataObj.setValue(result.get(i).getTermValue());
			list.add(dataObj);
		}
		return list;
	}
	
}
