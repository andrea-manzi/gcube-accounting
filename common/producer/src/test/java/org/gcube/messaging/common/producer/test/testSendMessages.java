package org.gcube.messaging.common.producer.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.gcube.messaging.common.messages.NodeAccountingMessage;
import org.gcube.messaging.common.messages.records.IntervalRecord;
import org.gcube.messaging.common.producer.ActiveMQClient;
import org.gcube.messaging.common.producer.GCUBELocalProducer;
import org.junit.Before;
import org.junit.Test;

public class testSendMessages {
	
	public  static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public  static final String DATE_FORMAT_DAY = "yyyy-MM-dd";
	
	public	 static SimpleDateFormat format= new SimpleDateFormat(DATE_FORMAT);
	public	 static SimpleDateFormat format_day= new SimpleDateFormat(DATE_FORMAT_DAY);
	
	@Before
	public void setup(){
		GCUBELocalProducer local = new GCUBELocalProducer();
		local.run();	
		
	}
	
	@Test
	public void testSendMessageToQueue () {
		
		String scope = "/gcube";
		IntervalRecord record = new IntervalRecord(new Long(3000));
		record.setAverageInvocationTime(new Double(0.5));
		record.setIP("127.0.0.1");
		record.setInvocationNumber(new Long(9000));
		try {
			record.setStartInterval(format.parse("2012-05-10 00:00:00"));
			record.setEndInterval(format.parse("2012-05-10 01:00:00"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
		NodeAccountingMessage<IntervalRecord> message = new NodeAccountingMessage<IntervalRecord>();
		message.setRecord("first", record);
		message.setScope(scope);
		message.setSourceGHN("ghn-andrea:8080");
		message.createTopicName(scope);

		
		ActiveMQClient.getSingleton().sendMessageToQueue(message);
		while(true);
		
	}
}
