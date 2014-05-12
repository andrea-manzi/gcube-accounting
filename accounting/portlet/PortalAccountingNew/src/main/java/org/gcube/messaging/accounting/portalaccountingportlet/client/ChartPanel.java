package org.gcube.messaging.accounting.portalaccountingportlet.client;

import java.util.ArrayList;
import java.util.List;

import org.gcube.messaging.accounting.portalaccountingportlet.client.obj.ChartObj;
import org.gcube.messaging.accounting.portalaccountingportlet.client.obj.ChartObjProperties;
import org.gcube.messaging.accounting.portalaccountingportlet.client.obj.Statistics;

import com.google.gwt.core.client.GWT;
import com.sencha.gxt.chart.client.chart.Chart;
import com.sencha.gxt.chart.client.chart.Chart.Position;
import com.sencha.gxt.chart.client.chart.axis.CategoryAxis;
import com.sencha.gxt.chart.client.chart.axis.NumericAxis;
import com.sencha.gxt.chart.client.chart.series.BarSeries;
import com.sencha.gxt.chart.client.draw.RGB;
import com.sencha.gxt.chart.client.draw.sprite.TextSprite;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutPack;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;


public class ChartPanel extends ContentPanel {

	final int WIDTH = 600;
	final int HEIGHT = 450;
	private String title;

	public List<ChartObj> chartObjList;
	public String label;

	private static final ChartObjProperties props = GWT.create(ChartObjProperties.class);

	public ChartPanel(String titleHead,List<Statistics> stats ) {

		final ListStore<ChartObj> store = new ListStore<ChartObj>(props.key());
		createChartObjList(stats);
		store.addAll(chartObjList);

		final Chart<ChartObj> chart = new Chart<ChartObj>();
		chart.setStore(store);
		chart.setShadowChart(true);

		NumericAxis<ChartObj> axis = new NumericAxis<ChartObj>();
		axis.setPosition(Position.BOTTOM);
		axis.addField(props.value());
		TextSprite title = new TextSprite("Value");
		title.setFontSize(18);
		axis.setTitleConfig(title);
		axis.setDisplayGrid(true);
		axis.setMinimum(0);
		axis.setMaximum(max(stats));
		chart.addAxis(axis);

		CategoryAxis<ChartObj, String> catAxis = new CategoryAxis<ChartObj, String>();
		catAxis.setPosition(Position.LEFT);
		catAxis.setField(props.name());
		title = new TextSprite(getGroupOptionLabel(stats));
		title.setFontSize(16);
		catAxis.setTitleConfig(title);
		chart.addAxis(catAxis);

		final BarSeries<ChartObj> bar = new BarSeries<ChartObj>();
		bar.setYAxisPosition(Position.BOTTOM);
		bar.addYField(props.value());
		bar.addColor(new RGB(148, 174, 10));
		//bar.addColor(new RGB(17, 95, 166));
		//bar.addColor(new RGB(166, 17, 32));
		chart.addSeries(bar);

		/*final Legend<ChartObj> legend = new Legend<ChartObj>();
		legend.setPosition(Position.RIGHT);
		legend.setItemHighlighting(true);*/
		
	//	legend.setItemHiding(true);
	//	chart.setLegend(legend);

		chart.setAnimated(true);
		chart.setShadowChart(true);
		//    panel.getElement().getStyle().setMargin(10, Unit.);
		this.setHeadingText(titleHead);

		this.setPixelSize(WIDTH-5, HEIGHT-5);
		this.setBodyBorder(true);
			
		this.setWidget(chart);
	}

	public void createChartObjList(List<Statistics> list){
		List<ChartObj> listChart=new ArrayList<ChartObj>();
		for(Statistics st:list){
			ChartObj obj=new ChartObj();
			String label="";
			List<String> labels=new ArrayList<String>();
			if(st.getUser()!=null)labels.add("user:"+st.getUser());
			if(st.getType()!=null)labels.add("type:"+st.getType());
			if(st.getVre()!=null)labels.add("scope:"+st.getVre());
			if(st.getDate()!=null)labels.add("date:"+st.getDate());
			int i=1;
			for(String tmp:labels){
				label=label+tmp;
				if(i<labels.size())label=label+"\n";
				++i;
			}		
/*			if(st.getUser()!=null)label=label+("user:"+st.getUser());
			else if(st.getType()!=null)label=label+("type:"+st.getType());
			else if(st.getVre()!=null)label=label+("scope:"+st.getVre());
			else if(st.getDate()!=null)label=label+("date:"+st.getDate());*/
			
			obj.setName(label);
			obj.setValue(Double.valueOf(st.getCNT()));
			listChart.add(obj);
		}
		chartObjList=listChart;
	}

	public static int max(List<Statistics> t) {
		int maximum = Integer.valueOf(t.get(0).getCNT());  
		for (int i=1; i<t.size(); i++) {
			if (Integer.valueOf(t.get(i).getCNT()) > maximum) { 
				maximum = Integer.valueOf(t.get(i).getCNT());  
			}
		}
		return maximum;
	}
	
	public String getGroupOptionLabel(List<Statistics> list){
		String label="";
		List<String> labels=new ArrayList<String>();
		if(list.get(0).getUser()!=null)labels.add("user");
		if(list.get(0).getType()!=null)labels.add("type");
		if(list.get(0).getVre()!=null)labels.add("scope");
		if(list.get(0).getDate()!=null)labels.add("date");
		int i=1;
		if(list.size()>0)label="grouped by ";
		for(String tmp:labels){
			label=label+tmp;
			if(i<labels.size())label=label+", ";
			++i;
		}	
		return label;
	}

	public void showGraph(){
		FramedPanel panel=new FramedPanel();
		panel.setTitle(title);  
		panel.setHeadingText(title);  
		panel.setWidth(WIDTH);  
		//panel.setHeight(HEIGHT);  

		panel.setWidget(this) ;
		panel.setButtonAlign(BoxLayoutPack.START);

		TextButton okButton=new TextButton("OK");
		okButton.setBorders(true);
		okButton.addSelectHandler(new SelectHandler() {
			public void onSelect(SelectEvent event) {
				AccountingUI.get().dialogBoxGen.hide();
			}
		});
		panel.addButton(okButton);
		AccountingUI.get().dialogBoxGen.createDialogBox(panel);	
	}
}

