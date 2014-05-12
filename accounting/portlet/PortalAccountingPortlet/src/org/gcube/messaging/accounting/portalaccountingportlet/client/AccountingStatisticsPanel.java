package org.gcube.messaging.accounting.portalaccountingportlet.client;

import java.util.ArrayList;

import org.gcube.portlets.widgets.gcubelivegrid.client.data.BufferedStore;
import org.gcube.portlets.widgets.gcubelivegrid.client.livegrid.BufferedGridPanel;
import org.gcube.portlets.widgets.gcubelivegrid.client.livegrid.BufferedGridView;

import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.SimpleStore;
import com.gwtext.client.data.Store;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.event.PanelListenerAdapter;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.event.ComboBoxListenerAdapter;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.layout.HorizontalLayout;

/**
 * 
 * @author Andrea Manzi (CERN)
 *
 */
public class AccountingStatisticsPanel extends Panel
{
	protected static BufferedStore store = null;
	BufferedGridPanel grid = new BufferedGridPanel();  

	public AccountingStatisticsPanel() 
	{ 

		this.setHeight(400);
		this.setCollapsible(true);
		this.setTitle("Statistics");
		store = Stores.getStatisticStore(GlobalInfo.getSelectedRecord(), GlobalInfo.getSelectedUSer(), "vre",GlobalInfo.getDates());
		BufferedGridView view = new BufferedGridView();
		grid.setLayout(new FitLayout());
		view.setLoadMask("Wait ...");
		view.setNearLimit(300);   
		view.setForceFit(true);

		grid.setEnableDragDrop(false);
		grid.setView(view);

		grid.setStripeRows(true);
		grid.setAutoScroll(true);		
		grid.setFrame(true);
		grid.setTrackMouseOver(true);   
		grid.setStore(store);
		grid.getStore().setRemoteSort(false);
		grid.setColumnModel(ColumnDefinition.GetStatisticModel(AccountingCostants.DefaultGrouping)); 
		grid.setHeight(300);
		grid.getView().setAutoFill(true);
		grid.getStore().setRemoteSort(true);
		grid.addListener(new PanelListenerAdapter(){
			public void onShow(Component component) {
				super.onShow(component);
				store.reload();
			}
		});


		Panel upperSection=new Panel();
		upperSection.setLayout(new HorizontalLayout(5));
		Button statisticButton = new Button();
		statisticButton.setText("Create Statistics");
		statisticButton.setTooltip("Create Statistic");
		statisticButton.setPressed(false);
		statisticButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e){
				super.onClick(button, e);
				if (GlobalInfo.getGrouping().compareTo("")==0){
					AccountingUI.get().notifyError("Please Select a grouping option");
				}
				AccountingUI.get().accountingUI.statistics.updateGrid();
			}
		});


		//create a Store using local array data  
		final Store store = new SimpleStore(new String[]{"filter"}, new String[][]{
				new String[]{"user"},
				new String[]{"vre"},
				new String[]{"type"},
				new String[]{"date"}
		} );  
		store.load();  

		final ComboBox cb = new ComboBox();  
		cb.setForceSelection(true);  
		cb.setMinChars(1);  
		cb.setFieldLabel("Filter");  
		cb.setStore(store);  
		cb.setDisplayField("filter");  
		cb.setMode(ComboBox.LOCAL);  
		cb.setTriggerAction(ComboBox.ALL);  
		cb.setEmptyText("Enter group");  
		cb.setLoadingText("Creating...");  
		cb.setTypeAhead(true);  
		cb.setSelectOnFocus(true);  
		cb.setWidth(200);  

		cb.setHideTrigger(false);  

		cb.addListener(new ComboBoxListenerAdapter() {  
			public boolean doBeforeSelect(ComboBox comboBox, Record record, int index) {  
				GlobalInfo.setGrouping(record.getAsString("filter"));
				return super.doBeforeSelect(comboBox, record, index);  
			} 
		});

		Button export = new Button("Export to CSV", new ButtonListenerAdapter() {

			public void onClick(Button button, EventObject e){
				super.onClick(button, e);
				if (AccountingStatisticsPanel.store.getTotalCount() == 0)
					AccountingUI.get().notifyError("No data to export");
				else {
					ArrayList<String[]> list = new ArrayList<String[]>();
					for (Record record : AccountingStatisticsPanel.store.getRecords())
					{
						String [] values = new String [record.getFields().length];
						for (int i = 0; i<values.length;i++){
							values[i] = record.getAsString(record.getFields()[i]);
						}
						list.add(values);
					}
					AccountingUI.get().service.export(list,"export" , Callbacks.exportCallback);     
				}
			}
		});
		
		Button chart = new Button("Create Chart", new ButtonListenerAdapter() {

			public void onClick(Button button, EventObject e){
				super.onClick(button, e);
				if (AccountingStatisticsPanel.store.getTotalCount() == 0)
					AccountingUI.get().notifyError("No data to display");
				else {
					String title = "Records " +
							(GlobalInfo.getSelectedRecord().compareTo(AccountingCostants.EntryType.Empty)==0?"":" for type "+GlobalInfo.getSelectedRecord())+
							(GlobalInfo.getSelectedUSer().compareTo("")==0?"":" for user "+GlobalInfo.getSelectedUSer()) +
							((GlobalInfo.getStartdate()== "")?"":" from Date "+GlobalInfo.getStartdate())+
							((GlobalInfo.getEnddate()== "")?"":" to Date "+GlobalInfo.getEnddate())+
							(GlobalInfo.getGrouping().compareTo("")==0?"":" grouped by "+GlobalInfo.getGrouping());
					
					AccountingChart chart = new AccountingChart(title,AccountingStatisticsPanel.store.getRecords());
					chart.showGraph();
				}
			}
		});

		upperSection.add(statisticButton);
		upperSection.add(cb);
		upperSection.add(export);
		upperSection.add(chart);
		this.add(upperSection);
		this.add(grid);
	}	
	public void updateGrid() {
		this.setTitle("Records " +
				((GlobalInfo.getSelectedRecord().compareTo(AccountingCostants.EntryType.Empty)==0?"":" for type "+GlobalInfo.getSelectedRecord()))+
				((GlobalInfo.getSelectedUSer().compareTo("")==0?"":" for user "+GlobalInfo.getSelectedUSer()))+
				((GlobalInfo.getStartdate()== "")?"":" from Date "+GlobalInfo.getStartdate())+
				((GlobalInfo.getEnddate()== "")?"":" to Date "+GlobalInfo.getEnddate())+
				(GlobalInfo.getGrouping().compareTo("")==0?"":" grouped by "+GlobalInfo.getGrouping()));
		store = Stores.getStatisticStore(GlobalInfo.getSelectedRecord(),
				GlobalInfo.getSelectedUSer(),GlobalInfo.getGrouping(),GlobalInfo.getDates());		 
		grid.reconfigure(store, ColumnDefinition.GetStatisticModel(GlobalInfo.getGrouping()));
		grid.show();
	}

}
