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
public class AquamapsPanel  extends FormPanel{

	public AquamapsPanel(){
		this.setTitle("Aquamaps Options");
		this.setWidth(280);
		this.setHeight(400);
		this.setId(AccountingCostants.AquamapsPanelId);
		this.setLabelAlign(Position.TOP);  
		FieldSet fieldSet = new FieldSet();  
		fieldSet.setLabelWidth(60);    
		fieldSet.setAutoHeight(true);  
		fieldSet.setBorder(false);
		TextField field1 = new TextField("Title", "TITLE", 100);
		field1.setGrow(true);
		TextField field2 = new TextField("AquamapsType", "AQUAMAPSTYPE", 100);
		field2.setGrow(true);
		TextField field3 = new TextField("Type", "SUBTYPE", 100);
		field3.setGrow(true);
		TextField field4 = new TextField("SpeciesCount", "SPECIESCOUNT", 100);
		field4.setGrow(true);
		TextField field5 = new TextField("GISEnabled", "GIS", 100);
		field5.setGrow(true);
		TextField field6 = new TextField("HSPECID", "HSPECID", 100);
		field6.setGrow(true);
		TextField field7 = new TextField("ObjectId", "OBJECTID", 100);
		field7.setGrow(true);
		
		fieldSet.add(field1);
		fieldSet.add(field2);
		fieldSet.add(field3);
		fieldSet.add(field4);
		fieldSet.add(field5);
		fieldSet.add(field6);
		fieldSet.add(field7);
		this.add(new PaddedPanel(fieldSet, 0, 0, 0, 0), new ColumnLayoutData(0.4));
	
	}
}
