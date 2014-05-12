package org.gcube.messaging.accounting.portalaccountingportlet.client.panel;

import org.gcube.messaging.accounting.portalaccountingportlet.client.AccountingCostants;
import org.gcube.messaging.accounting.portalaccountingportlet.client.data.ContentPair;

import com.gwtext.client.core.Position;
import com.gwtext.client.widgets.PaddedPanel;
import com.gwtext.client.widgets.form.FieldSet;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.layout.ColumnLayoutData;

public class ContentPanel extends FormPanel{
	FieldSet fieldSet = new FieldSet();  
	
	public ContentPanel(){
		this.setTitle("Content Details");
		this.setWidth(280);
		this.setHeight(400);
		this.setId(AccountingCostants.ContentPanelId);
		this.setLabelAlign(Position.TOP);  
		
		fieldSet.setLabelWidth(60);    
		fieldSet.setAutoHeight(true);  
		fieldSet.setBorder(false);  
		TextField field1 =  new TextField("Object Id", "identifier", 200);
		TextField field2 = 	new TextField("Object Name", "name", 200);
		field1.setGrow(true);
		field2.setGrow(true);
		fieldSet.add(field1);
		fieldSet.add(field2);
		this.add(new PaddedPanel(fieldSet, 0, 0, 0, 0), new ColumnLayoutData(0.4));
	
	}
	
	public void loadContent(ContentPair pair){
		this.getForm().findField("identifier").setValue(pair.getContentId());
		this.getForm().findField("name").setValue(pair.getContentName());
	}
}