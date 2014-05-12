package org.gcube.common.accounting.node.logparser;


import java.text.ParseException;
import java.util.Date;
import java.util.StringTokenizer;

import org.gcube.common.accounting.node.logparser.RIInvocationParser.EntryType;
import org.gcube.common.accounting.node.util.Util;
import org.gcube.common.core.scope.GCUBEScope;

/**
 * 
 * @author Andrea Manzi(CERN)
 * 
 *
 */
public class LogEntry {
	
	
	EntryType type = null;
	String line;
	CallInfo info ;
	GCUBEScope scope;
	double invocationTime;
	Date startDate;
	String time;
	
	private final static String tokensSeparator= ",";
	
	/**
	 * create a log entry object
	 * @param type type
	 * @param line line
	 * @param time time
	 * @throws ParseException ParseException
	 */
	public LogEntry(EntryType type,String line,String time) throws ParseException{
		this.type = type;
		this.line = line;
		this.startDate = createStartDate(time);
		this.time= time;
		parse(line);
	}

	/**
	 * 
	 * @param line line
	 */
	private void parse(String line){
		StringTokenizer tokenizer = new StringTokenizer(line,tokensSeparator);
		while (tokenizer.hasMoreTokens()){
			String token = tokenizer.nextToken();
			if ((token.startsWith(RIInvocationParser.EntryType.ENDCALL.type)))
				info = new CallInfo(token.substring(token.indexOf("(")+1,token.indexOf(")")),
						token.substring(token.lastIndexOf("(")+1,token.lastIndexOf(")")));
			else if (token.startsWith("/")) 
				scope = GCUBEScope.getScope(token);
			else if (token.startsWith("["))
				invocationTime = Double.valueOf(token.substring(token.indexOf("[")+1,token.indexOf("]")));			
		}
		
	}
	
	/**
	 * Store info about the call
	 * 
	 * @author Andrea Manzi(CERN)
	 *
	 */
	public class CallInfo{
		
		private String serviceName;
		private String serviceClass;
		private String callerIP;
		
		/**
		 * constructor
		 * @param from token
		 * @param to token
		 */
		public CallInfo(String from, String to){
			serviceClass = to.substring(0,to.indexOf(":"));
			String temp = to.substring(to.indexOf(":")+1);
			temp = temp.substring(0,temp.indexOf(":"));
			serviceName = temp;
			callerIP = from;
		}
		
		/**
		 * tostring
		 */
		public String toString () {
			return this.serviceClass+"_"+this.serviceName;
		}
		/**
		 * get the serviceclass
		 * @return serviceclass
		 */
		public String getServiceClass() {
			return serviceClass;
		}
		/**
		 * set the serviceclass
		 * @param serviceClass
		 */
		public void setServiceClass(String serviceClass) {
			this.serviceClass = serviceClass;
		}
		/**
		 * get the servicename
		 * @return servicename
		 */
		public String getServiceName() {
			return serviceName;
		}
		/**
		 * set the servicename
		 * @param serviceName
		 */
		public void setServiceName(String serviceName) {
			this.serviceName = serviceName;
		}
		/** 
		 * get the caller IP
		 * @return
		 */
		public String getCallerIP() {
			return callerIP;
		}

		/**
		 * set the caller IP
		 * @param callerIP
		 */
		public void setCallerIP(String callerIP) {
			this.callerIP = callerIP;
		}

	}

	private Date createStartDate (String time) throws ParseException{
		return Util.format.parse(time);
	}
	
	/**
	 * get the CallInfo object
	 * @return
	 */
	public CallInfo getInfo() {
		return info;
	}


	/**
	 * set the callInfo object
	 * @param info
	 */
	public void setInfo(CallInfo info) {
		this.info = info;
	}


	/**
	 * get the invocationTIme
	 * @return the invocation Time
	 */
	public double getInvocationTime() {
		return invocationTime;
	}


	/**
	 * set the invocation TIme
	 * @param invocationTime
	 */
	public void setInvocationTime(double invocationTime) {
		this.invocationTime = invocationTime;
	}


	/**
	 * get the scope
	 * @return scope
	 */
	public GCUBEScope getScope() {
		return scope;
	}


	/**
	 * set the scope
	 * @param scope the scope
	 */
	public void setScope(GCUBEScope scope) {
		this.scope = scope;
	}


	/** 
	 * get the entry type
	 * @return the entry type
	 */
	public EntryType getType() {
		return type;
	}


	/**
	 * set the entry type
	 * @param type
	 */
	public void setType(EntryType type) {
		this.type = type;
	}
	

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/** 
	 * get the time
	 * @return the time
	 */
	public String getTime() {
		return time;
	}

	/**
	 * set the time
	 * @param time the time
	 */
	public void setTime(String time) {
		this.time = time;
	}
}
