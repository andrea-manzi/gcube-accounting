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
public class ServiceTree extends TreePanel{	
	private static int HEIGHT=170;
	private static String LEAFTYPE="LEAFTYPE";
	static ServiceTree singleton;
	

	public static enum LeafType {
		CLASS("CLASS"),
		NAME("NAME");
		String type;
		LeafType(String type){this.type = type;}
		public String toString(){return this.type;}
	}

	public ServiceTree(String title)  {
		super(title);
		singleton=this;
		this.setTitle(title);  
		this.setHeight(HEIGHT);
		TreeNode root = new TreeNode(title);  
		this.setRootNode(root);
		this.setAutoScroll(true);
		this.addListener(treeListener);
		setRootVisible(false);  
		root.setExpanded(false); 
		NodeAccountingUI.nodeAccoutingService.getServices(Callbacks.listServicesCallback);		
		
	}
	
	public static ServiceTree get() {
		return singleton;
	}

	TreePanelListenerAdapter treeListener= new TreePanelListenerAdapter(){ 

		public void onClick(TreeNode node, EventObject e){
			super.onClick(node, e);
			if (node.getAttribute(LEAFTYPE).compareTo(LeafType.CLASS.name()) ==0){
				GlobalInfo.get().setSelectedServiceClass(node.getText());
				GlobalInfo.get().setSelectedServiceName("");
			}else{
				GlobalInfo.get().setSelectedServiceName(node.getText());
				GlobalInfo.get().setSelectedServiceClass(((TreeNode)node.getParentNode()).getText());
			}
					
			NodeAccountingUI.get().central.grid.updateGrid();

		}

	};

public void populateTree(Map<String,ArrayList<String>> services){
		this.collapseAll();
			for (String clazz :services.keySet()) {
				TreeNode classleaf = new TreeNode(clazz);  
				classleaf.setAttribute(LEAFTYPE, LeafType.CLASS);
				classleaf.setIconCls("service_class");
				classleaf.setExpanded(false);
				for (String name : services.get(clazz)){
					TreeNode leaf = new TreeNode(name);
					leaf.setAttribute(LEAFTYPE, LeafType.NAME);
					leaf.setIconCls("service_name");
					
					leaf.setExpandable(false);
					classleaf.appendChild(leaf);
				}
				this.getRootNode().appendChild(classleaf);    
				this.setTitle("Services" );
			}
		}  

	public void refresh(){
		for (Node child: this.getRootNode().getChildNodes()) child.remove();
		NodeAccountingUI.get().showLoading("Loading Services...",this.getId());
		NodeAccountingUI.nodeAccoutingService.getServices(Callbacks.listServicesCallback);
		
	}
}

	



