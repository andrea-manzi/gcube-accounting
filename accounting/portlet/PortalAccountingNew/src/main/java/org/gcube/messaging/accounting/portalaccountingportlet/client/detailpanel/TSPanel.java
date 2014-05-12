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

public class TSPanel extends FormPanel implements IsWidget, Editor<DataObj> {

	public TextField SUBTYPE,TITLE;
	private static TSPanel singleton;
	
	public static TSPanel get(){
		return singleton;
	}
	
	public TSPanel() {
		singleton=this;
		this.setTitle("TS Options");
		this.setWidth(280);
		this.setHeight(400);
		this.setId(AccountingCostants.TSPanelId);

		VerticalLayoutContainer  container = new VerticalLayoutContainer();
		SUBTYPE = new TextField();
		FieldLabel subtypeLabel=new FieldLabel(SUBTYPE, "SubType");
		TITLE = new TextField();
		FieldLabel titleLabel = new FieldLabel(TITLE, "Title");

		container.add(subtypeLabel, new VerticalLayoutData(1, -1));
		container.add(titleLabel, new VerticalLayoutData(1, -1));

		this.setWidget(container);
	}
	public Widget asWidget() {
		return this;
	}
	public void loadData(DataObj data){
		if(data.getTITLE()!=null)this.TITLE.setValue(data.getTITLE());
		if(data.getSUBTYPE()!=null)this.SUBTYPE.setValue(data.getSUBTYPE());
	}


}

