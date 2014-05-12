package org.gcube.messaging.common.consumer.mail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.xerces.dom.DocumentImpl;
import org.gcube.common.core.contexts.GHNContext;
import org.gcube.common.core.scope.GCUBEScope;
import org.gcube.common.core.utils.handlers.GCUBEHandler;
import org.gcube.common.core.utils.handlers.GCUBEScheduledHandler;
import org.gcube.common.core.utils.handlers.GCUBEScheduledHandler.Mode;
import org.gcube.common.core.utils.logging.GCUBELog;
import org.gcube.messaging.common.consumer.Constants;
import org.gcube.messaging.common.consumer.ServiceContext;
import org.gcube.messaging.common.consumer.webserver.impl.jetty.JettyWebServer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 *  Mail summary class
 * @author Andrea Manzi(CERN)
 *
 */
public class MailSummary extends GCUBEHandler<MailSummary> {

	GCUBELog logger = new GCUBELog(MailSummary.class);	
	
	public  static final String DATE_FORMAT_DAY = "yyyy-MM-dd";
	
	public	 static SimpleDateFormat format= new SimpleDateFormat(DATE_FORMAT_DAY);

	final static long daylyInterval = 24*3600; 

	protected static DailyScheduler scheduler = null;

	private String reportBase ="";

	/**
	 * The constructor
	 * @throws IOException IOException
	 */
	public MailSummary() throws IOException{
		if (scheduler == null)
			scheduler = new DailyScheduler(daylyInterval, Mode.EAGER,this);
		this.reportBase =  ((JettyWebServer)ServiceContext.getContext().getServer()).getResourceHandler().getBaseResource().getFile().getAbsolutePath()+
		File.separator+"report";
		new File(reportBase).mkdirs();
	}

	/**
	 * execute the scheduler
	 *
	 */
	public void execute()
	{
		try {
			scheduler.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() throws Exception {
		for (String scope :getScopesFromDB())
			ServiceContext.getContext().getMailClient().sendMailSummary(generateSummary(scope),scope);

	}

	private ArrayList<String> getScopesFromDB(){

		ResultSet result = null;
		ArrayList<String> scopes =  new ArrayList<String>();
		String query = "SELECT DISTINCT scope from  NOTIFICATION";
		try {
			result = ServiceContext.getContext().getMonitoringManager().query(query);

			while (result.next())
				scopes.add(result.getString("scope"));
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return scopes;
	}

	private String generateSummary(String scope,String ... date){

		String message = ("Daily Summary about infrastructure: "+ scope+"\n");
		message += "\n"+this.createReport(scope,date );
		return message;

	}

	private void createGHNReport(String scope, String date,ResultSet resultSubtype,String reportBaseDir) {

		ResultSet result;
		Document xmlReport= new DocumentImpl();
		Element rootReport = xmlReport.createElement("ROOT");
		String reportFileName= "";
		String reportDir ="";

		try {
			reportDir = reportBaseDir + File.separator +resultSubtype.getString("testSubtype");
			File reportFileDir = new File(reportDir);
			reportFileDir.mkdirs();
			reportFileName =reportDir+File.separator+"report.xml";
			String query = "SELECT * from  NOTIFICATION WHERE date='" +date+"' AND testSubtype='"+resultSubtype.getString("testSubtype")+"'";	
			logger.debug("Query :" +query);
			
			result =ServiceContext.getContext().getMonitoringManager().query(query);

			Element e = null;
			e = xmlReport.createElementNS(null, "TestSubtype");
			e.setAttributeNS(null, "Name", resultSubtype.getString("testSubtype"));
			
			while (result.next()){
				Element not = null;
				not = xmlReport.createElementNS(null, "Notification");
				
				Element ghn = null;
				ghn = xmlReport.createElementNS(null, "GHN");
				ghn.appendChild(xmlReport.createTextNode(result.getString("GHNName")));
	
				Element child = null;
				child = xmlReport.createElementNS(null, "TestType");
				child.appendChild(xmlReport.createTextNode(result.getString("testType")));
	
				Element serviceName = null;
				serviceName = xmlReport.createElementNS(null, "ServiceName");
				serviceName.appendChild(xmlReport.createTextNode(result.getString("ServiceName")));
	
				Element serviceClass = null;
				serviceClass = xmlReport.createElementNS(null, "ServiceClass");
				serviceClass.appendChild(xmlReport.createTextNode(result.getString("ServiceClass")));
				
				Element message = null;
				message = xmlReport.createElementNS(null, "Message");
				message.appendChild(xmlReport.createTextNode(result.getString("message")));
				
				
				Element time = null;
				time = xmlReport.createElementNS(null, "Time");
				time.appendChild(xmlReport.createTextNode(result.getString("time")));
				
				not.appendChild(child);
				not.appendChild(ghn);
				not.appendChild(serviceName);
				not.appendChild(serviceClass);
				not.appendChild(message	);
				not.appendChild(time);
				e.appendChild(not);
			}

			rootReport.appendChild(e);


		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		xmlReport.appendChild(rootReport);

		xmlReport.insertBefore(xmlReport.createProcessingInstruction ("xml-stylesheet", "type=\"text/xsl\" href=\"" + 
				"../report.xslt" + "\""),
				xmlReport.getDocumentElement());

		//serialize report
		try {
			createXMLFile(xmlReport,reportFileName);
		}

		catch (Exception e) {
			e.printStackTrace();
		}

	}

	private String createReport(String scope,String ... dateInput) {

		ResultSet resultSubtype = null;
		String date = "";
		if (dateInput.length == 0) {
			Calendar yesterday= Calendar.getInstance();
			yesterday.add(Calendar.DATE,-1);
			date = format.format(yesterday.getTime());
		} 		 
		else date = dateInput[0];
		
		//
		String reportIndexFileName = "";
		String reportIndexXSLTName = "";
		
		String reportXSLTName = "";
		String reportFileDir = "";
		String reportURL = "";
		String reportBaseURL = "";


		try {
			reportFileDir =reportBase+File.separator+date+File.separator+scope;
			new File(reportFileDir).mkdirs();
			reportIndexFileName = reportFileDir+File.separator+"index.xml";
			reportIndexXSLTName = reportFileDir+File.separator+"index.xslt";
			reportXSLTName = reportFileDir+File.separator+"report.xslt";
			reportBaseURL = "http://"+GHNContext.getContext().getHostname()+":"+ServiceContext.getContext().getHttpServerPort()+File.separator+"report"+File.separator+date+File.separator+scope;
			reportURL = reportBaseURL+File.separator+"index.xml";
		}catch (Exception e1) {
			e1.printStackTrace();
		}


		Document xmlIndex= new DocumentImpl();
		//Root element.
		Element rootIndex = xmlIndex.createElement("ROOT");
		try {
			resultSubtype = ServiceContext.getContext().getMonitoringManager().
			query("SELECT testSubType, count(testSubType) AS COUNT FROM NOTIFICATION WHERE date='"+date+"' GROUP BY testSubType");
			while (	resultSubtype.next()) {
				//create GHN node
				Element e = null;
				e = xmlIndex.createElementNS(null, "TestSubType");
				e.setAttributeNS(null, "Type", resultSubtype.getString("COUNT") + " " + resultSubtype.getString("testSubType")+" Notifications");
				e.setAttributeNS(null, "Report",reportBaseURL+File.separator+ resultSubtype.getString("testSubType")+"/report.xml");
				createGHNReport(scope,date,resultSubtype,reportFileDir);
				rootIndex.appendChild(e);

			}
			xmlIndex.appendChild(rootIndex);
			xmlIndex.insertBefore(xmlIndex.createProcessingInstruction ("xml-stylesheet", "type=\"text/xsl\" href=\"" + 
					"index.xslt" + "\""),
					xmlIndex.getDocumentElement());
			//create index
			createXMLFile(xmlIndex,reportIndexFileName);
			//copy XSLT files
			copyfile(GHNContext.getContext().getLocation()+File.separator+
					ServiceContext.getContext().getProperty(Constants.CONFIGDIR_JNDI_NAME, false)+File.separator+
					"index.xslt"
					,reportIndexXSLTName);
			copyfile(GHNContext.getContext().getLocation()+File.separator+
					ServiceContext.getContext().getProperty(Constants.CONFIGDIR_JNDI_NAME, false)+File.separator+
					"report.xslt"
					,reportXSLTName);
			createScopeHTMLFile(reportBase+File.separator+date,date);
			createIndexHTMLFile();

		}catch (Exception e) {
			e.printStackTrace();
		}
		
		

		return reportURL;

	}

	private void createIndexHTMLFile() throws IOException {
		File index = new File(this.reportBase+File.separator+"index.html");
		index.createNewFile();
		FileWriter writer = new FileWriter(index);
		writer.write("<html><body>");
		writer.write("<table border=\"1\"><tbody><tr><th bgcolor=\"#9acd32\" >Date</th></tr>");
		for (File dir :new File(this.reportBase).listFiles()){
			if (dir.isDirectory()){
				writer.write("<tr><th><a href=\"/report/"+dir.getName()+File.separator+"index.html\">"+ dir.getName() +"</a></th></tr>");
			}
		
		}
		writer.write("</table</tbody>");
		writer.write("</html></body>");
		writer.flush();
		writer.close();
		
	}
	
	private void createScopeHTMLFile(String reportFileDir, String date) throws IOException {
		File index = new File(reportFileDir+File.separator+"index.html");
		index.createNewFile();
		FileWriter writer = new FileWriter(index);
		writer.write("<html><body>");
		writer.write("<table border=\"1\"><tbody><tr><th bgcolor=\"#9acd32\" >scope</th></tr>");
		for (File dir :new File(reportFileDir).listFiles()){
			if (dir.isDirectory()){
				writer.write("<tr><th><a href=\"/report/"+date+File.separator+dir.getName()+File.separator+"index.xml\">"+ dir.getName() +"</a></th></tr>");
				/*for (File subDir :dir.listFiles()){
					if (subDir.isDirectory()){
						writer.write("<tr><th><a href=\"/report/"+date+File.separator+dir.getName()+File.separator+subDir.getName()+File.separator+"index.xml\">"+ dir.getName() +File.separator+subDir.getName()+"</a></th></tr>");						
					}
					
				}*/
			}
		}
		writer.write("</table</tbody>");
		writer.write("</html></body>");
		writer.flush();
		writer.close();
		
	}
	/**
	 * Send A daily report
	 * @param scope the scope
	 * @param date the date 
	 * @throws IOException exception
	 */
	public static void sendReport(GCUBEScope scope,String date) throws IOException 
	{
		ServiceContext.getContext().getMailClient().sendMailSummary( new MailSummary().generateSummary(scope.toString(),date),scope.toString());	
	}

	private static void copyfile(String srFile, String dtFile){
		try{
			File f1 = new File(srFile);
			File f2 = new File(dtFile);
			InputStream in = new FileInputStream(f1);

			OutputStream out = new FileOutputStream(f2);

			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0){
				out.write(buf, 0, len);
			}
			in.close();
			out.close();

		}
		catch(FileNotFoundException ex){
			ex.printStackTrace();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}

	private void createXMLFile(Document doc,String fileName) throws IOException, TransformerFactoryConfigurationError, TransformerException{
		File file = new File(fileName);
		file.createNewFile();
		Source source = new DOMSource(doc);
		Result stream = new StreamResult(new FileOutputStream(file));
		Transformer xformer = TransformerFactory.newInstance().newTransformer();
		xformer.setOutputProperty(OutputKeys.INDENT, "yes");
		xformer.transform(source, stream);

	}

	private class DailyScheduler extends GCUBEScheduledHandler {


		public long getInterval(){
			return this.interval;
		}

		public void setInterval(long interval){
			this.interval = interval*1000;
		}
		public DailyScheduler(long interval, Mode mode,MailSummary client) {
			super(interval, mode, client);
		}

	}
	
}
