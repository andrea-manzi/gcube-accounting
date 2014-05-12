package org.gcube.messaging.accounting.portal.logparser.entry;

import java.text.ParseException;

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

	
	public WebAppEntry(String line,EntryType type) throws ParseException{
		super();
		this.line = line;
		entryType = type;
		record = new  WebAppRecord();
		parse();
	}
	
	@Override
	public BaseRecord fillRecord() {
		record.setDate(this.getDate());
		((WebAppRecord)record).setSubType(WebAppRecord.WebAppSubType.valueOf(this.getEntryType().name()));
		if (this.getEntryType().compareTo(EntryType.WEB_APPLICATION_ACTIVATED)==0 ||this.getEntryType().compareTo(EntryType.WEB_APPLICATION_DEACTIVATED)==0)  
		{
			GHN ghn = new WebAppRecord().new GHN();
			ghn.setGHN_ID(this.getMessage().getWebAppEntry()[3]);
			ghn.setGHN_NAME(this.getMessage().getWebAppEntry()[4]);
			((WebAppRecord)record).addGhn(ghn);
			WebApplication application = new WebAppRecord().new WebApplication();	
			application.setWEB_APPLICATION_ID(this.getMessage().getWebAppEntry()[0]);
			application.setWEB_APPLICATION_NAME(this.getMessage().getWebAppEntry()[1]);
			application.setWEB_APPLICATION_VERSION(this.getMessage().getWebAppEntry()[2]);
			((WebAppRecord)record).addWebapplication(application);
		}
		else if (this.getEntryType().compareTo(EntryType.WEB_APPLICATION_DEPLOYED)==0 )
		{
			GHN ghn = new WebAppRecord().new GHN();
			ghn.setGHN_ID(this.getMessage().getWebAppEntry()[3]);
			ghn.setGHN_NAME(this.getMessage().getWebAppEntry()[4]);
			((WebAppRecord)record).addGhn(ghn);
			for (WebApplication app:this.getMessage().getListApplication())
				((WebAppRecord)record).addWebapplication(app);
		}
		else if (this.getEntryType().compareTo(EntryType.WEB_APPLICATION_UNDEPLOYED)==0 )
		{
			for (GHN ghn:this.getMessage().getListGHN())
				((WebAppRecord)record).addGhn(ghn);
			for (WebApplication app:this.getMessage().getListApplication())
				((WebAppRecord)record).addWebapplication(app);
		}
			
			
		return record;
	}
	

}
