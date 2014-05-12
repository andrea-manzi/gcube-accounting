package org.gcube.messaging.accounting.nodeaccountingportlet.client;

import com.google.gwt.core.client.GWT;

/**
 * 
 * @author Andrea Manzi (CERN)
 *
 */

public class Costants {

	public static final String 	servletURL = GWT.getModuleBaseURL() + "QueryServlet";
	public static final String 	servletStatisticURL = GWT.getModuleBaseURL() + "StatisticsServlet";
	public static final String 	exportServletURL = GWT.getModuleBaseURL() + "ExportServlet";
	
	
	public static final String DIVID="UIDiv";
	public static final String START="start";
	public static final String LIMIT="limit";
	public static final String DefaultGrouping="callerScope";
	
	public  enum  Colors {
			red("red"),
			blue("blue"),
			green("green"),
			silver("silver"),
			aqua("aqua"),
			black("black"),
			fuchsia("fuchsia"),
			gray("gray"),
			lime("lime"),
			maroon("maroon"),
			navy("navy"),
			olive("olive"),
			purple("purple"),
			teal("teal"),
			white("white"),
			yellow("yellow");
			String type;
			Colors(String type) {this.type = type;}
			};
}
