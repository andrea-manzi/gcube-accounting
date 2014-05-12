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
 * A sample RI Servlet to show RI Message table content
 * @author Andrea Manzi
 *
 */
public class RIServlet extends HttpServlet {

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

			response.getWriter().println("<p align=\"center\"><b>Following records are selected from the 'RIMessage' table.</b><br></p>");

			ResultSet resultRI = null;
			try {
				resultRI = ServiceContext.getContext().getMonitoringManager().query("SELECT DISTINCT GHNName FROM RIMessage");
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (Exception e) {

				e.printStackTrace();
			}

			response.getWriter().println("<div align=\"center\">"+
			"<center><table border=\"1\" borderColor=\"#ffe9bf\" cellPadding=\"0\" cellSpacing=\"0\" width=\"658\" height=\"63\"><tbody>");

			try {
				while (resultRI.next()){
					response.getWriter().println("<tr>" +
							"<td  vAlign=\"top\" width=\"47\" align=\"center\" height=\"19\"> <a href=/RI?GHN=" +resultRI.getString("GHNName")+">"+resultRI.getString("GHNName")+"</a></td></tr>");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

			finally{
				try {
					resultRI.close();
				} catch (SQLException e) {

					e.printStackTrace();
				}
			}
			response.getWriter().println("</tbody></table></center></div>");
		}else{
			ResultSet result = null;
			try {
				result = ServiceContext.getContext().getMonitoringManager().query("SELECT ServiceName,ServiceClass,testType,description,result,date,time FROM RIMessage where GHNName='"+request.getParameter("GHN")+"'");
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (Exception e) {

				e.printStackTrace();
			}


			response.getWriter().println("<p align=\"center\"><b>GHN "+request.getParameter("GHN")+"</b><br></p>"+
					"<div align=\"center\">"+
					"<center><table border=\"1\" borderColor=\"#ffe9bf\" cellPadding=\"0\" cellSpacing=\"0\" width=\"800\" height=\"63\"><tbody>"+
					"<td width=\"47\" align=\"center\" height=\"19\"><font color=\"#000000 \"><b>No.</b></font></td>"+
					"<td width=\"107\" height=\"19\"><font color=\"#000000 \"><b>ServiceName</b></font></td>"+
					"<td width=\"107\" height=\"19\"><font color=\"#000000 \"><b>SerciceClass</b></font></td>"+
					"<td width=\"107\" height=\"19\"><font color=\"#000000 \"><b>testType</b></font></td>"+
					"<td width=\"224\" height=\"19\"><font color=\"#000000\"><b>Description</b></font></td>"+
					"<td width=\"360\" height=\"19\"><font color=\"#000000\"><b>Result</b></font></td>"+
					"<td width=\"100\" height=\"19\"><font color=\"#000000\"><b>Date</b></font></td>"+
					"<td width=\"100\" height=\"19\"><font color=\"#000000\"><b>Time</b></font></td>");

			int i = 0;
			try {
				while (result.next()){
					response.getWriter().println("<tr>" +
							"<td  vAlign=\"top\" width=\"47\" align=\"center\" height=\"19\"> " + i++ +"</td>"+
							"<td  vAlign=\"top\" width=\"107\" height=\"19\"> " + result.getString("ServiceName")+" </td>"+
							"<td  vAlign=\"top\" width=\"107\" height=\"19\"> " + result.getString("ServiceClass")+" </td>"+
							"<td  vAlign=\"top\" width=\"107\" height=\"19\"> " + result.getString("testType")+" </td>"+
							"<td  vAlign=\"top\" width=\"224\" height=\"19\"> "+ result.getString("description")	+" </td>"+
							"<td  vAlign=\"top\" width=\"360\" height=\"19\"> "+ result.getString("result")	+" </td>"+
							"<td  vAlign=\"top\" width=\"100\" height=\"19\"> "+ result.getString("date")	+" </td>"+
							"<td  vAlign=\"top\" width=\"100\" height=\"19\"> "+ result.getString("time")	+" </td>"+
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


