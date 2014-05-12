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

public class AquamapsPanel extends FormPanel implements IsWidget, Editor<DataObj> {

	public TextField SUBTYPE,TITLE,AQUAMAPSTYPE,SPECIESCOUNT,GIS,HSPECID,OBJECTID;
	private static AquamapsPanel singleton;
	
	public static AquamapsPanel get(){
		return singleton;
	}
	
	public AquamapsPanel() {
		singleton=this;
		this.setTitle("Aquamaps Options");
		this.setWidth(280);
		this.setHeight(400);
		this.setId(AccountingCostants.AquamapsPanelId);

		VerticalLayoutContainer  container = new VerticalLayoutContainer();
		SUBTYPE = new TextField();
		FieldLabel subtypeLabel=new FieldLabel(SUBTYPE, "Type");
		TITLE = new TextField();
		FieldLabel titleLabel = new FieldLabel(TITLE, "Title");
		AQUAMAPSTYPE = new TextField();
		FieldLabel aquamapsType = new FieldLabel(AQUAMAPSTYPE, "AquamapsType");
		SPECIESCOUNT = new TextField();
		FieldLabel speciesCountLabel = new FieldLabel(SPECIESCOUNT, "SpeciesCount");
		GIS = new TextField();
		FieldLabel gisLabel = new FieldLabel(GIS, "GISEnabled");
		HSPECID = new TextField();
		FieldLabel hspecidLabel = new FieldLabel(HSPECID, "HSPECID");
		OBJECTID = new TextField();
		FieldLabel objectIdLabel = new FieldLabel(OBJECTID, "ObjectId");
		
		container.add(titleLabel, new VerticalLayoutData(1, -1));
		container.add(aquamapsType, new VerticalLayoutData(1, -1));
		container.add(subtypeLabel, new VerticalLayoutData(1, -1));
		container.add(speciesCountLabel, new VerticalLayoutData(1, -1));
		container.add(gisLabel, new VerticalLayoutData(1, -1));
		container.add(hspecidLabel, new VerticalLayoutData(1, -1));
		container.add(objectIdLabel, new VerticalLayoutData(1, -1));
		
		this.setWidget(container);
	}
	public Widget asWidget() {
		return this;
	}

	public void loadData(DataObj data){
		if(data.getTITLE()!=null)this.TITLE.setValue(data.getTITLE());
		if(data.getSUBTYPE()!=null)this.SUBTYPE.setValue(data.getSUBTYPE());
		if(data.getAQUAMAPSTYPE()!=null)this.AQUAMAPSTYPE.setValue(data.getAQUAMAPSTYPE());
		if(data.getSPECIESCOUNT()!=null)this.SPECIESCOUNT.setValue(data.getSPECIESCOUNT());
		if(data.getGIS()!=null)this.GIS.setValue(data.getGIS());
		if(data.getHSPECID()!=null)this.HSPECID.setValue(data.getHSPECID());
		if(data.getOBJECTID()!=null)this.OBJECTID.setValue(data.getOBJECTID());

	}

}

