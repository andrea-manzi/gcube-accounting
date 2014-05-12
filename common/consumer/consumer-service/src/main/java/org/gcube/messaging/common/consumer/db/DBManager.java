package org.gcube.messaging.common.consumer.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.gcube.common.core.utils.logging.GCUBELog;
import org.gcube.messaging.common.consumer.ServiceContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

//import com.mysql.jdbc.CommunicationsException;


/**
 * 
 * @author  Luca Frosini (CNR), Andrea Manzi(CERN)
 *
 */
public abstract class DBManager {

	GCUBELog logger = new GCUBELog(DBManager.class);
	/**
	 * Connection to the db - persist for life of program
	 */
	protected Connection connection;

	protected static File backupFolder = null;

	/**
	 * dbFileName is used to open or create files that hold the state
	 * of the db. It can contain directory names relative to the current 
	 * working directory
	 * 
	 */

	protected String dbFileBaseFolder;

	protected String dbFileName;
	
	protected String dbName;

	/**
	 * Username for db connection
	 */
	protected String username = ServiceContext.getContext().getDbuser();
	

	/**
	 * Password for db connection
	 */
	protected String password = ServiceContext.getContext().getDbpass();

	/**
	 * Queries file
	 */
	protected File queriesFile;
	
	protected PoolManager poolManager = null;


	/**
	 * Constructor for SQLDBManager
	 *
	 */
	public DBManager() {}


	protected static int backupIntervalMS = 3600 * 1000 * Integer.valueOf((String) ServiceContext.getContext().getProperty("scheduledBackupInHours", true));

	public static interface BaseConsumer {
		/**
		 * @param resultset the ResultSet to consume
		 * @throws Exception if the Consuming of the ResultSet fails
		 */
		void consume(ResultSet resultset) throws Exception;
	}

	/**
	 * Load the HSQL Database Engine JDBC driver and open the connection to the db.
	 * 
	 * @throws ClassNotFoundException if the HSQL Database Engine JDBC driver is not loaded
	 * @throws SQLException if the connection to the db fails
	 * @throws Exception if the tables creation fails
	 */
	public abstract void open() throws ClassNotFoundException, SQLException, Exception ;

	/**
	 * Shutdown the db and close the connection to the db.
	 * @throws Exception 
	 */
	public synchronized void close() throws Exception {

		try {
			if (connection == null) 
				reconnectToDB();
			
			if (ServiceContext.getContext().getUseEmbeddedDB()){
				connection.createStatement().execute("SHUTDOWN");
			}
		} 
		catch (SQLException e) {
			throw e;
		} finally {
			connection.close(); // If there are no other open connection
		}
	}


	/**
	 * checkpoint  the db and create a backup
	 * 
	 * @throws SQLException if the CHECKPOINT query fails
	 */
	public synchronized void backup() throws SQLException, Exception {

		try {
			if (connection == null) 
				reconnectToDB();
			connection.createStatement().execute("CHECKPOINT");

		} catch (SQLException e) {
			throw e;
		}
		backupFolder.mkdirs();
		new Thread() {
			public void run() {
				try {
					zipFolder(new File(dbFileBaseFolder).listFiles());
				}catch (Exception e) {
					logger.error("Error creating a backup for the DB",e);
				}
			}
		}.start();
	}



	/**
	 * Executes SQL command SELECT and invokes the given consumer.
	 * 
	 * @param expression the SQL expression to evaluate
	 * @param consumer the resultset consumer to invoke
	 * @throws SQLException if the query fails
	 * @throws Exception if the given resultset consumer fails
	 */
	public synchronized void queryAndConsume(String expression, BaseConsumer consumer) throws SQLException,Exception {
		Statement statement = null;	
		try {
			if (connection == null) 
				reconnectToDB();
				
			statement = connection.createStatement();

			ResultSet resultSet = statement.executeQuery(expression);
			consumer.consume(resultSet);
		} 
		catch (Exception e){
			reconnectToDB();
			throw e;
		}/*catch (SQLException e) {

			throw e;
		} catch (Exception e) {

			throw e;
		} finally {
			if (statement != null) statement.close();
		}    */    

	}


	/**
	 * Executes SQL command SELECT and invokes the given consumer.
	 * 
	 * @param expression the SQL expression to evaluate
	 * @return Result Set
	 * @throws SQLException if the query fails
	 * @throws Exception if the given resultset consumer fails
	 */
	public synchronized ResultSet query(String expression) throws SQLException,Exception {
		Statement statement = null;	
		try {
			if (connection == null) 
				reconnectToDB();
			statement = connection.createStatement();

			return statement.executeQuery(expression);
		} 
		catch (Exception e){
			reconnectToDB();
			throw e;}
		
		/*}catch (SQLException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		} */
	}

	/**
	 * Executes SQL command SELECT and invokes the given consumer, giging back JSON formatetted output.
	 * 
	 * @param expression the SQL expression to evaluate
	 * @return Result Set
	 * @throws SQLException if the query fails
	 * @throws Exception if the given resultset consumer fails
	 */
	public synchronized String  queryJSON(String expression) throws SQLException,Exception {
		Statement statement = null;	
		try {
			if (connection == null) 
				reconnectToDB();
			statement = connection.createStatement();
			return toJSon(statement.executeQuery(expression));
		} 
		catch (Exception e){
			reconnectToDB();
			throw e;
		} finally {
			if (statement != null) statement.close();
		}
	}



	/**
	 * Executes SQL commands CREATE, DROP, INSERT and UPDATE.
	 * 
	 * @param expression the SQL expression to modify the db
	 * @throws Exception 
	 * 
	 */
	public synchronized void update(String expression) throws Exception {
		Statement statement = null;
		try {
			if (connection == null) 
				reconnectToDB();
			statement = connection.createStatement();

			statement.executeUpdate(expression); // Run the Query		    
		} 
	
		catch (SQLException e) {
			throw e;
		}catch (Exception e){
			reconnectToDB();
		} 
		finally {
			if (statement != null) statement.close();
		}
	}

	/**
	 * Create db tables.
	 * 
	 * @throws SQLException if the tables creation fails
	 * @throws Exception if the parsing of query file fails
	 */
	protected void createDB() throws SQLException, Exception{


		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		Document domDocument = null;
		try {
			builder = factory.newDocumentBuilder();
			domDocument = builder.parse(queriesFile);
		} catch (Exception e) {
			throw e;
		}

		Element root = domDocument.getDocumentElement();

		NodeList nodeList = root.getChildNodes();
		for(int i=0; i<nodeList.getLength(); i++){
			Node node = nodeList.item(i);
			if (node.getFirstChild() == null)
				continue;
			String expression = node.getFirstChild().getNodeValue().trim();
			if (expression.compareTo("")==0)
				continue;
			logger.debug("Executing update query n. " + i + "\nThe query is '" + expression + "'");
			update(expression);
			logger.debug("Query: '" + expression + "' executed successfully");
		} 

		logger.info("DB created successfully");
	}

	/**
	 * 
	 * @param resultSet
	 * @return Json String
	 * @throws SQLException
	 */
	public static String toJSon(ResultSet resultSet ) throws SQLException
	{

		StringBuilder json = new StringBuilder();

		json.append("{\"data\":[");

		ResultSetMetaData metaData = resultSet.getMetaData();
		int numberOfColumns = metaData.getColumnCount();

		int row = 0;
		while(resultSet.next()){
			
			if (row>0) json.append(",{");
			else json.append('{');

			for (int column = 1; column <=numberOfColumns; column++){
				if (column>1) json.append(',');					
				json.append(quote(metaData.getColumnName(column)));
				json.append(':');
				json.append(quote(resultSet.getString(column)));
			}

			json.append('}');

			row++;
		}

		json.append("],\"total_count\":");
		json.append(row);
		json.append("}");

		return json.toString();

	}

	/**
	 * Produce a string in double quotes with backslash sequences in all the
	 * right places. A backslash will be inserted within </, allowing JSON
	 * text to be delivered in HTML. In JSON text, a string cannot contain a
	 * control character or an unescaped quote or backslash.
	 * @param string A String
	 * @return  A String correctly formatted for insertion in a JSON text.
	 */
	protected static String quote(String string) {
		if (string == null || string.length() == 0) {
			return "\"\"";
		}

		char         b;
		char         c = 0;
		int          i;
		int          len = string.length();
		StringBuffer sb = new StringBuffer(len + 4);
		String       t;

		sb.append('"');
		for (i = 0; i < len; i += 1) {
			b = c;
			c = string.charAt(i);
			switch (c) {
			case '\\':
			case '"':
				sb.append('\\');
				sb.append(c);
				break;
			case '/':
				if (b == '<') {
					sb.append('\\');
				}
				sb.append(c);
				break;
			case '\b':
				sb.append("\\b");
				break;
			case '\t':
				sb.append("\\t");
				break;
			case '\n':
				sb.append("\\n");
				break;
			case '\f':
				sb.append("\\f");
				break;
			case '\r':
				sb.append("\\r");
				break;
			default:
				if (c < ' ' || (c >= '\u0080' && c < '\u00a0') ||
						(c >= '\u2000' && c < '\u2100')) {
					t = "000" + Integer.toHexString(c);
					sb.append("\\u" + t.substring(t.length() - 4));
				} else {
					sb.append(c);
				}
			}
		}
		sb.append('"');
		return sb.toString();
	}


	protected void zipFolder (File [] files) throws Exception {
		// Create a buffer for reading the files
		byte[] buf = new byte[1024];


		// Create the ZIP file
		DateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmssZ");
		String outFilename = backupFolder+File.separator+dateFormat.format(new Date().getTime())+".zip";
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outFilename));

		// Compress the files
		for (int i=0; i<files.length; i++) {
			FileInputStream in = new FileInputStream(files[i]);

			// Add ZIP entry to output stream.
			out.putNextEntry(new ZipEntry(files[i].getAbsolutePath()));

			// Transfer bytes from the file to the ZIP file
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}

			// Complete the entry
			out.closeEntry();
			in.close();
		}

		// Complete the ZIP file
		out.close();
		logger.debug("DB Backup created @ "+outFilename);
	}
	
	abstract protected void connectToMySql() throws Exception;
	
	
	/**
	 * 
	 * Load the HSQL Database Engine JDBC driver
	 * hsqldb.jar should be in the class path or made part of 
	 * the current jar
	 *
	 * @throws Exception
	 */
	protected void connectToEmbeddedDB() throws Exception{
		/* 
		 * Load the HSQL Database Engine JDBC driver
		 * hsqldb.jar should be in the class path or made part of 
		 * the current jar
		 */
		Class.forName("org.hsqldb.jdbcDriver");
		/* 
		 * Connect to the database. This will load the db files and start the
		 * database if it is not already running.
		 */
		connection = DriverManager.getConnection("jdbc:hsqldb:" + dbFileName, username, password);
		connection.setAutoCommit(true);
	}
	
	protected void reconnectToDB() throws Exception {
		if (ServiceContext.getContext().getUseEmbeddedDB())
			connectToEmbeddedDB();	
		else connectToMySql();
	}
}

