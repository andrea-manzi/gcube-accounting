package org.gcube.messaging.accounting.nodeaccountingportlet.client;

import org.gcube.messaging.accounting.nodeaccountingportlet.client.Costants.Colors;

import com.googlecode.gchart.client.GChart;
import com.gwtext.client.data.Record;
import com.gwtext.client.widgets.Window;
/**
 * 
 * @author Andrea Manzi (CERN)
 *
 */
public class NodeAccountingChart extends GChart {

	final int WIDTH = 500;
	final int HEIGHT = 300;
	private String title;
	private int maxRecords = 0;

	public NodeAccountingChart(String title,Record [] records ) {
		this.title=title;
		setChartSize(WIDTH, HEIGHT);
		setChartTitle("<b><big>" + title + "</big><br>&nbsp;</b>");
		maxRecords = max( records);

		for (int iCurve=0; iCurve < records.length; iCurve++) { 
			addCurve();     
			getCurve().getSymbol().setSymbolType(
					SymbolType.VBAR_SOUTHWEST);
			getCurve().getSymbol().setBackgroundColor(Colors.values()[iCurve%Colors.values().length].name());
			getCurve().setLegendLabel(records[iCurve].getAsString(records[iCurve].getFields()[1]));
			getCurve().getSymbol().setHovertextTemplate(
			GChart.formatAsHovertext(records[iCurve].getAsString(records[iCurve].getFields()[1])));
			getCurve().getSymbol().setModelWidth(1.0);
			getCurve().getSymbol().setBorderColor(Colors.black.name());
			getCurve().getSymbol().setBorderWidth(1);

			getCurve().addPoint(iCurve+1,
					records[iCurve].getAsDouble(records[iCurve].getFields()[0]));
		}

		getXAxis().addTick( records.length/2. +1,records[0].getFields()[1]); 
	
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

	public static int max(Record[] t) {
		int maximum = t[0].getAsInteger(t[0].getFields()[0]);   
		for (int i=1; i<t.length; i++) {
			if (t[i].getAsInteger(t[i].getFields()[0]) > maximum) {
				maximum = t[i].getAsInteger(t[i].getFields()[0]);  
			}
		}
		return maximum;
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

