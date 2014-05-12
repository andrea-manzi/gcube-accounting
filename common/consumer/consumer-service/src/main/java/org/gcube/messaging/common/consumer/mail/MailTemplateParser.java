package org.gcube.messaging.common.consumer.mail;

import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.gcube.common.core.utils.logging.GCUBELog;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 
 * @author Andrea Manzi(CERN)
 *
 */
public class MailTemplateParser {

	/**
	 * The Logger
	 */
	public  static GCUBELog logger = new GCUBELog(MailTemplateParser.class);
	
	private HashMap<String,String> templateMap = new HashMap<String,String>();
	
	private Element root = null;
	
	public MailTemplateParser (String file) throws Exception{
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		Document domDocument = null;
		try {
			builder = factory.newDocumentBuilder();
			domDocument = builder.parse(file);
		} catch (Exception e) {
			throw e;
		}
	
		root = domDocument.getDocumentElement();
	
	}
	
	/**
	 * parse the Templates
	 */
	public void parseMailTemplates(){
		
		NodeList nodeList = root.getChildNodes();
		Element el = null;
		for(int i=0; i<nodeList.getLength(); i++){
			Node node = nodeList.item(i);
			if (node.getFirstChild()==null)
				continue;
			String expression = node.getFirstChild().getNodeValue().trim();
			if (expression.compareTo("")==0)
				continue;
			if ( node  instanceof Element){
				el = (Element) node;
				logger.debug("Adding MailTemplate for Message Type : "+el.getTagName());
				templateMap.put(el.getTagName(),node.getFirstChild().getNodeValue().trim());
			} 
		}
		
	}
	
	public HashMap<String, String> getTemplateMap() {
		return templateMap;
	}

	public void setTemplateMap(HashMap<String, String> templateMap) {
		this.templateMap = templateMap;
	}
}
