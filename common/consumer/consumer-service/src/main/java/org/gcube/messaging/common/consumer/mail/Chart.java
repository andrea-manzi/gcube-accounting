package org.gcube.messaging.common.consumer.mail;

import java.net.URL;
import java.util.ArrayList;


/**
 * 
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public class Chart {
	
	private  URL url =null;
	private String Description ="";
	private String size="";
	private String type="";
	private String label="";
	private ArrayList<Double> data;

	
	private static String baseChartURL ="http://chart.apis.google.com/chart?";
	private static String typeString= "cht=";
	private static String sizeString= "chs=";
	private static String labelString= "chl=";
	private static String dataString= "chd=";
	private static String limitString= "chds=";
	private static String urlSeparator="\n&amp;";
	
	
	/**
	 * The cosntructor
	 *
	 */
	public Chart() {
		this.data = new ArrayList<Double>();
	}
	/**
	 * get the description
	 * @return the description
	 */
	public String getDescription() {
		return Description;
	}
	/**
	 * set the description
	 * @param description the description
	 */
	public void setDescription(String description) {
		Description = description;
	}
	/**
	 * ger the URL
	 * @return the URL
	 */
	public URL getUrl() {
		return url;
	}
	/**
	 * Set the URL
	 * 
	 * @param url the URL
	 */
	public void setUrl(URL url) {
		this.url = url;
	}

	/**
	 * get the size
	 * @return the size
	 */
	public String getSize() {
		return size;
	}
	/**
	 * set the size
	 * @param size the size
	 */
	public void setSize(String size) {
		this.size = size;
	}
	/**
	 * get the type
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * set the type
	 * @param type the type
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	/**
	 * add data
	 * @param data the data
	 */
	public void addData (Double data){
		this.data.add(data);
	}
	/**
	 * get the string of values
	 * @return the values
	 */
	private  String getValuesString(){
		String values ="t:";
		for (Double dataString : this.data)
			values =values.concat(dataString+",");
		return values.substring(0, values.length()-1);
	}
	private  String getLimits(){
		double min =-1;
		double max = -1;
		 for (Double dataString : this.data ) {
			 if (max == -1) max = dataString.doubleValue();
			 else if (dataString.doubleValue() > max)
				 max = dataString.doubleValue();
			 if (min == -1) min = dataString.doubleValue();
			 else if(dataString.doubleValue() < min)
				 min = dataString.doubleValue();
		 }
		return min+","+max;
	}
	
	/**
	 * get the label
	 * @return the lable
	 */
	public String getLabel(){
		return this.label;
	}
	/**
	 * set the label
	 * @param label
	 */
	public void setLabel(String label){
		this.label = label;
	}
	
	/**
	 * get the CHART URL as string
	 * @return the chart URL as string
	 */
	public String getChartString () {
		return baseChartURL+"\n"+sizeString+this.getSize()+urlSeparator+dataString+this.getValuesString()+urlSeparator
		+limitString+getLimits()+urlSeparator+typeString+ this.getType()+urlSeparator+labelString+this.getLabel();
		
	}

	/**
	 * get the google chart URL
	 * @return the google chart URL
	 */
	public String getChartHTMLTag() {
		return "<div><img src=\""+this.getChartString() +"\" alt=\""+this.getDescription()+"\"/></div>";
		
	}
	
}
