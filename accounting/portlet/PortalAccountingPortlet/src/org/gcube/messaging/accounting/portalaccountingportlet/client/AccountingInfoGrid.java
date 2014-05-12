package org.gcube.messaging.accounting.portalaccountingportlet.client;

import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.event.PanelListenerAdapter;


/**
 * 
 * @author Andrea Manzi (CERN)
 *
 */
public class AccountingInfoGrid extends Panel {

	public AccountingLiveGrid  grid=new AccountingLiveGrid();
	public AccountingStatisticsPanel statistics = new AccountingStatisticsPanel(); 

	AccountingInfoGrid() {
		this.setHeight(800);
		this.add(grid);
		this.add(statistics);
		this.setFrame(true);	
		
		this.addListener(new PanelListenerAdapter(){                   
			public void onShow(Component component) {
				grid.getStore().reload();
				statistics.grid.getStore().reload();
			}
		});
	}

}
