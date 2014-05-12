package org.gcube.common.consumer.service.test;

import org.gcube.common.core.scope.GCUBEScope;
import org.gcube.common.core.security.GCUBESecurityManager;
import org.gcube.common.core.security.GCUBESecurityManagerImpl;


/**
 * 
 * @author gcube
 *
 */
public class QueryAccountingDBTest {
	 /* @param args
	 */
	public static void main(String[] args) {

		if((args.length<1) || (args.length>5) ){
			System.out.println("Usage:");
			System.out.println("\tjava  QueryAccountingDBTest  query callerscope host port] \n\n");
					
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
			//String json = call.queryAccountingDB(args[0]);
			//System.out.println(json);
			//JSONObject input=new JSONObject(json);
			//JSONArray root=input.getJSONArray("data");
			/*for (int i =0; i< root.length();i++) {
				String name=(String)root.getJSONObject(i).get("User");
				System.out.println(name);*/

		//}	
		} catch (Exception e) {
				e.printStackTrace();
		}
		
	}
}
