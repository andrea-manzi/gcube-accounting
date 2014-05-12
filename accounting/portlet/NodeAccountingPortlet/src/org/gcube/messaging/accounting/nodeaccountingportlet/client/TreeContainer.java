package org.gcube.messaging.accounting.nodeaccountingportlet.client;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.DatePicker;
import com.gwtext.client.widgets.PaddedPanel;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.event.DatePickerListenerAdapter;
import com.gwtext.client.widgets.form.DateField;
import com.gwtext.client.widgets.form.FieldSet;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.layout.ColumnLayoutData;
import com.gwtext.client.widgets.layout.HorizontalLayout;
import com.gwtext.client.widgets.layout.VerticalLayout;
import com.gwtext.client.widgets.tree.TreeNode;
import com.gwtext.client.widgets.tree.event.TreePanelListenerAdapter;

/**
 * 
 * @author Andrea Manzi (CERN)
 *
 */
public class TreeContainer extends Panel{

	static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.S";
	static private DateTimeFormat format = DateTimeFormat.getFormat(DATE_FORMAT);

	public ServiceTree types;
	public GHNTree ghns;
	public FormPanel downSection=new FormPanel();
	public Panel upperSection=new Panel();
	public Panel scopeSection=new Panel();
	public ScopeComboBox cb = null;
	static TreeContainer singleton;

	private DateField dateStart;
	private Date dateStartDate;

	TreePanelListenerAdapter treeListener= new TreePanelListenerAdapter(){ 
		public void onClick(TreeNode node, EventObject e){
			super.onClick(node, e);
		}
	};

	TreeContainer(String title){
		singleton = this;
		this.setTitle(title);
		this.setWidth(350);
		this.setHeight(700);
		this.setFrame(true);	
		this.setCollapsible(true);
		this.setLayout(new VerticalLayout(5));

		types=new ServiceTree("Services");
		types.setWidth(this.getWidth()-10);
		//types.setAutoWidth(true);
		ghns=new GHNTree();		
		ghns.addListener(treeListener);
		ghns.setWidth(this.getWidth()-10);
		//ghns.setAutoWidth(true);
		upperSection.setWidth(this.getWidth()-10);
		//upperSection.setAutoWidth(true);
		upperSection.setLayout(new HorizontalLayout(5));
		Button clearButton = new Button();
		clearButton.setText("Clear Filters");
		clearButton.setTooltip("Clear the Query Filters");
		clearButton.setPressed(false);
		clearButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e){
				super.onClick(button, e);
				GlobalInfo.get().reset();
				setDateDefaults();
				NodeAccountingUI.get().central.grid.updateGrid();

			}
		});

		Button refreshButton = new Button();
		refreshButton.setText("Refresh Lists");
		refreshButton.setTooltip("Refresh Selection lists ");
		refreshButton.setPressed(false);
		refreshButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e){
				super.onClick(button, e);
				GlobalInfo.get().reset();
				setDateDefaults();
				NodeAccountingUI.get().central.grid.updateGrid();
				NodeAccountingUI.get().tree.ghns.refresh();
				NodeAccountingUI.get().tree.types.refresh();
				NodeAccountingUI.nodeAccoutingService.getScopes(Callbacks.setScopes);
			}
		});
		upperSection.add(clearButton);
		upperSection.add(refreshButton);

		scopeSection.setLayout(new HorizontalLayout(5));
		scopeSection.setAutoWidth(true);
		scopeSection.setWidth(this.getWidth());
		cb = new ScopeComboBox(this.getWidth());
		scopeSection.add(cb);

		downSection.setWidth(this.getWidth()-10);
		downSection.setLayout(new HorizontalLayout(5));
		//downSection.setAutoWidth(true);

		FieldSet fieldSet = new FieldSet();
		fieldSet.setLabelWidth(60);    
		fieldSet.setAutoHeight(true);  
		fieldSet.setBorder(false);  
		dateStart = new DateField("Date Start", "dateStart", 100);

		dateStart.addListener(new DatePickerListenerAdapter() {  
			public void onSelect(DatePicker dataPicker, Date date) {  
				GlobalInfo.get().setStartdate(format.format(date));
				NodeAccountingUI.get().central.grid.updateGrid();
			}  
		}); 
		DateField dateEnd = new DateField("Date End", "dateEnd", 100);
		dateEnd.setValue(new Date());
		dateEnd.addListener(new DatePickerListenerAdapter() {  
			public void onSelect(DatePicker dataPicker, Date date) {  
				GlobalInfo.get().setEnddate(format.format(date));
				NodeAccountingUI.get().central.grid.updateGrid();
			}  
		}); 

		fieldSet.add(dateStart); 
		fieldSet.add(dateEnd);
		fieldSet.setLegend("Time Intervals");

		downSection.add(new PaddedPanel(fieldSet, 0, 0, 0, 0), new ColumnLayoutData(0.4));
		GlobalInfo.get().setEnddate(format.format(new Date()));
		this.add(upperSection);
		this.add(scopeSection);
		this.add(types);
		this.add(ghns);
		this.add(downSection);

		NodeAccountingUI.nodeAccoutingService.getStartDate(Callbacks.setStartDateCallback);
		NodeAccountingUI.nodeAccoutingService.getScopes(Callbacks.setScopes);
	}

	public DateField getDateStart() {
		return dateStart;
	}

	public void setDateStart(String dateSt) {
		dateStartDate = format.parse(dateSt);
		dateStart.setValue(dateStartDate);
		GlobalInfo.get().setStartdate(dateSt);

	}

	private void setDateDefaults(){

		GlobalInfo.get().setStartdate(format.format(dateStartDate));
		GlobalInfo.get().setEnddate(format.format(new Date()));
		((DateField)this.downSection.getForm().findField("dateStart")).setValue(dateStartDate);
		((DateField)this.downSection.getForm().findField("dateEnd")).setValue(new Date());

	}

	public static TreeContainer get() {
		return singleton;
	}

	
}

