package org.gcube.messaging.accounting.portalaccountingportlet.client;

import java.util.ArrayList;
import java.util.List;

import org.gcube.messaging.accounting.portalaccountingportlet.client.obj.RecordType;
import org.gcube.messaging.accounting.portalaccountingportlet.client.obj.RecordTypeProperties;

import com.google.gwt.core.client.GWT;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.CellClickEvent;
import com.sencha.gxt.widget.core.client.event.CellClickEvent.CellClickHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.grid.CheckBoxSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.info.Info;

public class FiltTypePanel extends ContentPanel{
	private static int WIDTH=220;	
	private static int HEIGHT=150;
	private static String title="Record Types";
	static FiltTypePanel singleton;
	RecordTypeProperties props = GWT.create(RecordTypeProperties.class);

	public Grid<RecordType> grid;
	public ListStore<RecordType> store;
	public ColumnModel<RecordType> cm;

	public CheckBoxSelectionModel<RecordType> sm;

	public FiltTypePanel(){
		super();		
		singleton = this;
		this.setTitle(title);  
		this.setAnimCollapse(false);
		this.setHeadingText(title);
		this.setPixelSize(WIDTH,HEIGHT);

		store = new ListStore<RecordType>(props.key());	
		IdentityValueProvider<RecordType> identity = new IdentityValueProvider<RecordType>();
		sm = new CheckBoxSelectionModel<RecordType>(identity);

		cm = ColumnDefinition.TypeModel(sm);
		grid = new Grid<RecordType>(store, cm) {};	 

		grid.setSelectionModel(sm);
		grid.getSelectionModel().setSelectionMode(SelectionMode.MULTI);
		grid.addCellClickHandler(new CellClickHandler() {			
			public void onCellClick(CellClickEvent event) {
				List<String> selectedRecordTypes=new ArrayList<String>();
				for(RecordType tmp: sm.getSelectedItems()){
					selectedRecordTypes.add(tmp.getName());
				}				
				GlobalInfo.setSelectedTypes(selectedRecordTypes);
			}
		});

		final VerticalLayoutContainer con = new VerticalLayoutContainer();
		con.setBorders(true);
		con.setScrollMode(ScrollMode.AUTOX);
		con.add(grid, new VerticalLayoutData(1, 1));		

		//tool button
		ToolButton clear = new ToolButton("clear");
		clear.addSelectHandler(new SelectHandler() {
			public void onSelect(SelectEvent event) {
				GlobalInfo.setSelectedTypes(new ArrayList<String>());		
				sm.deselectAll();
			}
		});
		clear.setWidth("50px");
		clear.getElement().setInnerText("clear");

		this.getHeader().addTool(clear);
		this.setWidget(con);
		
		AccountingUI.service.getRecordTypes(Callbacks.listRecordTypesCallback);
	}


	public void load(List<RecordType> results){
		store.clear();
		store.addAll(results);
		store.commitChanges();
		cm = ColumnDefinition.TypeModel(sm);
		grid.reconfigure(store, cm);
		this.show();
	}

	public void update(ArrayList<ArrayList<String>> RecordTypes) {
		List<RecordType> list=new ArrayList<RecordType>();
		for (int i = 1;i< RecordTypes.size();i++){
			for (String RecordType :RecordTypes.get(i)) {
				RecordType u=new RecordType();
				u.setName(RecordType);
				list.add(u);
			}
		} 
		this.load(list);
		this.setHeadingText("Record Types : " + (RecordTypes.size()-1));	
	}

	public static FiltTypePanel get() {
		return singleton;
	}
}
