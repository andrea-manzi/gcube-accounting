package org.gcube.messaging.accounting.portalaccountingportlet.client;

import java.util.ArrayList;

import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.tree.TreeNode;
import com.gwtext.client.widgets.tree.TreePanel;
import com.gwtext.client.widgets.tree.event.TreePanelListenerAdapter;

/**
 * 
 * @author Andrea Manzi (CERN)
 *
 */
public class AccountingUserTree extends TreePanel {
	private static int WIDTH=220;
	private static int HEIGHT=400;	
	private static String title="Portal Users";
	static AccountingUserTree singleton;
	
	AccountingUserTree(){
		super("Portal Users");		
		singleton = this;
		this.setSize(WIDTH, HEIGHT);
		this.setAutoScroll(true);
		TreeNode root=new TreeNode(title);
		this.setRootNode(root);		
		setRootVisible(false);
		this.addListener(treeListener);
		this.getRootNode().setExpanded(true);
		this.getRootNode().setAttribute("ID", title);
		AccountingUI.service.getUsers(Callbacks.listUsersCallback);
			
	}
	
	public static AccountingUserTree get() {
		return singleton;
	}
	
	 TreePanelListenerAdapter treeListener= new TreePanelListenerAdapter(){ 
	    	
	    	public void onClick(TreeNode node, EventObject e){
	    		super.onClick(node, e);
	    		GlobalInfo.setSelectedUSer(node.getText());
	    		AccountingUI.get().accountingUI.grid.updateGrid();
	    			
	    	}
	    };

	public void populateTree(ArrayList<ArrayList<String>> users){
		
		for (int i = 1;i< users.size();i++){
			for (String user :users.get(i)) {
				TreeNode typeLeaf = new TreeNode(user);  
				typeLeaf.setExpanded(true);
				typeLeaf.setIconCls("user_icon");
				this.getRootNode().appendChild(typeLeaf);    
				this.setTitle("Portal Users : " + (users.size()-1));
			}
		}  
		
	}
}
