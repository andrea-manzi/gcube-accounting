package org.gcube.messaging.accounting.nodeaccountingportlet.client;


import org.gcube.messaging.accounting.nodeaccountingportlet.stubs.NodeAccountingService;
import org.gcube.messaging.accounting.nodeaccountingportlet.stubs.NodeAccountingServiceAsync;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.WindowResizeListener;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.RootPanel;
import com.gwtext.client.core.Ext;
import com.gwtext.client.core.ExtElement;
import com.gwtext.client.core.Function;
import com.gwtext.client.core.Margins;
import com.gwtext.client.core.RegionPosition;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Tool;
import com.gwtext.client.widgets.layout.BorderLayout;
import com.gwtext.client.widgets.layout.BorderLayoutData;
import com.gwtext.client.widgets.layout.FitLayout;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class NodeAccountingUI implements EntryPoint {


	/**
	 * Create a remote service proxy to talk to the server-side  service.
	 */
	public static final NodeAccountingServiceAsync nodeAccoutingService = GWT
	.create(NodeAccountingService.class);


	private static ServiceDefTarget endpoint = (ServiceDefTarget) nodeAccoutingService;
	private static final String mainTitle = "Node Accounting";
	private Panel mainPanel = new Panel(mainTitle);
	private String sessionID = "";	
	private static NodeAccountingUI singleton;
	public CentralPanel central = null;
	public DetailPanel accountingDetail = null;
	public TreeContainer tree = null;


	public String getSessionID() {
		return sessionID;
	}

	public void showLoading(String message, String elementId){
		final ExtElement element = Ext.get(elementId);    
		element.mask(message);
	}

	public void hideLoading(String elementId){
		final ExtElement element = Ext.get(elementId);    
		element.unmask();
	}

	public void notifyError(String msg){
		MessageBox.alert(msg);
	}

	public void showMessage(String msg){
		MessageBox.alert(msg);
	}
	public static NodeAccountingUI get(){
		return singleton;
	}

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		sessionID = Cookies.getCookie("JSESSIONID");	
		endpoint.setServiceEntryPoint(GWT.getModuleBaseURL() + "NodeAccountingService"/*+ ";jsessionid=" + sessionID*/);
		singleton = this;
		createLayout();
		//Viewport viewport = new Viewport(mainPanel);
		/* Add a listener for the resizing of the window */
		com.google.gwt.user.client.Window.addWindowResizeListener(new WindowResizeListener(){

			public void onWindowResized(int width, int height) {
				RootPanel root = RootPanel.get(Costants.DIVID);

				int leftBorder = root.getAbsoluteLeft();

				int rightScrollBar = 17;

				int rootWidth = com.google.gwt.user.client.Window.getClientWidth() - 2* leftBorder - rightScrollBar;
				if (rootWidth > 1200)
					mainPanel.setWidth(1200);
				else
					mainPanel.setWidth(rootWidth);
			}

		});
	}

	private void createLayout(){

		RootPanel root = RootPanel.get(Costants.DIVID);
		mainPanel.setWidth(1200);
		mainPanel.setHeight(800);
		mainPanel.setBorder(false);
		mainPanel.setLayout(new FitLayout());  
		mainPanel.setId("MainPanel");

		Panel borderPanel = new Panel(); 
		borderPanel.setLayout(new BorderLayout());  

		tree = new TreeContainer("Selection");
		BorderLayoutData westData = new BorderLayoutData(RegionPosition.WEST);  
		westData.setSplit(true);
		westData.setMargins(new Margins(0, 0, 0, 0));  

		borderPanel.add(tree, westData); 

		central = new CentralPanel();
		BorderLayoutData centerData = new BorderLayoutData(RegionPosition.CENTER);
		borderPanel.add(central, centerData);


		accountingDetail = new DetailPanel();
		BorderLayoutData eastData = new BorderLayoutData(RegionPosition.EAST);
		eastData.setSplit(true);
		eastData.setMargins(new Margins(0, 0, 0, 0));
		borderPanel.add(accountingDetail, eastData);

		mainPanel.add(borderPanel);
		mainPanel.addTool(new Tool(Tool.REFRESH, new Function() {  
			public void execute() {  
				central.grid.getStore().reload();
			}  
		}, "Refresh")); 

		// add the mainPanel to the root panel
		root.add(mainPanel);

	}

}
