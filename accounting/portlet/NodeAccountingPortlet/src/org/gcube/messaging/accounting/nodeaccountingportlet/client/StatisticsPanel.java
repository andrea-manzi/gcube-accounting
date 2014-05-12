package org.gcube.messaging.accounting.nodeaccountingportlet.client;

import java.util.ArrayList;

import org.gcube.messaging.accounting.nodeaccountingportlet.charts.BarChart;
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
import com.gwtext.client.widgets.layout.AnchorLayout;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.layout.HorizontalLayout;

/**
 * 
 * @author Andrea Manzi (CERN)
 *
 */
public class StatisticsPanel extends Panel
{
	protected static BufferedStore store = null;
	BufferedGridPanel grid = new BufferedGridPanel();  

	public StatisticsPanel() 
	{ 

		this.setHeight(400);
		this.setCollapsible(true);
		this.setTitle("Statistics");
		store = Stores.getStatisticStore(GlobalInfo.get());
		BufferedGridView view = new BufferedGridView();
		grid.setLayout(new AnchorLayout());
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
		grid.setColumnModel(ColumnDefinition.GetStatisticModel(Costants.DefaultGrouping)); 
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
				if (GlobalInfo.get().getGrouping().compareTo("")==0){
					NodeAccountingUI.get().notifyError("Please Select a grouping option");
				}
				NodeAccountingUI.get().central.statistics.updateGrid();
			}
		});


		//create a Store using local array data  
		final Store store = new SimpleStore(new String[]{"filter"}, new String[][]{
				new String[]{"GHNName"},
				new String[]{"callerScope"},
				new String[]{"ServiceClass"},
				new String[]{"ServiceName"},
				new String[]{"Date"},
				new String[]{"callerIP"}
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
				GlobalInfo.get().setGrouping(record.getAsString("filter"));
				return super.doBeforeSelect(comboBox, record, index);  
			} 
		});

		//create a Store using local array data  
		final Store storeGraph = new SimpleStore(new String[]{"index"}, new String[][]{
				new String[]{"Number of Invocations"},
				new String[]{"Average Invocation Time"},
				
		} );  
		store.load();  

		final ComboBox cbGraph = new ComboBox();  
		cbGraph.setForceSelection(true);  
		cbGraph.setMinChars(1);  
		cbGraph.setFieldLabel("Index");  
		cbGraph.setStore(storeGraph);  
		cbGraph.setDisplayField("index");  
		cbGraph.setMode(ComboBox.LOCAL);  
		cbGraph.setTriggerAction(ComboBox.ALL);  
		cbGraph.setEmptyText("Enter graph index");  
		cbGraph.setLoadingText("Creating...");  
		cbGraph.setTypeAhead(true);  
		cbGraph.setSelectOnFocus(true);  
		cbGraph.setWidth(200);  

		cbGraph.setHideTrigger(false);  

		cbGraph.addListener(new ComboBoxListenerAdapter() {  
			public boolean doBeforeSelect(ComboBox comboBox, Record record, int index) {
				GlobalInfo.get().setGraphTypeTitle(record.getAsString("index"));
				if (record.getAsString("index").contains("Number"))
					GlobalInfo.get().setGraphIndex("SUMINVOCATION");
				else GlobalInfo.get().setGraphIndex("AVERAGE");
				return super.doBeforeSelect(comboBox, record, index);  
			} 
		});


		
		Button export = new Button("Export to CSV", new ButtonListenerAdapter() {

			public void onClick(Button button, EventObject e){
				super.onClick(button, e);
				if (StatisticsPanel.store.getTotalCount() == 0)
					NodeAccountingUI.get().notifyError("No data to export");
				else {
					ArrayList<String[]> list = new ArrayList<String[]>();
					for (Record record : StatisticsPanel.store.getRecords())
					{
						String [] values = new String [record.getFields().length];
						for (int i = 0; i<values.length;i++){
							values[i] = record.getAsString(record.getFields()[i]);
						}
						list.add(values);
					}
					NodeAccountingUI.get().nodeAccoutingService.export(list,"export" , Callbacks.exportCallback);     
				}
			}
		});
		
		Button chart = new Button("Create Chart", new ButtonListenerAdapter() {

			public void onClick(Button button, EventObject e){
				super.onClick(button, e);
				if (StatisticsPanel.store.getTotalCount() == 0)
					NodeAccountingUI.get().notifyError("No data to display");
				else if (GlobalInfo.get().getGraphIndex().compareTo("")==0)
					NodeAccountingUI.get().notifyError("Please Select a graph Index");
				else {
					BarChart chart = new BarChart(GlobalInfo.get().getGraphTitle(GlobalInfo.get().getGraphTypeTitle()),StatisticsPanel.store.getRecords());
					chart.showGraph();
				}
			}
		});

		upperSection.add(statisticButton);
		upperSection.add(cb);
		upperSection.add(export);
		upperSection.add(cbGraph);
		upperSection.add(chart);
		this.add(upperSection);
		this.add(grid);
	}	
	
	public void updateGrid() {
		this.setTitle(GlobalInfo.get().getStatisticTitle("Statistics"));
		store = Stores.getStatisticStore(GlobalInfo.get());		 
		grid.reconfigure(store, ColumnDefinition.GetStatisticModel(GlobalInfo.get().getGrouping()));
		grid.show();
	}

}
