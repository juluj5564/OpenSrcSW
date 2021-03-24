package practice;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
 
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.List;

import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordList;
import org.snu.ids.kkma.index.KeywordExtractor;

public class makeKeyword {

	public makeKeyword(String args) {
		// TODO Auto-generated method stub
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setIgnoringElementContentWhitespace(true);
		DocumentBuilder documentBuilder = null;
		try {
			documentBuilder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Document document = null;
		try {
			document = documentBuilder.parse(args);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Element root = document.getDocumentElement();
		NodeList children = root.getElementsByTagName("body");
		
		for(int j = 0; j < children.getLength(); j++) {
			Element doc = (Element) children.item(j);
			//System.out.println(doc.getTextContent());
			String body_String = doc.getTextContent();
			KeywordExtractor ke = new KeywordExtractor();
			KeywordList kl = ke.extractKeyword(body_String, true);
			StringBuilder new_String = new StringBuilder("");
			for(int i = 0; i < kl.size(); i++) {
				Keyword kwrd = kl.get(i);
				new_String.append(kwrd.getString());
				new_String.append(":");
				new_String.append(Integer.toString(kwrd.getCnt()));
				new_String.append("#");
			}
			doc.setTextContent(new_String.toString());
			//System.out.println(new_String);
		}
		
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		
    	Transformer transformer = null;
		try {
			transformer = transformerFactory.newTransformer();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
    	
    	DOMSource source = new DOMSource(root);
    	
    	StreamResult result = null;
		try {
			result = new StreamResult(new FileOutputStream(new File("C:/Users/a/eclipse-workspace/openSource/index.xml")));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	try {
			transformer.transform(source, result);
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
