package org.gcube.messaging.accounting.portalaccountingportlet.client;

import java.util.ArrayList;
import java.util.List;

import org.gcube.messaging.accounting.portalaccountingportlet.client.obj.Vre;
import org.gcube.messaging.accounting.portalaccountingportlet.client.obj.VreProperties;

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

public class FiltVrePanel extends ContentPanel{
	private static int WIDTH=220;	
	private static int HEIGHT=150;
	private static String title="Portal Scopes";
	static FiltVrePanel singleton;
	VreProperties props = GWT.create(VreProperties.class);

	public Grid<Vre> grid;
	public ListStore<Vre> store;
	public ColumnModel<Vre> cm;

	public CheckBoxSelectionModel<Vre> sm;

	public FiltVrePanel(){
		super();		
		singleton = this;
		this.setTitle(title);  
		this.setAnimCollapse(false);
		this.setHeadingText(title);
		this.setPixelSize(WIDTH,HEIGHT);

		store = new ListStore<Vre>(props.key());	
		IdentityValueProvider<Vre> identity = new IdentityValueProvider<Vre>();
		sm = new CheckBoxSelectionModel<Vre>(identity);

		cm = ColumnDefinition.VreModel(sm);
		grid = new Grid<Vre>(store, cm) {};	 

		grid.setSelectionModel(sm);
		grid.getSelectionModel().setSelectionMode(SelectionMode.MULTI);
		grid.addCellClickHandler(new CellClickHandler() {			
			public void onCellClick(CellClickEvent event) {
				List<String> selectedVres=new ArrayList<String>();
				for(Vre tmp: sm.getSelectedItems()){
					selectedVres.add(tmp.getName());
				}				
				GlobalInfo.setSelectedVres(selectedVres);
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
				GlobalInfo.setSelectedVres(new ArrayList<String>());		
				sm.deselectAll();
			}
		});
		clear.setWidth("50px");
		clear.getElement().setInnerText("clear");

		this.getHeader().addTool(clear);
		this.setWidget(con);
		
		AccountingUI.service.getVres(Callbacks.listVresCallback);
	}


	public void load(List<Vre> results){
		store.clear();
		store.addAll(results);
		store.commitChanges();
		cm = ColumnDefinition.VreModel(sm);
		grid.reconfigure(store, cm);
		this.show();
	}

	public void update(ArrayList<ArrayList<String>> Vres) {
		List<Vre> list=new ArrayList<Vre>();
		for (int i = 1;i< Vres.size();i++){
			for (String Vre :Vres.get(i)) {
				Vre u=new Vre();
				u.setName(Vre);
				list.add(u);
			}
		} 
		this.load(list);
		this.setHeadingText("Portal Scopes : " + (Vres.size()-1));	
	}

	public static FiltVrePanel get() {
		return singleton;
	}
}
