package org.gcube.messaging.accounting_system;

import java.sql.Timestamp;
import java.util.HashMap;

import org.gcube.common.core.scope.GCUBEScope;
import org.gcube.messaging.accounting.system.SystemAccounting;
import org.gcube.messaging.accounting.system.SystemAccountingFactory;


/**
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public class SystemAccountingTest {
	
	public static void main (String [] args){
		
		try { 
			
			SystemAccounting acc = SystemAccountingFactory.getSystemAccountingInstance();
			
			String type = "TestType7";
			HashMap<String,Object> parameters = new HashMap<String,Object>();
			parameters.put("bo",0);
			parameters.put("testParameter1","andrea");
			parameters.put("testParameter", new Timestamp(System.currentTimeMillis()));
			
			String sourceGHN="pcd4science3.cern.ch:8080";
			GCUBEScope scope = GCUBEScope.getScope("/testing");
			acc.sendSystemAccountingMessage(type, scope, sourceGHN,parameters);
			
		}catch (Exception e){
			e.printStackTrace();
		}
	}

}
