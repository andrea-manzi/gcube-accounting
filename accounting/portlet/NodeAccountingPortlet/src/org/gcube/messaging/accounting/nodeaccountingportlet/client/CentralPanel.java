package org.gcube.messaging.accounting.nodeaccountingportlet.client;

import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.event.PanelListenerAdapter;


/**
 * 
 * @author Andrea Manzi (CERN)
 *
 */
public class CentralPanel extends Panel {

	public NodeRecordsLiveGrid  grid=new NodeRecordsLiveGrid();
	public StatisticsPanel statistics = new StatisticsPanel(); 

	CentralPanel() {
		this.setHeight(800);
		grid.setAutoWidth(true);
		statistics.setAutoWidth(true);
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
