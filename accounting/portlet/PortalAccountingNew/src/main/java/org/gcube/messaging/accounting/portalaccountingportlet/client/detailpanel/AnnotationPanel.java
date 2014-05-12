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

public class AnnotationPanel extends FormPanel implements IsWidget, Editor<DataObj> {

	public TextField ACTION,NAME,SUBTYPE;

	private static AnnotationPanel singleton;
	public static AnnotationPanel get(){
		return singleton;
	}
	public AnnotationPanel() {
		singleton=this;
		this.setTitle("Annotation Options");
		this.setWidth(280);
		this.setHeight(400);
		this.setId(AccountingCostants.AnnotationPanelId);

		VerticalLayoutContainer  container = new VerticalLayoutContainer();
		ACTION = new TextField();
		FieldLabel ActionLabel=new FieldLabel(ACTION, "OperationType");
		NAME = new TextField();
		FieldLabel NameLabel = new FieldLabel(NAME, "Name");
		SUBTYPE = new TextField();
		FieldLabel TypeLabel = new FieldLabel(SUBTYPE, "Type");

		container.add(NameLabel, new VerticalLayoutData(1, -1));
		container.add(TypeLabel, new VerticalLayoutData(1, -1));
		container.add(ActionLabel, new VerticalLayoutData(1, -1));

		this.setWidget(container);
	}
	public Widget asWidget() {
		return this;
	}
	public void loadData(DataObj data){
		if(data.getACTION()!=null)this.ACTION.setValue(data.getACTION());
		if(data.getNAME()!=null)this.NAME.setValue(data.getNAME());
		if(data.getSUBTYPE()!=null)this.SUBTYPE.setValue(data.getSUBTYPE());

	}


}

