package org.gcube.common.accounting.node.probes;


import java.io.StringWriter;
import java.util.HashMap;

import org.gcube.common.accounting.node.persistence.RIAccountingData;
import org.gcube.common.core.contexts.GCUBEServiceContext;
import org.gcube.common.core.contexts.GCUBEServiceContext.Status;
import org.gcube.common.core.contexts.GHNContext;
import org.gcube.common.core.resources.runninginstance.ScopedAccounting;
import org.gcube.common.core.scope.GCUBEScope;
import org.gcube.common.core.utils.handlers.GCUBEHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public class RIProfileUpdater extends GCUBEHandler<GCUBEServiceContext> {

	/** Logger */
	private static Logger logger = LoggerFactory.getLogger(RIProfileUpdater.class);


	private HashMap<String, RIAccountingData> data= null;

	public RIProfileUpdater (HashMap<String, RIAccountingData> data) throws Exception{
		this.data = data;
		RIAccountingData tempData = data.values().iterator().next();
		logger.debug("Publishing RI profile for service "+tempData.getServiceClass()+"_"+tempData.getServiceName());
		this.setHandled(GHNContext.getContext().getServiceContext(tempData.getServiceClass(),tempData.getServiceName()));
	}

	@Override
	public void run() throws Exception {
		updateAccoutingInfoOnProfile();

	}

	private void updateAccoutingInfoOnProfile() throws Exception{

		//fill the info 
		//retrieving RIAccountingData info
		HashMap<GCUBEScope, ScopedAccounting> map  = new HashMap<GCUBEScope, ScopedAccounting>();
		
		for (String scope : data.keySet()) {
			try {
				//setting TOTALInCalls
				
				ScopedAccounting accounting = null;
				if ((accounting = map.get(GCUBEScope.getScope(scope)))== null){
					accounting = new ScopedAccounting();
					accounting.setScope(GCUBEScope.getScope(scope));
				}
				
				accounting.setTotalINCalls(data.get(scope).getTotalCalls());
				
				//Setting averageCalls
				accounting.getAverageCallsMap().clear();
				for ( Long key :data.get(scope).getAvgCallsNumber().getIntervalMapping().keySet()) 
					accounting.getAverageCallsMap().put(key, data.get(scope).getAvgCallsNumber().getStatistics(
						data.get(scope).getAvgCallsNumber().getIntervalMapping().get(key)));
				//Setting averageInvovationTime
				accounting.getAverageTimeMap().clear();
				for ( Long key :data.get(scope).getAvgInvocationTime().getIntervalMapping().keySet()) 
					accounting.getAverageTimeMap().put(key, data.get(scope).getAvgInvocationTime().getStatistics(
							data.get(scope).getAvgInvocationTime().getIntervalMapping().get(key)));
				
	
				//Setting topCallerHost
				String topCallerGHN = data.get(scope).getTopCallerData().getTopCallerInfo().getCallerHost();
				accounting.setTopCallerGHN(topCallerGHN);
				accounting.setTopCallerGHNtotalCalls(data.get(scope).getTopCallerData().getTopCallerInfo().getTotalCalls());
				
				accounting.setTopCallerGHNavgDailyCalls(
						(data.get(scope).getTopCallerData().getCallerMap().get(topCallerGHN).get(data.get(scope).getTopCallerData().getIntervalXday()).getAvg()));
				accounting.setTopCallerGHNavgHourlyCalls(
						(data.get(scope).getTopCallerData().getCallerMap().get(topCallerGHN).get(data.get(scope).getTopCallerData().getIntervalXHour()).getAvg()));
				
				map.put(GCUBEScope.getScope(scope), accounting);
				
			}catch (Exception e)
				{
					logger.error("Error updating RI Profile",e);
					throw e;
				}
		}
		try {
			this.getHandled().getInstance().setAccounting(map);
			StringWriter writer = new StringWriter();
			this.getHandled().getInstance().store(writer);
			logger.debug(writer.toString());
		} catch (Exception e){
			logger.error("Error updating Map on Profile",e);
			throw e;
		}
	
		this.getHandled().setStatus(Status.UPDATED);
	}

}



