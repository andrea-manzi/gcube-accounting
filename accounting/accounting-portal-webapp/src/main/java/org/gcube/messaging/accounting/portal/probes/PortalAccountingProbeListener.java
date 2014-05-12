package org.gcube.messaging.accounting.portal.probes;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.gcube.common.portal.PortalContext;
import org.gcube.messaging.common.producer.JMSLocalProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public class PortalAccountingProbeListener implements ServletContextListener{
	
	static Logger logger = LoggerFactory.getLogger(PortalAccountingProbeListener.class);
	
	static long intervalTime=3600;
	
	ScheduledExecutorService scheduler = null;
	
	JMSLocalProducer producer = null;
	

	public void contextInitialized(ServletContextEvent arg0) {
		
		String scope = "/"+ PortalContext.getConfiguration().getInfrastructureName();
		
		producer = new JMSLocalProducer(scope);
		producer.run();
		
		PortalAccountingProbe probe = new PortalAccountingProbe();
		
		scheduler=  Executors.newSingleThreadScheduledExecutor();
			
		scheduler.scheduleWithFixedDelay(probe,0,intervalTime, TimeUnit.SECONDS);    
			 
	}
	
	

	public void contextDestroyed(ServletContextEvent arg0) {
		
		String scope = "/"+ PortalContext.getConfiguration().getInfrastructureName();
		
		producer.stopConnections(scope);	
		scheduler.shutdown();
		
	}

}
