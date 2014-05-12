package org.gcube.messaging.accounting.portalaccountingportlet.client;


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
				new ColumnConfig("user", "user", 150, true, null, "user"),
				new ColumnConfig("type", "type", 100, true, null, "type"),
				new ColumnConfig("vre", "vre", 300,true, null, "vre"),
				new ColumnConfig("date", "date", 75,true, null,"date"),
				new ColumnConfig("time", "time", 75,true, null, "time")}
		);
		
		return basecol;
	}

	public static ColumnModel CollectionsModel (){
		ColumnModel col= new ColumnModel(new BaseColumnConfig[]{
				new ColumnConfig("id", "id", 100, true, null, "id"),
				new ColumnConfig("name", "name", 200,true, null, "name")
				}
		);
		return col;
	}
	
	public static ColumnModel TermsModel (){
		ColumnModel col= new ColumnModel(new BaseColumnConfig[]{
				new ColumnConfig("name", "name", 100, true, null, "name"),
				new ColumnConfig("value", "value", 200,true, null, "value")
				}
		);
		return col;
	}
	
	public static ColumnModel GCUBEUsersModel (){
		ColumnModel col= new ColumnModel(new BaseColumnConfig[]{
				new ColumnConfig("name", "name", 100, true, null, "name"),
				new ColumnConfig("vre", "vre", 200,true, null, "vre")
				}
		);
		return col;
	}
	
	public static ColumnModel GHNDetailModel (){
		ColumnModel col= new ColumnModel(new BaseColumnConfig[]{
				new ColumnConfig("ghnid", "ghnid", 100, true, null, "ghnid"),
				new ColumnConfig("ghnnme", "ghnnme", 200,true, null, "ghnnme")
				}
		);
		return col;
	}
	
	public static ColumnModel WebAppDetailModel (){
		ColumnModel col= new ColumnModel(new BaseColumnConfig[]{
				new ColumnConfig("webappid", "webappid", 100, true, null, "webappid"),
				new ColumnConfig("webappname", "webappname", 200,true, null, "webappname"),
				new ColumnConfig("webappversion", "webappversion", 200,true, null, "webappversion")
				}
		);
		return col;
	}
	
	public static ColumnModel GetStatisticModel(String group){
		ColumnModel col= new ColumnModel(new BaseColumnConfig[]{
					new ColumnConfig(group, group, 300, true, null, group),
					new ColumnConfig("value", "CNT", 100,true, null, "value")
					
		}
			);
		return col;
	}
	
}
