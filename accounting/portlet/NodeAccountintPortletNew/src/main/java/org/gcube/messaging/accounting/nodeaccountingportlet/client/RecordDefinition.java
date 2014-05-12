package org.gcube.messaging.accounting.nodeaccountingportlet.client;


import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.FloatFieldDef;
import com.gwtext.client.data.IntegerFieldDef;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.StringFieldDef;


/**
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public class RecordDefinition {

	

	public static RecordDef record=new RecordDef(
			new FieldDef[]{
					new StringFieldDef("GHNName"),
					new StringFieldDef("ServiceClass"),
					new StringFieldDef("ServiceName"),
					new StringFieldDef("callerScope"),
					new StringFieldDef("startDate"),
					new StringFieldDef("endDate"),
					new IntegerFieldDef("timeframe"),
					new IntegerFieldDef("invocationNumber"),
					new FloatFieldDef("averageInvocationTime"),
					new StringFieldDef("callerIP")
		});
	

	public static RecordDef getStatisticsRecord(String group){
		return new  RecordDef(
				new FieldDef[]{
						new FloatFieldDef("AVERAGE"),
						new IntegerFieldDef("SUMINVOCATION"),
						(group.compareTo("Date")==0)?new StringFieldDef("DATE(startDate)"):new StringFieldDef(group)

				});
	}
	
	public static RecordDef getScopeRecord(){
		return new  RecordDef(
				new FieldDef[]{
						new StringFieldDef("callerScope")

				});
	}


}
