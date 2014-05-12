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

public class ReportGeneratorPanel extends FormPanel implements IsWidget, Editor<DataObj> {

	public TextField MIMETYPE,SUBTYPE,type,NAME;
	private static ReportGeneratorPanel singleton;
	
	public static ReportGeneratorPanel get(){
		return singleton;
	}
	
	public ReportGeneratorPanel() {
		singleton=this;
		this.setTitle("Report Generator Output Options");
		this.setWidth(280);
		this.setHeight(400);
		this.setId(AccountingCostants.ReportGeneratorPanelId);

		VerticalLayoutContainer  container = new VerticalLayoutContainer();
		MIMETYPE = new TextField();
		FieldLabel mimetypeLabel=new FieldLabel(MIMETYPE, "MimeType");
		type = new TextField();
		FieldLabel typeLabel = new FieldLabel(type, "HLType");
		NAME = new TextField();
		FieldLabel nameLabel = new FieldLabel(NAME, "Report Name");
		SUBTYPE = new TextField();
		FieldLabel subtypeLabel = new FieldLabel(SUBTYPE, "Output Type");

		container.add(subtypeLabel, new VerticalLayoutData(1, -1));
		container.add(nameLabel, new VerticalLayoutData(1, -1));
		container.add(mimetypeLabel, new VerticalLayoutData(1, -1));
		container.add(typeLabel, new VerticalLayoutData(1, -1));
		
		this.setWidget(container);
	}
	public Widget asWidget() {
		return this;
	}
	public void loadData(DataObj data){
		if(data.getMIMETYPE()!=null)this.MIMETYPE.setValue(data.getMIMETYPE());
		if(data.getType()!=null)this.type.setValue(data.getType());
		if(data.getNAME()!=null)this.NAME.setValue(data.getNAME());
		if(data.getSUBTYPE()!=null)this.SUBTYPE.setValue(data.getSUBTYPE());
		
	}


}

