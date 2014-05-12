package org.gcube.messaging.common.consumer.stubs.calls;

import org.gcube.common.core.scope.GCUBEScope;

/**
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public interface MessagingConsumerServiceCallInterface {

	/**
	 * backup DB
	 * @throws Exception Exception
	 */
	public void backupMonitoringDB() throws Exception;
	/**
	 * 
	 * @throws Exception Exception
	 */
	public void backupAccountingDB() throws Exception;
	/**
	 * 
	 * @param query query
	 * @return results
	 * @throws Exception Exception
	 */
	public String queryMonitoringDB(final String query) throws Exception;
	/**
	 * 
	 * @param query query
	 * @return results
	 * @throws Exception Exception
	 */
	public String queryAccountingDB(final String query) throws Exception;
	
	/**
	 * 
	 * @param query query
	 * @return results
	 * @throws Exception Exception
	 */
	public String querySystemAccountingDB(final String query) throws Exception;
	
	
	/**
	 * 
	 * @param date date
	 * @param scope scope
	 * @throws Exception Exception
	 */
	public void sendReport(String date,GCUBEScope scope)  throws Exception;
}

