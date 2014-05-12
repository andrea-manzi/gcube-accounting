package org.gcube.messaging.accounting.nodeaccountingportlet.client;

import java.util.ArrayList;
import java.util.Map;


import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.Node;
import com.gwtext.client.widgets.layout.AnchorLayout;
import com.gwtext.client.widgets.tree.TreeNode;
import com.gwtext.client.widgets.tree.TreePanel;
import com.gwtext.client.widgets.tree.event.TreePanelListenerAdapter;

/**
 * 
 * @author Andrea Manzi (CERN)
 *
 */
public class GHNTree extends TreePanel {
	private static int HEIGHT=400;	
	private static String title="GHNs";
	private static String LEAFTYPE="LEAFTYPE";
	static GHNTree singleton;
	
	public static enum LeafType {
		DOMAIN("DOMAIN"),
		GHN("GHN");
		String type;
		LeafType(String type){this.type = type;}
		public String toString(){return this.type;}
	}
	
	GHNTree(){
		super("GHNs");		
		singleton = this;
		this.setHeight(HEIGHT);
		this.setAutoScroll(true);
		TreeNode root=new TreeNode(title);
		root.setExpanded(false);
		this.setRootNode(root);		
		setRootVisible(false);
		this.addListener(treeListener);
		this.getRootNode().setAttribute("ID", title);
		NodeAccountingUI.nodeAccoutingService.getGHNs(Callbacks.listGHNsCallback);
		
	}
	
	public static GHNTree get() {
		return singleton;
	}
	
	 TreePanelListenerAdapter treeListener= new TreePanelListenerAdapter(){ 
	    	
	    	public void onClick(TreeNode node, EventObject e){
	    		super.onClick(node, e);
	    		if (node.getAttribute(LEAFTYPE).compareTo(LeafType.DOMAIN.name())==0) {}
	    		else {
	    			GlobalInfo.get().setSelectedGHN(node.getText());
	    			NodeAccountingUI.get().central.grid.updateGrid();
	    		}
	    	}
	    };

	public void populateTree(Map<String,ArrayList<String>> ghns){
		
		this.collapseAll();
		for (String domain :ghns.keySet()) {
			TreeNode domainleaf = new TreeNode(domain);
			domainleaf.setIconCls("server_root");
			domainleaf.setExpanded(false);
			domainleaf.setAttribute(LEAFTYPE, LeafType.DOMAIN);
			for (String name : ghns.get(domain)){
				TreeNode ghn = new TreeNode(name);
				ghn.setAttribute(LEAFTYPE, LeafType.GHN);
				ghn.setIconCls("server_icon");
				ghn.setExpanded(false);
				domainleaf.appendChild(ghn);
			}
			this.getRootNode().appendChild(domainleaf);    
		}
	}
	
	public void refresh(){
		for (Node child: this.getRootNode().getChildNodes()) child.remove();
		NodeAccountingUI.get().showLoading("Loading GHNs...,",this.getId());
		NodeAccountingUI.nodeAccoutingService.getGHNs(Callbacks.listGHNsCallback);
		
	}

		
}
