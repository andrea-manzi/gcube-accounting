package org.gcube.messaging.accounting.portalaccountingportlet.client;


import java.util.ArrayList;
import java.util.List;

import org.gcube.messaging.accounting.portalaccountingportlet.client.interfaces.AccountingUIService;
import org.gcube.messaging.accounting.portalaccountingportlet.client.interfaces.AccountingUIServiceAsync;
import org.gcube.portlets.widgets.guidedtour.client.GCUBEGuidedTour;
import org.gcube.portlets.widgets.guidedtour.client.steps.GCUBETemplate1Text1Image;
import org.gcube.portlets.widgets.guidedtour.client.steps.GCUBETemplate2Text2Image;
import org.gcube.portlets.widgets.guidedtour.client.steps.TourStep;
import org.gcube.portlets.widgets.guidedtour.client.types.ThemeColor;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer.BorderLayoutData;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;



/*@SuppressWarnings("deprecation")
*/
public class AccountingUI implements EntryPoint,IsWidget {

	public static AccountingUIServiceAsync service = (AccountingUIServiceAsync) GWT.create(AccountingUIService.class);
	private static ServiceDefTarget endpoint = (ServiceDefTarget) service;
	private static final String mainTitle = "Portal Accounting";
	private String specificationLink="#";
	//private Panel mainPanel = new Panel(mainTitle);
	private FramedPanel mainPanel;
	private String sessionID = "";	
	private static AccountingUI singleton;
	public AccountingRightPanel accountingUI;
	public PopUpDetailPanel accountingDetail = new PopUpDetailPanel();
	public PopupPanel popUpForLoadingIcon;
	public int MINWIDTH=800;
	public int MAXWIDTH=1200;
	public int WIDTH;
	public int HEIGHT;
	//dialog box
	public AccountingDialogBox dialogBoxGen = new AccountingDialogBox();


	public void onModuleLoad() {
		showGuidedTour();
		mainPanel=new FramedPanel();
		mainPanel.setHeadingText(mainTitle);
		sessionID = Cookies.getCookie("JSESSIONID");	
		endpoint.setServiceEntryPoint(GWT.getModuleBaseURL() + "AccountingUI");;
		singleton = this;
		createLayout();

		final Timer resizeTimer = new Timer() {  
			@Override
			public void run() {
				RootPanel root = RootPanel.get(AccountingCostants.DIVID);
				int leftBorder = root.getAbsoluteLeft();
				int rightScrollBar = 17;
				int rootWidth = com.google.gwt.user.client.Window.getClientWidth() - 2* leftBorder - rightScrollBar;

				if (rootWidth > MAXWIDTH) WIDTH=MAXWIDTH;
				else if (rootWidth< MINWIDTH) WIDTH=MINWIDTH;					
				else WIDTH=rootWidth;

				mainPanel.setWidth(WIDTH);
			}
		};
		//resize handler
		Window.addResizeHandler(new ResizeHandler() {
			public void onResize(ResizeEvent event) {
				resizeTimer.schedule(200); //ms 
			}
		});
		resizeTimer.schedule(200); //also set dimensions at the beginning
	}

	public String getSessionID() {
		return sessionID;
	}

	public void notifyError(String msg){
		dialogBoxGen.printMsgInDialogBox(msg);
	}

	public void showMessage(String msg){
		dialogBoxGen.printMsgInDialogBox(msg);
	}
	public static AccountingUI get(){
		return singleton;
	}

	private void createLayout(){
		RootPanel root = RootPanel.get(AccountingCostants.DIVID);
		mainPanel.setWidth(1200);
		mainPanel.setHeight(800);
		mainPanel.setBorders(false);
		mainPanel.setId("MainPanel");

		final BorderLayoutContainer con = new BorderLayoutContainer();
		con.setBorders(true);

		//west
		ContentPanel west = new ContentPanel();
		BorderLayoutData westData = new BorderLayoutData(220);
		//westData.setCollapsible(true);
		westData.setSplit(true);
		//westData.setCollapseMini(true);
		westData.setMargins(new Margins(0, 5, 0, 5));
		//center
		ContentPanel center = new ContentPanel();
		MarginData centerData = new MarginData();
		center.setResize(false);

		//setting 
		con.setWestWidget(west, westData);
		con.setCenterWidget(center, centerData);
		//    con.setEastWidget(east, eastData);	 
		SimpleContainer simple = new SimpleContainer();
		simple.add(con, new MarginData(0));

		//west widget
		AccountingLeftPanel treeContainer = new AccountingLeftPanel("Filters");
		west.setWidget(treeContainer);
		west.setHeadingText("Filters");

		//center widget
		accountingUI = new AccountingRightPanel();
		center.setWidget(accountingUI);


		mainPanel.add(simple);

		ToolButton toolButton = new ToolButton("refresh");
		toolButton.addSelectHandler(new SelectHandler() {
			public void onSelect(SelectEvent event) {
				AccountingUI.get().accountingUI.grid.updateGrid();	
			}
		});
		toolButton.setWidth("50px");
		toolButton.getElement().setInnerText("refresh");
		toolButton.getElement().getStyle().setBorderStyle(BorderStyle.SOLID);
		toolButton.getElement().getStyle().setBorderWidth(1,Unit.PX );
		toolButton.getElement().getStyle().setBorderColor("black");	

		mainPanel.addTool(toolButton);
		// add the mainPanel to the root panel
		root.add(mainPanel);
	}

	private void showGuidedTour() {
		String title;
		List<String> images=new ArrayList<String>();
		List<String> textArrays=new ArrayList<String>();

		//Filter Panel
		images=new ArrayList<String>();
		textArrays=new ArrayList<String>();
		title="Filter Panel";
		images.add("images/AccountingUI-filter.png");
		textArrays.add("On the left side there is a panel where several filters " +
				"are provided. The client is able to choose the filter/filters he " +
				"wants by selecting (single/multi selection) and apply the filter/filters. " +
				"It is possible to clear one specific category or all of them." +
				"Provided filter categories are:" +
				"record type, user, scope and date." 
				);
		TourStep step1 = createTemplateStep(title,images,textArrays);

		// Main Grid
		images=new ArrayList<String>();
		textArrays=new ArrayList<String>();
		title="Main Grid";
		images.add("images/AccountingUI-mainGrid.png");
		textArrays.add("The main grid on the right side contains filtered information." +
				" The client by applying the filters he wants is able to show the filter " +
				"information in the grid and sort it by a specific column." 
				);
		TourStep step2 = createTemplateStep(title,images,textArrays);

		// Statistics Grid
		images=new ArrayList<String>();
		textArrays=new ArrayList<String>();
		title="Statistics Grid";
		images.add("images/AccountingUI-statisticsGrid.png");
		textArrays.add("The statistics panel is located on the right-down side and provides " +
				"both filted and grouped information. There is the possibility for " +
				"multiple grouping, showing result, exporting result and making charts" +
				" with them. Once the client select the grouping options he just clicks" +
				" on the 'create statistics' button and then he is able to make a chart" +
				" or export them. " 
				);
		TourStep step3 = createTemplateStep(title,images,textArrays);

		// More about Main Grid
		images=new ArrayList<String>();
		textArrays=new ArrayList<String>();
		title="More about Main Grid..";
		images.add("images/AccountingUI-popup.png");
		textArrays.add("By double clicking on one of the rows a pop up panel " +
				"is shown with all details information about the specific record."
				);
		TourStep step4 = createTemplateStep(title,images,textArrays);
		
		// More about Statistics Grid
		images=new ArrayList<String>();
		textArrays=new ArrayList<String>();
		title="More about Statistics Grid..";
		images.add("images/AccountingUI-selection.png");
		images.add("images/AccountingUI-export.png");
		textArrays.add("For making chart, specific selected records are expected. ");
		textArrays.add(
				"For exporting the result of the statistics " +
				"just click on the 'export to CSV' and a pop up " +
				"window will be shown for showing or saving the csv file. "
				);
		TourStep step5 = createTemplateStep(title,images,textArrays);
		
		// Chart
		images=new ArrayList<String>();
		textArrays=new ArrayList<String>();
		title="Chart";
		images.add("images/AccountingUI-chart.png");
		textArrays.add("The information provided in the chart depend on the " +
				"selected grouping options and the number of records shown."
				);
		TourStep step6 = createTemplateStep(title,images,textArrays);

		//step1.setTextVerticalAlignment(VerticalAlignment.ALIGN_MIDDLE);

		GCUBEGuidedTour gt = new GCUBEGuidedTour("The Accounting Portlet", 
				AccountingUI.class.getName(), 
				specificationLink, 780, 350, false, ThemeColor.BLUE);
		gt.addStep(step1);
		gt.addStep(step2);
		gt.addStep(step3);
		gt.addStep(step4);
		gt.addStep(step5);
		gt.addStep(step6);

		gt.openTour();
	}	


	private TourStep createTemplateStep( final String header,final List<String> images, final List<String> textArrays){
		if(images.size()!=textArrays.size())return null;
		if(images==null||textArrays==null||header==null)return null;

		TourStep step=null;
		if(images.size()==1){
			step = new GCUBETemplate1Text1Image(true) { 
				@Override
				public String setStepTitle() {
					return header;
				} 
				@Override
				public String setStepImage() {
					return images.get(0);
				} 
				@Override
				public String setStepBody() {
					return "<div style=\"line-height: 19px; padding: 10px; font-size: 14px; \">" +
							"<div style=\"padding-bottom: 40px;\">" +
							textArrays.get(0)+
							"</div>" +
							"</div>";
				}
			};
		}
		else if(images.size()==2){
			step = new GCUBETemplate2Text2Image(false) {
				@Override
				public String setStepTitle() {
					return header;
				} 
				@Override
				public String setStepImage() {
					return images.get(0);
				} 
				@Override
				public String setStepBody() {
					return "<div style=\"line-height: 19px; padding: 10px; font-size: 14px; \">" +
							"<div style=\"padding-bottom: 40px;\">" +
							textArrays.get(0)+
							"</div>" +
							"</div>";
				}
				@Override
				public String setStepOtherImage() {
					return images.get(1);
				}
				@Override
				public String setStepOtherBody() {
					return "<div style=\"line-height: 19px; padding: 10px; font-size: 14px; \">" +
							"<div style=\"padding-bottom: 40px;\">" +
							textArrays.get(1)+
							"</div>" +
							"</div>";
				}
			};			
		}
		return step;
	}

	public Widget asWidget() {
		// TODO Auto-generated method stub
		return null;
	}
}

