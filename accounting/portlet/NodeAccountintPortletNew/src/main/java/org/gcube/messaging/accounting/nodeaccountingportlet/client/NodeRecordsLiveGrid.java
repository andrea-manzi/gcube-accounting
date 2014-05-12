package org.gcube.messaging.accounting.nodeaccountingportlet.client;


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
import com.gwtext.client.widgets.layout.FitLayout;



/**
 * 
 * @author Andrea Manzi (CERN)
 *
 */
public class NodeRecordsLiveGrid extends BufferedGridPanel {

	protected BufferedStore store = null;
	protected ColumnModel colmodel = null;
	

	final RowSelectionModel sm = new RowSelectionModel(true);

	public NodeRecordsLiveGrid() {
		
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
				String temp =  tmp.getAsString("callerScope").substring(1);
				int index = -1;
				if ((index = temp.indexOf("/")) != -1)
					tmp.set("callerScope", temp.substring(index));
		}  
		});  

		this.setSelectionModel(sm);  

		this.doOnRender(new Function() {  
			public void execute() {  
				sm.selectFirstRow();  
			}  
		}, 10);  

  
		//retrieving info store
		store = Stores.getStore(GlobalInfo.get());
		BufferedGridToolbar toolbar = new BufferedGridToolbar(view);
		toolbar.setDisplayInfo(true);
		//retrieving info for column mode
		colmodel =ColumnDefinition.ColumnModel();
		this.setStore(store);
		this.getStore().setRemoteSort(true);
		this.setColumnModel(colmodel);
		this.setEnableDragDrop(false);
		this.setView(view);
		this.setAutoWidth(true);
		this.setTopToolbar(toolbar);
		this.setHeight(400);
		this.getView().setAutoFill(true);      
		this.setStripeRows(true);
		this.setAutoScroll(true);		
		this.setFrame(true);
		this.setTrackMouseOver(true);
		this.setAutoExpandColumn(0);

		this.addListener(new PanelListenerAdapter(){
			public void onShow(Component component) {
				super.onShow(component);
				store.reload();
			}
		});

	}

	private String getGridTitle(){
		return GlobalInfo.get().getGridTitle();
		}
	
	public void updateGrid() {
		
		this.setTitle(getGridTitle());
		store = Stores.getStore(GlobalInfo.get());
		colmodel = ColumnDefinition.ColumnModel();  
		colmodel.setDefaultSortable(true);		 
		this.reconfigure(store, colmodel);
		this.show();
	}

}
