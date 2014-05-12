package org.gcube.messaging.accounting.portalaccountingportlet.client;

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
public class AccountingTreeContainer extends Panel{
	
	static final String DATE_FORMAT = "yyyy-MM-dd";
	static private DateTimeFormat format = DateTimeFormat.getFormat(DATE_FORMAT);
	
	public AccountingTree types;
	public AccountingUserTree users;
	public FormPanel downSection=new FormPanel();
	public Panel upperSection=new Panel();
	static AccountingTreeContainer singleton;
	
	private DateField dateStart;
	private Date dateStartDate;
	
	TreePanelListenerAdapter treeListener= new TreePanelListenerAdapter(){ 
		public void onClick(TreeNode node, EventObject e){
    		super.onClick(node, e);
		}
	};
	
	AccountingTreeContainer(String title){
		singleton = this;
		this.setTitle(title);
		this.setWidth(220);
		this.setHeight(700);
		this.setFrame(true);	
		this.setCollapsible(true);
		this.setLayout(new VerticalLayout(5));
		
		types=new AccountingTree("Types");
		types.setWidth(this.getWidth()-10);
		
		users=new AccountingUserTree();		
		users.addListener(treeListener);
		users.setWidth(this.getWidth()-10);
		
		upperSection.setWidth(this.getWidth()-10);
		
		Button clearButton = new Button();
		clearButton.setText("Clear Filters");
		clearButton.setTooltip("Clear the Query Filters");
		clearButton.setPressed(false);
		clearButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e){
				super.onClick(button, e);
				GlobalInfo.setSelectedUSer("");
				GlobalInfo.setSelectedRecord(GlobalInfo.DefaultRecord);
				setDateDefaults();
				AccountingUI.get().accountingUI.grid.updateGrid();
				
			}
		});
		
		upperSection.add(clearButton);
	
		downSection.setWidth(this.getWidth()-10);
		downSection.setLayout(new HorizontalLayout(5));
		
		
		FieldSet fieldSet = new FieldSet();
		fieldSet.setLabelWidth(60);    
		fieldSet.setAutoHeight(true);  
		fieldSet.setBorder(false);  
		dateStart = new DateField("Date Start", "dateStart", 100);
		
		dateStart.addListener(new DatePickerListenerAdapter() {  
             public void onSelect(DatePicker dataPicker, Date date) {  
                 GlobalInfo.setStartdate(format.format(date));
                 AccountingUI.get().accountingUI.grid.updateGrid();
             }  
         }); 
		DateField dateEnd = new DateField("Date End", "dateEnd", 100);
		dateEnd.setValue(new Date());
		dateEnd.addListener(new DatePickerListenerAdapter() {  
            public void onSelect(DatePicker dataPicker, Date date) {  
                GlobalInfo.setEnddate(format.format(date));
                AccountingUI.get().accountingUI.grid.updateGrid();
            }  
        }); 
		
		fieldSet.add(dateStart); 
		fieldSet.add(dateEnd);
		
		downSection.add(new PaddedPanel(fieldSet, 0, 0, 0, 0), new ColumnLayoutData(0.4));
		GlobalInfo.setEnddate(format.format(new Date()));
		this.add(upperSection);
		this.add(types);
		this.add(users);
	    this.add(downSection);
	    
	    AccountingUI.service.getStartDate(Callbacks.setStartDateCallback);
	}
	
	public DateField getDateStart() {
		return dateStart;
	}

	public void setDateStart(String dateSt) {
		dateStartDate = format.parse(dateSt);
		dateStart.setValue(dateStartDate);
		GlobalInfo.setStartdate(dateSt);
		
	}

	private void setDateDefaults(){
		
		GlobalInfo.setStartdate(format.format(dateStartDate));
		GlobalInfo.setEnddate(format.format(new Date()));
		((DateField)this.downSection.getForm().findField("dateStart")).setValue(dateStartDate);
		((DateField)this.downSection.getForm().findField("dateEnd")).setValue(new Date());
	
	}
	
	public static AccountingTreeContainer get() {
		return singleton;
	}
}

