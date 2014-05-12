package org.gcube.messaging.common.consumer.servlet;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.gcube.common.core.utils.logging.GCUBELog;
import org.gcube.messaging.common.consumer.ServiceContext;

/**
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public class PortalLoginServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private GCUBELog logger = new GCUBELog(PortalLoginServlet.class);

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);

		if (request.getParameter("type")== null)
		{

			response.getWriter().println("<p align=\"center\"><b>Following records are selected from the 'PORTALACCOUNTING' tables.</b><br></p>");

			ResultSet resultGHN = null;
			try {
				resultGHN = ServiceContext.getContext().getAccountingManager().query("SELECT DISTINCT type FROM PORTALACCOUNTING");
			} catch (SQLException e) {
				logger.error(e);
			} catch (Exception e) {
				logger.error(e);
			}

			response.getWriter().println("<div align=\"center\">"+
			"<center><table border=\"1\" borderColor=\"#ffe9bf\" cellPadding=\"0\" cellSpacing=\"0\" width=\"658\" height=\"63\"><tbody>");

			try {
				while (resultGHN.next()){
					response.getWriter().println("<tr>" +
							"<td  vAlign=\"top\" width=\"47\" align=\"center\" height=\"19\"> <a href=/Portal?type=" +resultGHN.getString("type") +">"+resultGHN.getString("type")+"</a></td></tr>");
				}
			} catch (SQLException e) {
				logger.error(e);	
			}

			finally{
				try {
					resultGHN.close();
				} catch (SQLException e) {
					logger.error(e);
				}
			}
			response.getWriter().println("</tbody></table></center></div>");
		}else{
			ResultSet result = null;
			try {
				result = ServiceContext.getContext().getAccountingManager().query("SELECT PORTALACCOUNTING.id,PORTALACCOUNTING.user,PORTALACCOUNTING.vre,PORTALACCOUNTING.type,PORTALACCOUNTING.date,PORTALACCOUNTING.time" +
						" from PORTALACCOUNTING where PORTALACCOUNTING.type='"+request.getParameter("type")+"'");
			} catch (SQLException e) {
				logger.error(e);
			} catch (Exception e) {
				logger.error(e);
			}


			response.getWriter().println("<p align=\"center\"><b>type "+request.getParameter("type")+"</b><br></p>"+
					"<div align=\"center\">"+
					"<center><table border=\"1\" borderColor=\"#ffe9bf\" cellPadding=\"0\" cellSpacing=\"0\" width=\"800\" height=\"63\"><tbody>"+
					"<td width=\"47\" align=\"center\" height=\"19\"><font color=\"#000000 \"><b>No.</b></font></td>"+
					"<td width=\"107\" height=\"19\"><font color=\"#000000 \"><b>Id</b></font></td>"+
					"<td width=\"107\" height=\"19\"><font color=\"#000000 \"><b>User</b></font></td>"+
					"<td width=\"107\" height=\"19\"><font color=\"#000000 \"><b>VRE</b></font></td>"+
					"<td width=\"107\" height=\"19\"><font color=\"#000000 \"><b>Type</b></font></td>"+
					"<td width=\"224\" height=\"19\"><font color=\"#000000\"><b>Date</b></font></td>"+
					"<td width=\"100\" height=\"19\"><font color=\"#000000\"><b>Time</b></font></td>");

			int i = 0;
			try {
				while (result.next()){
					response.getWriter().println("<tr>" +
							"<td  vAlign=\"top\" width=\"47\" align=\"center\" height=\"19\"> " + i++ +"</td>"+
							"<td  vAlign=\"top\" width=\"107\" height=\"19\"> " + result.getString("id")+" </td>"+
							"<td  vAlign=\"top\" width=\"107\" height=\"19\"> " + result.getString("user")+" </td>"+
							"<td  vAlign=\"top\" width=\"107\" height=\"19\"> " + result.getString("vre")+" </td>"+
							"<td  vAlign=\"top\" width=\"100\" height=\"19\"> "+ result.getString("type")	+" </td>"+
							"<td  vAlign=\"top\" width=\"100\" height=\"19\"> "+ result.getString("date")	+" </td>"+
							"<td  vAlign=\"top\" width=\"100\" height=\"19\"> "+ result.getString("time")	+" </td>"+
							"</tr>");						
				}
			} catch (SQLException e) {
				logger.error(e);
			}finally{
				try {
					result.close();
				} catch (SQLException e) {
					logger.error(e);
				}
			}
			response.getWriter().println("</tbody></table></center></div>");
		}


	}
}
