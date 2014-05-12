package org.gcube.messaging.accounting.portalaccountingportlet.client;

import java.util.ArrayList;


import org.gcube.messaging.accounting.portalaccountingportlet.client.detailpanel.*;
import org.gcube.messaging.accounting.portalaccountingportlet.client.obj.DataObj;
import org.gcube.messaging.accounting.portalaccountingportlet.client.obj.TermPair;

import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.TabItemConfig;
import com.sencha.gxt.widget.core.client.TabPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutPack;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.info.Info;

public class PopUpDetailPanel extends FramedPanel{

	public TabPanel detailsContainer;
	public int tabPanelWidth;
	public int tabPanelHeight;
	//panels
	public CollectionPanel collectionPanel = new CollectionPanel();
	public TermPanel termPanel = new TermPanel();
	public BrowsePanel browsePanel=new BrowsePanel();
	public TSPanel tsPanel = new TSPanel();
	public ContentPanelOrig contentPanel=new ContentPanelOrig();
	public HLPanel hlPanel=new HLPanel();
	public AISPanel aisPanel=new AISPanel();
	public GCUBEUsersPanel gcubeUsersPanel=new GCUBEUsersPanel();
	public AnnotationPanel annotationPanel=new AnnotationPanel();
	public ReportGeneratorPanel reportGenPanel=new ReportGeneratorPanel();
	public TemplateGeneratorPanel templateGenPanel=new TemplateGeneratorPanel();
	public WorkflowDocumentPanel workflowPanel=new WorkflowDocumentPanel();
	public GHNDetailPanel ghnDetailPanel=new GHNDetailPanel();
	public WebAppPanel webappPanel=new WebAppPanel();
	public WebAppDetailPanel webappdetailPanel=new WebAppDetailPanel();
	public AquamapsPanel aquamapsPanel=new AquamapsPanel();
	public WarPanel warPanel=new WarPanel();
	public SMPanel smPanel=new SMPanel();


	PopUpDetailPanel() {
		this.setHeadingText("Details Panel");
		this.setWidth(650);
		this.setHeight(300); 

		tabPanelWidth=600;
		tabPanelHeight=200;

		detailsContainer = new TabPanel();
		detailsContainer.setPixelSize(tabPanelWidth, tabPanelHeight);
		detailsContainer.setAnimScroll(true);
		detailsContainer.setTabScroll(true);
		//detailsContainer.setCloseContextMenu(true);

		this.setWidget(detailsContainer);	
		this.setButtonAlign(BoxLayoutPack.START);

		TextButton closeButton=new TextButton("Close");
		closeButton.addSelectHandler(new SelectHandler() {
			public void onSelect(SelectEvent event) {	
				AccountingUI.get().dialogBoxGen.hide();
			}
		});
		this.addButton(closeButton);		
	}

	public void reCreateTabPanel(){
		detailsContainer.clear();
		detailsContainer.removeFromParent();
		detailsContainer = new TabPanel();
		detailsContainer.setPixelSize(tabPanelWidth, tabPanelHeight);
		detailsContainer.setAnimScroll(true);
		detailsContainer.setTabScroll(true);
		this.setWidget(detailsContainer);
	}
	public void showMessage(String msg){
		AccountingUI.get().dialogBoxGen.printMsgInDialogBox(msg);
	}


	public void loadPanel(DataObj record){
		reCreateTabPanel();

		switch(GlobalInfo.getSelectedRecord()){
		case SimpleSearchRecord:
			detailsContainer.add(collectionPanel, new TabItemConfig("Collection Details", false));
			detailsContainer.add(termPanel, new TabItemConfig("Term Details", false));

			collectionPanel.removeOperator();

			
			TermPair pair = new TermPair();
			pair.setTermValue(record.getTermValue());
			ArrayList<TermPair> pairs = new ArrayList<TermPair>();
			pairs.add(pair);
			termPanel.loadCollectionsDetails(pairs,true);
			break;
		case QuickSearchRecord:
			detailsContainer.add(termPanel, new TabItemConfig("Term Details", false));

			pair = new TermPair();
			pair.setTermValue(record.getTermValue());
			pairs = new ArrayList<TermPair>();
			pairs.add(pair);
			termPanel.loadCollectionsDetails(pairs,true);
			break;
		case GoogleSearchRecord:
			detailsContainer.add(termPanel, new TabItemConfig("Term Details", false));

			pair = new TermPair();
			pair.setTermValue(record.getTermValue());
			pairs = new ArrayList<TermPair>();
			pairs.add(pair);
			termPanel.loadCollectionsDetails(pairs,true);
			break;
		case AdvancedSearchRecord:
			detailsContainer.add(collectionPanel, new TabItemConfig("Collection Details", false));
			detailsContainer.add(termPanel, new TabItemConfig("Term Details", false));
			collectionPanel.addOperator(record.getOperator());
			break;
		case BrowseRecord:
			detailsContainer.add(collectionPanel, new TabItemConfig("Collection Details", false));
			detailsContainer.add(browsePanel,new TabItemConfig("Browse Details", false));
			browsePanel.loadData(record);
			collectionPanel.removeOperator();
			break;
		case ContentRecord:
			detailsContainer.add(contentPanel,new TabItemConfig("Content Details", false));
			break;
		case TSRecord:
			detailsContainer.add(tsPanel,new TabItemConfig("TS Details", false));						
			tsPanel.loadData(record);
			break;
		case AISRecord:
			detailsContainer.add(aisPanel,new TabItemConfig("AIS Details", false));			
			aisPanel.loadData(record);
			break;
		case HLRecord:
			detailsContainer.add(hlPanel,new TabItemConfig("HL Details", false));

			hlPanel.loadData(record);
			if(record.getSUBTYPE()!=null){
				if (record.getSUBTYPE().compareTo(AccountingCostants.HLSubTypeSent)== 0){
					detailsContainer.add(gcubeUsersPanel,new TabItemConfig("GCUBE Users", false));
				}
			}
			break;
		case AnnotationRecord:
			detailsContainer.add(annotationPanel,new TabItemConfig("Annotation", false));
			annotationPanel.loadData(record);
			detailsContainer.add(contentPanel,new TabItemConfig("Content Details", false));
			break;
		case DocumentWorkflowRecord:
			detailsContainer.add(workflowPanel,new TabItemConfig("Workflow", false));
			workflowPanel.loadData(record);
			break;
		case ReportRecord:
						
			if (record.getSUBTYPE()!=null){
				if(record.getSUBTYPE().compareTo(AccountingCostants.GENERATE_REPORT_OUTPUT)== 0){
					detailsContainer.add(reportGenPanel,new TabItemConfig("Report", false));
					reportGenPanel.loadData(record);
				}
				else {
					detailsContainer.add(templateGenPanel,new TabItemConfig("Template", false));
					templateGenPanel.loadData(record);
				}
			}
			else {
				detailsContainer.add(templateGenPanel,new TabItemConfig("Template", false));
				templateGenPanel.loadData(record);
			}
			break;
		case AquamapsRecord:
			detailsContainer.add(aquamapsPanel,new TabItemConfig("Aqua Maps", false));
			aquamapsPanel.loadData(record);
			break;
		case WebAppRecord:
			detailsContainer.add(webappPanel,new TabItemConfig("WebApp", false));
			webappPanel.loadData(record);
			detailsContainer.add(ghnDetailPanel,new TabItemConfig("GHN Details", false));
			detailsContainer.add(webappdetailPanel,new TabItemConfig("WebApp Details", false));
			break;
		case WarRecord:
			detailsContainer.add(warPanel,new TabItemConfig("War", false));
			warPanel.loadData(record);
			break;
		case StatisticalManagerRecord:
			detailsContainer.add(smPanel,new TabItemConfig("Statistical Manager details", false));
			smPanel.loadData(record);
			break;
		case LoginRecord:
		case GenericRecord:
			break;
		case Empty:
			this.showMessage("detailsContainer not available, please select first a record type");
			break;
		default:
			break;
		}
		AccountingUI.get().dialogBoxGen.createDialogBox(this);		
	}


	public Widget asWidget() {
		// TODO Auto-generated method stub
		return null;
	}

}

