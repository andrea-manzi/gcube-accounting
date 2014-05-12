package org.gcube.messaging.accounting.nodeaccountingportlet.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.gcube.common.core.utils.logging.GCUBELog;

/**
 * 
 * @author Andrea Manzi (CERN)
 *
 */
public class StatisticsServlet  extends HttpServlet{

	
	public static NodeAccountingServiceImpl service = null;
	
	GCUBELog logger = new GCUBELog(QueryServlet.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		handleRequest(req, resp);
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException  {
		handleRequest(req, resp);
	}
	
	protected void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		if (service== null)
			service = new  NodeAccountingServiceImpl();
		String sortColumn=request.getParameter("sort");
		String sortDir=request.getParameter("dir");
		String serviceName=request.getParameter("serviceName");
		String serviceClass=request.getParameter("serviceClass");
		String callerScope=request.getParameter("callerScope");
		String GHNName=request.getParameter("GHNName");
		String group=request.getParameter("GroupBy");
		String dateStart=request.getParameter("DateStart");
		String dateEnd=request.getParameter("DateEnd");
		
		StringBuilder sb=new StringBuilder();
		try{
			sb.append(service.getStatistics(serviceClass, serviceName,callerScope,GHNName,group,sortColumn,sortDir,new String[] {dateStart,dateEnd}));
			response.setContentType("application/json; charset=utf-8");		
			response.getWriter().write(sb.toString());
			response.setStatus(HttpServletResponse.SC_OK);
		}catch(Exception e){
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}


}
