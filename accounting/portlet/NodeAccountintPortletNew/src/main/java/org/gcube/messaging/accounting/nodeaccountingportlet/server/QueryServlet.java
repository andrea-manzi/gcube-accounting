package org.gcube.messaging.accounting.nodeaccountingportlet.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.gcube.messaging.accounting.nodeaccountingportlet.client.Costants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 
 * @author Andrea Manzi (CERN)
 *
 */
public class QueryServlet extends HttpServlet{

	
	public static NodeAccountingServiceImpl service = null;
	
	/** Logger */
	private static Logger logger = LoggerFactory.getLogger(QueryServlet.class);
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
		int start = 0;
		int limit = 300;
		if (request.getParameter(Costants.START) != null)
			start = Integer.parseInt(request.getParameter(Costants.START));
		if (request.getParameter(Costants.LIMIT) != null)
			limit =Integer.parseInt(request.getParameter(Costants.LIMIT));
		
		String sortColumn=request.getParameter("sort");
		String sortDir=request.getParameter("dir");
		String ghn=request.getParameter("GHN");
		String serviceclass=request.getParameter("serviceClass");
		String servicename=request.getParameter("serviceName");
		String scope=request.getParameter("scope");
		String dateStart=request.getParameter("DateStart");
		String dateEnd=request.getParameter("DateEnd");
		StringBuilder sb=new StringBuilder();
		try{
			sb.append(service.getRecords(ghn,scope,serviceclass, servicename,sortColumn,sortDir,start,limit,new String[] {dateStart,dateEnd}));
			response.setContentType("application/json; charset=utf-8");		
			response.getWriter().write(sb.toString());
			response.setStatus(HttpServletResponse.SC_OK);
		}catch(Exception e){
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}


}
