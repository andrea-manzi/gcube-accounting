package org.gcube.messaging.common.consumer.servlet;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.gcube.messaging.common.consumer.ServiceContext;

/**
 * 
 * @author Andrea Manzi (CERN)
 *
 */
public class NodeAccountingServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);

		if (request.getParameter("GHN")== null)
		{

			response.getWriter().println("<p align=\"center\"><b>Following records are selected from the 'NODEACCOUNTING' table.</b><br></p>");

			ResultSet resultGHN = null;
			try {
				resultGHN = ServiceContext.getContext().getAccountingManager().query("SELECT DISTINCT GHNName FROM NODEACCOUNTING");
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (Exception e) {

				e.printStackTrace();
			}

			response.getWriter().println("<div align=\"center\">"+
			"<center><table border=\"1\" borderColor=\"#ffe9bf\" cellPadding=\"0\" cellSpacing=\"0\" width=\"658\" height=\"63\"><tbody>");

			try {
				while (resultGHN.next()){
					response.getWriter().println("<tr>" +
							"<td  vAlign=\"top\" width=\"47\" align=\"center\" height=\"19\"> <a href=/NODEINVOCATION?GHN=" +resultGHN.getString("GHNName") +">"+resultGHN.getString("GHNName")+"</a></td></tr>");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

			finally{
				try {
					resultGHN.close();
				} catch (SQLException e) {

					e.printStackTrace();
				}
			}
			response.getWriter().println("</tbody></table></center></div>");
		}else{
			ResultSet result = null;
			try {/*
				GHNName                                     VARCHAR(64) NOT NULL,
							ServiceClass                       			VARCHAR(64) NOT NULL,
 	                        ServiceName									VARCHAR(128) NOT NULL,
 	                        callerScope 	                            VARCHAR(128) NOT NULL,
 	                        date										DATE  NOT NULL,
 	                        timeframe									VARCHAR(10) NOT NULL,
 	                        invocationNumber							INTEGER  NOT NULL,
 	                        averageInvocationTime						FLOAT  NOT NULL,
			 */
				result = ServiceContext.getContext().getAccountingManager().query("SELECT * from NODEACCOUNTING where GHNName='"+request.getParameter("GHN")+"'");
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (Exception e) {

				e.printStackTrace();
			}


			response.getWriter().println("<p align=\"center\"><b>GHN "+request.getParameter("GHN")+"</b><br></p>"+
					"<div align=\"center\">"+
					"<center><table border=\"1\" borderColor=\"#ffe9bf\" cellPadding=\"0\" cellSpacing=\"0\" width=\"800\" height=\"63\"><tbody>"+
					"<td width=\"47\" align=\"center\" height=\"19\"><font color=\"#000000 \"><b>No.</b></font></td>"+
					"<td width=\"107\" height=\"19\"><font color=\"#000000 \"><b>ServiceClass</b></font></td>"+
					"<td width=\"107\" height=\"19\"><font color=\"#000000\"><b>ServiceName</b></font></td>"+
					"<td width=\"360\" height=\"19\"><font color=\"#000000\"><b>CallerScope</b></font></td>"+
					"<td width=\"100\" height=\"19\"><font color=\"#000000\"><b>startDate</b></font></td>"+
					"<td width=\"100\" height=\"19\"><font color=\"#000000\"><b>endDate</b></font></td>"+
					"<td width=\"100\" height=\"19\"><font color=\"#000000\"><b>timeframe</b></font></td>" +
					"<td width=\"100\" height=\"19\"><font color=\"#000000\"><b>CallerIp</b></font></td>" +
					"<td width=\"100\" height=\"19\"><font color=\"#000000\"><b>InvocationNumber</b></font></td>"+
					"<td width=\"100\" height=\"19\"><font color=\"#000000\"><b>AverageInvocationTime</b></font></td>");
		

			int i = 0;
			try {
				while (result.next()){
					response.getWriter().println("<tr>" +
							"<td  vAlign=\"top\" width=\"47\" align=\"center\" height=\"19\"> " + i++ +"</td>"+
							"<td  vAlign=\"top\" width=\"107\" height=\"19\"> " + result.getString("ServiceClass")+" </td>"+
							"<td  vAlign=\"top\" width=\"107\" height=\"19\"> " + result.getString("ServiceName")+" </td>"+
							"<td  vAlign=\"top\" width=\"224\" height=\"19\"> "+ result.getString("callerScope")	+" </td>"+
							"<td  vAlign=\"top\" width=\"100\" height=\"19\"> "+ result.getString("startDate")	+" </td>"+
							"<td  vAlign=\"top\" width=\"100\" height=\"19\"> "+ result.getString("endDate")	+" </td>"+
							"<td  vAlign=\"top\" width=\"100\" height=\"19\"> "+ result.getString("timeframe")	+" </td>"+
							"<td  vAlign=\"top\" width=\"100\" height=\"19\"> "+ result.getString("callerIP")	+" </td>"+
							"<td  vAlign=\"top\" width=\"100\" height=\"19\"> "+ result.getString("invocationNumber")	+" </td>"+
							"<td  vAlign=\"top\" width=\"100\" height=\"19\"> "+ result.getString("averageInvocationTime")	+" </td>"+
					"</tr>");						
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}finally{
				try {
					result.close();
				} catch (SQLException e) {

					e.printStackTrace();
				}
			}
			response.getWriter().println("</tbody></table></center></div>");
		}


	}
}
