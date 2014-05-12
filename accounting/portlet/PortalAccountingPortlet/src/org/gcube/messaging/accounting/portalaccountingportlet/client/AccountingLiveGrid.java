package org.gcube.messaging.accounting.portalaccountingportlet.client;


import org.gcube.messaging.accounting.portalaccountingportlet.client.AccountingCostants.EntryType;
import org.gcube.portlets.widgets.gcubelivegrid.client.data.BufferedStore;
import org.gcube.portlets.widgets.gcubelivegrid.client.livegrid.BufferedGridPanel;
import org.gcube.portlets.widgets.gcubelivegrid.client.livegrid.BufferedGridToolbar;
import org.gcube.portlets.widgets.gcubelivegrid.client.livegrid.BufferedGridView;

import com.gwtext.client.core.Function;
import com.gwtext.client.data.Record;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.event.PanelListenerAdapter;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.RowSelectionModel;
import com.gwtext.client.widgets.grid.event.RowSelectionListenerAdapter;
import com.gwtext.client.widgets.layout.AnchorLayout;



/**
 * 
 * @author Andrea Manzi (CERN)
 *
 */
public class AccountingLiveGrid extends BufferedGridPanel {

	protected BufferedStore store = null;
	protected ColumnModel colmodel = null;
	

	final RowSelectionModel sm = new RowSelectionModel(true);

	public AccountingLiveGrid() {
		
		this.setTitle(getGridTitle());
		this.setFrame(true);		
		this.setLayout(new AnchorLayout());
		this.setPaddings(0);		

		BufferedGridView view = new BufferedGridView();

		view.setLoadMask("Wait ...");
		view.setNearLimit(300);   
		view.setForceFit(true);


		sm.addListener(new RowSelectionListenerAdapter() {  
			public void onRowSelect(RowSelectionModel sm, int rowIndex, Record record) {  
				Record tmp = record.copy();
				String temp =  tmp.getAsString("vre").substring(1);
				int index = -1;
				if ((index = temp.indexOf("/")) != -1)
					tmp.set("vre", temp.substring(index));
				AccountingUI.get().accountingDetail.formPanel.getForm().loadRecord(tmp);
				AccountingUI.get().accountingDetail.loadPanel(tmp);
				if (GlobalInfo.getSelectedRecord().compareTo(EntryType.SimpleSearchRecord)==0 ||
						GlobalInfo.getSelectedRecord().compareTo(EntryType.AdvancedSearchRecord)==0 ||
						GlobalInfo.getSelectedRecord().compareTo(EntryType.BrowseRecord)==0 )
					AccountingUI.service.getCollections(record.getAsString("Id"), Callbacks.listCollectionsDetailsCallback);
				if (GlobalInfo.getSelectedRecord().compareTo(EntryType.AdvancedSearchRecord)==0)
					AccountingUI.service.getTerms(record.getAsString("Id"), Callbacks.listTermsDetailsCallback);
				if (GlobalInfo.getSelectedRecord().compareTo(EntryType.ContentRecord)==0 )
					AccountingUI.service.getContent(record.getAsString("Id"), Callbacks.listContentDetailsCallback);
				if (GlobalInfo.getSelectedRecord().compareTo(EntryType.HLRecord)==0 && 
						record.getAsString("SUBTYPE").compareTo(AccountingCostants.HLSubTypeSent)== 0)
					AccountingUI.service.getAddresseesGCUBEUsers(record.getAsString("Id"), Callbacks.lisGCUBEAdreesseesCallback);
				if (GlobalInfo.getSelectedRecord().compareTo(EntryType.AnnotationRecord)==0 )
					AccountingUI.service.getContent(record.getAsString("Id"), Callbacks.listContentDetailsCallback);
				if (GlobalInfo.getSelectedRecord().compareTo(EntryType.WebAppRecord)==0 ) {
					AccountingUI.service.getGHNDetail(record.getAsString("Id"), Callbacks.getGHNDetailsCallback);
					AccountingUI.service.getWebAppDetail(record.getAsString("Id"), Callbacks.getWebAppDetailsCallback);
				}
				
			}  
		});  

		this.setSelectionModel(sm);  

		this.doOnRender(new Function() {  
			public void execute() {  
				sm.selectFirstRow();  
			}  
		}, 10);  

  
		//retrieving info store
		store = Stores.getStore(GlobalInfo.getSelectedRecord(),GlobalInfo.getSelectedUSer(),GlobalInfo.getDates());
		BufferedGridToolbar toolbar = new BufferedGridToolbar(view);
		toolbar.setDisplayInfo(true);
		//retrieving info for column mode
		colmodel =ColumnDefinition.ColumnModel();
		this.setStore(store);
		this.getStore().setRemoteSort(true);
		this.setColumnModel(colmodel);
		this.setEnableDragDrop(false);
		this.setView(view);
		this.setTopToolbar(toolbar);
		this.setHeight(400);
		this.getView().setAutoFill(true);      
		this.setStripeRows(true);
		this.setAutoScroll(true);		
		this.setFrame(true);
		this.setTrackMouseOver(true);

		this.addListener(new PanelListenerAdapter(){
			public void onShow(Component component) {
				super.onShow(component);
				store.reload();
			}
		});

	}

	private String getGridTitle(){
		return "Showing Records "+
		((GlobalInfo.getSelectedRecord().compareTo(AccountingCostants.EntryType.Empty)==0?"":" of type "+GlobalInfo.getSelectedRecord()))+
		((GlobalInfo.getSelectedUSer().compareTo("")==0?"":" for User "+GlobalInfo.getSelectedUSer()))+
		((GlobalInfo.getStartdate()== "")?"":" from Date "+GlobalInfo.getStartdate())+
		((GlobalInfo.getEnddate()== "")?"":" to Date "+GlobalInfo.getEnddate());
	}
	
	public void updateGrid() {
		
		this.setTitle(getGridTitle());
		store = Stores.getStore(GlobalInfo.getSelectedRecord(),
				GlobalInfo.getSelectedUSer(),GlobalInfo.getDates());
		
		colmodel = ColumnDefinition.ColumnModel();  
		colmodel.setDefaultSortable(true);		 
		this.reconfigure(store, colmodel);
		this.show();
	}

}
