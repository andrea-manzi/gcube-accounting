package org.gcube.messaging.accounting.portalaccountingportlet.client.detailpanel;

import org.gcube.messaging.accounting.portalaccountingportlet.client.AccountingCostants;
import org.gcube.messaging.accounting.portalaccountingportlet.client.obj.DataObj;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.FormPanel;
import com.sencha.gxt.widget.core.client.form.TextField;

public class WorkflowDocumentPanel extends FormPanel implements IsWidget, Editor<DataObj> {

	public TextField SUBTYPE,WORKFLOWID,REPORTNAME,STATUS,STEPNUMBER;
	private static WorkflowDocumentPanel singleton;
	
	public static WorkflowDocumentPanel get(){
		return singleton;
	}
	
	public WorkflowDocumentPanel() {
		singleton=this;
		this.setTitle("WAR Details");
		this.setWidth(280);
		this.setHeight(400);
		this.setId(AccountingCostants.WarPanelId);

		VerticalLayoutContainer  container = new VerticalLayoutContainer();
		SUBTYPE = new TextField();
		FieldLabel subtypeLabel=new FieldLabel(SUBTYPE, "Type");
		WORKFLOWID = new TextField();
		FieldLabel workflowId = new FieldLabel(WORKFLOWID, "WorkflowId");
		REPORTNAME = new TextField();
		FieldLabel reportName = new FieldLabel(REPORTNAME, "Report Name");
		STATUS = new TextField();
		FieldLabel status = new FieldLabel(STATUS, "Status");
		STEPNUMBER = new TextField();
		FieldLabel stepNumber = new FieldLabel(STEPNUMBER, "Number of Steps");
		
		container.add(subtypeLabel, new VerticalLayoutData(1, -1));
		container.add(workflowId, new VerticalLayoutData(1, -1));
		container.add(reportName, new VerticalLayoutData(1, -1));
		container.add(status, new VerticalLayoutData(1, -1));
		container.add(stepNumber, new VerticalLayoutData(1, -1));
		
		this.setWidget(container);
	}
	public Widget asWidget() {
		return this;
	}
	public void loadData(DataObj data){
		if(data.getWORKFLOWID()!=null)this.WORKFLOWID.setValue(data.getWORKFLOWID());
		if(data.getREPORTNAME()!=null)this.REPORTNAME.setValue(data.getREPORTNAME());
		if(data.getSTATUS()!=null)this.STATUS.setValue(data.getSTATUS());
		if(data.getSUBTYPE()!=null)this.SUBTYPE.setValue(data.getSUBTYPE());
		if(data.getSTEPNUMBER()!=null)this.STEPNUMBER.setValue(data.getSTEPNUMBER());
	}


}

