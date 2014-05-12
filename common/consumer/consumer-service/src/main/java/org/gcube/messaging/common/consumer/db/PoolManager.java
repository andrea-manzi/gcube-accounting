package org.gcube.messaging.common.consumer.db;

import java.sql.Connection;
import java.sql.DriverManager;

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDriver;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.commons.pool.impl.StackKeyedObjectPoolFactory;
import org.gcube.messaging.common.consumer.ServiceContext;

public  class PoolManager {
	
	private GenericObjectPool internalDBconnectionPool; 
	private ConnectionFactory internalDBconnectionFactory;
	private PoolableConnectionFactory internalDBpoolableConnectionFactory;
	private PoolingDriver internalDBdriver;
	private String validationQUERY="Select 1";
	private  String internalDBconnectionString=null; 
	private String dbName;


	public PoolManager (String dbName) {
	
		this.dbName = dbName;
		
		try {
			internalDBconnectionString=ServiceContext.getContext().getDbhost()+
										":"+ServiceContext.getContext().getDbport()+
										"/"+dbName;
			

			Class.forName("com.mysql.jdbc.Driver");
			internalDBconnectionString="jdbc:mysql://"+internalDBconnectionString;
		

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		internalDBconnectionPool = new GenericObjectPool(null);
		internalDBconnectionPool.setMaxActive(ServiceContext.getContext().getMaxDBConnections().intValue());
		internalDBconnectionPool.setTestOnBorrow(true);
		internalDBconnectionPool.setWhenExhaustedAction(GenericObjectPool.WHEN_EXHAUSTED_BLOCK);
		internalDBconnectionFactory = new DriverManagerConnectionFactory(internalDBconnectionString,  ServiceContext.getContext().getDbuser(), 
		ServiceContext.getContext().getDbpass());

		internalDBpoolableConnectionFactory = new PoolableConnectionFactory(internalDBconnectionFactory,internalDBconnectionPool,
				new StackKeyedObjectPoolFactory(),validationQUERY,false,true);
		internalDBdriver = new PoolingDriver();
		internalDBdriver.registerPool(dbName,internalDBconnectionPool);

		
	}


	public  Connection getInternalDBConnection()throws Exception{
		return DriverManager.getConnection("jdbc:apache:commons:dbcp:"+dbName);
	}
	


	public  String getInternalConnectionString(){return internalDBconnectionString;}

}
