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

public class makeCollection {

	public makeCollection(String args) {
		// TODO Auto-generated method stub
		File dir = new File(args + "/2주차실습html");
		//System.out.println(dir);
		
		File []fileList = dir.listFiles();
		
	 	DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	  	DocumentBuilder docBuilder = null;
	  	
		try {
			docBuilder = docFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		    	
		Document doc = docBuilder.newDocument();
		
		Element docs = doc.createElement("docs");
    	doc.appendChild(docs);
		
		int num = 0;
		
		for(File file : fileList) {
			String titleline = "";
        	String bodyline = "";
		        	
			if(file.isFile()) {
				try{
					Element docid = doc.createElement("doc");
		            docs.appendChild(docid);
		            docid.setAttribute("id", Integer.toString(num));
				            
		            Element title = doc.createElement("title");
		            docid.appendChild(title);
				            
		            Element body = doc.createElement("body");
		            docid.appendChild(body);
				            
					File fileadd = new File(args + "/2주차실습html/" + file.getName());

	            	BufferedReader bufReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileadd),"UTF-8"));

	            	String line = "";
	            	while((line = bufReader.readLine()) != null){
	            		if(line.contains("<title>")) {
	            			int start = line.indexOf("<title>") + 7;
	            			int end = line.indexOf("</title>");
	            			//System.out.println(start + "  " + end);
	            			titleline = titleline.concat(line.substring(start, end));
	            		}
	            		if(line.contains("<p>")) {
	            			int start = line.indexOf("<p>") + 3;
	            			int end = line.indexOf("</p>");
	            			//System.out.println(start + "  " + end);
	            			bodyline = bodyline.concat(line.substring(start, end));
	            		}
	            	}
	            	bufReader.close();
			            	
	            	title.appendChild(doc.createTextNode(titleline));
	            	body.appendChild(doc.createTextNode(bodyline));
		            	
		        }catch (FileNotFoundException e) {
		        }catch(IOException e){
		            System.out.println(e);
		        }
			}
			num++;
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
		    	
    	DOMSource source = new DOMSource(doc);
    	
    	StreamResult result = null;
		try {
			result = new StreamResult(new FileOutputStream(new File("C:/Users/a/eclipse-workspace/openSource/collection.xml")));
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
