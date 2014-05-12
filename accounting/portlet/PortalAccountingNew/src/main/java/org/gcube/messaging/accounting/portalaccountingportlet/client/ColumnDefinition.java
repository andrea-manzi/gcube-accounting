package org.gcube.messaging.accounting.portalaccountingportlet.client;


import java.util.ArrayList;
import java.util.List;

import org.gcube.messaging.accounting.portalaccountingportlet.client.obj.DataObj;
import org.gcube.messaging.accounting.portalaccountingportlet.client.obj.DataObjProperties;
import org.gcube.messaging.accounting.portalaccountingportlet.client.obj.GroupObj;
import org.gcube.messaging.accounting.portalaccountingportlet.client.obj.GroupObjProperties;
import org.gcube.messaging.accounting.portalaccountingportlet.client.obj.RecordType;
import org.gcube.messaging.accounting.portalaccountingportlet.client.obj.RecordTypeProperties;
import org.gcube.messaging.accounting.portalaccountingportlet.client.obj.Statistics;
import org.gcube.messaging.accounting.portalaccountingportlet.client.obj.StatisticsProperties;
import org.gcube.messaging.accounting.portalaccountingportlet.client.obj.User;
import org.gcube.messaging.accounting.portalaccountingportlet.client.obj.UserProperties;
import org.gcube.messaging.accounting.portalaccountingportlet.client.obj.Vre;
import org.gcube.messaging.accounting.portalaccountingportlet.client.obj.VreProperties;

import com.google.gwt.core.client.GWT;
import com.sencha.gxt.widget.core.client.grid.CheckBoxSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;



public class ColumnDefinition {

	public static ColumnModel<DataObj> MainModel(){
		DataObjProperties props = GWT.create(DataObjProperties.class);

		ColumnConfig<DataObj, String> user = new ColumnConfig<DataObj, String>(props.user(), 150, "User");
		ColumnConfig<DataObj, String> vre = new ColumnConfig<DataObj, String>(props.vre(), 150, "Vre");
		ColumnConfig<DataObj, String> date = new ColumnConfig<DataObj, String>(props.date(), 150, "Date");
		ColumnConfig<DataObj, String> time = new ColumnConfig<DataObj, String>(props.time(), 150, "Time");
		ColumnConfig<DataObj, String> type = new ColumnConfig<DataObj, String>(props.type(), 150, "Type");

		List<ColumnConfig<DataObj, ?>> l = new ArrayList<ColumnConfig<DataObj, ?>>();
		l.add(user);
		l.add(vre);
		l.add(date);
		l.add(time);
		l.add(type);
		return new ColumnModel<DataObj>(l);
	}

	public static ColumnModel<DataObj> CollectionModel(){
		DataObjProperties props = GWT.create(DataObjProperties.class);

		ColumnConfig<DataObj, String> id = new ColumnConfig<DataObj, String>(props.id(), 300, "Id");
		ColumnConfig<DataObj, String> name = new ColumnConfig<DataObj, String>(props.name(), 300, "Name");

		List<ColumnConfig<DataObj, ?>> l = new ArrayList<ColumnConfig<DataObj, ?>>();
		l.add(id);
		l.add(name);
		return new ColumnModel<DataObj>(l);
	}
	public static ColumnModel<DataObj> GCUBEUsersModel(){
		DataObjProperties props = GWT.create(DataObjProperties.class);

		ColumnConfig<DataObj, String> vre = new ColumnConfig<DataObj, String>(props.vre(), 300, "Vre");
		ColumnConfig<DataObj, String> name = new ColumnConfig<DataObj, String>(props.name(), 300, "Name");

		List<ColumnConfig<DataObj, ?>> l = new ArrayList<ColumnConfig<DataObj, ?>>();
		l.add(name);
		l.add(vre);
		return new ColumnModel<DataObj>(l);
	}
	public static ColumnModel<DataObj> TermsModel2(){
		DataObjProperties props = GWT.create(DataObjProperties.class);

		ColumnConfig<DataObj, String> value = new ColumnConfig<DataObj, String>(props.value(), 300, "Value");
		ColumnConfig<DataObj, String> name = new ColumnConfig<DataObj, String>(props.name(), 300, "Name");

		List<ColumnConfig<DataObj, ?>> l = new ArrayList<ColumnConfig<DataObj, ?>>();
		l.add(name);
		l.add(value);
		return new ColumnModel<DataObj>(l);
	}
	public static ColumnModel<DataObj> TermsModel1(){
		DataObjProperties props = GWT.create(DataObjProperties.class);

		ColumnConfig<DataObj, String> value = new ColumnConfig<DataObj, String>(props.value(), 300, "Value");

		List<ColumnConfig<DataObj, ?>> l = new ArrayList<ColumnConfig<DataObj, ?>>();
		l.add(value);
		return new ColumnModel<DataObj>(l);
	}
	public static ColumnModel<DataObj> GHNDetailModel(){
		DataObjProperties props = GWT.create(DataObjProperties.class);

		ColumnConfig<DataObj, String> ghnid = new ColumnConfig<DataObj, String>(props.ghnid(), 300, "ghnid");
		ColumnConfig<DataObj, String> ghnname = new ColumnConfig<DataObj, String>(props.ghnname(), 300, "ghnname");

		List<ColumnConfig<DataObj, ?>> l = new ArrayList<ColumnConfig<DataObj, ?>>();
		l.add(ghnid);
		l.add(ghnname);
		return new ColumnModel<DataObj>(l);
	}

	public static ColumnModel<DataObj> WebAppDetailModel(){
		DataObjProperties props = GWT.create(DataObjProperties.class);

		ColumnConfig<DataObj, String> webappid = new ColumnConfig<DataObj, String>(props.webappid(), 300, "webappid");
		ColumnConfig<DataObj, String> webappname = new ColumnConfig<DataObj, String>(props.webappname(), 300, "webappname");

		List<ColumnConfig<DataObj, ?>> l = new ArrayList<ColumnConfig<DataObj, ?>>();
		l.add(webappid);
		l.add(webappname);
		return new ColumnModel<DataObj>(l);
	}
	
	public static ColumnModel<Statistics> StatisticsModel(List<String> groups,
			CheckBoxSelectionModel<Statistics> sm){
		StatisticsProperties props = GWT.create(StatisticsProperties.class);
		List<ColumnConfig<Statistics, ?>> l = new ArrayList<ColumnConfig<Statistics, ?>>();

		l.add(sm.getColumn());
		if(groups==null || groups.size()<1){ // default if sth went wrong and group list is null
			ColumnConfig<Statistics, String> groupUser = new ColumnConfig<Statistics, String>(props.user(), 150, "user");
			l.add(groupUser);
		}
		else{
			if(groups.contains("user")){
				ColumnConfig<Statistics, String> groupUser = new ColumnConfig<Statistics, String>(props.user(), 150, "user");
				l.add(groupUser);
			}
			if(groups.contains("vre")){
				ColumnConfig<Statistics, String> groupVre = new ColumnConfig<Statistics, String>(props.vre(), 150, "vre");
				l.add(groupVre);
			}
			if(groups.contains("type")){
				ColumnConfig<Statistics, String> groupType = new ColumnConfig<Statistics, String>(props.type(), 150, "type");
				l.add(groupType);
			}
			if(groups.contains("date")){
				ColumnConfig<Statistics, String> groupDate = new ColumnConfig<Statistics, String>(props.date(), 150, "date");
				l.add(groupDate);
			}
		}
		ColumnConfig<Statistics, String> valueCol = new ColumnConfig<Statistics, String>(props.CNT(), 150, "value");
		l.add(valueCol);

		return new ColumnModel<Statistics>(l);
	}
	public static ColumnModel<User> UserModel(CheckBoxSelectionModel<User> sm){
		UserProperties props = GWT.create(UserProperties.class);
		ColumnConfig<User, String> name = new ColumnConfig<User, String>(props.name(), 300, "User");
		List<ColumnConfig<User, ?>> l = new ArrayList<ColumnConfig<User, ?>>();
		l.add(sm.getColumn());
		l.add(name);
		return new ColumnModel<User>(l);
	}
	public static ColumnModel<RecordType> TypeModel(CheckBoxSelectionModel<RecordType> sm){
		RecordTypeProperties props = GWT.create(RecordTypeProperties.class);
		ColumnConfig<RecordType, String> name = new ColumnConfig<RecordType, String>(props.name(), 300, "Type");
		List<ColumnConfig<RecordType, ?>> l = new ArrayList<ColumnConfig<RecordType, ?>>();
		l.add(sm.getColumn());
		l.add(name);
		return new ColumnModel<RecordType>(l);
	}
	public static ColumnModel<Vre> VreModel(CheckBoxSelectionModel<Vre> sm){
		VreProperties props = GWT.create(VreProperties.class);
		ColumnConfig<Vre, String> name = new ColumnConfig<Vre, String>(props.name(), 300, "Scope");
		List<ColumnConfig<Vre, ?>> l = new ArrayList<ColumnConfig<Vre, ?>>();
		l.add(sm.getColumn());
		l.add(name);
		return new ColumnModel<Vre>(l);
	}
	public static ColumnModel<GroupObj> GroupObjModel(CheckBoxSelectionModel<GroupObj> sm){
		GroupObjProperties props = GWT.create(GroupObjProperties.class);
		ColumnConfig<GroupObj, String> name = new ColumnConfig<GroupObj, String>(props.name(), 180, "Group Type");
		List<ColumnConfig<GroupObj, ?>> l = new ArrayList<ColumnConfig<GroupObj, ?>>();
		l.add(sm.getColumn());
		l.add(name);
		return new ColumnModel<GroupObj>(l);
	}
}
