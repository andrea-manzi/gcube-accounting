package org.gcube.messaging.accounting.nodeaccountingportlet.charts;


import org.gcube.messaging.accounting.nodeaccountingportlet.client.GlobalInfo;
import org.gcube.messaging.accounting.nodeaccountingportlet.client.Costants.Colors;

import com.googlecode.gchart.client.GChart;
import com.gwtext.client.data.Record;
import com.gwtext.client.widgets.Window;
/**
 * 
 * @author Andrea Manzi (CERN)
 *
 */
public class BarChart extends GChart {

	final int WIDTH = 500;
	final int HEIGHT = 300;
	private String title;
	double maxRecords = 0;
	int maxRecordsCount = 0;
	private static int MAX_RECORDS = 30; 
	

	
	
	public BarChart(String title,Record [] records ) {
		this.title=title;
		setChartSize(WIDTH, HEIGHT);
		setChartTitle("<b><big>" + title + "</big><br>&nbsp;</b>");
		maxRecordsCount = getRecourdsCount(records);
		maxRecords = max( records);

		for (int iCurve=0; iCurve < maxRecordsCount; iCurve++) { 
			addCurve();     
			getCurve().getSymbol().setSymbolType(
					SymbolType.VBAR_SOUTHWEST);
			getCurve().getSymbol().setBackgroundColor(Colors.values()[iCurve%Colors.values().length].name());
			getCurve().setLegendLabel(records[iCurve].getAsString(records[iCurve].getFields()[2]));
			getCurve().getSymbol().setHovertextTemplate(
			GChart.formatAsHovertext(records[iCurve].getAsString(records[iCurve].getFields()[2])));
			getCurve().getSymbol().setModelWidth(1.0);
			getCurve().getSymbol().setBorderColor(Colors.black.name());
			getCurve().getSymbol().setBorderWidth(1);

			getCurve().addPoint(iCurve+1,
					records[iCurve].getAsDouble((GlobalInfo.get().getGraphIndex())));
		}

		getXAxis().addTick( maxRecordsCount/2. +1,GlobalInfo.get().getGraphTypeTitle()); 
	
		getXAxis().setTickLabelFontSize(20);
		getXAxis().setTickLabelThickness(40);
		getXAxis().setTickLength(6);     
		getXAxis().setTickThickness(0); 
		getXAxis().setAxisMin(0);        
		getYAxis().setAxisMin(0);         
		getYAxis().setAxisMax(maxRecords);
		getYAxis().setTickLabelFormat("####");
		if (maxRecords <10) getYAxis().setTickCount(2);
		getYAxis().setHasGridlines(true);
	}

	public  double max(Record[] t) {
		double maximum = t[0].getAsDouble(GlobalInfo.get().getGraphIndex());   
		for (int i=1; i<maxRecordsCount; i++) {
			if (t[i].getAsDouble(GlobalInfo.get().getGraphIndex()) > maximum) {
				maximum = t[i].getAsDouble(GlobalInfo.get().getGraphIndex());  
			}
		}
		return maximum;
	}

	private int getRecourdsCount(Record [] records){
		System.out.println("Length: " +records.length);
		return (records.length< MAX_RECORDS)? records.length: MAX_RECORDS;
	}
	
	
	public void showGraph(){
		final Window window = new Window();  
		window.setTitle(title);  
		window.setClosable(true);  
		window.setWidth(WIDTH*2);  
		window.setHeight(HEIGHT*2);  
		window.setPlain(true);  
		window.add(this) ;
		window.setCloseAction(Window.CLOSE);
		this.update();
		window.show();
	
	}
}

