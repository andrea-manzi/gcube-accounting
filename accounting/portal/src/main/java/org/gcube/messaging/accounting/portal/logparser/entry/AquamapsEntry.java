package org.gcube.messaging.accounting.portal.logparser.entry;

import java.text.ParseException;

import org.gcube.messaging.accounting.portal.logparser.AccessLogParser.EntryType;
import org.gcube.messaging.common.messages.records.AquamapsRecord;
import org.gcube.messaging.common.messages.records.AquamapsRecord.AquamapsSubType;
import org.gcube.messaging.common.messages.records.BaseRecord;

public class AquamapsEntry  extends LogEntry {
	
	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;

	/**
	 * constructor
	 * @param line the line to parse
	 * @param The EntryType
	 * @throws ParseException exception
	 */
	public AquamapsEntry(String line,EntryType type) throws ParseException{
		super();
		this.line = line;
		record = new AquamapsRecord();
		entryType = type;
		parse();
	}
	
	private String title;
	private String type;
	private long speciesCount;
	private boolean gis;
	private String hspecId;
	
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
	@Override
	public BaseRecord fillRecord() {
		record.setDate(this.getDate());
			
		((AquamapsRecord)record).setTitle(this.getMessage().getAquamapsEntry()[0]);
		((AquamapsRecord)record).setType(this.getMessage().getAquamapsEntry()[1]);
		((AquamapsRecord)record).setGis(Boolean.parseBoolean(this.getMessage().getAquamapsEntry()[2]));
		((AquamapsRecord)record).setSpeciesCount(Long.parseLong(this.getMessage().getAquamapsEntry()[3]));
		if (this.getEntryType().compareTo(EntryType.AQUAMAPSOBJECTGENERATION)== 0){
			((AquamapsRecord)record).setHspecId(this.getMessage().getAquamapsEntry()[4]);
			((AquamapsRecord)record).setAquamapsSubtype(AquamapsSubType.AquamapsGeneration);
		}
		else {
			((AquamapsRecord)record).setObjectID(this.getMessage().getAquamapsEntry()[4]);
			((AquamapsRecord)record).setAquamapsSubtype(AquamapsSubType.AquamapsSavedItem);
		}
		return record;
	}
	
	

}
