package org.gcube.messaging.common.consumer.liferay;

import java.io.File;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.xml.rpc.ServiceException;

import org.gcube.common.core.contexts.GHNContext;
import org.gcube.messaging.common.consumer.ServiceContext;
import org.gcube.vomanagement.usermanagement.ws.GetGroupId;
import org.gcube.vomanagement.usermanagement.ws.GetGroupIdResponse;
import org.gcube.vomanagement.usermanagement.ws.LiferaySOAPGroupManagerPortType;
import org.gcube.vomanagement.usermanagement.ws.LiferaySOAPRoleManagerPortType;
import org.gcube.vomanagement.usermanagement.ws.LiferaySOAPUserManagerPortType;
import org.gcube.vomanagement.usermanagement.ws.ListRolesByUser;
import org.gcube.vomanagement.usermanagement.ws.ListRolesByUserResponse;
import org.gcube.vomanagement.usermanagement.ws.ListUsersByGroup;
import org.gcube.vomanagement.usermanagement.ws.ListUsersByGroupResponse;
import org.gcube.vomanagement.usermanagement.ws.RoleModel;
import org.gcube.vomanagement.usermanagement.ws.UserModel;
import org.gcube.vomanagement.usermanagement.ws.UserModelCustomAttrsMapEntry;
import org.gcube.vomanagement.usermanagement.ws.service.LiferaySOAPGroupManagerServiceAddressingLocator;
import org.gcube.vomanagement.usermanagement.ws.service.LiferaySOAPRoleManagerServiceAddressingLocator;
import org.gcube.vomanagement.usermanagement.ws.service.LiferaySOAPUserManagerServiceAddressingLocator;



/**
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public class LiferayClient {

	
	static String serviceUserManagerName = "LiferaySOAPUserManager";
	static String serviceGropuManagerName = "LiferaySOAPGroupManager";
	static String serviceRoleName = "LiferaySOAPRoleManager";
	
	public static String basePortalURL= (String ) ServiceContext.getContext().getProperty("portalBaseURL", true);
	public static String siteManagerRoleName= (String ) ServiceContext.getContext().getProperty("siteManagerRoleName", true);
	public static String infraManagerRoleName= (String ) ServiceContext.getContext().getProperty("infraManagerRoleName", true);
	
	/*public static String basePortalURL= "http://grid8.4dsoft.hu:8080";
	public static String siteManagerRoleName= "Site-Manager";
	public static String infraManagerRoleName= "Infrastructure-Manager";
	
	public static String receiveNotificationCustomAttributeName= "receive_notifications";
	public static String receiveSummaryCustomAttributeName= "receive_summary";
	public static String siteNameCustomAttributeName= "site";
	public static String siteDomainsCustomAttributeName= "site_domains";
	*/
	
	public static String receiveNotificationCustomAttributeName= (String ) ServiceContext.getContext().getProperty("receiveNotificationCustomAttributeName", true);
	public static String receiveSummaryCustomAttributeName= (String ) ServiceContext.getContext().getProperty("receiveSummaryCustomAttributeName", true);
	public static String siteNameCustomAttributeName= (String ) ServiceContext.getContext().getProperty("siteNameCustomAttributeName", true);
	public static String siteDomainsCustomAttributeName= (String ) ServiceContext.getContext().getProperty("siteDomainsCustomAttributeName", true);
		
	
	public UserModel[] getUsersPerGroup(String groupID) throws ServiceException, Exception{
		LiferaySOAPUserManagerPortType ptUser = new LiferaySOAPUserManagerServiceAddressingLocator().getLiferaySOAPUserManagerPortTypePort(_getURL(serviceUserManagerName));	
	    ListUsersByGroup req = new ListUsersByGroup();
		req.setArg0(groupID);
		ListUsersByGroupResponse response_users = ptUser.listUsersByGroup(req);
		UserModel [] users = response_users.get_return();
		return users;
	}
	
	
	
	/**
	 * 
	 * 
	 */
	public  RoleModel[] getUserRoles(String userID) throws ServiceException, Exception{
	
		LiferaySOAPRoleManagerPortType roleManagerStub = new LiferaySOAPRoleManagerServiceAddressingLocator().
		getLiferaySOAPRoleManagerPortTypePort(_getURL(serviceRoleName));
		ListRolesByUser roleData = new ListRolesByUser ();
		roleData.setArg0(userID);
		
		ListRolesByUserResponse listRolesResponse = roleManagerStub.listRolesByUser(roleData);
		return listRolesResponse.get_return();
	}
	
	
	/**
	 * Get back site admins ( declared on root scope)
	 * @throws Exception 
	 * @throws ServiceException 
	 * 
	 * 
	 */
			
	public ArrayList<UserModel> getUsersWithRole (String rolemame) throws ServiceException, Exception{
		
		LiferaySOAPGroupManagerPortType groupPortType = new LiferaySOAPGroupManagerServiceAddressingLocator()
		.getLiferaySOAPGroupManagerPortTypePort(_getURL(serviceGropuManagerName)) ;
		GetGroupId groupIDRequest = new GetGroupId();
		String infrastructureGroup = GHNContext.getContext().getStartScopes()[0].getInfrastructure().getName();
		groupIDRequest.setArg0(infrastructureGroup);
		GetGroupIdResponse response =groupPortType.getGroupId(groupIDRequest);
		
		
		 ArrayList<UserModel> users = new ArrayList<UserModel>();
		 
		//get all users on a given group and role
		for (UserModel user: getUsersPerGroup(response.get_return())){
			
			RoleModel[] roles = getUserRoles(user.getUserId());
			for (RoleModel role : roles){
				if (role!= null && role.getRoleName() != null){
			
					if (role.getRoleName().compareTo(rolemame) ==0) 
							users.add(user);
				}
			}
		}
		
		return users;
		
	}

	public static void main(String[] args)  {
		
		LiferayClient client = new LiferayClient();
		
		try {
			for (UserModel user :client.getUsersWithRole(infraManagerRoleName)){
				System.out.println(user.getScreenName());
				System.out.println(user.getEmail());
				for(int i = 0;i <user.getCustomAttrsMap().getEntry().length; i++){
					UserModelCustomAttrsMapEntry entry =user.getCustomAttrsMap().getEntry(i);
					System.out.println(entry.getKey());
					System.out.println(entry.getValue());
				}
			}
			
		} 
		catch (RemoteException e ){
			e.printStackTrace();
		}catch (Exception e ){
			e.printStackTrace();
		}
		
		try {
			for (UserModel user :client.getUsersWithRole(siteManagerRoleName)){
				System.out.println(user.getScreenName());
				System.out.println(user.getEmail());
		
				for(int i = 0;i <user.getCustomAttrsMap().getEntry().length; i++){
					UserModelCustomAttrsMapEntry entry =user.getCustomAttrsMap().getEntry(i);
					System.out.println(entry.getKey());
					System.out.println(entry.getValue());
				}
			}
			
		} 
		catch (RemoteException e ){
			e.printStackTrace();
		}catch (Exception e ){
			e.printStackTrace();
		}
	
	}
	
	private URL _getURL(String serviceName) throws Exception {
		return new URL(LiferayClient.basePortalURL+ File.separator + "usermanagement-ws" + File.separator +  serviceName);
		}

}
