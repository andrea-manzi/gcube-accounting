package org.gcube.messaging.common.messages.records;

public class AquamapsRecord  extends BaseRecord {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String title;
	private String type;
	private long speciesCount;

	private boolean gis;
	private String hspecId;
	private String objectID;
	
	public String getObjectID() {
		return objectID;
	}


	public void setObjectID(String objectID) {
		this.objectID = objectID;
	}


	public AquamapsSubType getAquamapsSubtype() {
		return aquamapsSubtype;
	}


	public void setAquamapsSubtype(AquamapsSubType aquamapsSubtype) {
		this.aquamapsSubtype = aquamapsSubtype;
	}



	private AquamapsSubType aquamapsSubtype;

	
	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public long getSpeciesCount() {
		return speciesCount;
	}


	public void setSpeciesCount(long speciesCount) {
		this.speciesCount = speciesCount;
	}


	public boolean isGis() {
		return gis;
	}


	public void setGis(boolean gis) {
		this.gis = gis;
	}


	public String getHspecId() {
		return hspecId;
	}


	public void setHspecId(String hspecId) {
		this.hspecId = hspecId;
	}



	/**
	 * 
	 * @author Andrea Manzi(CERN)
	 *
	 */
	public enum AquamapsSubType {
		AquamapsGeneration("AquamapsGeneration"),
		AquamapsSavedItem("AqumapsSavedItem");
		String type;
		AquamapsSubType(String type) {this.type = type;}
		public String toString() {return this.type;}
	};
	

}
