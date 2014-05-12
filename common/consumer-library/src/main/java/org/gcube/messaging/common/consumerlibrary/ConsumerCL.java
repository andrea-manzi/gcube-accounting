package org.gcube.messaging.common.consumerlibrary;

import org.gcube.common.clients.Call;
import org.gcube.common.clients.delegates.ProxyDelegate;
import org.gcube.common.clients.exceptions.ServiceException;
import org.gcube.common.clients.stubs.jaxws.JAXWSUtils.Empty;
import org.gcube.messaging.common.consumerlibrary.fws.MessagingServiceJAXWSStubs;
import org.gcube.messaging.common.consumerlibrary.query.Query;

/**
 * 
 * @author andrea
 *
 */
public class ConsumerCL  {
	
	private final ProxyDelegate<MessagingServiceJAXWSStubs> delegate;

	public ConsumerCL(ProxyDelegate<MessagingServiceJAXWSStubs> config) {
		this.delegate=config;
	}

	public void backupMonitoringDB() throws Exception {
		Call<MessagingServiceJAXWSStubs,Empty> call = new Call<MessagingServiceJAXWSStubs,Empty>() {
	
			public Empty call(MessagingServiceJAXWSStubs endpoint) throws Exception {
				return endpoint.backupMonitoringDB(new Empty());
			
			}
		};
		try {
			delegate.make(call);
		}

		catch(Exception e) {
			throw new ServiceException (e);
		}
	}

	public void backupAccountingDB() throws Exception {
		Call<MessagingServiceJAXWSStubs,Empty> call = new Call<MessagingServiceJAXWSStubs,Empty>() {
			
			public Empty call(MessagingServiceJAXWSStubs endpoint) throws Exception {
				return endpoint.backupAccountingDB(new Empty());
			
			}
		};
		try {
			delegate.make(call);
		}

		catch(Exception e) {
			throw new ServiceException (e);
		}
		
	}
	
	

	public String queryMonitoringDB(final String query) throws Exception {
		Call<MessagingServiceJAXWSStubs,String> call = new Call<MessagingServiceJAXWSStubs,String>() {
			
			public String call(MessagingServiceJAXWSStubs endpoint) throws Exception {
				return endpoint.queryMonitoringDB(query);
			
			}
		};
		try {
			return delegate.make(call);
		}

		catch(Exception e) {
			throw new ServiceException (e);
		}
	}

	public String queryAccountingDB(final String query) throws Exception {
	
		Call<MessagingServiceJAXWSStubs,String> call = new Call<MessagingServiceJAXWSStubs,String>() {
			
			public String call(MessagingServiceJAXWSStubs endpoint) throws Exception {
				return endpoint.queryAccountingDB(query);
			
			}
		};
		try {
			return delegate.make(call);
		}

		catch(Exception e) {
			throw new ServiceException (e);
		}
	}

	public String querySystemAccountingDB(final String query) throws Exception {
			Call<MessagingServiceJAXWSStubs,String> call = new Call<MessagingServiceJAXWSStubs,String>() {
			
			public String call(MessagingServiceJAXWSStubs endpoint) throws Exception {
				return endpoint.querySystemAccountingDB(query);
			
			}
		};
		try {
			return delegate.make(call);
		}

		catch(Exception e) {
			throw new ServiceException (e);
		}
	
	}

	
	/**
	 * Returns a query from the interface or class which defines its type.
	 * The method is intended as a means to bind query interfaces to concrete implementations provided
	 * along with implementations of this interface.
	 * @param <QUERY> the type of the query which produces results 
	 * @param type the query interface or class.
	 * @return the query.
	 * @throws IllegalAccessException  IllegalAccessException
	 * @throws InstantiationException  IllegalAccessException
	 */
	public <QUERY extends Query<ConsumerCL>> QUERY getQuery(Class<QUERY> type,ConsumerCL call) throws InstantiationException, IllegalAccessException {
		QUERY query= type.newInstance();
		query.setCall(call);
		return query;
	}


}
