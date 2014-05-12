package org.gcube.messaging.common.messages;


import java.io.Serializable;
/**
 * Models a Test 
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public class Test implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TestType type;
	private Priority priority;
	private String Description;
	private Integer testNumber;
	private Object testResult;
	
	/**
	 * get the result object
	 * @return the result object
	 */
	public Object getTestResult() {
		return testResult;
	}

	/**
	 * set the result object
	 * @param testResult the result object
	 */
	public void setTestResult(Object testResult) {
		this.testResult = testResult;
	}
	/**
	 * default constructor
	 */
	public Test() {	}
	
	/**
	 * get the testType
	 * @return the testType
	 */
	public TestType getType() {
		return type;
	}

	/**
	 * set the test type
	 * @param type the test type
	 */
	public void setType(TestType type) {
		this.type = type;
	}

	/** Result types */
	public static enum TestType {
    	DISK_QUOTA("DISK_QUOTA"),
    	CPU_LOAD("CPU_LOAD"),
    	MEMORY_AVAILABLE("MEMORY_AVAILABLE"),
    	LAST_UPDATE("LAST_UPDATE"),
    	NOTIFICATION("NOTIFICATION"),
    	TEST("TEST"),
    	CALLINFO("CALLINFO"),
    	CPUINFO("CPUINFO");
    	String type;
    	TestType(String type) {this.type = type;}
		public String toString() {return this.type;}
    };
    
    /** Priority types */
	public static enum Priority {
		HIGH("HIGH"),
		LOW("LOW");
    	String priority;
    	
		Priority(String priority) {this.priority = priority;}
		public String toString() {return this.priority;}
    };
	
    /**
     * get the priority
     * @return the priority
     */
    public Priority getPriority() {
		return priority;
	}
    /**
     * set the priority
     * @param priority the priority
     */
	public void setPriority(Priority priority) {
		this.priority = priority;
	}
    
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
	public String toString()
	{
		return this.type+"/" + this.Description+"/" +this.testNumber +"/" +this.priority;
	}
	
	
}
