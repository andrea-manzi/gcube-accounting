package org.gcube.messaging.accounting.portalaccountingportlet.client.detailpanel;

import java.util.ArrayList;
import java.util.List;

import org.gcube.messaging.accounting.portalaccountingportlet.client.AccountingCostants;
import org.gcube.messaging.accounting.portalaccountingportlet.client.ColumnDefinition;
import org.gcube.messaging.accounting.portalaccountingportlet.client.obj.CollectionPair;
import org.gcube.messaging.accounting.portalaccountingportlet.client.obj.DataObj;
import org.gcube.messaging.accounting.portalaccountingportlet.client.obj.DataObjProperties;

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

public class CollectionPanel extends ContentPanel{
	
	private Grid<DataObj> grid;
	private ListStore<DataObj> store;
	private TextField operator;
	private FieldLabel operatorLabel;
	private static CollectionPanel singleton;
	
	public static CollectionPanel get(){
		return singleton;
	}
	
	public CollectionPanel(){
		singleton=this;
		this.setId(AccountingCostants.CollectionPanelID);
		DataObjProperties props = GWT.create(DataObjProperties.class);
		store = new ListStore<DataObj>(props.key());
		
		ColumnModel<DataObj> cm = ColumnDefinition.CollectionModel();
		grid = new Grid<DataObj>(store, cm) {};
		final GridSelectionModel<DataObj> sm = new GridSelectionModel<DataObj>(){};
		grid.setSelectionModel(sm);
		grid.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		
		VerticalLayoutContainer con = new VerticalLayoutContainer();
	    con.setBorders(true);
	    
	    operator = new TextField();
		operatorLabel=new FieldLabel(operator, "Operator");
	    con.add(operatorLabel, new VerticalLayoutData(1, -1));
	    operator.hide();
	    operatorLabel.hide();
	    
	    con.add(grid, new VerticalLayoutData(1, -1));
	    
	    this.setHeight(400);
		this.setBodyBorder(false);  
		this.add(con);
	}
	

	public void loadCollectionsDetails(ArrayList<CollectionPair> result){
		List<DataObj> collections = getCollections(result);  
		store.clear();
		store.addAll(collections);
		store.commitChanges();
		this.show();
	}
	
	private List<DataObj> getCollections(ArrayList<CollectionPair> result){
		List<DataObj> list=new ArrayList<DataObj>();		
		for (int i =0 ;i< result.size();i++){
			DataObj dataObj=new DataObj();
			dataObj.setId(result.get(i).getId());
			dataObj.setName(result.get(i).getName());
			list.add(dataObj);
		}
		return list;
	}
	
	public void addOperator(String nameOperator){
		operator.setValue(nameOperator);
		operator.show();
		operatorLabel.show();
	}
	public void removeOperator(){
		operator.hide();
		operatorLabel.hide();
	}
	
}
