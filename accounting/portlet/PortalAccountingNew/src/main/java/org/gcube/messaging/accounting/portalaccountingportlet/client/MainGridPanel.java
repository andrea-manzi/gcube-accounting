package org.gcube.messaging.accounting.portalaccountingportlet.client;


import java.util.List;

import org.gcube.messaging.accounting.portalaccountingportlet.client.AccountingCostants.EntryType;
import org.gcube.messaging.accounting.portalaccountingportlet.client.obj.DataObj;
import org.gcube.messaging.accounting.portalaccountingportlet.client.obj.DataObjProperties;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.core.client.resources.ThemeStyles;
import com.sencha.gxt.data.client.loader.RpcProxy;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.CellDoubleClickEvent;
import com.sencha.gxt.widget.core.client.event.CellDoubleClickEvent.CellDoubleClickHandler;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridSelectionModel;
import com.sencha.gxt.widget.core.client.grid.LiveGridView;
import com.sencha.gxt.widget.core.client.grid.LiveToolItem;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;




public class MainGridPanel extends ContentPanel {

	private PagingLoader<PagingLoadConfig, PagingLoadResult<DataObj>> gridLoader ;
	private LiveGridView<DataObj> liveGridView;
	private ListStore<DataObj> store;
	public int HEIGHT=400;

	List<String> types; //record type
	List<String> users;
	List<String> vres;	
	String dateStart;
	String dateEnd;

	public MainGridPanel() {
		//itemDriver.initialize(AccountingUI.get().accountingDetail.formPanel);

		configureParam();
		RpcProxy<PagingLoadConfig, PagingLoadResult<DataObj>> proxy = new RpcProxy<PagingLoadConfig, PagingLoadResult<DataObj>>() {
			@Override
			public void load(PagingLoadConfig loadConfig, AsyncCallback<PagingLoadResult<DataObj>> callback) {
				AccountingUI.service.getRecordsForLiveGrid(loadConfig, types, users,vres, new String[] {dateStart,dateEnd}, callback);
			}
		};
		DataObjProperties props = GWT.create(DataObjProperties.class);
		store = new ListStore<DataObj>(props.key());

		gridLoader = new PagingLoader<PagingLoadConfig, PagingLoadResult<DataObj>>(proxy);
		gridLoader.setRemoteSort(true);

		liveGridView = new LiveGridView<DataObj>();
		liveGridView.setForceFit(true);

		ColumnModel<DataObj> cm = ColumnDefinition.MainModel();

		final Grid<DataObj> grid = new Grid<DataObj>(store, cm) {
			@Override
			protected void onAfterFirstAttach() {
				super.onAfterFirstAttach();
				Scheduler.get().scheduleDeferred(new ScheduledCommand() {
					public void execute() {
						gridLoader.load(0, liveGridView.getCacheSize());
					}
				});
			}
		};
		final GridSelectionModel<DataObj> sm = new GridSelectionModel<DataObj>(){};
		grid.setSelectionModel(sm);
		grid.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		grid.addCellDoubleClickHandler(new CellDoubleClickHandler() {			
			public void onCellClick(CellDoubleClickEvent event) {
				//grid.getSelectionModel().getSelectedItem()
				DataObj record = grid.getSelectionModel().getSelectedItem();
				GlobalInfo.setSelectedRecord(EntryType.valueOf(record.getType()));
				String vre =  record.getVre().substring(1);
				int index = -1;
				if ((index = vre.indexOf("/")) != -1)	record.setVre(vre.substring(index));
								
				//the calls for the detail panels
				if(record.getType().compareTo(EntryType.BrowseRecord.type)==0 ){
					//collections call..
					AccountingUI.service.getCollections(record.getId(), Callbacks.listCollectionsDetailsCallback);
					//browse call .. 
					AccountingUI.service.getDetails(record.getId(), EntryType.valueOf(record.getType()), 
							Callbacks.getDetailsCallback);
				} else
					if(	record.getType().compareTo(EntryType.AdvancedSearchRecord.type)==0){
					//collections call..
					AccountingUI.service.getCollections(record.getId(), Callbacks.listCollectionsDetailsCallback);
					//terms call
					AccountingUI.service.getTerms(record.getId(), Callbacks.listTermsDetailsCallback);
					//AdvancedSearchRecord call .. 
					AccountingUI.service.getDetails(record.getId(), EntryType.valueOf(record.getType()), 
							Callbacks.getDetailsCallback);
				} else
					if (record.getType().compareTo(EntryType.SimpleSearchRecord.type)==0){
					//collection call
					AccountingUI.service.getCollections(record.getId(), Callbacks.listCollectionsDetailsCallback);
					//SimpleSearchRecord call .. 
					AccountingUI.service.getDetails(record.getId(), EntryType.valueOf(record.getType()), 
							Callbacks.getDetailsCallback);
				} else				
					if (record.getType().compareTo(EntryType.ContentRecord.type)==0 ){
					//contentRecord call
					AccountingUI.service.getContent(record.getId(), Callbacks.listContentDetailsCallback);
					AccountingUI.get().accountingDetail.loadPanel(record);
				} else
					if (record.getType().compareTo(EntryType.AnnotationRecord.type)==0 ){
						//annotation call
						AccountingUI.service.getDetails(record.getId(), EntryType.valueOf(record.getType()), 
								Callbacks.getDetailsCallback);
						//content call
						AccountingUI.service.getContent(record.getId(), Callbacks.listContentDetailsCallback);
				}
				else if (record.getType().compareTo(EntryType.WebAppRecord.type)==0 ) {
						//GHN call
						AccountingUI.service.getGHNDetail(record.getId(), Callbacks.getGHNDetailsCallback);
						//web app call
						AccountingUI.service.getWebAppDetail(record.getId(), Callbacks.getWebAppDetailsCallback);
						AccountingUI.get().accountingDetail.loadPanel(record); 
				} else {
					//GoogleSearchRecord
					AccountingUI.service.getDetails(record.getId(), EntryType.valueOf(record.getType()), 
							Callbacks.getDetailsCallback);
				}
	
			}
		});
		grid.setView(liveGridView);
		grid.getView().setForceFit(true);
		grid.setLoadMask(true);
		grid.setLoader(gridLoader);

		ToolBar toolBar = new ToolBar();
		toolBar.add(new LiveToolItem(grid));
		toolBar.addStyleName(ThemeStyles.getStyle().borderTop());
		toolBar.getElement().getStyle().setProperty("borderBottom", "none");

		final VerticalLayoutContainer con = new VerticalLayoutContainer();
		con.setBorders(true);
		con.setScrollMode(ScrollMode.AUTOX);
		ToolBar empty = new ToolBar();
		empty.setHeight(20);
		con.add(grid, new VerticalLayoutData(1, 1));
		con.add(toolBar, new VerticalLayoutData(1, -1));
		con.add(empty, new VerticalLayoutData(1, -1));

		this.setHeight(HEIGHT);
		Timer takingHeaderTimer=new Timer(){
			@Override
			public void run(){
				AccountingUI.get().accountingUI.grid.setTitle(getGridTitle());
				AccountingUI.get().accountingUI.grid.setHeadingText(getGridTitle());
			}
		};
		takingHeaderTimer.schedule(2000);
		
		this.setBodyBorder(false);  
		this.setWidget(con);
	}

	public String getGridTitle(){
		return ("Records for selected filters" +
				((GlobalInfo.getStartdate()== "")?"":" from Date "+GlobalInfo.getStartdate())+
				((GlobalInfo.getEnddate()== "")?"":" to Date "+GlobalInfo.getEnddate())
				);
	}
	public void configureParam(){
		users=GlobalInfo.getSelectedUsers();
		types=GlobalInfo.getSelectedTypes();
		vres=GlobalInfo.getSelectedVres();
		dateStart=GlobalInfo.getStartdate();
		dateEnd=GlobalInfo.getEnddate();		
	}

	public void updateGrid() {
		store.clear();
		configureParam();
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			public void execute() {
				gridLoader.load(0, liveGridView.getCacheSize());
			}
		});
		this.setHeadingText(getGridTitle());
		this.show();
	}

}
