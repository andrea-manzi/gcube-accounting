package org.gcube.messaging.accounting.portalaccountingportlet.client.obj;

import java.io.Serializable;

import com.google.gwt.core.client.GWT;
import com.kfuntak.gwt.json.serialization.client.JsonSerializable;
import com.kfuntak.gwt.json.serialization.client.Serializer;

public class DataObj implements JsonSerializable, Serializable{
	private static final long serialVersionUID = 1L;
	
	public String Id;    	 //loginRecord
	public String user;      //loginRecord
	public String vre;       //loginRecord
	public String date;      //loginRecord
	public String time;      //loginRecord
	public String type;      //loginRecord		
	
	public String NAME;		  //webappRecord
	public String ACTION;     //webappRecord
	public String SUBTYPE;    //webappRecord
	
	public String WARID;		//warRecord
	public String WARNAME;		//warRecord
	public String WEBAPPNAME;	//warRecord
	public String WEBAPPVERSION;//warRecord
	public String CATEGORY;		//warRecord
	
	public String TEMPLATEID;	//reportRecord
	public String TEMPLATENAME; //reportRecord
	public String AUTHOR;		//reportRecord
	public String MIMETYPE;		//reportRecord

	public String WORKFLOWID;	//documentWorkflowRecord
	public String REPORTNAME;	//documentWorkflowRecord
	public String STATUS;		//documentWorkflowRecord
	public String STEPNUMBER;	//documentWorkflowRecord
	
	public String browseBy;	  //browseRecord
 	public String isDistinct; //browseRecord
 	
 	public String operator;   //advancedSearchRecord
 	
 	public String termValue;  //simpleSearchRecord
 	 	
 	public String identifier; //contentRecord
 	public String name;		  //contentRecord
 	
 	public String IDENTIFIER; //aisRecord
 	public String TITLE; 	//tsRecord

 	public String AQUAMAPSTYPE;	  //aquamapsRecord
 	public String SPECIESCOUNT;	  //aquamapsRecord
 	public String GIS;			  //aquamapsRecord
 	public String HSPECID;		  //aquamapsRecord
 	public String OBJECTID;		  //aquamapsRecord
 	
 	public String FILENAME;	  //SMRECORD
 	public String FILETYPE;	  //SMRECORD
 	public String ALGORITHMNAME;			  //SMRECORD
 	public String EXECUTIONOUTCOME;		  //SMRECORD
 	public String EXECUTIONTIME;		  //SMRECORD
 	
	public String ghnid;	//ghnRecord
 	public String ghnname;	//ghnRecord
 	
 	public String webappid;		//webappDetailRecord
 	public String webappname;	//webappDetailRecord
 	
 	public String id;    //collectionsRecord
 	public String value;  //termRecord
 	
 	public String CNT;  //getStatisticsRecord
 	
 	public String getFILENAME() {
		return FILENAME;
	}

	public void setFILENAME(String fILENAME) {
		FILENAME = fILENAME;
	}

	public String getFILETYPE() {
		return FILETYPE;
	}

	public void setFILETYPE(String fILETYPE) {
		FILETYPE = fILETYPE;
	}

	public String getALGORITHMNAME() {
		return ALGORITHMNAME;
	}

	public void setALGORITHMNAME(String aLGORITHMNAME) {
		ALGORITHMNAME = aLGORITHMNAME;
	}

	public String getEXECUTIONOUTCOME() {
		return EXECUTIONOUTCOME;
	}

	public void setEXECUTIONOUTCOME(String eXECUTIONOUTCOME) {
		EXECUTIONOUTCOME = eXECUTIONOUTCOME;
	}

	public String getEXECUTIONTIME() {
		return EXECUTIONTIME;
	}

	public void setEXECUTIONTIME(String eXECUTIONTIME) {
		EXECUTIONTIME = eXECUTIONTIME;
	}



	public static Serializer createSerializer(){
		   return GWT.create(Serializer.class);
	}
	  
	public DataObj() {
	}

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getVre() {
		return vre;
	}
	public void setVre(String vre) {
		this.vre = vre;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public String getNAME() {
		return NAME;
	}

	public void setNAME(String nAME) {
		NAME = nAME;
	}

	public String getACTION() {
		return ACTION;
	}

	public void setACTION(String aCTION) {
		ACTION = aCTION;
	}

	public String getSUBTYPE() {
		return SUBTYPE;
	}

	public void setSUBTYPE(String sUBTYPE) {
		SUBTYPE = sUBTYPE;
	}

	public String getWARID() {
		return WARID;
	}

	public void setWARID(String wARID) {
		WARID = wARID;
	}

	public String getWARNAME() {
		return WARNAME;
	}

	public void setWARNAME(String wARNAME) {
		WARNAME = wARNAME;
	}

	public String getWEBAPPNAME() {
		return WEBAPPNAME;
	}

	public void setWEBAPPNAME(String wEBAPPNAME) {
		WEBAPPNAME = wEBAPPNAME;
	}

	public String getWEBAPPVERSION() {
		return WEBAPPVERSION;
	}

	public void setWEBAPPVERSION(String wEBAPPVERSION) {
		WEBAPPVERSION = wEBAPPVERSION;
	}

	public String getCATEGORY() {
		return CATEGORY;
	}

	public void setCATEGORY(String cATEGORY) {
		CATEGORY = cATEGORY;
	}

	public String getTEMPLATEID() {
		return TEMPLATEID;
	}

	public void setTEMPLATEID(String tEMPLATEID) {
		TEMPLATEID = tEMPLATEID;
	}

	public String getTEMPLATENAME() {
		return TEMPLATENAME;
	}

	public void setTEMPLATENAME(String tEMPLATENAME) {
		TEMPLATENAME = tEMPLATENAME;
	}

	public String getAUTHOR() {
		return AUTHOR;
	}

	public void setAUTHOR(String aUTHOR) {
		AUTHOR = aUTHOR;
	}

	public String getMIMETYPE() {
		return MIMETYPE;
	}

	public void setMIMETYPE(String mIMETYPE) {
		MIMETYPE = mIMETYPE;
	}

	public String getWORKFLOWID() {
		return WORKFLOWID;
	}

	public void setWORKFLOWID(String wORKFLOWID) {
		WORKFLOWID = wORKFLOWID;
	}

	public String getREPORTNAME() {
		return REPORTNAME;
	}

	public void setREPORTNAME(String rEPORTNAME) {
		REPORTNAME = rEPORTNAME;
	}

	public String getSTATUS() {
		return STATUS;
	}

	public void setSTATUS(String sTATUS) {
		STATUS = sTATUS;
	}

	public String getSTEPNUMBER() {
		return STEPNUMBER;
	}

	public void setSTEPNUMBER(String sTEPNUMBER) {
		STEPNUMBER = sTEPNUMBER;
	}

	public String getBrowseBy() {
		return browseBy;
	}

	public void setBrowseBy(String browseBy) {
		this.browseBy = browseBy;
	}

	public String getIsDistinct() {
		return isDistinct;
	}

	public void setIsDistinct(String isDistinct) {
		this.isDistinct = isDistinct;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getTermValue() {
		return termValue;
	}

	public void setTermValue(String termValue) {
		this.termValue = termValue;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIDENTIFIER() {
		return IDENTIFIER;
	}

	public void setIDENTIFIER(String iDENTIFIER) {
		IDENTIFIER = iDENTIFIER;
	}

	public String getTITLE() {
		return TITLE;
	}

	public void setTITLE(String tITLE) {
		TITLE = tITLE;
	}

	public String getAQUAMAPSTYPE() {
		return AQUAMAPSTYPE;
	}

	public void setAQUAMAPSTYPE(String aQUAMAPSTYPE) {
		AQUAMAPSTYPE = aQUAMAPSTYPE;
	}

	public String getSPECIESCOUNT() {
		return SPECIESCOUNT;
	}

	public void setSPECIESCOUNT(String sPECIESCOUNT) {
		SPECIESCOUNT = sPECIESCOUNT;
	}

	public String getGIS() {
		return GIS;
	}

	public void setGIS(String gIS) {
		GIS = gIS;
	}

	public String getHSPECID() {
		return HSPECID;
	}

	public void setHSPECID(String hSPECID) {
		HSPECID = hSPECID;
	}

	public String getOBJECTID() {
		return OBJECTID;
	}

	public void setOBJECTID(String oBJECTID) {
		OBJECTID = oBJECTID;
	}

	public String getGhnid() {
		return ghnid;
	}

	public void setGhnid(String ghnid) {
		this.ghnid = ghnid;
	}

	public String getGhnname() {
		return ghnname;
	}

	public void setGhnname(String ghnname) {
		this.ghnname = ghnname;
	}

	public String getWebappid() {
		return webappid;
	}

	public void setWebappid(String webappid) {
		this.webappid = webappid;
	}

	public String getWebappname() {
		return webappname;
	}

	public void setWebappname(String webappname) {
		this.webappname = webappname;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getCNT() {
		return CNT;
	}

	public void setCNT(String cNT) {
		CNT = cNT;
	}



	
}
