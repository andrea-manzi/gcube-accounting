package org.gcube.messaging.accounting.portal.probes;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.gcube.messaging.common.producer.GCUBELocalProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * 
 * @author Andrea Manzi(CERN)
 * 
 * parse exclusion filter file
 */
public class ParseFilters {
	
	static Logger logger = LoggerFactory.getLogger(ParseFilters.class);

	
	public static String defaultFiltersLocation =  System.getenv("CATALINA_HOME")+
		File.separator+"shared"+File.separator+"d4s"+File.separator+"bannedList.xml";
	
	private String filtersLocation = "";
	private Document domDocument = null;
	private ArrayList<String> containsFilter = new ArrayList<String>();
	private ArrayList<String> equalsFilter = new ArrayList<String>();

	
	/**
	 * constructor
	 * @throws Exception
	 */
	public ParseFilters () throws Exception{
		this.filtersLocation = defaultFiltersLocation;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;

		try {
			builder = factory.newDocumentBuilder();
			domDocument = builder.parse(new File(this.filtersLocation));
			parse();
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		
	}

	/**
	 * 
	 * @throws Exception
	 */
	private void parse () throws Exception{
		Element root = domDocument.getDocumentElement();
		try {
			NodeList nodes = root.getChildNodes();
			for(int j=0; j<nodes.getLength(); j++){
				Node node = nodes.item(j);
				if (node  instanceof Element) {
					if (((Element) node).getTagName().compareTo("Contains")==0 ) {
						logger.debug("Found Contains filter: "+node.getTextContent().trim());
						containsFilter.add(node.getTextContent().trim());
					}
					else if  (((Element) node).getTagName().compareTo("Equal")==0 ) {
						logger.debug("Found Equal filter: "+node.getTextContent().trim());
						equalsFilter.add(node.getTextContent().trim());
					}
				}
			}
		}catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
	}	

	public boolean applyFilters(String username){
		
		for (String cont :containsFilter){
			if(username.contains(cont)){
				return true;
			}
		}
		for (String equal :equalsFilter){
			if(username.compareTo(equal)==0)
				return true;
		}
		return false;
		
	}
	
}
