package org.gcube.messaging.accounting.nodeaccountingportlet.client;


/**
 * 
 * @author Andrea Manzi (CERN)
 *
 */
public class GlobalInfo {

	private String selectedServiceClass = "";
	private String selectedServiceName = "";
	private String selectedGHN = "";
	private String selectedScope = "";
	private String startdate = "";
	private String graphTypeTitle = "";
	
	private String enddate = "";
	
	private static GlobalInfo singleton= null;
	
	public static GlobalInfo get(){
		if (singleton == null)
			singleton = new GlobalInfo();
		return singleton;
	}
	
	public String[] getDates(){
		return new String[] { getStartdate(),getEnddate()};
	}
	
	public  String getGrouping() {
		return grouping;
	}
	public void setGrouping(String grouping) {
		this.grouping = grouping;
	}
	public  String getStartdate() {
		return this.startdate;
	}
	public  void setStartdate(String startdate) {
		this.startdate = startdate;
	}
	public  String getEnddate() {
		return this.enddate;
	}
	public  void setEnddate(String enddate) {
		this.enddate = enddate;
	}

	public  String getSelectedServiceClass() {
		return selectedServiceClass;
	}

	public  void setSelectedServiceClass(String selectedServiceClass) {
		this.selectedServiceClass = selectedServiceClass;
	}

	public String getSelectedServiceName() {
		return selectedServiceName;
	}

	public  void setSelectedServiceName(String selectedServiceName) {
		this.selectedServiceName = selectedServiceName;
	}

	public String getSelectedGHN() {
		return selectedGHN;
	}

	public  void setSelectedGHN(String selectedGHN) {
		this.selectedGHN = selectedGHN;
	}

	public String getSelectedScope() {
		return selectedScope;
	}

	public  void setSelectedScope(String selectedScope) {
		this.selectedScope = selectedScope;
	}
	
	public String getGraphIndex() {
		return graphIndex;
	}

	public void setGraphIndex(String graphIndex) {
		this.graphIndex = graphIndex;
	}
	private String grouping = "";
	private String user = "";
	
	private String graphIndex = "";
	
	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getGridTitle(){
		return "Showing Records "+((get().getSelectedServiceClass().compareTo("")==0)?"":" for service class "+get().getSelectedServiceClass())+
		((get().getSelectedServiceName().compareTo("")==0)?"":" for service name "+get().getSelectedServiceName())+
		((get().getSelectedGHN().compareTo("")==0)?"":" on the GHN "+get().getSelectedGHN())+
		((get().getSelectedScope().compareTo("")==0)?"":" for caller Scope  "+get().getSelectedScope())+
		((get().getStartdate()== "")?"":" from Date "+GlobalInfo.get().getStartdate())+
		((get().getEnddate()== "")?"":" to Date "+GlobalInfo.get().getEnddate());
	}
	
	public String getTitle(String type){
		return "Showing "+type+ ((get().getSelectedServiceClass().compareTo("")==0)?"":" for service class "+get().getSelectedServiceClass())+
		((get().getSelectedServiceName().compareTo("")==0)?"":" for service name "+get().getSelectedServiceName())+
		((get().getSelectedGHN().compareTo("")==0)?"":" on the GHN "+get().getSelectedGHN())+
		((get().getSelectedScope().compareTo("")==0)?"":" for caller Scope  "+get().getSelectedScope())+
		((get().getStartdate()== "")?"":" from Date "+GlobalInfo.get().getStartdate())+
		((get().getEnddate()== "")?"":" to Date "+GlobalInfo.get().getEnddate());
	}
	

	public String getStatisticTitle(String type){
		return getTitle(type)+
		((get().getGrouping().compareTo("")==0)?"":" grouped by  "+get().getGrouping());
	}
	
	public String getGraphTitle(String indexType){
		return getStatisticTitle(indexType);
	}
	
	public String getGraphTypeTitle() {
		return graphTypeTitle;
	}

	public void setGraphTypeTitle(String graphTypeTitle) {
		this.graphTypeTitle = graphTypeTitle;
	}
	public void reset(){
		get().setSelectedGHN("");
		get().setSelectedServiceClass("");
		get().setSelectedScope("");
		get().setSelectedServiceName("");
		get().setGraphIndex("");
		get().setGrouping("");
	}
	
	
	

}

