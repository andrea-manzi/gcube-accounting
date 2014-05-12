package org.gcube.messaging.accounting.portalaccountingportlet.client;


import java.util.ArrayList;
import java.util.List;

import org.gcube.messaging.accounting.portalaccountingportlet.client.AccountingCostants.EntryType;
import org.gcube.messaging.accounting.portalaccountingportlet.client.obj.GroupObj;
import org.gcube.messaging.accounting.portalaccountingportlet.client.obj.GroupObjProperties;
import org.gcube.messaging.accounting.portalaccountingportlet.client.obj.RecordType;
import org.gcube.messaging.accounting.portalaccountingportlet.client.obj.RecordTypeProperties;
import org.gcube.messaging.accounting.portalaccountingportlet.client.obj.User;
import org.gcube.messaging.accounting.portalaccountingportlet.client.obj.Vre;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Event;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.data.shared.IconProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutPack;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.CellClickEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.CellClickEvent.CellClickHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.grid.CheckBoxSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.ColumnHeader.Group;
import com.sencha.gxt.widget.core.client.info.Info;
import com.sencha.gxt.widget.core.client.menu.Item;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import com.sencha.gxt.widget.core.client.tree.Tree;

public class PopUpSelectionGroupPanel extends ContentPanel{
	private static int WIDTH=250;	
	private static int HEIGHT=250;
	static PopUpSelectionGroupPanel singleton;
	
	public Grid<GroupObj> grid;
	public ListStore<GroupObj> store;
	public ColumnModel<GroupObj> cm;
	public CheckBoxSelectionModel<GroupObj> sm;

	
	//TreeStore<GroupObj> groupStore;
	GroupObjProperties props = GWT.create(GroupObjProperties.class);
	//Tree<GroupObj, String> tree;
	
	public PopUpSelectionGroupPanel(String title)  {
		super();		
		singleton=this;
		this.setTitle(title);  
		this.setAnimCollapse(false);
		this.setHeadingText(title);
		this.setPixelSize(WIDTH,HEIGHT);
		
		store = new ListStore<GroupObj>(props.key());	
		IdentityValueProvider<GroupObj> identity = new IdentityValueProvider<GroupObj>();
		sm = new CheckBoxSelectionModel<GroupObj>(identity);

		cm = ColumnDefinition.GroupObjModel(sm);
		grid = new Grid<GroupObj>(store, cm) {};	 

		grid.setSelectionModel(sm);
		grid.getSelectionModel().setSelectionMode(SelectionMode.MULTI);
		grid.addCellClickHandler(new CellClickHandler() {			
			public void onCellClick(CellClickEvent event) {
				List<String> selectedGroups=new ArrayList<String>();
				for(GroupObj tmp: sm.getSelectedItems()){
					selectedGroups.add(tmp.getName());
				}
				GlobalInfo.setSelectedGroupsBy(selectedGroups);
			}
		});
		
		//tool button
		ToolButton clear = new ToolButton("clear");
		clear.addSelectHandler(new SelectHandler() {
			public void onSelect(SelectEvent event) {
				GlobalInfo.setSelectedGroupsBy(new ArrayList<String>());	
				sm.deselectAll();
			}
		});
		clear.setWidth("50px");
		clear.getElement().setInnerText("clear");
		
		FramedPanel fr = new FramedPanel();
		fr.setBodyStyle("background: white; padding: 5px");
		fr.setWidget(grid);
		fr.setHeaderVisible(false);
		fr.setButtonAlign(BoxLayoutPack.START);
		TextButton okButton=new TextButton("OK");
		okButton.setBorders(true);
		okButton.addSelectHandler(new SelectHandler() {
			public void onSelect(SelectEvent event) {
				AccountingUI.get().dialogBoxGen.hide();
				if(GlobalInfo.getSelectedGroupsBy().size()>0){
					AccountingUI.get().accountingUI.statistics.statisticsButton.enable();
				}
				else{
					AccountingUI.get().accountingUI.statistics.statisticsButton.disable();
				}
			}
		});
		fr.addButton(okButton);
		
	    
		this.getHeader().addTool(clear);		
		this.setWidget(fr);
		this.update();
	}

	public static PopUpSelectionGroupPanel get() {
		return singleton;
	}

	public static void setSingleton(PopUpSelectionGroupPanel singleton) {
		PopUpSelectionGroupPanel.singleton = singleton;
	}

	public void update(){
		if(store.getAll().size()==0){
		List<GroupObj> list=new ArrayList<GroupObj>();
		GroupObj gr=new GroupObj();
		gr.setName("user");
		GroupObj gr2=new GroupObj();
		gr2.setName("type");
		GroupObj gr3=new GroupObj();
		gr3.setName("vre");
		GroupObj gr4=new GroupObj();
		gr4.setName("date");
		list.add(gr);
		list.add(gr2);
		list.add(gr3);
		list.add(gr4);
		this.store.addAll(list);
		this.setHeadingText("Groups : " + list.size());		
		}
	}
}



