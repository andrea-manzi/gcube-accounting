package org.gcube.messaging.accounting.portalaccountingportlet.client;

import java.util.ArrayList;
import java.util.List;

import org.gcube.messaging.accounting.portalaccountingportlet.client.obj.User;
import org.gcube.messaging.accounting.portalaccountingportlet.client.obj.UserProperties;

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

public class FiltUserPanel extends ContentPanel{
	private static int WIDTH=220;	
	private static int HEIGHT=150;
	private static String title="Portal Users";
	static FiltUserPanel singleton;
	UserProperties props = GWT.create(UserProperties.class);

	public Grid<User> grid;
	public ListStore<User> store;
	public ColumnModel<User> cm;

	public CheckBoxSelectionModel<User> sm;

	public FiltUserPanel(){
		super();		
		singleton = this;
		this.setTitle(title);  
		this.setAnimCollapse(false);
		this.setHeadingText(title);
		this.setPixelSize(WIDTH,HEIGHT);

		store = new ListStore<User>(props.key());	
		IdentityValueProvider<User> identity = new IdentityValueProvider<User>();
		sm = new CheckBoxSelectionModel<User>(identity);

		cm = ColumnDefinition.UserModel(sm);
		grid = new Grid<User>(store, cm) {};	 

		grid.setSelectionModel(sm);
		grid.getSelectionModel().setSelectionMode(SelectionMode.MULTI);
		grid.addCellClickHandler(new CellClickHandler() {			
			public void onCellClick(CellClickEvent event) {
				List<String> selectedUsers=new ArrayList<String>();
				for(User tmp: sm.getSelectedItems()){
					selectedUsers.add(tmp.getName());
				}				
				GlobalInfo.setSelectedUsers(selectedUsers);
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
				GlobalInfo.setSelectedUsers(new ArrayList<String>());		
				sm.deselectAll();
			}
		});
		clear.setWidth("50px");
		clear.getElement().setInnerText("clear");

		this.getHeader().addTool(clear);
		this.setWidget(con);
		
		AccountingUI.service.getUsers(Callbacks.listUsersCallback);
	}


	public void load(List<User> results){
		store.clear();
		store.addAll(results);
		store.commitChanges();
		cm = ColumnDefinition.UserModel(sm);
		grid.reconfigure(store, cm);
		this.show();
	}

	public void update(ArrayList<ArrayList<String>> users) {
		List<User> list=new ArrayList<User>();
		for (int i = 1;i< users.size();i++){
			for (String user :users.get(i)) {
				User u=new User();
				u.setName(user);
				list.add(u);
			}
		} 
		this.load(list);
		this.setHeadingText("Portal Users : " + (users.size()-1));	
	}

	public static FiltUserPanel get() {
		return singleton;
	}
}
