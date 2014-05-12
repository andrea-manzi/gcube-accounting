package org.gcube.messaging.accounting.portalaccountingportlet.client;

import java.util.ArrayList;
import java.util.List;

import org.gcube.messaging.accounting.portalaccountingportlet.client.obj.Statistics;
import org.gcube.messaging.accounting.portalaccountingportlet.client.obj.StatisticsProperties;

import com.google.gwt.core.client.GWT;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutData;
import com.sencha.gxt.widget.core.client.container.Container;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.RowDoubleClickEvent;
import com.sencha.gxt.widget.core.client.event.RowDoubleClickEvent.RowDoubleClickHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.grid.CheckBoxSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridSelectionModel;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

public class StatisticsGridPanel extends ContentPanel{

	public Grid<Statistics> grid;
	public ListStore<Statistics> store;
	public ColumnModel<Statistics> cm;

	public int HEIGHT=350;
	public TextButton clearButton,chartButton,exportButton,statisticsButton;
	public PopUpSelectionGroupPanel popUpGroupSelection;
	public CheckBoxSelectionModel<Statistics> sm;

	public StatisticsGridPanel(){
		//middle section - grid
		StatisticsProperties props = GWT.create(StatisticsProperties.class);
		store = new ListStore<Statistics>(props.key());	
		IdentityValueProvider<Statistics> identity = new IdentityValueProvider<Statistics>();
		sm = new CheckBoxSelectionModel<Statistics>(identity);
		
		cm = ColumnDefinition.StatisticsModel(GlobalInfo.getSelectedGroupsBy(), sm);
		grid = new Grid<Statistics>(store, cm) {};	 
		
		grid.setSelectionModel(sm);
		grid.getSelectionModel().setSelectionMode(SelectionMode.MULTI);
		grid.addRowDoubleClickHandler(new RowDoubleClickHandler() {
			public void onRowDoubleClick(RowDoubleClickEvent event) {
				//
			}
		});

		//upper section - toolbar
		ToolBar upperSection=new ToolBar();

		statisticsButton=new TextButton("Create Statistics");
		statisticsButton.setBorders(true);
		statisticsButton.addSelectHandler(new SelectHandler() {
			public void onSelect(SelectEvent event) {
				if (GlobalInfo.getSelectedGroupsBy().size()<1){
					AccountingUI.get().notifyError("Please select at least one grouping option");
				}
				else {
					AccountingUI.get().accountingUI.statistics.updateGrid();
				}
			}
		});
		statisticsButton.disable();
		
		popUpGroupSelection=new PopUpSelectionGroupPanel("group selection");
		popUpGroupSelection.update();
		TextButton groupButton=new TextButton("Select GroupBy option");
		groupButton.setBorders(true);
		groupButton.addSelectHandler(new SelectHandler() {
			public void onSelect(SelectEvent event) {
				AccountingUI.get().dialogBoxGen.createDialogBox(popUpGroupSelection);
			}
		});		

		exportButton=new TextButton("Export to CSV");
		exportButton.setBorders(true);
		exportButton.addSelectHandler(new SelectHandler() {
			public void onSelect(SelectEvent event) {
				if (AccountingUI.get().accountingUI.statistics.store.size()<1)
					AccountingUI.get().notifyError("No data to export");
				else {
					ArrayList<String[]> list = new ArrayList<String[]>();
					for (Statistics record : AccountingUI.get().accountingUI.statistics.store.getAll())
					{	
						int length=0;
						if(record.getCNT()!=null)++length;
						if(record.getUser()!=null)++length;
						if(record.getVre()!=null)++length;
						if(record.getType()!=null)++length;

						String [] values = new String [length];
						int i=0;						
						if(record.getUser()!=null){	values[i] = record.getUser();i++;}
						if(record.getVre()!=null){	values[i] = record.getVre();i++;}
						if(record.getType()!=null){	values[i] = record.getType();i++;}
						if(record.getCNT()!=null){	values[i] = record.getCNT();i++;}

						list.add(values);
					}
					AccountingUI.service.export(list,"export" , Callbacks.exportCallback);     
				}
			}
		});
		exportButton.disable();
		
		chartButton=new TextButton("Create Chart");
		chartButton.setBorders(true);
		chartButton.addSelectHandler(new SelectHandler() {
			public void onSelect(SelectEvent event) {
				if (AccountingUI.get().accountingUI.statistics.store.size()<1)
					AccountingUI.get().notifyError("No data to export");
				if (AccountingUI.get().accountingUI.statistics.sm.getSelectedItems().size()<1)
					AccountingUI.get().notifyError("Please select which records you want to show on the chart from the grid of statistics! ");
				else {
					String title=getHeaderTitle();
					ChartPanel chart = new ChartPanel(title,AccountingUI.get().accountingUI.statistics.sm.getSelectedItems());
					chart.showGraph();
				}
			}
		});
		chartButton.disable();
		
		clearButton=new TextButton("Clear Statistics");
		clearButton.setBorders(true);
		clearButton.addSelectHandler(new SelectHandler() {
			public void onSelect(SelectEvent event) {
				store.clear();
				store.commitChanges();
				clearButton.disable();
				exportButton.disable();
				chartButton.disable();
			}
		});
		clearButton.disable();
		
		upperSection.add(groupButton);
		upperSection.add(statisticsButton);
		upperSection.add(clearButton);
		upperSection.add(exportButton);
		upperSection.add(chartButton);		

		final VerticalLayoutContainer con = new VerticalLayoutContainer();
		con.setBorders(true);
		con.setScrollMode(ScrollMode.AUTOX);
		ToolBar empty = new ToolBar();
		empty.setHeight(20);
		con.add(upperSection, new VerticalLayoutData(1, -1));
		con.add(grid, new VerticalLayoutData(1, 1));
		
		this.setTitle("Statistics");
		this.setHeight(HEIGHT);
		this.setBodyBorder(false); 
		this.setWidget(con);
	}


	public void loadStatistics(List<Statistics> results){
		store.clear();
		store.addAll(results);
		//enable buttons
		clearButton.enable();
		exportButton.enable();
		chartButton.enable();
		store.commitChanges();
		cm = ColumnDefinition.StatisticsModel(GlobalInfo.getSelectedGroupsBy(),sm);
		grid.reconfigure(store, cm);

		this.show();
	}

	public void updateGrid() {
		String headerTitle=getHeaderTitle();
		this.setHeadingText(headerTitle);
		
		AccountingUI.service.getStatistics(GlobalInfo.getSelectedTypes(), 
				GlobalInfo.getSelectedUsers(),
				GlobalInfo.getSelectedVres(), 
				GlobalInfo.getSelectedGroupsBy(),
				GlobalInfo.getDates(), Callbacks.getStatisticsCallback);
	}
	
	public String getHeaderTitle(){
		String groups="";
		int i=1;
		for(String tmp:GlobalInfo.getSelectedGroupsBy()){
			groups=groups+tmp;
			if(i<GlobalInfo.getSelectedGroupsBy().size())groups=groups+", ";
			i++;
		}
		return ("Records for selected filters" +
				((GlobalInfo.getStartdate()== "")?"":" from Date "+GlobalInfo.getStartdate())+
				((GlobalInfo.getEnddate()== "")?"":" to Date "+GlobalInfo.getEnddate())+
				" grouped by "+groups);
	}

}
