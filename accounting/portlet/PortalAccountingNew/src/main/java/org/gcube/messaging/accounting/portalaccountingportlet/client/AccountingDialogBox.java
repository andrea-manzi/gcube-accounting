package org.gcube.messaging.accounting.portalaccountingportlet.client;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutPack;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

public class AccountingDialogBox extends DialogBox{

	public AccountingDialogBox(){
		super();
	}
	
	/*
	 * initializing a dialog box with a widget inside
	 */
	public void createDialogBox (Widget widg){
		//Hiding if exists no matter showing or not
		/*if(AccountingUI.get().dialogBoxGen!=null){
			AccountingUI.get().dialogBoxGen.hide();
		}*/
		this.hide();
		this.clear();
		this.setText("[+]");
		this.setAnimationEnabled(true);
		this.getElement().getStyle().setZIndex(50);
		this.setWidget(widg);
		this.center();	
		//setting
		/*AccountingUI.get().dialogBoxGen=this;*/
	}
	
	/*
	 * initializing a dialog box with a message panel inside
	 */
	public void printMsgInDialogBox(String message){
		//Hiding if exists no matter showing or not
		/*if(AccountingUI.get().dialogBoxGen!=null){
			AccountingUI.get().dialogBoxGen.hide();
		}*/
		this.hide();
		this.clear();
		this.setAnimationEnabled(true);
		this.getElement().getStyle().setZIndex(100);
		this.setText("[+]");
		
		SafeHtmlBuilder builder = new SafeHtmlBuilder();				
		builder.appendEscapedLines(message);
		FramedPanel panel = new FramedPanel();
		panel.setHeadingText("");
		panel.setWidth(300);
		panel.setBodyStyle("background: none; padding: 5px");		
		HTML html = new HTML();
		html.setHTML(builder.toSafeHtml());
		panel.add(html);		
		panel.setButtonAlign(BoxLayoutPack.START);
		TextButton closeButton=new TextButton("Close");
		closeButton.addSelectHandler(new SelectHandler() {
			public void onSelect(SelectEvent event) {	
				AccountingUI.get().dialogBoxGen.hide(); //
			}
		});
		//adding the button
		panel.addButton(closeButton);
		//key handlers -------------
		final FocusPanel foc = new FocusPanel();
		foc.addKeyDownHandler(new KeyDownHandler() {
			public void onKeyDown(KeyDownEvent event) {	
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER ||
						event.getNativeKeyCode() == KeyCodes.KEY_ESCAPE) {
					AccountingUI.get().dialogBoxGen.hide(); //
				}
			}
		});
		foc.add(panel);
		//--------------

		this.setWidget(foc);
		this.center();	
		Timer focusTimer = new Timer() {  
			@Override
			public void run() {
				foc.setFocus(true);
			}
		};		
		focusTimer.schedule(200);
		//setting
		/*AccountingUI.get().dialogBoxGen=this;*/
	}
}
