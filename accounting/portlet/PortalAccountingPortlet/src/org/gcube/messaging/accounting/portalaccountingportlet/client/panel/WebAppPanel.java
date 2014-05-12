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
 * @author Andrea Manzi (CERN)
 *
 */
public class WebAppPanel  extends FormPanel{

	public WebAppPanel(){
		this.setTitle("WebApp Options");
		this.setWidth(280);
		this.setHeight(400);
		this.setId(AccountingCostants.WebAppPanelId);
		this.setLabelAlign(Position.TOP);  
		FieldSet fieldSet = new FieldSet();  
		fieldSet.setLabelWidth(60);    
		fieldSet.setAutoHeight(true);  
		fieldSet.setBorder(false);
		TextField field3 = new TextField("Type", "SUBTYPE", 100);
		field3.setGrow(true);
		fieldSet.add(field3);
		this.add(new PaddedPanel(fieldSet, 0, 0, 0, 0), new ColumnLayoutData(0.4));
	
	}
}
