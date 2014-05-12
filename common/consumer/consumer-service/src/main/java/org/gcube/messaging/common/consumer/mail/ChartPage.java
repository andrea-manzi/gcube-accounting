package org.gcube.messaging.common.consumer.mail;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 
 * 
 * @author Andrea Manzi();
 *
 */
public class ChartPage {
	private ArrayList<Chart> charts;
	private File chartFile = null;
	
	/**
	 * costructor
	 * @param output hte file output
	 */
	public ChartPage(File output) {
		this.charts = new ArrayList<Chart>();
		this.chartFile = output;
	}
	
	/**
	 * Add a chart to the page
	 * @param chart the chart
	 */
	public void addChart(Chart chart ){
		this.charts.add(chart);
	}
	
	/**
	 * create the chart 
	 * @throws IOException exception
	 */
	public void createChartHTMLFile() throws IOException {
		this.chartFile.createNewFile();
		FileWriter writer = new FileWriter(chartFile);
		
		writer.write("<html><body>");
		for (Chart chart :charts)
			writer.write(chart.getChartHTMLTag());
			
		writer.write("</html></body>");
		writer.flush();
		writer.close();
		
	}

}
