package org.gcube.messaging.accounting.portal.test;

import java.io.File;

import org.apache.catalina.LifecycleState;
import org.apache.catalina.startup.Tomcat;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestAccountingPortal {
	
	/** The tomcat instance. */
	private Tomcat mTomcat;
	/** The temporary directory in which Tomcat and the app are deployed. */
	private String mWorkingDir = System.getProperty("java.io.tmpdir");
	
	String location ="target";
	
	private WebArchive war = defaultWar();

	
	@Before
	public void setup() throws Throwable {
	  mTomcat = new Tomcat();
	  mTomcat.setPort(0);
	  mTomcat.setBaseDir(mWorkingDir);
	  mTomcat.getHost().setAppBase(mWorkingDir);
	  mTomcat.getHost().setAutoDeploy(true);
	  mTomcat.getHost().setDeployOnStartup(true);
	  mTomcat.addWebapp(mTomcat.getHost(), "/accounting-portal-webapp", warFile().getAbsolutePath()); 
	  
	  mTomcat.start();
	 
	}
	
	private File warFile() {

		File warFile = new File(location, "accounting-portal-webapp.war");

//		warFile.delete(); // seems safer than plain overwrite to avoid war corruption
//
//		war.as(ZipExporter.class).exportTo(warFile, true);

		return warFile;

	}

	private WebArchive defaultWar() {

		WebArchive war = ShrinkWrap.create(WebArchive.class);
		return war;

		
	}
	@Test
	public void testWebAppStop() throws InterruptedException{
		Thread.sleep(20000);
	}
	
	@After
	public final void teardown() throws Throwable {
	  if (mTomcat.getServer() != null
	            && mTomcat.getServer().getState() != LifecycleState.DESTROYED) {
	        if (mTomcat.getServer().getState() != LifecycleState.STOPPED) {
	              mTomcat.stop();
	        }
	        mTomcat.destroy();
	    }
	}
	

	
}
