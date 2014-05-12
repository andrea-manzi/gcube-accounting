package org.gcube.messaging.accounting.portalaccountingportlet.client;

import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;



public class AccountingRightPanel extends VerticalLayoutContainer {

	public MainGridPanel grid=new MainGridPanel();
	public StatisticsGridPanel statistics = new StatisticsGridPanel(); 
	public int HEIGHT=800;

	AccountingRightPanel() {
		this.setHeight(HEIGHT);
		this.add(grid);
		this.add(statistics);	
		
	}

}
