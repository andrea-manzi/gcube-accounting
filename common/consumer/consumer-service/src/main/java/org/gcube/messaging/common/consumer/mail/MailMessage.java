package org.gcube.messaging.common.consumer.mail;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * 
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public class MailMessage {

	String message;
	ArrayList<String>  emailRecipients = null;
	String subject;
	
	public MailMessage (ArrayList<String>  emailRecipients , String message, String subject) {
		this.emailRecipients = emailRecipients;
		this.message= message;
		this.subject = subject;
	}
	
	public void sendMail(){
		try {
			System.getProperties().put("mail.host", InetAddress.getLocalHost().getHostName());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		if (emailRecipients== null) return;
		for (int a = 0; a<emailRecipients.size();a++) {


			try {
				// Establish a network connection for sending mail
				URL u = new URL("mailto:" + emailRecipients.get(a));      // Create a mailto: URL
				URLConnection c = u.openConnection(); // Create a URLConnection for it
				c.setDoInput(false);                  // Specify no input from this URL
				c.setDoOutput(true);                  // Specify we'll do output
				c.connect();                          // Connect to mail host
				PrintWriter out =                     // Get output stream to mail host
					new PrintWriter(new OutputStreamWriter(c.getOutputStream()));

				// Write out mail headers.  Don't let users fake the From address
				out.println("From: \"ConsumerMonitor@gcube-system.org <" +
						System.getProperty("user.name") + "@" +
						InetAddress.getLocalHost().getHostName() + ">");
				out.println("To: "+emailRecipients.get(a));
				out.println("Subject: " +subject);
				out.println();  // blank line to end the list of headers
				out.println(message);

				// Close the stream to terminate the message
				out.close();

			}
			catch (Exception e) {  // Handle any exceptions, print error message.

				e.printStackTrace();
			}
		}
	}
}
