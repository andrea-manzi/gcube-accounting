package org.gcube.messaging.common.consumerlibrary.query;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


import org.gcube.messaging.common.consumerlibrary.ConsumerCL;
import org.gcube.messaging.common.consumerlibrary.impl.Constants;
import org.gcube.messaging.common.consumerlibrary.json.JSONArray;
import org.gcube.messaging.common.consumerlibrary.json.JSONException;
import org.gcube.messaging.common.consumerlibrary.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 
 * @author Andrea Manzi(CERN)
 *
 * @param <CALL>
 */
public abstract class Query<CALL extends ConsumerCL> implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	Logger logger= LoggerFactory.getLogger(this.getClass());
	
	
	protected CALL call;
	
	protected String results;
	
	protected String query = null;
	
	protected String orderClause = "";
	
	protected String limitClause = "";
	
	protected String groupClause = "";
	
	protected 	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	

	/**
	 * The method implemented by subclasses, that performed the query
	 * @return the query result as a string
	 * @throws Exception
	 */
	protected abstract String performQuery() throws Exception ;
	
	/**
	 * Generic SELECT/SHOW MYSQL query
	 * @return A String containing the query results in JSON format
	 * @throws Exception if the query is not valid, or is not a SELECT/SHOW statement
	 * @throws QueryNotSetException
	 */
	public String query() throws Exception, QueryNotSetException {
		if (query == null)
			throw new QueryNotSetException();
		checkQuery(getQuery());
		setResults(performQuery());
		return	getResults();
		
	}
	
	
	private void checkQuery(String query ) throws Exception{
		if (!(query.startsWith("SELECT")) && 
				!(query.startsWith("SHOW") ))
				throw new Exception("The query MUST be a SELECT/SHOW statement");
				
	}
	
	/**
	 * get results as JSON object
	 * @return A JSON containing the query results in JSON format
	 * @throws JSONException
	 * @throws EmptyResultException
	 */
	public JSONArray toJSON() throws EmptyResultException {
		try {
			//logger.debug(getResults());
			return new  JSONObject(getResults()).getJSONArray("data");	
		}catch (Exception e){
			logger.error(e.getMessage());
			throw new EmptyResultException();
		}
		
		
	}
	
	
	/**
	 * Get the dimension of the given table
	 * @param tableName
	 * @return the table dimension
	 * @throws Exception
	 */
	public Long getDimensions(String tableName) throws Exception {
		this.setQuery("SELECT COUNT(*) AS COUNT FROM "+tableName);
		String json = performQuery();
		return Long.valueOf((String) new JSONObject(json).getJSONArray("data").getJSONObject(0).get("COUNT"));
	
	}
	
	/**
	 * Debug operation, used to print on log query results
	 * @throws JSONException
	 */
	public void printQueryResults() throws Exception{
		for (int k = 0; k < toJSON().getJSONObject(0).names().length();k++)
			logger.debug(toJSON().getJSONObject(0).names().get(k).toString());
		for (int i = 0 ;i<toJSON().length();i++){
			for (int j = 0; j<toJSON().getJSONObject(i).names().length();j++){
				logger.debug(toJSON().getJSONObject(i).get(toJSON().getJSONObject(i).names().getString(j)).toString());
			}
		logger.debug("");
		}
	}
	

	/**
	 * get the CALL object
	 * @return the CALL object
	 */
	public CALL getCall() {
		return call;
	}

	/**
	 * set the CALL object 
	 * @param call the call object
	 */
	public void setCall(CALL call) {
		this.call = call;
	}
	
	/**
	 * Get the Query result stored inside the query object
	 * @return a String object representing the query results
	 */
	public String getResults() {
		return results;
	}
	
	
	/**
	 * set the results inside the query object
	 * @param results
	 */
	public void setResults(String results) {
		this.results = results;
	}
	
	/**
	 * Return the query string ( composed by the user query + grouping+ordering+limits)
	 * @return the query string ( composed by the user query + grouping+ordering+limits)
	 */
	public String getQuery() {
		return query +getGroupClause()+getOrderClause()+getLimitClause();
	}

	/**
	 * set the Query
	 * @param query the query
	 */
	public void setQuery(String query) {
		this.results = null;
		this.query = query;
	}
	
	/**
	 * get the order clause for this query
	 * @return the order clause
	 */
	public String getOrderClause() {
		return orderClause;
	}

	/**
	 * set the order clause
	 * @param order
	 */
	public void setOrderClause(String order) {
		this.orderClause =" "+Constants.ORDERBY+" "+order;
	}

	/**
	 * get the limit clause
	 * @return
	 */
	public String getLimitClause() {
		return limitClause;
	}

	/**
	 * set the limit clause and starting index
	 * @param limit
	 * @param start
	 */
	public void setLimitClause(Integer start, Integer limit) {
		this.limitClause =" "+Constants.LIMIT+" "+start.toString()+ ","+limit.toString();
	}
	
	/**
	 * set the limit clause
	 * @param limit
	 */
	public void setLimitClause(Integer limit) {
		this.limitClause =" "+Constants.LIMIT+" "+limit.toString();
	}

	/**
	 * get the grouping clause
	 * @return
	 */
	public String getGroupClause() {
		return groupClause;
	}
	
	/**
	 * set the grouping clause
	 * @param group
	 */
	public void setGroupClause(String group) {
		this.groupClause =" "+Constants.GROUPBY+" "+group;
	}
	
	/**
	 * 	getResultsAsArray	(first array contains columns names)
	 * @return  ArrayList<ArrayList<String>>
	 * @throws Exception 
	 */
	public ArrayList<ArrayList<String>> getResultsAsArray () throws Exception{
		ArrayList<ArrayList<String>> results = new ArrayList<ArrayList<String>>();
		try {
			ArrayList<String> names = new ArrayList<String>();
			JSONArray tmp = toJSON();
			for (int k = 0; k<tmp.getJSONObject(0).names().length();k++)
					names.add((String)tmp.getJSONObject(0).names().get(k));
			results.add(names);
			for (int i = 0 ;i<tmp.length();i++){
				ArrayList<String> row = new ArrayList<String>();
				for (int j = 0; j<tmp.getJSONObject(i).names().length();j++){
					row.add((String)tmp.getJSONObject(i).get(tmp.getJSONObject(i).names().getString(j)));
				}
				results.add(row);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return results;
	}
}
