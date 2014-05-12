package org.gcube.messaging.common.messages.records;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public class HLRecord extends BaseRecord{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String ID;
	private String Name;
	private String type;
	private HLSubType HLsubType;
	
	private ArrayList<GCUBEUser> addresseesUsers= new ArrayList<GCUBEUser>();
	
	
	public enum HLSubType {
		HL_FOLDER_ITEM_CREATED("HL_FOLDER_ITEM_CREATED"),
		HL_FOLDER_ITEM_REMOVED("HL_FOLDER_ITEM_REMOVED"),
		HL_FOLDER_ITEM_IMPORTED("HL_FOLDER_ITEM_IMPORTED"),
		HL_ITEM_SENT("HL_ITEM_SENT"),
		HL_WORKSPACE_CREATED("HL_WORKSPACE_CREATED");
		String type;
		HLSubType(String type) {this.type = type;}
		public String toString() {return this.type;}
	};
	
	/**
	 * the ID
	 * @return the ID
	 */
	public String getID() {
		return ID;
	}


	/**
	 *  the ID
	 * @param iD  the ID
	 */
	public void setID(String iD) {
		ID = iD;
	}

	/**
	 * the name 
	 * @return
	 */
	public String getName() {
		return Name;
	}

	/**
	 * the name 
	 * @return the name
	 */
	public void setName(String name) {
		Name = name;
	}


	/**
	 * the type
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * the type
	 * @return the type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * the HLSubType
	 * @return HLSubType
	 */
	public HLSubType getHLsubType() {
		return HLsubType;
	}
	/**
	 * HLSubType
	 * @param hLsubType
	 */
	public void setHLsubType(HLSubType hLsubType) {
		HLsubType = hLsubType;
	}

	/**
	 * addresseesUsers
	 * @return ArrayList<GCUBEUser>
	 */
	public ArrayList<GCUBEUser> getAddresseesUsers() {
		return addresseesUsers;
	}

	/**
	 * addresseesUsers
	 * @param addresseesUsers
	 */
	public void setAddresseesUsers(ArrayList<GCUBEUser> addresseesUsers) {
		this.addresseesUsers = addresseesUsers;
	}


	public class GCUBEUser implements Serializable
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String user;
		private String vre;
		
		public GCUBEUser(){}
		
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
		
		
	}

}

