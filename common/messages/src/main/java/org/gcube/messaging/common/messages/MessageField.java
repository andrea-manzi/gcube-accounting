package org.gcube.messaging.common.messages;

import java.io.Serializable;
import org.gcube.messaging.common.messages.util.SQLType;


/**
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public class MessageField implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	
	private String name;
	private Object value;
	private SQLType sqlType;
		

	public SQLType getSqlType() {
		return sqlType;
	}
	public void setSqlType(SQLType sqlType) {
		this.sqlType = sqlType;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	
}
