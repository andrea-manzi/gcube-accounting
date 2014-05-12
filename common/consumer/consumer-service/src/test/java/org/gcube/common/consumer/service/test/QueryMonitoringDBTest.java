package org.gcube.common.consumer.service.test;

import org.gcube.common.core.scope.GCUBEScope;
import org.gcube.common.core.security.GCUBESecurityManager;
import org.gcube.common.core.security.GCUBESecurityManagerImpl;

/**
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public class QueryMonitoringDBTest {
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		if((args.length<1) || (args.length>5) ){
			System.out.println("Usage:");
			System.out.println("\tjava  QueryMonitoringDBTest  query callerscope host port] \n\n");
					
			return;
		}
		GCUBEScope gCubeScope = GCUBEScope.getScope(args[1]);	
		GCUBESecurityManager secMan= new GCUBESecurityManagerImpl(){	public boolean isSecurityEnabled() {return false;}};
		//query example: SELECT * FROM GHNMessage WHERE Date="2009-10-16"
		/*,{"MESSAGEID":"73144",
		 * "GHNNAME":"node9.p.d4science.research-infrastructures.eu:8080",
		 * "TESTTYPE":"DISK_QUOTA",
		"DESCRIPTION":"Check the local GHN Disk Quota",
		"RESULT":"10428568",
		"SCOPE":"/d4science.research-infrastructures.eu",
		"DATE":"2009-10-16","TIME":"14:21:20"}]*/
		
		try {
			//MessagingConsumerServiceCall call = new MessagingConsumerServiceCall(gCubeScope, new GCUBESecurityManager[]{secMan});
			//call.setEndpoint(args[2], args[3]);
			//String json = call.queryMonitoringDB(args[0]);
			//System.out.println(json);
			//JSONObject input=new JSONObject(json);
			//JSONArray root=input.getJSONArray("data");
			//for (int i =0; i< root.length();i++) {
			//	String name=(String)root.getJSONObject(i).get("GHNNAME");
			//	System.out.println(name);
				/*JSONObject type=root.getJSONObject(i).getJSONObject("TESTTYPE");
				System.out.println(type.toString());
				JSONObject desc=root.getJSONObject(i).getJSONObject("DESCRIPTION");
				System.out.println(desc.toString());
				JSONObject result=root.getJSONObject(i).getJSONObject("RESULT");
				System.out.println(result.toString());
				JSONObject scope=root.getJSONObject(i).getJSONObject("SCOPE");
				System.out.println(scope.toString());
				JSONObject date=root.getJSONObject(i).getJSONObject("DATE");
				System.out.println(date.toString());*/
		//}	
		} catch (Exception e) {
				e.printStackTrace();
		}
		
	}

}
