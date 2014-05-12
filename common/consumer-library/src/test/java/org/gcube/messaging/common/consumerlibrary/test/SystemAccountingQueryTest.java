package org.gcube.messaging.common.consumerlibrary.test;

import java.util.concurrent.TimeUnit;


import org.gcube.common.scope.api.ScopeProvider;
import org.gcube.messaging.common.consumerlibrary.ConsumerCL;
import org.gcube.messaging.common.consumerlibrary.proxies.Proxies;
import org.gcube.messaging.common.consumerlibrary.query.SystemAccountingQuery;
import org.junit.Test;

/**
 * Andrea Manzi(CERN)
 */
public class SystemAccountingQueryTest {

@Test
public void systemAccountingQueryTest(){
	
	//	ClientRuntime.start();
	try {
			

			ScopeProvider.instance.set("/d4science.research-infrastructures.eu");
			ConsumerCL library = Proxies.consumerService().withTimeout(1, TimeUnit.MINUTES).build();
			SystemAccountingQuery query = library.getQuery(SystemAccountingQuery.class,library);
			
			for (String str : query.getTypes()){
				System.out.println(query.getDimensions(str));
				System.out.println(query.getTypeContentAsJSONString(str));
			}
			
			
			
		}catch (Exception e){
			e.printStackTrace();
		}
	}
}
