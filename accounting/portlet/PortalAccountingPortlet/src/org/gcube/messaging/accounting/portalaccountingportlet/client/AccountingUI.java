package org.gcube.messaging.accounting.portalaccountingportlet.client;


import org.gcube.messaging.accounting.portalaccountingportlet.client.interfaces.AccountingUIService;
import org.gcube.messaging.accounting.portalaccountingportlet.client.interfaces.AccountingUIServiceAsync;

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
 * 
 * @author Andrea Manzi(CERN)
 *
 */
@SuppressWarnings("deprecation")
public class AccountingUI implements EntryPoint {
	
	public static AccountingUIServiceAsync service = (AccountingUIServiceAsync) GWT.create(AccountingUIService.class);
	private static ServiceDefTarget endpoint = (ServiceDefTarget) service;
	private static final String mainTitle = "Portal Accounting";
	private Panel mainPanel = new Panel(mainTitle);
	private String sessionID = "";	
	private static AccountingUI singleton;
	public AccountingInfoGrid accountingUI = null;
	public AccountingDetailPanel accountingDetail = null;

	
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
	public static AccountingUI get(){
		return singleton;
	}
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		sessionID = Cookies.getCookie("JSESSIONID");	
		endpoint.setServiceEntryPoint(GWT.getModuleBaseURL() + "AccountingUI");;
		singleton = this;
		createLayout();
		//Viewport viewport = new Viewport(mainPanel);
		/* Add a listener for the resizing of the window */
		com.google.gwt.user.client.Window.addWindowResizeListener(new WindowResizeListener(){

			public void onWindowResized(int width, int height) {
				RootPanel root = RootPanel.get(AccountingCostants.DIVID);
				
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
		
		RootPanel root = RootPanel.get(AccountingCostants.DIVID);
		mainPanel.setWidth(1200);
		mainPanel.setHeight(800);
        mainPanel.setBorder(false);
        mainPanel.setLayout(new FitLayout());  
        mainPanel.setId("MainPanel");
        
     	Panel borderPanel = new Panel(); 
        borderPanel.setLayout(new BorderLayout());  
       
        AccountingTreeContainer tree = new AccountingTreeContainer("Selection");
        BorderLayoutData westData = new BorderLayoutData(RegionPosition.WEST);  
        westData.setSplit(true);
        westData.setMargins(new Margins(0, 0, 0, 0));  
         
        borderPanel.add(tree, westData); 
		
        accountingUI = new AccountingInfoGrid();
		BorderLayoutData centerData = new BorderLayoutData(RegionPosition.CENTER);
		borderPanel.add(accountingUI, centerData);
		
		
		accountingDetail = new AccountingDetailPanel();
		accountingDetail.setTitle("Details");
		BorderLayoutData eastData = new BorderLayoutData(RegionPosition.EAST);
		eastData.setSplit(true);
		eastData.setMargins(new Margins(0, 0, 0, 0));
		borderPanel.add(accountingDetail, eastData);
		
		mainPanel.add(borderPanel);
		mainPanel.addTool(new Tool(Tool.REFRESH, new Function() {  
			public void execute() {  
				accountingUI.grid.getStore().reload();
			}  
		}, "Refresh")); 
		
		// add the mainPanel to the root panel
		root.add(mainPanel);
		 
	}
}

