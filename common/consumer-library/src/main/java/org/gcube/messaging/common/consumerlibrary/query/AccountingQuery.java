package org.gcube.messaging.common.consumerlibrary.query;

import org.gcube.messaging.common.consumerlibrary.ConsumerCL;


/**
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public class AccountingQuery extends Query<ConsumerCL>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	@Override
	public String performQuery() throws Exception {
		logger.debug(getQuery());
		Long startTime = System.currentTimeMillis();
		String result = call.queryAccountingDB(getQuery());
		Long endTime  = System.currentTimeMillis();
		logger.debug("Query time = "+ (endTime - startTime)+" ms");
		return result;
		
	}
	
}
