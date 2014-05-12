package org.gcube.messaging.common.consumerlibrary.test;


import java.util.concurrent.TimeUnit;

import org.gcube.common.scope.api.ScopeProvider;
import org.gcube.messaging.common.consumerlibrary.ConsumerCL;
import org.gcube.messaging.common.consumerlibrary.proxies.Proxies;
import org.gcube.messaging.common.consumerlibrary.query.NodeAccountingQuery;
import org.gcube.messaging.common.consumerlibrary.query.NodeAccountingQuery.InvocationInfo;
import org.junit.Test;


public class TestClient {
	
	@Test
	public void testClientTest(){
		try {
			String startDate = 	"2012-07-01 00:00:00";
			String endDate = "2012-07-31 23:59:59";

			ScopeProvider.instance.set("/d4science.research-infrastructures.eu");
			ConsumerCL library = Proxies.consumerService().withTimeout(1, TimeUnit.MINUTES).build();
			NodeAccountingQuery query = library.getQuery(NodeAccountingQuery.class,library);
			
			InvocationInfo info = query.getInvocationPerHour(startDate, endDate);
			System.out.println(info.getInvocationCount());
			System.out.println(info.getAvgInvocationTime());
			
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	

}
