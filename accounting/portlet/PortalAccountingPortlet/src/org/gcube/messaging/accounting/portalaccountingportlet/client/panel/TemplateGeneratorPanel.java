package org.gcube.messaging.accounting.portalaccountingportlet.client.panel;

import org.gcube.messaging.accounting.portalaccountingportlet.client.AccountingCostants;

import com.gwtext.client.core.Position;
import com.gwtext.client.widgets.PaddedPanel;
import com.gwtext.client.widgets.form.FieldSet;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.layout.ColumnLayoutData;

/**
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public class TemplateGeneratorPanel extends FormPanel{

	public TemplateGeneratorPanel(){
		this.setTitle("Template/Report Generator Options");
		this.setWidth(280);
		this.setHeight(400);
		this.setId(AccountingCostants.TemplateGeneratorPanelId);
		this.setLabelAlign(Position.TOP);  
		FieldSet fieldSet = new FieldSet();  
		fieldSet.setLabelWidth(60);    
		fieldSet.setAutoHeight(true);  
		fieldSet.setBorder(false);
		TextField field1 = new TextField("SubType", "SUBTYPE", 100);
		field1.setGrow(true);
		TextField field2 = new TextField("Template ID", "TEMPLATEID", 100);
		field2.setGrow(true);
		TextField field3 = new TextField("Template/Report Name", "TEMPLATENAME", 100);
		field3.setGrow(true);
		TextField field4 = new TextField("Author", "AUTHOR", 100);
		field4.setGrow(true);
		
		fieldSet.add(field1);
		fieldSet.add(field2);
		fieldSet.add(field3);
		fieldSet.add(field4);

		this.add(new PaddedPanel(fieldSet, 0, 0, 0, 0), new ColumnLayoutData(0.4));
	
	}
}

