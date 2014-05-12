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


public class SMPanel extends FormPanel implements IsWidget, Editor<DataObj> {

	public TextField SUBTYPE,filename,filetype,algorithmname,executiontime, executionoutcome;
	private static SMPanel singleton;
	
	public static SMPanel get(){
		return singleton;
	}
	
	public SMPanel() {
		singleton=this;
		this.setTitle("Statistical Manager Output Options");
		this.setWidth(280);
		this.setHeight(400);
		this.setId(AccountingCostants.SMPanelId);

		VerticalLayoutContainer  container = new VerticalLayoutContainer();
		filename = new TextField();
		FieldLabel filenameLabel=new FieldLabel(filename, "File Name");
		filetype = new TextField();
		FieldLabel filetypeLabel = new FieldLabel(filetype, "File Type");
		algorithmname = new TextField();
		FieldLabel algnameLabel = new FieldLabel(algorithmname, "Algorithm Name");
		executionoutcome = new TextField();
		FieldLabel execoutcomeLabel = new FieldLabel(executionoutcome, "Execution Outcome");
		executiontime = new TextField();
		FieldLabel executiontimeLabel = new FieldLabel(executiontime, "Execution Time");
		SUBTYPE = new TextField();
		FieldLabel subtypeLabel = new FieldLabel(SUBTYPE, "Output Type");

		container.add(subtypeLabel, new VerticalLayoutData(1, -1));
		container.add(filenameLabel, new VerticalLayoutData(1, -1));
		container.add(filetypeLabel, new VerticalLayoutData(1, -1));
		container.add(algnameLabel, new VerticalLayoutData(1, -1));
		container.add(execoutcomeLabel, new VerticalLayoutData(1, -1));
		container.add(executiontimeLabel, new VerticalLayoutData(1, -1));
		
		this.setWidget(container);
	}
	public Widget asWidget() {
		return this;
	}
	public void loadData(DataObj data){
		if(data.getSUBTYPE()!=null)this.SUBTYPE.setValue(data.getSUBTYPE());
		if(data.getFILENAME()!=null)this.filename.setValue(data.getFILENAME());
		if(data.getFILETYPE()!=null)this.filetype.setValue(data.getFILETYPE());
		if(data.getALGORITHMNAME()!=null)this.algorithmname.setValue(data.getALGORITHMNAME());
		if(data.getEXECUTIONOUTCOME()!=null)this.executionoutcome.setValue(data.getEXECUTIONOUTCOME());
		if(data.getEXECUTIONTIME()!=null)this.executiontime.setValue(data.getEXECUTIONTIME());
		
	}


}

