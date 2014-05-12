package org.gcube.messaging.common.consumer.webserver.impl.jetty;

import java.io.IOException;

import org.gcube.common.core.utils.logging.GCUBELog;
import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Handler;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.ResourceHandler;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.servlet.ServletHandler;

/**

 * Jetty Web webserver
 * 
 * @author Luca Frosini (ISTI-CNR), Andrea Manzi(CERN)
 */
public class JettyWebServer implements org.gcube.messaging.common.consumer.webserver.WebServer {
	
	/** 
	 * Class logger. 
	 */
	protected static final GCUBELog logger = new GCUBELog(JettyWebServer.class);
	
	protected static final JettyWebServerLogger webServerLogger = new JettyWebServerLogger();
	
	private Server server = new Server(); 
	
	private ResourceHandler resourceHandler = null;
	/**
	 * Initialize the WebServer with default connector (SelectChannelConnector) on specified port
	 * and with default Handler
	 * {@inheritDoc}
	 */
	public void initDefaults(String basePath,int port){
		
		Connector connector = new SelectChannelConnector();
		connector.setPort(port);
		
        connector.setServer(server);
		
		resourceHandler = new ResourceHandler();
		resourceHandler.setResourceBase(basePath);
       
		
//		ServletHandler handler=new ServletHandler();
//        handler.addServletWithMapping("org.gcube.messaging.common.consumer.servlet.GHNServlet", "/GHN");
//        handler.addServletWithMapping("org.gcube.messaging.common.consumer.servlet.RIServlet", "/RI");
//        handler.addServletWithMapping("org.gcube.messaging.common.consumer.servlet.NotificationServlet", "/NOTIFICATION");
//        handler.addServletWithMapping("org.gcube.messaging.common.consumer.servlet.NodeAccountingServlet", "/NODEINVOCATION");
//        handler.addServletWithMapping("org.gcube.messaging.common.consumer.servlet.PortalLoginServlet", "/Portal");
//         
//       
        /*WebAppContext context = new WebAppContext();

        context.setResourceBase(basePath);
        context.setContextPath("/");
        context.setServer(server);*/
 
		try {
			logger.debug("HTTP Server Base Path : " + resourceHandler.getBaseResource().getFile().getAbsolutePath());
		} catch (IOException e) {
			logger.error(e);
		}
		
		server.setConnectors(new Connector[]{connector});
		server.setHandlers(new Handler[] {resourceHandler,/*handlercontext*/});	
	}
	
	/**
	 * @param connectors Connectors
	 */
	public void setConnectors(Connector[] connectors) {
		server.setConnectors(connectors);
	}
	
	/**
	 * @param handlers Handlers
	 */
	public void setHandlers(Handler[] handlers){
		server.setHandlers(handlers);
	}

	
	/**
	 * {@inheritDoc}
	 */
	public void startServer() throws Exception {
		try {
			server.start();
		} catch (Exception e) {
			String error = "Error while starting WebServer";
			logger.error(error,e);
		}
	
	
	}

	public ResourceHandler getResourceHandler() {
		return resourceHandler;
	}

	public void setResourceHandler(ResourceHandler resourceHandler) {
		this.resourceHandler = resourceHandler;
	}
	
}
