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

public class HLPanel extends FormPanel implements IsWidget, Editor<DataObj> {

	public TextField IDENTIFIER,SUBTYPE,type,NAME;
	private static HLPanel singleton;
	
	public static HLPanel get(){
		return singleton;
	}
	
	public HLPanel() {
		singleton=this;
		this.setTitle("HL Options");
		this.setWidth(280);
		this.setHeight(400);
		this.setId(AccountingCostants.HLPanelId);

		VerticalLayoutContainer  container = new VerticalLayoutContainer();
		IDENTIFIER = new TextField();
		FieldLabel identifierLabel=new FieldLabel(IDENTIFIER, "Id");
		type = new TextField();
		FieldLabel typeLabel = new FieldLabel(type, "HLType");
		NAME = new TextField();
		FieldLabel nameLabel = new FieldLabel(NAME, "Name");
		SUBTYPE = new TextField();
		FieldLabel subtypeLabel = new FieldLabel(SUBTYPE, "Operation");
		
		container.add(identifierLabel, new VerticalLayoutData(1, -1));
		container.add(typeLabel, new VerticalLayoutData(1, -1));
		container.add(nameLabel, new VerticalLayoutData(1, -1));
		container.add(subtypeLabel, new VerticalLayoutData(1, -1));
		
		this.setWidget(container);
	}
	public Widget asWidget() {
		return this;
	}
	public void loadData(DataObj data){
		if(data.getIDENTIFIER()!=null)this.IDENTIFIER.setValue(data.getIDENTIFIER());
		if(data.getType()!=null)this.type.setValue(data.getType());
		if(data.getNAME()!=null)this.NAME.setValue(data.getNAME());
		if(data.getSUBTYPE()!=null)this.SUBTYPE.setValue(data.getSUBTYPE());
		
	}


}

