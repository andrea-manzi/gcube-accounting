package org.gcube.messaging.accounting.nodeaccountingportlet.client;

import com.gwtext.client.data.Record;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.layout.VerticalLayout;

/**
 * 
 * @author Andrea Manzi (CERN)
 *
 */

public class DetailPanel extends Panel{

	DetailPanel() {
		this.setLayout(new VerticalLayout(1));
		this.setAutoScroll(true);
		this.setWidth(0);
		this.setHeight(0);
		

	}

	public void showMessage(String msg){
		MessageBox.alert(msg);
	}
	
	
	public void loadPanel(Record record){
	
	}
	
}

