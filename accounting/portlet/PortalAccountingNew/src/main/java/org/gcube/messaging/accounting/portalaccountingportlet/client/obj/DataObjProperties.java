package org.gcube.messaging.accounting.portalaccountingportlet.client.obj;

import com.google.gwt.editor.client.Editor.Path;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;




public interface DataObjProperties extends PropertyAccess<DataObj> {

	@Path("Id")
	ModelKeyProvider<DataObj> key();

	ValueProvider<DataObj, String> user();      //loginRecord
	ValueProvider<DataObj, String> vre();       //loginRecord
	ValueProvider<DataObj, String> date();      //loginRecord
	ValueProvider<DataObj, String> time();      //loginRecord
	ValueProvider<DataObj, String> type();      //loginRecord		
	
	ValueProvider<DataObj, String> NAME();		  //webappRecord
	ValueProvider<DataObj, String> ACTION();     //webappRecord
	ValueProvider<DataObj, String> SUBTYPE();    //webappRecord
	
	ValueProvider<DataObj, String> WARID();		//warRecord
	ValueProvider<DataObj, String> WARNAME();		//warRecord
	ValueProvider<DataObj, String> WEBAPPNAME();	//warRecord
	ValueProvider<DataObj, String> WEBAPPVERSION();//warRecord
	ValueProvider<DataObj, String> CATEGORY();		//warRecord
	
	ValueProvider<DataObj, String> TEMPLATEID();	//reportRecord
	ValueProvider<DataObj, String> TEMPLATENAME(); //reportRecord
	ValueProvider<DataObj, String> AUTHOR();		//reportRecord
	ValueProvider<DataObj, String> MIMETYPE();		//reportRecord

	ValueProvider<DataObj, String> WORKFLOWID();	//documentWorkflowRecord
	ValueProvider<DataObj, String> REPORTNAME();	//documentWorkflowRecord
	ValueProvider<DataObj, String> STATUS();		//documentWorkflowRecord
	ValueProvider<DataObj, String> STEPNUMBER();	//documentWorkflowRecord
	
	ValueProvider<DataObj, String> browseBy();	  //browseRecord
 	ValueProvider<DataObj, String> isDistinct(); //browseRecord
 	
 	ValueProvider<DataObj, String> operator();   //advancedSearchRecord
 	
 	ValueProvider<DataObj, String> termValue();  //simpleSearchRecord
 	
 	ValueProvider<DataObj, String> identifier(); //contentRecord
 	ValueProvider<DataObj, String> name();		  //contentRecord
 	
 	ValueProvider<DataObj, String> IDENTIFIER(); //aisRecord
 	ValueProvider<DataObj, String> TITLE(); 	//tsRecord

 	ValueProvider<DataObj, String> AQUAMAPSTYPE();	  //aquamapsRecord
 	ValueProvider<DataObj, String> SPECIESCOUNT();	  //aquamapsRecord
 	ValueProvider<DataObj, String> GIS();			  //aquamapsRecord
 	ValueProvider<DataObj, String> HSPECID();		  //aquamapsRecord
 	ValueProvider<DataObj, String> OBJECTID();		  //aquamapsRecord
 	
	ValueProvider<DataObj, String> FILENAME();	  //smRecord
 	ValueProvider<DataObj, String> FILETYPE();	  //smRecord
 	ValueProvider<DataObj, String> ALGORITHMNAME();			  //smRecord
 	ValueProvider<DataObj, String> EXECUTIONOUTCOME();		  //smRecord
 	ValueProvider<DataObj, String> EXECUTIONTIME();		  //smRecord
 	
 	
 	ValueProvider<DataObj, String> ghnid();	//ghnRecord
 	ValueProvider<DataObj, String> ghnname();	//ghnRecord
 	
 	ValueProvider<DataObj, String> webappid();		//webappDetailRecord
 	ValueProvider<DataObj, String> webappname();	//webappDetailRecord
 	
 	ValueProvider<DataObj, String> id();    //collectionsRecord
 	ValueProvider<DataObj, String> value();  //termRecord
 	
 	ValueProvider<DataObj, String> CNT();  //getStatisticsRecord
}