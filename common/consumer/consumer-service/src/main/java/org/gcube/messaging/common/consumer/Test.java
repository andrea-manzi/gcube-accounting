package org.gcube.messaging.common.consumer;


import java.io.Serializable;

/**
 * 
 *  Class that Models a Test
 *  
 * @author Andrea Manzi(CERN)
 *
 */
public class Test implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String type;
	private String Description;
	private Integer testNumber;
	
	
	/**
	 * get the test number
	 * @return the test number
	 */
	public Integer getTestNumber() {
		return testNumber;
	}

	/**
	 * set the test number
	 * @param testNumber the test number
	 */
	public void setTestNumber(Integer testNumber) {
		this.testNumber = testNumber;
	}
	/**
	 * get the type
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * set the type
	 * @param type the type
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * get the description
	 * @return the description
	 */
	public String getDescription() {
		return Description;
	}

	/**
	 * set the description
	 * @param description the description
	 */
	public void setDescription(String description) {
		Description = description;
	}
	/**
	 * to string
	 */
	public String toString(){
		return this.type+"/" + this.Description+"/" +this.testNumber; 
		}
	
	public Test() {	}
}
