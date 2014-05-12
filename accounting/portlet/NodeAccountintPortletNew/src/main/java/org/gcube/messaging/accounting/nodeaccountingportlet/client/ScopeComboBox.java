package org.gcube.messaging.accounting.nodeaccountingportlet.client;

import java.util.ArrayList;

import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.Reader;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.Store;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.event.ComboBoxListenerAdapter;

public class ScopeComboBox extends ComboBox{

	public ScopeComboBox(int Width) {
		Reader reader = new ArrayReader(RecordDefinition.getScopeRecord()); 
		this.setFieldLabel("callerScope");  
		this.setStore(new Store(reader));  
		this.setDisplayField("callerScope");  
		this.setMode(ComboBox.LOCAL);  
		this.setTriggerAction(ComboBox.ALL);
		this.setEmptyText("Select caller scope");  
		this.setLoadingText("Loading...");  
		this.setTypeAhead(true);  
		this.setSelectOnFocus(true);  
		this.setWidth(Width-20);
		this.setHideTrigger(false);
		this.setListWidth(this.getWidth()-20);
		this.addListener(new ComboBoxListenerAdapter() {  	 	
				public void onSelect(ComboBox comboBox, Record record, int index) {  
					GlobalInfo.get().setSelectedScope(record.getAsString("callerScope"));
					NodeAccountingUI.get().central.grid.updateGrid();
					super.onSelect(comboBox, record, index);  
			} 
		});

	}
	
	public void updateCB(ArrayList<ArrayList<String>> scope) {
		Record[] records = new Record[scope.size()-1];
		for (int i = 1;i< scope.size();i++)
			for (String sc :scope.get(i)) {
				records[i-1]=RecordDefinition.getScopeRecord().createRecord(new String[]{sc});
			}
		this.getStore().removeAll();
		this.getStore().add(records);
	}
	
}
