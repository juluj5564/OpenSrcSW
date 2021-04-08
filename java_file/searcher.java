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
import java.io.ObjectInputStream;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordList;
import org.snu.ids.kkma.index.KeywordExtractor;

public class searcher {

	public searcher(String[] input_args) {
		// TODO Auto-generated constructor stub
		
		FileInputStream fileStream = null;
		try {
			fileStream = new FileInputStream(input_args[0]);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ObjectInputStream objectInputStream = null;
		try {
			objectInputStream = new ObjectInputStream(fileStream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Object object = null;
		try {
			object = objectInputStream.readObject();
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			objectInputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// collection.xml ¿­±â
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
			document = documentBuilder.parse("collection.xml");
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		Element root = document.getDocumentElement();
		NodeList children = root.getElementsByTagName("title");
		String[] title_arr = new String[children.getLength()];
		for(int j = 0; j < children.getLength(); j++) {
			Element doc = (Element) children.item(j);
			title_arr[j] = doc.getTextContent();
		}
		
		HashMap hashMap = (HashMap)object;
		
		KeywordExtractor ke = new KeywordExtractor();
		KeywordList kl = ke.extractKeyword(input_args[1], true);
		double[] result_arr = CalcSim(kl, hashMap, title_arr);
		
		switch(result_arr.length) {
		case 0:
			System.out.println("No Document here");
			break;
		case 1:
			System.out.println("Only 1 Document.");
			System.out.println("So Highest title is " + title_arr[0]);
			break;
		case 2:
			System.out.println("Only 2 Documents."); 
			System.out.print("So First High title is ");
			if(result_arr[0] >= result_arr[1]) {
				System.out.println(title_arr[0]);
				System.out.println("Second High title is" + title_arr[1]);
			}
			else {
				System.out.println(title_arr[1]);
				System.out.println("Second High title is" + title_arr[0]);
			}
			break;
		default:
			for(int i = 0; i < result_arr.length; i++) {
				for(int j = 0; j < result_arr.length - 1; j++) {
					if(result_arr[j] < result_arr[j + 1]) {
						double num = result_arr[j];
						result_arr[j] = result_arr[j + 1];
						result_arr[j + 1] = num;
						String str = title_arr[j];
						title_arr[j] = title_arr[j + 1];
						title_arr[j + 1] = str;
					}
				}
			}
			System.out.println("First High title is " + title_arr[0]);
			System.out.println("Second High title is " + title_arr[1]);
			System.out.println("Third High title is " + title_arr[2]);
			break;
		}
	}
	
	public double[] CalcSim(KeywordList kl, HashMap hashMap, String[] title_arr) {
		double[] arr = null;
		return arr;
	}
}
