package org.gcube.messaging.accounting.portalaccountingportlet.client.obj;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.kfuntak.gwt.json.serialization.client.JsonSerializable;
import com.kfuntak.gwt.json.serialization.client.Serializer;

public class StatisticsData implements JsonSerializable{
	public List<Statistics> data=new ArrayList<Statistics>();
	public String total_count;
	
	public static Serializer createSerializer(){
		   return GWT.create(Serializer.class);
	}

	public List<Statistics> getData() {
		return data;
	}

	public void setData(List<Statistics> data) {
		this.data = data;
	}

	public String getTotal_count() {
		return total_count;
	}

	public void setTotal_count(String total_count) {
		this.total_count = total_count;
	}
	
}