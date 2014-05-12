package org.gcube.messaging.accounting.portalaccountingportlet.client;

import java.util.ArrayList;

import org.gcube.messaging.accounting.portalaccountingportlet.client.data.TermPair;
import org.gcube.messaging.accounting.portalaccountingportlet.client.panel.*;

import com.gwtext.client.core.Position;
import com.gwtext.client.data.Record;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.PaddedPanel;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.form.DateField;
import com.gwtext.client.widgets.form.FieldSet;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.layout.AccordionLayout;
import com.gwtext.client.widgets.layout.ColumnLayoutData;
import com.gwtext.client.widgets.layout.VerticalLayout;

/**
 * 
 * @author Andrea Manzi (CERN)
 *
 */

public class AccountingDetailPanel extends Panel{

	public FormPanel formPanel =  new FormPanel();
	public Panel details =  new Panel();

	AccountingDetailPanel() {
		this.setLayout(new VerticalLayout(1));
		this.setAutoScroll(true);
		this.setCollapsible(true);
		this.setWidth(280);
		this.setHeight(700);
		
		formPanel.setFrame(true);  
		formPanel.setLabelAlign(Position.TOP);  
		formPanel.setPaddings(5);  
		formPanel.setWidth(280);
		formPanel.setHeight(300);
		details.setWidth(280);
		details.setHeight(400);
		details.setLayout(new AccordionLayout());
		FieldSet fieldSet = new FieldSet();  
		fieldSet.setLabelWidth(60);    
		fieldSet.setAutoHeight(true);  
		fieldSet.setBorder(false);  

		//the field names must match the data field values from the Store  
		fieldSet.add(new TextField("Username", "user", 200));  
		fieldSet.add(new TextField("Scope", "vre", 250));  
		fieldSet.add(new DateField("Date", "date", 100));  
		fieldSet.add(new TextField("Time", "time", 100));
		fieldSet.add(new TextField("Type", "type", 200));
	
		formPanel.add(new PaddedPanel(fieldSet, 0, 0, 0, 0), new ColumnLayoutData(0.4));
		createChildrenPanels();
		this.add(formPanel);
		this.add(details);

	}
	
	public void createChildrenPanels(){
		details.add(new CollectionPanel());
		details.add(new TermPanel());
		details.add(new BrowsePanel());
		details.add(new TSPanel());
		details.add(new ContentPanel());
		details.add(new HLPanel());
		details.add(new AISPanel());
		details.add(new GCUBEUsersPanel());
		details.add(new AnnotationPanel());
		details.add(new ReportGeneratorPanel());
		details.add(new TemplateGeneratorPanel());
		details.add(new WorkflowDocumentPanel());
		details.add(new GHNDetailPanel());
		details.add(new WebAppPanel());
		details.add(new WebAppDetailPanel());
		details.add(new AquamapsPanel());
		details.add(new WarPanel());
		
		for(Component item: details.getComponents()){
			item.disable();
			item.setVisible(false);
		}
	}
	public void showMessage(String msg){
		MessageBox.alert(msg);
	}
	
	
	public void loadPanel(Record record){
		for(Component item: details.getComponents()){
			item.disable();
			item.setVisible(false);
		}
		switch(GlobalInfo.getSelectedRecord()){
			case SimpleSearchRecord:
				details.getComponent(AccountingCostants.TermsPanelId).enable();
				details.getComponent(AccountingCostants.TermsPanelId).setVisible(true);
				details.getComponent(AccountingCostants.CollectionPanelID).enable();
				details.getComponent(AccountingCostants.CollectionPanelID).setVisible(true);
				((CollectionPanel)details.getComponent(AccountingCostants.CollectionPanelID)).field.disable();
				((CollectionPanel)details.getComponent(AccountingCostants.CollectionPanelID)).field.hide();
				TermPair pair = new TermPair();
				pair.setTermValue(record.getAsString("termValue"));
				ArrayList<TermPair> pairs = new ArrayList<TermPair>();
				pairs.add(pair);
				((TermPanel)details.getComponent(AccountingCostants.TermsPanelId)).loadTermsDetails(pairs);
				break;
			case QuickSearchRecord:
			case GoogleSearchRecord:
				details.getComponent(AccountingCostants.TermsPanelId).enable();
				details.getComponent(AccountingCostants.TermsPanelId).setVisible(true);
				pair = new TermPair();
				pair.setTermValue(record.getAsString("termValue"));
				pairs = new ArrayList<TermPair>();
				pairs.add(pair);
				((TermPanel)details.getComponent(AccountingCostants.TermsPanelId)).loadTermsDetails(pairs);
				break;
			case AdvancedSearchRecord:
				details.getComponent(AccountingCostants.TermsPanelId).enable();
				details.getComponent(AccountingCostants.TermsPanelId).setVisible(true);
				details.getComponent(AccountingCostants.CollectionPanelID).enable();
				details.getComponent(AccountingCostants.CollectionPanelID).setVisible(true);
				((CollectionPanel)details.getComponent(AccountingCostants.CollectionPanelID)).addOperator(record.getAsString("operator"));
				
				break;
			case BrowseRecord:
				details.getComponent(AccountingCostants.BrowsePanelId).enable();
				details.getComponent(AccountingCostants.BrowsePanelId).setVisible(true);
				details.getComponent(AccountingCostants.CollectionPanelID).enable();
				details.getComponent(AccountingCostants.CollectionPanelID).setVisible(true);
				((BrowsePanel)details.getComponent(AccountingCostants.BrowsePanelId)).getForm().loadRecord(record);
				((CollectionPanel)details.getComponent(AccountingCostants.CollectionPanelID)).field.disable();
				((CollectionPanel)details.getComponent(AccountingCostants.CollectionPanelID)).field.hide();
				
				break;
			case ContentRecord:
				details.getComponent(AccountingCostants.ContentPanelId).enable();
				details.getComponent(AccountingCostants.ContentPanelId).setVisible(true);
				
				break;
			case TSRecord:
				details.getComponent(AccountingCostants.TSPanelId).enable();
				details.getComponent(AccountingCostants.TSPanelId).setVisible(true);
				((TSPanel)details.getComponent(AccountingCostants.TSPanelId)).getForm().loadRecord(record);
				
				break;
			case AISRecord:
				details.getComponent(AccountingCostants.AISPanelId).enable();
				details.getComponent(AccountingCostants.AISPanelId).setVisible(true);
				((AISPanel)details.getComponent(AccountingCostants.AISPanelId)).getForm().loadRecord(record);
				break;
			case HLRecord:
				details.getComponent(AccountingCostants.HLPanelId).enable();
				details.getComponent(AccountingCostants.HLPanelId).setVisible(true);
				((HLPanel)details.getComponent(AccountingCostants.HLPanelId)).getForm().loadRecord(record);
				if (record.getAsString("SUBTYPE").compareTo(AccountingCostants.HLSubTypeSent)== 0){
					details.getComponent(AccountingCostants.GCUBEUSersAddresseesPanelId).enable();
					details.getComponent(AccountingCostants.GCUBEUSersAddresseesPanelId).setVisible(true);
				}
				
				break;
			case AnnotationRecord:
				details.getComponent(AccountingCostants.AnnotationPanelId).enable();
				details.getComponent(AccountingCostants.AnnotationPanelId).setVisible(true);
				((AnnotationPanel)details.getComponent(AccountingCostants.AnnotationPanelId)).getForm().loadRecord(record);
				details.getComponent(AccountingCostants.ContentPanelId).enable();
				details.getComponent(AccountingCostants.ContentPanelId).setVisible(true);
				break;
			case DocumentWorkflowRecord:
				details.getComponent(AccountingCostants.WorkflowDocumentPanelId).enable();
				details.getComponent(AccountingCostants.WorkflowDocumentPanelId).setVisible(true);
				((WorkflowDocumentPanel)details.getComponent(AccountingCostants.WorkflowDocumentPanelId)).getForm().loadRecord(record);
				break;
			case ReportRecord:
				if (record.getAsString("SUBTYPE").compareTo(AccountingCostants.GENERATE_REPORT_OUTPUT)== 0){
					details.getComponent(AccountingCostants.ReportGeneratorPanelId).enable();
					details.getComponent(AccountingCostants.ReportGeneratorPanelId).setVisible(true);
					((ReportGeneratorPanel)details.getComponent(AccountingCostants.ReportGeneratorPanelId)).getForm().loadRecord(record);
				}
				else {
					details.getComponent(AccountingCostants.TemplateGeneratorPanelId).enable();
					details.getComponent(AccountingCostants.TemplateGeneratorPanelId).setVisible(true);
					((TemplateGeneratorPanel)details.getComponent(AccountingCostants.TemplateGeneratorPanelId)).getForm().loadRecord(record);
				}
				break;
			case AquamapsRecord:
				details.getComponent(AccountingCostants.AquamapsPanelId).enable();
				details.getComponent(AccountingCostants.AquamapsPanelId).setVisible(true);
				((AquamapsPanel)details.getComponent(AccountingCostants.AquamapsPanelId)).getForm().loadRecord(record);
				break;
			case WebAppRecord:
				details.getComponent(AccountingCostants.WebAppPanelId).enable();
				details.getComponent(AccountingCostants.WebAppPanelId).setVisible(true);
				((WebAppPanel)details.getComponent(AccountingCostants.WebAppPanelId)).getForm().loadRecord(record);
				details.getComponent(AccountingCostants.GHNDetailPanelId).enable();
				details.getComponent(AccountingCostants.GHNDetailPanelId).setVisible(true);
				details.getComponent(AccountingCostants.WebAppDetailPanelId).enable();
				details.getComponent(AccountingCostants.WebAppDetailPanelId).setVisible(true);
				break;
			case WarRecord:
				details.getComponent(AccountingCostants.WarPanelId).enable();
				details.getComponent(AccountingCostants.WarPanelId).setVisible(true);
				((WarPanel)details.getComponent(AccountingCostants.WarPanelId)).getForm().loadRecord(record);
				break;
			case LoginRecord:
			case GenericRecord:
				break;
			case Empty:
				this.showMessage("Details not available, please select first a record type");
				break;
			default:
				break;
			
		}
		
	}
	
}

