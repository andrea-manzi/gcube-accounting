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

public class BrowsePanel extends FormPanel implements IsWidget, Editor<DataObj> {

	public TextField browseBy,isDistinct;
	private static BrowsePanel singleton;
	
	public static BrowsePanel get(){
		return singleton;
	}
	
	public BrowsePanel() {
		singleton=this;
		this.setTitle("Browse Options");
		this.setWidth(280);
		this.setHeight(400);
		this.setId(AccountingCostants.BrowsePanelId);

		VerticalLayoutContainer  container = new VerticalLayoutContainer();
		browseBy = new TextField();
		FieldLabel browseByLabel=new FieldLabel(browseBy, "BrowseBy");
		isDistinct = new TextField();
		FieldLabel isDistinctLabel = new FieldLabel(isDistinct, "IsDistinct");

		container.add(browseByLabel, new VerticalLayoutData(1, -1));
		container.add(isDistinctLabel, new VerticalLayoutData(1, -1));

		this.setWidget(container);
	}
	public Widget asWidget() {
		return this;
	}

	public void loadData(DataObj data){
		if(data.getIsDistinct()!=null)this.isDistinct.setValue(data.getIsDistinct());
		if(data.getBrowseBy()!=null)this.browseBy.setValue(data.getBrowseBy());
	}

}

