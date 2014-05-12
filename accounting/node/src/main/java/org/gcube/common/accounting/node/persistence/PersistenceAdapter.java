package org.gcube.common.accounting.node.persistence;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


import org.gcube.common.accounting.node.util.Util;
import org.gcube.common.core.contexts.GCUBEServiceContext;
import org.gcube.common.core.contexts.GHNContext;
import org.gcube.common.core.scope.GCUBEScope;
import org.gcube.common.core.utils.logging.GCUBELog;


/**
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public class PersistenceAdapter {
	
	private GCUBELog logger = null;
	
	private HashMap<String,HashMap<String,RIAccountingData>> map = null;
	
	private static String persistenceFolder = GHNContext.getContext().getStorageRoot()+File.separator+"Accounting";
	
	private static File accountingDataFileName = new File(persistenceFolder+File.separator+"Accounting.store");
	
	private Calendar lastUpdate;
	
	private Long interval;
	
	private boolean recreateState = false;
	

	public PersistenceAdapter(GCUBELog logger, Long interval){
		//check if the persistenceFolder exists
		this.interval= interval;
		if (!(new File(persistenceFolder).exists())) {
			logger.debug("Creating persistence folder");
			new File(persistenceFolder).mkdirs();
		} 
		this.logger= logger;
	}

	
	public Calendar getLastUpdate() {
		return lastUpdate;
	}


	public void setLastUpdate(Calendar lastUpdate) {
		this.lastUpdate = lastUpdate;
	}


	public void store() throws Exception{
	
		File tmpFile = null;
		ObjectOutputStream oos = null;
		
		try{
			//safe writing using renameTo which is atomic (should be on most platforms)..
			tmpFile = File.createTempFile("persistence",".tmp",new File(persistenceFolder));
			FileOutputStream fos = new FileOutputStream(tmpFile);
			oos = new ObjectOutputStream(fos);
			//TO DO store 
			oos.writeObject(map);
			oos.writeObject(lastUpdate);
			oos.writeObject(interval);
			oos.flush();
			fos.getFD().sync();//wait until all is written out onto FS
			
			if (!tmpFile.renameTo(accountingDataFileName)) //waiting for first OS in which rename does not overwrites to trigger an otherwise dangereous prior delete
				throw new Exception("Could not rename temporary serialisation file for "+accountingDataFileName);
			
		} 
		catch (Exception e) {
			if (tmpFile!=null) {
				tmpFile.delete();
			}
			throw e;
		}
		finally {
			try {if (oos!=null) oos.close();}catch(Exception e){logger.warn("Could not close stream on file serialisation");}	
		}
	}
	
	
	public void load() throws Exception {
		
		if (accountingDataFileName.exists())
		{
				//rebuild resource form file
			FileInputStream fis = new FileInputStream(accountingDataFileName);
			ObjectInputStream ois=null;
			try {
				ois=new ObjectInputStream(fis);
				map  =  (HashMap<String,HashMap<String,RIAccountingData>>) ois.readObject();
				lastUpdate = (Calendar) ois.readObject();
				Long pastInterval = (Long) ois.readObject();
				if (pastInterval != null && pastInterval.longValue() != interval.longValue()){recreateState = true;}
				if(recreateState)
					logger.warn("Interval changed. recreating state");
			}
			finally{
				try{if (ois!=null) ois.close();}catch(Exception e){logger.warn("Could not close stream on file deserialisation");}
			} 
		}else recreateState = true;
		
		if (recreateState){
			map= new HashMap<String,HashMap<String,RIAccountingData>>();
			
			//get Scopes for all RI registered
			for (GCUBEServiceContext context :GHNContext.getContext().getServiceContexts()){
				HashMap<String,RIAccountingData> mapServices = null;
				String key = new MapKey(context.getServiceClass()  ,context.getName()).getKey();
				if ((mapServices = map.get(key)) == null)
					mapServices = new HashMap<String,RIAccountingData>();
				for( GCUBEScope scope:context.getInstance().getScopes().values()){
		
					RIAccountingData data = new RIAccountingData(this.getInterval());
					data.setServiceClass(context.getServiceClass());
					data.setServiceName(context.getName());
					mapServices.put(scope.toString(),data);
				}
				map.put(key,mapServices);
			}
			
		}else {//Synchronize state
			//checking new deployed services
			for (GCUBEServiceContext context :GHNContext.getContext().getServiceContexts()){
				String key = new MapKey(context.getServiceClass()  ,context.getName()).getKey();
				if (map.get(key) ==null) {
					HashMap<String,RIAccountingData> mapServices = new HashMap<String,RIAccountingData>();
					for( GCUBEScope scope:context.getInstance().getScopes().values()){
						RIAccountingData data = new RIAccountingData(this.getInterval());
						data.setServiceClass(context.getServiceClass());
						data.setServiceName(context.getName());
						mapServices.put(scope.toString(),data);
					}
					map.put(key,mapServices);
				}
			}
			
			ArrayList<String> servicesToRemove = new ArrayList<String>();
			//checking undeployed services
			for (String service : map.keySet()){
				String clazz = Util.getClazz(service);
				String name = Util.getName(service);
				try {
					GHNContext.getContext().getServiceContext(clazz, name);
				}catch (Exception e){
					//service undeployed, remove info
					servicesToRemove.add(service);
					
				}
			}
			for (String service : servicesToRemove)
				map.remove(service);
		}
			 
	}	
	
	public Long getInterval() {
		return interval;
	}


	public void setInterval(Long interval) {
		this.interval = interval;
	}

	
	public HashMap<String, HashMap<String,RIAccountingData>> getMap() {
		return map;
	}


	public void setMap(HashMap<String, HashMap<String,RIAccountingData>> map) {
		this.map = map;
	}
	
	public class MapKey<CLASS ,NAME> implements Serializable{
		private CLASS clazz;
		private NAME name;

		public MapKey(CLASS clazz ,NAME name){
			this.clazz = clazz;
			this.name= name;

		}
		public String getKey() {
			return clazz+"_"+name;		
			
		}
		
		
	}

	public boolean alreadyUpdated() {
		if (this.getLastUpdate() == null) return false;
		else if ((Calendar.getInstance().getTimeInMillis()/1000- this.getInterval()) > this.getLastUpdate().getTimeInMillis()/1000) return false;
		else return true;
	}



}

