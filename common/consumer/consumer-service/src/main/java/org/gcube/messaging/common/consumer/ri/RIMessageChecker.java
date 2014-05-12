package org.gcube.messaging.common.consumer.ri;

import org.gcube.common.core.scope.GCUBEScope;
import org.gcube.messaging.common.consumer.MessageChecker;
import org.gcube.messaging.common.consumer.ServiceContext;
import org.gcube.messaging.common.consumer.Constants.MailTemplateToken;
import org.gcube.messaging.common.messages.*;

/**
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public class RIMessageChecker extends MessageChecker<RIMessage<?>>{

	public RIMessageChecker (GCUBEScope scope){super(scope);}
	/**
	 * 
	 * @param message
	 */
	public void check(RIMessage<?> message){
		if (!(message.getTopic().contains("RI"))) 
			return;
	
		RINotification not = new RINotification();
		not.setType(message.getTest().getType().toString());
		
		not.setMessage(
				ServiceContext.getContext().getMailTemplateParser().
				getTemplateMap().get(message.getTest().getType().name()).
				replace(MailTemplateToken.INFO.toString(),  message.getTest().getDescription()));
		
		not.setServiceClass(message.getServiceClass());
		not.setServiceName(message.getServiceName());
		not.setSourceGHN(message.getSourceGHN());
		not.setScope(GCUBEScope.getScope(message.getScope()));
		not.setTime(message.getTime());
	
		switch (message.getTest().getType())
		{
			case NOTIFICATION:
			{
				if (message.getTest().getPriority().compareTo(Test.Priority.HIGH)== 0){
						ServiceContext.getContext().getNotifier().enqueue(not);
						ServiceContext.getContext().getMonitoringManager().insertNotification(not);
				}
			}
			break;
			default:
				break;
		}

		ServiceContext.getContext().getMonitoringManager().InsertRIMessage(message);

	}
}
