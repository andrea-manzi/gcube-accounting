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

public class TemplateGeneratorPanel extends FormPanel implements IsWidget, Editor<DataObj> {

	public TextField TEMPLATEID,SUBTYPE,TEMPLATENAME,AUTHOR;
	private static TemplateGeneratorPanel singleton;
	
	public static TemplateGeneratorPanel get(){
		return singleton;
	}
	
	public TemplateGeneratorPanel() {
		singleton=this;
		this.setTitle("Template/Report Generator Options");
		this.setWidth(280);
		this.setHeight(400);
		this.setId(AccountingCostants.TemplateGeneratorPanelId);

		VerticalLayoutContainer  container = new VerticalLayoutContainer();
		TEMPLATEID = new TextField();
		FieldLabel templateidLabel=new FieldLabel(TEMPLATEID, "Template ID");
		TEMPLATENAME = new TextField();
		FieldLabel templatenameLabel = new FieldLabel(TEMPLATENAME, "Template/Report Name");
		AUTHOR = new TextField();
		FieldLabel authorLabel = new FieldLabel(AUTHOR, "Author");
		SUBTYPE = new TextField();
		FieldLabel subtypeLabel = new FieldLabel(SUBTYPE, "SubType");

		container.add(subtypeLabel, new VerticalLayoutData(1, -1));
		container.add(templateidLabel, new VerticalLayoutData(1, -1));
		container.add(templatenameLabel, new VerticalLayoutData(1, -1));
		container.add(authorLabel, new VerticalLayoutData(1, -1));
		
		this.setWidget(container);
	}
	public Widget asWidget() {
		return this;
	}
	public void loadData(DataObj data){
		if(data.getTEMPLATEID()!=null)this.TEMPLATEID.setValue(data.getTEMPLATEID());
		if(data.getTEMPLATENAME()!=null)this.TEMPLATENAME.setValue(data.getTEMPLATENAME());
		if(data.getAUTHOR()!=null)this.AUTHOR.setValue(data.getAUTHOR());
		if(data.getSUBTYPE()!=null)this.SUBTYPE.setValue(data.getSUBTYPE());
		
	}


}

