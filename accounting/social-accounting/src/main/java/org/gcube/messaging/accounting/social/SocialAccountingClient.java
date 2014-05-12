package org.gcube.messaging.accounting.social;

import java.util.List;

import org.gcube.portal.databook.server.DBCassandraAstyanaxImpl;
import org.gcube.portal.databook.server.DatabookStore;
import org.gcube.portal.databook.shared.Feed;
import org.gcube.portal.databook.shared.Notification;

/**
 * 
 * @author Andrea
 *
 */
public class SocialAccountingClient {
	
	DatabookStore store = null;
	
	
	public SocialAccountingClient(){
		 store = new DBCassandraAstyanaxImpl();

	}
	
	public void getPortalPrivacyLevelFeeds() throws Exception{
		
		List<Feed>  feeds  =store.getAllPortalPrivacyLevelFeeds();
		
		System.out.println("The Total Portal privacy Feeds are: "+feeds.size());
		
	}
	
	public void getNewsFeedStatisticsForUser(String user) throws Exception{
	
		List<Feed> feeds = store.getAllFeedsByUser(user);
	
		System.out.println("The Total Feeds for user: "+ user +" are: "+feeds.size());
		
		List<String> liked= store.getAllLikedFeedIdsByUser(user);
		
		System.out.println("The Total Likes for user: "+ user +" are: "+liked.size());
		

	}
	
	public void getNewsFeedStatisticsForVRE(String vre) throws Exception{
		
		List<Feed> feeds = store.getAllFeedsByVRE(vre);
	
		System.out.println("The Total Feeds for the vre: "+ vre +" are: "+feeds.size());
		

	}
	
	public void getNotificationByUser(String user) throws Exception{
		
		List<Notification> nots=store.getAllNotificationByUser(user ,0); 
		
		System.out.println("The Total Notification for the user: "+ user +" are: "+nots.size());
	}

	
	public static void main(String [] args ){
		
		String user = "andrea.manzi";
		String scope= "/d4science.research-infrastructures.eu/gCubeApps/iSearch";
		
		SocialAccountingClient accounting = new SocialAccountingClient();
		
		try {
			accounting.getNewsFeedStatisticsForUser(user);
			
			accounting.getNewsFeedStatisticsForVRE(scope);
			
			accounting.getNotificationByUser(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
}
