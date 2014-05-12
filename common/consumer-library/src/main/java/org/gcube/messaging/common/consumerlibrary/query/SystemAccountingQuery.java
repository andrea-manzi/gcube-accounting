package org.gcube.messaging.common.consumerlibrary.query;

import java.util.ArrayList;

import org.gcube.messaging.common.consumerlibrary.ConsumerCL;
import org.gcube.messaging.common.consumerlibrary.json.JSONArray;


/**
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public class SystemAccountingQuery extends Query<ConsumerCL>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	@Override
	public String performQuery() throws Exception {
		logger.debug(getQuery());
		Long startTime = System.currentTimeMillis();
		String result = call.querySystemAccountingDB(getQuery());
		Long endTime  = System.currentTimeMillis();
		logger.debug("Query time = "+ (endTime - startTime)+" ms");
		return result;	
	}

	/**
	 * Get the types ( tables ) stored on the SystemAccounting DB
	 * 
	 * @return List of types
	 * @throws QueryNotSetException
	 * @throws Exception
	 */
	public  ArrayList<String> getTypes() throws QueryNotSetException, Exception{
		this.setQuery("SHOW TABLES");
		this.query();
		ArrayList<String> results = new ArrayList<String>();
		int i =0;
		for (ArrayList<String> list : this.getResultsAsArray()){
			if (i==0) {
				i++;
				continue;
			}
			results.add(list.get(0));
		}
		return results;
	}
	
	/**
	 * Get the table content stored on the SystemAccounting DB
	 * 
	 * @param type the table name
	 * @return table content
	 * @throws QueryNotSetException
	 * @throws Exception
	 */
	public  ArrayList<ArrayList<String>> getTypeContent(String name) throws QueryNotSetException, Exception{
		this.getTypeContentAsJSONString(name);
		return this.getResultsAsArray();
	}
	
	/**
	 * Get the table content stored on the SystemAccounting DB as JSON
	 * 
	 * @param type the table name
	 * @return table content
	 * @throws QueryNotSetException
	 * @throws Exception
	 */
	public  String getTypeContentAsJSONString(String name) throws QueryNotSetException, Exception{
		this.setQuery("SELECT * FROM " + name );
		return this.query();
		
	}
	
	/**
	 * Get the table content stored on the SystemAccounting DB as JSON Object
	 * 
	 * @param type the table name
	 * @return table content as JSONArray
	 * @throws QueryNotSetException
	 * @throws Exception
	 */
	public  JSONArray getTypeContentAsJSONObject(String name) throws QueryNotSetException, Exception{
		this.setQuery("SELECT * FROM " + name );
		this.query();
		return this.toJSON();
		
	}
	
	/**
	 * Get the table content stored on the SystemAccounting DB as JSON
	 * 
	 * @param query the SQL Query
	 * @return query results as JSON String
	 * @throws QueryNotSetException
	 * @throws Exception
	 */
	public  String queryTypeContentAsJSONString(String query) throws QueryNotSetException, Exception{
		this.setQuery(query);
		return this.query();
		
	}
	
	/**
	 * Get the table content stored on the SystemAccounting DB as JSON Object
	 * 
	 * @param query the SQL Query
	 * @return query results as JSON Object
	 * @throws QueryNotSetException
	 * @throws Exception
	 */
	public  JSONArray queryTypeContentAsJSONObject(String query) throws QueryNotSetException, Exception{
		this.setQuery(query);
		this.query();
		return this.toJSON();
		
	}
}





