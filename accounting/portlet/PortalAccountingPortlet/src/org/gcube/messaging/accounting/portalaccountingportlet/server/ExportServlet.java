package org.gcube.messaging.accounting.portalaccountingportlet.server;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.gcube.common.core.utils.logging.GCUBELog;



/**
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public class ExportServlet extends HttpServlet{



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
		String file=request.getParameter("file");
		FileReader reader = new FileReader(file);
		try{
			response.setContentType("application/vnd.ms-excel; charset=utf-8");
			response.setHeader("Content-Disposition", "attachment; filename=export.csv");
			int i = 0;
			while(( i = reader.read()) != -1){
				response.getWriter().write(i);	
			}
			new File (file).delete();
			reader.close();
			response.setStatus(HttpServletResponse.SC_OK);
		}catch(Exception e){
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}
}