package org.gcube.messaging.common.consumer.ghn;

import java.text.SimpleDateFormat;

import org.gcube.common.core.monitoring.GCUBEMessage;
import org.gcube.common.core.scope.GCUBEScope;
import org.gcube.common.core.utils.logging.GCUBELog;
import org.gcube.messaging.common.consumer.MessageChecker;
import org.gcube.messaging.common.consumer.ServiceContext;
import org.gcube.messaging.common.consumer.Constants.MailTemplateToken;
import org.gcube.messaging.common.messages.GHNMessage;
import org.gcube.messaging.common.messages.Test;

/**
 * Check GHNMessages and perform actions
 * 
 * @author Andrea Manzi (CERN)
 *
 */
public class GHNMessageChecker extends MessageChecker<GHNMessage<?>>{

	static Long diskQuotaThreshold = null;
	static Long virtualMemoryThreshold = null;
	static Double cpuLoadThreshold = null;

	/**
	 * logger
	 */
	public  static GCUBELog logger = new GCUBELog(GHNMessageChecker.class);

	static SimpleDateFormat format = new SimpleDateFormat(GCUBEMessage.DATE_FORMAT_NOW);

	/**
	 * The constructor
	 * @param scope the GHNCHecker scope
	 */
	public GHNMessageChecker(GCUBEScope scope)
	{ 	
		super(scope);

		//getting configuration from the IS
		if ((diskQuotaThreshold = ServiceContext.getContext().getMailClient().getScopeMap().get(scope.toString()).getDiskQuotaThreshold()) == null)
		{
			try{
				diskQuotaThreshold = ((Long) ServiceContext.getContext().getProperty("diskQuotaThreshold", true));
				logger.debug("Configure DiskQuota filter with threshold: "+ diskQuotaThreshold);
			}catch (RuntimeException e) {
				logger.debug("Unable to configure DiskQuota filter");
			}
		}else logger.debug("DiskQuota filter retrieved from IS: "+diskQuotaThreshold);

		if ((virtualMemoryThreshold = ServiceContext.getContext().getMailClient().getScopeMap().get(scope.toString()).getVirtualMemoryThreshold()) == null)			
			try {
				virtualMemoryThreshold = ((Long) ServiceContext.getContext().getProperty("virtualMemoryThreshold", true));
				logger.debug("Configure VirtualMemory filter with threshold: "+ virtualMemoryThreshold);
			}catch (RuntimeException e) {
				logger.debug("Unable to configure VirtualMemory filter");
			} else logger.debug("VirtualMemory filter retrieved from IS: "+virtualMemoryThreshold);


		if ((cpuLoadThreshold = ServiceContext.getContext().getMailClient().getScopeMap().get(scope.toString()).getCpuLoadThreshold()) == null)
		{
			try {
				cpuLoadThreshold = ((Double) ServiceContext.getContext().getProperty("cpuLoadThreshold", true));
				logger.debug("Configure CPU Load filter with threshold: "+ cpuLoadThreshold);
			}catch (RuntimeException e) {
				logger.debug("Unable to configure CPULoad filter");
			} 
		} else logger.debug("CPU Load filter  retrieved from IS: "+cpuLoadThreshold);
	}

	/**
	 * {@inheritDoc}
	 */
	public void check(GHNMessage <?>message) throws Exception{

		if (!(message.getTopic().contains("GHN"))) 
			return;

		GHNNotification not = new GHNNotification();

		boolean notify = false;
		boolean quota_disk_not_assessable = false;

		switch (message.getTest().getType())
		{

		case  DISK_QUOTA:
		{
			if ((diskQuotaThreshold != null) && ((Long)message.getTest().getTestResult()).longValue()==0){
				not.setMessage(
						ServiceContext.getContext().getMailTemplateParser().
					getTemplateMap().get("DISK_QUOTA_NOT_ASSESSABLE"));
				quota_disk_not_assessable = true;
				notify=true;
			}else if ((diskQuotaThreshold != null) && (((Long)message.getTest().getTestResult()).compareTo(diskQuotaThreshold)<0))
			{ 
				not.setMessage(
						ServiceContext.getContext().getMailTemplateParser().
					getTemplateMap().get(message.getTest().getType().name()).
					replace(MailTemplateToken.SPACE.toString(), message.getTest().getTestResult().toString()));
				notify=true;
			} 				
		}	
		break;
		case CPU_LOAD:
		{
			if ((cpuLoadThreshold != null ) && (((Double)message.getTest().getTestResult()).compareTo(cpuLoadThreshold) >0))
			{
				//calculate CPU overload
				Double cpuLoad= (Double)message.getTest().getTestResult();
				Long overload = new Double(cpuLoad*100).longValue()-100;
				
				not.setMessage(
						ServiceContext.getContext().getMailTemplateParser().
					getTemplateMap().get(message.getTest().getType().name()).
					replace(MailTemplateToken.OVERLOAD.toString(), overload.toString()));
				notify=true;
			}
		}
		break;
		case MEMORY_AVAILABLE:
		{
			if ((virtualMemoryThreshold != null) && (((Long)message.getTest().getTestResult()).compareTo(virtualMemoryThreshold) <0))
			{		
				not.setMessage(
						ServiceContext.getContext().getMailTemplateParser().
					getTemplateMap().get(message.getTest().getType().name()).
					replace(MailTemplateToken.SPACE.toString(),  message.getTest().getTestResult().toString()));
				notify=true;	
				}
		}
		break;
		case NOTIFICATION:
			if (message.getTest().getPriority().compareTo(Test.Priority.HIGH)== 0) 	{
				not.setMessage(
						ServiceContext.getContext().getMailTemplateParser().
					getTemplateMap().get(message.getTest().getType().name()).
					replace(MailTemplateToken.INFO.toString(),  message.getTest().getDescription()));
				notify = true;
			}
			break;
		default:
			break;
		}
		if (notify){
			not.setSourceGHN(message.getSourceGHN());
			not.setScope(GCUBEScope.getScope(message.getScope()));
			if (quota_disk_not_assessable)
				not.setType("DISK_QUOTA_NOT_ASSESSABLE");
			else
				not.setType(message.getTest().getType().toString());
			not.setTime(message.getTime());
			ServiceContext.getContext().getNotifier().enqueue(not);
			ServiceContext.getContext().getMonitoringManager().insertNotification(not);
		}
		ServiceContext.getContext().getMonitoringManager().InsertGHNMessage(message);

	}
}
