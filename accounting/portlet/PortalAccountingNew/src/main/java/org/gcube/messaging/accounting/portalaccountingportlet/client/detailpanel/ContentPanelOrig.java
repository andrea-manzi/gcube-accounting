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

public class ContentPanelOrig extends FormPanel implements IsWidget, Editor<DataObj> {

	public TextField identifier,name;
	private static ContentPanelOrig singleton;
	
	public static ContentPanelOrig get(){
		return singleton;
	}
	
	public ContentPanelOrig() {
		singleton=this;
		this.setTitle("Content Details");
		this.setWidth(280);
		this.setHeight(400);
		this.setId(AccountingCostants.ContentPanelId);

		VerticalLayoutContainer  container = new VerticalLayoutContainer();
		identifier = new TextField();
		FieldLabel identifierByLabel=new FieldLabel(identifier, "Object Id");
		name = new TextField();
		FieldLabel nameLabel = new FieldLabel(name, "Object Name");

		container.add(identifierByLabel, new VerticalLayoutData(1, -1));
		container.add(nameLabel, new VerticalLayoutData(1, -1));

		this.setWidget(container);
	}
	public Widget asWidget() {
		return this;
	}
	public void loadData(DataObj data){
		if(data.getIdentifier()!=null)this.identifier.setValue(data.getIdentifier());
		if(data.getName()!=null)this.name.setValue(data.getName());
	}


}

