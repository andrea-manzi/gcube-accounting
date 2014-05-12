package org.gcube.messaging.common.messages.records;

/**
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public class AISRecord extends BaseRecord {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String ID;
	private String name;
	private AISSubType AISsubType;


	public enum AISSubType {
		SCRIPT_CREATED("SCRIPT_CREATED"),
		SCRIPT_REMOVED("SCRIPT_REMOVED"),
		SCRIPT_LAUNCHED ("SCRIPT_LAUNCHED");
		String type;
		AISSubType(String type) {this.type = type;}
		public String toString() {return this.type;}
	};
	
	/**
	 * get ID
	 * @return the ID
	 */
	public String getID() {
		return ID;
	}


	/**
	 * Set the ID
	 * @param iD
	 */
	public void setID(String iD) {
		ID = iD;
	}

	/**
	 * get the name
	 * @return the name
	 */
	public String getName() {
		return name;
	}


	/**
	 * Set the name
	 * @param name the name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * get the AIS SubType
	 * @return the AIS SubType
	 */
	public AISSubType getAISsubType() {
		return AISsubType;
	}

	/**
	 * the AISSubtype
	 * @param AISsubType AISSubtype
	 */
	public void setAISsubType(AISSubType aISsubType) {
		AISsubType = aISsubType;
	}

	
}
