package org.gcube.messaging.accounting.system;

/**
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public class SystemAccountingFactory {

	
	static SystemAccounting accounting = null;
		
	/**
	 *  Get a static Instance of the System Accounting library
	 * 
	 * @return SystemAccounting 
	 * @throws Exception Initialization Exception
	 */
	public static SystemAccounting getSystemAccountingInstance() throws Exception {
		if (accounting == null)
			accounting= new SystemAccounting();
		return accounting;
	}
	
}
