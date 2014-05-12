package org.gcube.messaging.common.messages.records;

/**
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public class TSRecord extends BaseRecord{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String title;
	private TSSubType TSsubType;


	public enum TSSubType {
		TS_CSV_IMPORTED("TS_CSV_IMPORTED"),
		TS_CURATION_STARTED("TS_CURATION_STARTED"),
		TS_CURATION_CLOSED("TS_CURATION_CLOSED"),
		TS_TIMESERIES_SAVED("TS_TIMESERIES_SAVED"),
		TS_TIMESERIES_PUBLISHED("TS_TIMESERIES_PUBLISHED");
		String type;
		TSSubType(String type) {this.type = type;}
		public String toString() {return this.type;}
	};
	
	/**
	 * Return the title
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}


	/**
	 * 	set the title
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * get the TS subtype
	 * @return the subtype
	 */
	public TSSubType getTSsubType() {
		return TSsubType;
	}


	/**
	 * set TS Subtype
	 * @param TSsubType Subtype
	 */
	public void setTSsubType(TSSubType tSsubType) {
		TSsubType = tSsubType;
	}

}
