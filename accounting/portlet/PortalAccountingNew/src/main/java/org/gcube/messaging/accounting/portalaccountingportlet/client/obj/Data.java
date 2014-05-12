package org.gcube.messaging.accounting.portalaccountingportlet.client.obj;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.kfuntak.gwt.json.serialization.client.JsonSerializable;
import com.kfuntak.gwt.json.serialization.client.Serializer;

public class Data implements JsonSerializable{
	public List<DataObj> data=new ArrayList<DataObj>();
	public String total_count;
	
	public static Serializer createSerializer(){
		   return GWT.create(Serializer.class);
	}

	public List<DataObj> getData() {
		return data;
	}

	public void setData(List<DataObj> data) {
		this.data = data;
	}

	public String getTotal_count() {
		return total_count;
	}

	public void setTotal_count(String total_count) {
		this.total_count = total_count;
	}
	
}