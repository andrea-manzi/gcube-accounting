package org.gcube.messaging.accounting.nodeaccountingportlet.client;


import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;

/**
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public class ColumnDefinition {
  
	public static ColumnModel ColumnModel (){
		ColumnModel basecol= new ColumnModel(new BaseColumnConfig[]{
				new ColumnConfig("GHN", "GHNName", 150, true, null, "GHNName"),
				new ColumnConfig("ServiceClass", "ServiceClass", 100, true, null, "ServiceClass"),
				new ColumnConfig("ServiceName", "ServiceName", 100,true, null, "ServiceName"),
				new ColumnConfig("CallerScope", "callerScope", 200,true, null, "callerScope"),
				new ColumnConfig("StartTime", "startDate", 75,true, null,"startDate"),
				new ColumnConfig("EndTime", "endDate", 75,true, null,"endDate"),
				new ColumnConfig("Inv No", "invocationNumber", 75,true, null, "invocationNumber"),
				new ColumnConfig("Avg Inv Time", "averageInvocationTime", 80,true, null, "averageInvocationTime"),
				new ColumnConfig("Caller", "callerIP", 75,true, null, "callerIP"),
		}
		);
		
		return basecol;
	}

	
	public static ColumnModel GetStatisticModel(String group){
		ColumnModel col= new ColumnModel(new BaseColumnConfig[]{
					(group.compareTo("Date")==0)?new ColumnConfig(group, "DATE(startDate)", 300, true, null, group):
						new ColumnConfig(group, group, 300, true, null, group),
					new ColumnConfig("Avg. Inv. Time", "AVERAGE", 150,true, null, "Avg. Inv. Time"),
					new ColumnConfig("Number Of Invocations", "SUMINVOCATION", 150,true, null, "Number Of Invocations")		
		}
			);
		return col;
	}
	
}
