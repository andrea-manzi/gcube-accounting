package org.gcube.messaging.accounting.portal.logparser.entry;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.gcube.messaging.accounting.portal.logparser.AccessLogParser.EntryType;
import org.gcube.messaging.common.messages.records.BaseRecord;
import org.gcube.messaging.common.messages.records.WebAppRecord.GHN;
import org.gcube.messaging.common.messages.records.WebAppRecord.WebApplication;
import org.gcube.messaging.common.messages.records.WebAppRecord;

/**
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public class WebAppEntry extends LogEntry {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4173528605022856737L;
	
	private StringTokenizer tokenizer = null;
	
	//webapp 
	private String []webAppEntry = new String [5];
		
	//webapplications
	private ArrayList<WebApplication> listApplication = new ArrayList<WebApplication>();
		
	//webapplications
	private ArrayList<GHN> listGHN = new ArrayList<GHN>();

	
	public WebAppEntry(String line,EntryType type) throws ParseException{
		super();
		this.line = line;
		entryType = type;
		record = new  WebAppRecord();
		parse();
	}
	
	protected static final String hlAddresssesSeparator= ";";
	protected static final String hlAddresssesSeparator2= ":";

	
	protected static ArrayList<WebApplication> getApplications(String line){
		ArrayList<WebApplication> webApps  = new ArrayList<WebApplication>();
		String ghnString = line.substring(line.indexOf(LogEntry.parameterValueSeparator)+1).trim();
		StringTokenizer tokenizer = new StringTokenizer(ghnString,hlAddresssesSeparator);
		while(tokenizer.hasMoreElements()){
			String tok = tokenizer.nextToken();
			
			WebApplication webApp = new  WebAppRecord().new WebApplication();
			if (tok.contains(WebAppTokens.ID.tokens))
				webApp.setWEB_APPLICATION_ID(getValue(tok));
			else if (tok.contains(WebAppTokens.NAME.tokens))
				webApp.setWEB_APPLICATION_NAME(getValue(tok));
			else if (tok.contains(WebAppTokens.VERSION.tokens))
				webApp.setWEB_APPLICATION_NAME(getValue(tok));
			
			webApps.add(webApp);
		}
		return webApps;
		
	} 
	
	protected enum WebAppTokens {
		APPLICATIONS("APPLICATIONS"),
		ID("ID"),
		NAME("NAME"),
		VERSION("VERSION"),
		GHN_ID("GHN_ID"),
		GHN_NAME("GHN_NAME");

		String tokens;
		WebAppTokens(String tokens) {this.tokens = tokens;}
		public String toString() {return this.tokens;}
	}
	
	
	@Override
	public BaseRecord fillRecord() {
		
		tokenizer = new StringTokenizer(message.getMessage(),Message.messageTokensSeparator);
		
		
		switch(this.getEntryType()) {
		case WEB_APPLICATION_ACTIVATED:
		case WEB_APPLICATION_DEACTIVATED: 
		{
			while(tokenizer.hasMoreElements()){
				String tok = tokenizer.nextToken();
		
				if (tok.contains(WebAppTokens.ID.tokens))
					webAppEntry[0] = getValue(tok);
				else if (tok.contains(WebAppTokens.NAME.tokens))
					webAppEntry[1] = getValue(tok);
				else if (tok.contains(WebAppTokens.VERSION.tokens))
					webAppEntry[2] = getValue(tok);
				else if (tok.contains(WebAppTokens.GHN_ID.tokens))
					webAppEntry[3] = getValue(tok);
				else if (tok.contains(WebAppTokens.GHN_NAME.tokens))
					webAppEntry[4] = getValue(tok);
				
			}
		}
		break;	
		case WEB_APPLICATION_DEPLOYED:
		{
			while(tokenizer.hasMoreElements()){
				String tok = tokenizer.nextToken();
				if (tok.contains(WebAppTokens.GHN_ID.tokens))
					webAppEntry[0] = getValue(tok);
				else if (tok.contains(WebAppTokens.GHN_NAME.tokens))
					webAppEntry[1] = getValue(tok);
				else if (tok.contains(WebAppTokens.APPLICATIONS.tokens))
					listApplication = WebAppEntry.getApplications(tok);
			}
		}
		break;
		case WEB_APPLICATION_UNDEPLOYED:
		{
			while(tokenizer.hasMoreElements()){
				String tok = tokenizer.nextToken();
				if (tok.contains(WebAppTokens.APPLICATIONS.tokens)){
					listApplication = WebAppEntry.getApplications(tok);
					listGHN = WarEntry.getGHN(tok);
				}
			}
		}
		break;
		
			
		}
		record.setDate(this.getDate());
		((WebAppRecord)record).setSubType(WebAppRecord.WebAppSubType.valueOf(this.getEntryType().name()));
		if (this.getEntryType().compareTo(EntryType.WEB_APPLICATION_ACTIVATED)==0 ||this.getEntryType().compareTo(EntryType.WEB_APPLICATION_DEACTIVATED)==0)  
		{
			GHN ghn = new WebAppRecord().new GHN();
			ghn.setGHN_ID(webAppEntry[3]);
			ghn.setGHN_NAME(webAppEntry[4]);
			((WebAppRecord)record).addGhn(ghn);
			WebApplication application = new WebAppRecord().new WebApplication();	
			application.setWEB_APPLICATION_ID(webAppEntry[0]);
			application.setWEB_APPLICATION_NAME(webAppEntry[1]);
			application.setWEB_APPLICATION_VERSION(webAppEntry[2]);
			((WebAppRecord)record).addWebapplication(application);
		}
		else if (this.getEntryType().compareTo(EntryType.WEB_APPLICATION_DEPLOYED)==0 )
		{
			GHN ghn = new WebAppRecord().new GHN();
			ghn.setGHN_ID(webAppEntry[3]);
			ghn.setGHN_NAME(webAppEntry[4]);
			((WebAppRecord)record).addGhn(ghn);
			for (WebApplication app:listApplication)
				((WebAppRecord)record).addWebapplication(app);
		}
		else if (this.getEntryType().compareTo(EntryType.WEB_APPLICATION_UNDEPLOYED)==0 )
		{
			for (GHN ghn:listGHN)
				((WebAppRecord)record).addGhn(ghn);
			for (WebApplication app:listApplication)
				((WebAppRecord)record).addWebapplication(app);
		}
			
			
		return record;
	}
	
	private static String getValue(String token){
		return token.substring(token.indexOf(LogEntry.parameterValueSeparator)+1).trim();

	}

}
