package org.gcube.messaging.accounting.portalaccountingportlet.client;


import org.gcube.messaging.accounting.portalaccountingportlet.client.AccountingCostants.EntryType;

import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.tree.TreeNode;
import com.gwtext.client.widgets.tree.TreePanel;
import com.gwtext.client.widgets.tree.event.TreePanelListenerAdapter;

/**
 * 
 * @author Andrea Manzi (CERN)
 *
 */
public class AccountingTree extends TreePanel{
	private static int WIDTH=220;	
	private static int HEIGHT=170;



	public AccountingTree(String title)  {
		super(title);		
		this.setTitle(title);  
		this.setSize(WIDTH,HEIGHT);
		TreeNode root = new TreeNode(title);  
		this.setRootNode(root);
		this.setAutoScroll(true);

		for (AccountingCostants.EntryType type :AccountingCostants.EntryType.values()) {
			if (type.compareTo(EntryType.Empty) ==0)
				continue;
			TreeNode typeLeaf = new TreeNode(type.name());  
			typeLeaf.setExpanded(true);  
			root.appendChild(typeLeaf);    
		}

		this.addListener(treeListener);
		setRootVisible(false);  
		root.setExpanded(true); 

	}

	TreePanelListenerAdapter treeListener= new TreePanelListenerAdapter(){ 

		public void onClick(TreeNode node, EventObject e){
			super.onClick(node, e);
			GlobalInfo.setSelectedRecord(AccountingCostants.EntryType.valueOf(node.getText()));
			AccountingUI.get().accountingUI.grid.updateGrid();

		}

	};

}



