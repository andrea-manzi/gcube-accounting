package org.gcube.common.consumer.service.test;

import org.gcube.common.core.scope.GCUBEScope.MalformedScopeExpressionException;
import org.gcube.common.core.utils.logging.GCUBELog;
import org.gcube.common.mycontainer.Deployment;
import org.gcube.common.mycontainer.Gar;
import org.gcube.common.mycontainer.MyContainerTestRunner;
import org.gcube.messaging.common.consumer.ServiceContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.gcube.common.core.contexts.GCUBEServiceContext.Status.*;
import static org.junit.Assert.*;

@RunWith(MyContainerTestRunner.class)
public class MessagingConsumerTest {
	

		GCUBELog logger = null;
	
		@Deployment
		static Gar myGar = new Gar("consumer-service").addInterfaces("../wsdl").addConfigurations("../config");

		@Before
		public void setUp() throws MalformedScopeExpressionException, Exception{
			logger = new GCUBELog(this.getClass());

		}


		@Test
		public void testStartup() throws Exception {
		
				//assertTrue(ServiceContext.getContext().getStatus() == READIED);
				//logger.info("service is ready");
			}

	}

