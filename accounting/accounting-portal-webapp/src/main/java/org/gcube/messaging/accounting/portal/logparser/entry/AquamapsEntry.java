package org.gcube.messaging.accounting.portal.logparser.entry;

import java.text.ParseException;
import java.util.StringTokenizer;

import org.gcube.messaging.accounting.portal.logparser.AccessLogParser.EntryType;
import org.gcube.messaging.common.messages.records.AquamapsRecord;
import org.gcube.messaging.common.messages.records.AquamapsRecord.AquamapsSubType;
import org.gcube.messaging.common.messages.records.BaseRecord;

public class AquamapsEntry  extends LogEntry {
	
	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	
	//aquamaps
	private String []aquamapsEntry = new String [5];


	protected enum AquamapsTokens {
		TITLE("TITLE"),
		TYPE("TYPE"),
		ID("ID"),
		HSPEC("HSPEC"),
		SPECIES_COUNT("SPECIES_COUNT"),
		GIS("GIS");
		String tokens;
		AquamapsTokens(String tokens) {this.tokens = tokens;}
		public String toString() {return this.tokens;}
	}
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
		
		tokenizer = new StringTokenizer(message.getMessage(),Message.messageTokensSeparator);
		while(tokenizer.hasMoreElements()){
			String tok = tokenizer.nextToken();
			
			if (tok.contains(AquamapsTokens.TITLE.tokens))
				aquamapsEntry[0] = Message.getValue(tok);
			else if (tok.contains(AquamapsTokens.TYPE.tokens))
				aquamapsEntry[1] = Message.getValue(tok);
			else if (tok.contains(AquamapsTokens.GIS.tokens))
				aquamapsEntry[2] = Message.getValue(tok);
			else if (tok.contains(AquamapsTokens.SPECIES_COUNT.tokens))
				aquamapsEntry[3] = Message.getValue(tok);
			else if (tok.contains(AquamapsTokens.HSPEC.tokens))
				aquamapsEntry[4] = Message.getValue(tok);
			else if (tok.contains(AquamapsTokens.ID.tokens))
				aquamapsEntry[4] = Message.getValue(tok);
			
		}
		record.setDate(this.getDate());
			
		((AquamapsRecord)record).setTitle(aquamapsEntry[0]);
		((AquamapsRecord)record).setType(aquamapsEntry[1]);
		((AquamapsRecord)record).setGis(Boolean.parseBoolean(aquamapsEntry[2]));
		((AquamapsRecord)record).setSpeciesCount(Long.parseLong(aquamapsEntry[3]));
		if (this.getEntryType().compareTo(EntryType.AQUAMAPSOBJECTGENERATION)== 0){
			((AquamapsRecord)record).setHspecId(aquamapsEntry[4]);
			((AquamapsRecord)record).setAquamapsSubtype(AquamapsSubType.AquamapsGeneration);
		}
		else {
			((AquamapsRecord)record).setObjectID(aquamapsEntry[4]);
			((AquamapsRecord)record).setAquamapsSubtype(AquamapsSubType.AquamapsSavedItem);
		}
		return record;
	}
	
	

}
