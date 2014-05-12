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

public class AISPanel extends FormPanel implements IsWidget, Editor<DataObj> {

	public TextField IDENTIFIER,NAME,SUBTYPE;

	private static AISPanel singleton;
	public static AISPanel get(){
		return singleton;
	}
	public AISPanel() {
		singleton=this;		
		this.setTitle("AIS Options");
		this.setWidth(280);
		this.setHeight(400);
		this.setId(AccountingCostants.AISPanelId);

		VerticalLayoutContainer  container = new VerticalLayoutContainer();
		IDENTIFIER = new TextField();
		FieldLabel IdLabel=new FieldLabel(IDENTIFIER, "Id");
		NAME = new TextField();
		FieldLabel NameLabel = new FieldLabel(NAME, "Name");
		SUBTYPE = new TextField();
		FieldLabel TypeLabel = new FieldLabel(SUBTYPE, "Type");

		container.add(IdLabel, new VerticalLayoutData(1, -1));
		container.add(NameLabel, new VerticalLayoutData(1, -1));
		container.add(TypeLabel, new VerticalLayoutData(1, -1));

		this.setWidget(container);
	}
	public Widget asWidget() {
		return this;
	}
	public void loadData(DataObj data){
		if(data.getIDENTIFIER()!=null)this.IDENTIFIER.setValue(data.getIDENTIFIER());
		if(data.getNAME()!=null)this.NAME.setValue(data.getNAME());
		if(data.getSUBTYPE()!=null)this.SUBTYPE.setValue(data.getSUBTYPE());

	}


}

