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

public class WarPanel extends FormPanel implements IsWidget, Editor<DataObj> {

	public TextField SUBTYPE,WARID,WARNAME,WEBAPPNAME,WEBAPPVERSION,CATEGORY;
	private static WarPanel singleton;
	
	public static WarPanel get(){
		return singleton;
	}
	
	public WarPanel() {
		singleton=this;
		this.setTitle("WAR Details");
		this.setWidth(280);
		this.setHeight(400);
		this.setId(AccountingCostants.WarPanelId);

		VerticalLayoutContainer  container = new VerticalLayoutContainer();
		SUBTYPE = new TextField();
		FieldLabel subtypeLabel=new FieldLabel(SUBTYPE, "MimeType");
		WARID = new TextField();
		FieldLabel waridLabel = new FieldLabel(WARID, "Id");
		WARNAME = new TextField();
		FieldLabel warName = new FieldLabel(WARNAME, "Report Name");
		WEBAPPNAME = new TextField();
		FieldLabel webappName = new FieldLabel(WEBAPPNAME, "Output Type");
		WEBAPPVERSION = new TextField();
		FieldLabel webappVersion = new FieldLabel(WEBAPPVERSION, "Version");
		CATEGORY = new TextField();
		FieldLabel category = new FieldLabel(CATEGORY, "Category");
		
		container.add(waridLabel, new VerticalLayoutData(1, -1));

		container.add(subtypeLabel, new VerticalLayoutData(1, -1));
		container.add(warName, new VerticalLayoutData(1, -1));
		container.add(webappName, new VerticalLayoutData(1, -1));
		container.add(webappVersion, new VerticalLayoutData(1, -1));
		container.add(category, new VerticalLayoutData(1, -1));
		
		this.setWidget(container);
	}
	public Widget asWidget() {
		return this;
	}
	public void loadData(DataObj data){
		if(data.getWARID()!=null)this.WARID.setValue(data.getWARID());
		if(data.getWARNAME()!=null)this.WARNAME.setValue(data.getWARNAME());
		if(data.getWEBAPPNAME()!=null)this.WEBAPPNAME.setValue(data.getWEBAPPNAME());
		if(data.getSUBTYPE()!=null)this.SUBTYPE.setValue(data.getSUBTYPE());
		if(data.getWEBAPPVERSION()!=null)this.WEBAPPVERSION.setValue(data.getWEBAPPVERSION());
		if(data.getCATEGORY()!=null)this.CATEGORY.setValue(data.getCATEGORY());

	}


}

