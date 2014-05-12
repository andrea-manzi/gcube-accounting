package org.gcube.messaging.accounting.portalaccountingportlet.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.GroupLayout.Alignment;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.shared.DateTimeFormat.PredefinedFormat;
import com.google.gwt.layout.client.Layout;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.HistoryListener;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.ParseErrorEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.ParseErrorEvent.ParseErrorHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.DateField;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.validator.MinDateValidator;
import com.sencha.gxt.widget.core.client.info.Info;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

public class AccountingLeftPanel extends VerticalLayoutContainer{

	static final String DATE_FORMAT = "yyyy-MM-dd";
	static private DateTimeFormat format = DateTimeFormat.getFormat(DATE_FORMAT);

	public FiltTypePanel typeTree;
	public FiltUserPanel userTree;
	public FiltVrePanel vreTree;

	public ContentPanel downSection;
	public ToolBar upperSection;

	static AccountingLeftPanel singleton;

	private DateField dateStart,dateEnd;
	private Date dateStartDate,dateEndDate;
	int i=0;

	AccountingLeftPanel(String title){
		singleton = this;
		this.setTitle(title);
		this.setWidth(220);
		this.setHeight(800);
		this.setBorders(false);
		//this.setHeaderVisible(false);
		//this.setCollapsible(true);

		//upper section 
		upperSection=new ToolBar();
		
		TextButton clearButton=new TextButton("Clear Filters");
		clearButton.setBorders(true);
		clearButton.addSelectHandler(new SelectHandler() {
			public void onSelect(SelectEvent event) {
				FiltTypePanel.get().grid.getSelectionModel().deselectAll();
				FiltUserPanel.get().grid.getSelectionModel().deselectAll();
				FiltVrePanel.get().grid.getSelectionModel().deselectAll();
				GlobalInfo.setSelectedRecord(GlobalInfo.DefaultRecord); //this is useless
				GlobalInfo.setSelectedTypes(new ArrayList<String>());
				GlobalInfo.setSelectedUsers(new ArrayList<String>());
				GlobalInfo.setSelectedVres(new ArrayList<String>());
				setDateDefaults();
				AccountingUI.get().accountingUI.grid.updateGrid();	
			}
		});
		
		
		TextButton submitButton=new TextButton("Filter");
		submitButton.setBorders(true);
		submitButton.addSelectHandler(new SelectHandler() {
			public void onSelect(SelectEvent event) {	
				AccountingUI.get().accountingUI.grid.updateGrid();	
			}
		});
			
		upperSection.add(clearButton);
		upperSection.add(submitButton);

		//middle section
		typeTree=new FiltTypePanel();		
		userTree=new FiltUserPanel();				
		vreTree=new FiltVrePanel();		

		//down section
		downSection=new ContentPanel();
		downSection.setWidth(220+"px");
		VerticalLayoutContainer vert=new VerticalLayoutContainer();

		dateStart = new DateField();
		dateStart.addValueChangeHandler(new ValueChangeHandler<Date>() {
			public void onValueChange(ValueChangeEvent<Date> event) {
				GlobalInfo.setStartdate(format.format(event.getValue()));
			}
		});
		dateStart.addParseErrorHandler(new ParseErrorHandler() {
			public void onParseError(ParseErrorEvent event) {
				Info.display("Parse Error", event.getErrorValue() + " could not be parsed as a date");
			}
		});
		dateEnd = new DateField();
		dateEnd.addValueChangeHandler(new ValueChangeHandler<Date>() {
			public void onValueChange(ValueChangeEvent<Date> event) {
				GlobalInfo.setEnddate(format.format(event.getValue()));
			}
		});
		dateEnd.addParseErrorHandler(new ParseErrorHandler() {
			public void onParseError(ParseErrorEvent event) {
				Info.display("Parse Error", event.getErrorValue() + " could not be parsed as a date");
			}
		});
		dateEnd.setValue(new Date());
		GlobalInfo.setEnddate(format.format(new Date()));

		FieldLabel dateStartLabel = new FieldLabel(dateStart, "Start Date");
		vert.add(dateStartLabel, new VerticalLayoutData(1, -1));
		FieldLabel dateEndLabel = new FieldLabel(dateEnd, "End Date");
		vert.add(dateEndLabel, new VerticalLayoutData(1, -1));
		downSection.add(vert);
		downSection.setHeadingText("Dates");
		//tool button
		ToolButton clear = new ToolButton("clear");
		clear.addSelectHandler(new SelectHandler() {
			public void onSelect(SelectEvent event) {
				setDateDefaults();
			}
		});
		clear.setWidth("50px");
		clear.getElement().setInnerText("clear");
		downSection.getHeader().addTool(clear);

		this.add(upperSection);
		this.add(typeTree);
		this.add(userTree);
		this.add(vreTree);
		this.add(downSection);

		AccountingUI.service.getStartDate(Callbacks.setStartDateCallback);
	}

	public DateField getDateStart() {
		return dateStart;
	}
	public void setDateStart(String dateSt) {
		dateStartDate = format.parse(dateSt);
		dateStart.setValue(dateStartDate);
		dateStart.addValidator(new MinDateValidator(dateStartDate));
		dateEnd.addValidator(new MinDateValidator(dateStartDate));
		GlobalInfo.setStartdate(dateSt);
	}

	private void setDateDefaults(){		
		GlobalInfo.setStartdate(format.format(dateStartDate));
		GlobalInfo.setEnddate(format.format(new Date()));
		dateStart.setValue(dateStartDate);
		dateStart.addValidator(new MinDateValidator(dateStartDate));
		dateEnd.setValue(new Date());
		dateEnd.addValidator(new MinDateValidator(dateStartDate));
	}

	public static AccountingLeftPanel get() {
		return singleton;
	}
}

