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

public class WebAppPanel extends FormPanel implements IsWidget, Editor<DataObj> {

	public TextField SUBTYPE;
	private static WebAppPanel singleton;
	
	public static WebAppPanel get(){
		return singleton;
	}
	
	public WebAppPanel() {
		singleton=this;
		this.setTitle("WebApp Options");
		this.setWidth(280);
		this.setHeight(400);
		this.setId(AccountingCostants.WebAppPanelId);

		VerticalLayoutContainer  container = new VerticalLayoutContainer();
		SUBTYPE = new TextField();
		FieldLabel subtypeLabel=new FieldLabel(SUBTYPE, "SubType");

		container.add(subtypeLabel, new VerticalLayoutData(1, -1));

		this.setWidget(container);
	}
	public Widget asWidget() {
		return this;
	}

	public void loadData(DataObj data){
		if(data.getSUBTYPE()!=null)this.SUBTYPE.setValue(data.getSUBTYPE());
	}

}

