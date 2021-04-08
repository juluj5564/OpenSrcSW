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
		int print_num = 0;
		
		for(int i = 0; i < result_arr.length; i++) {
			if(result_arr[i] != 0) {
				print_num++;
			}
		}
		
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
		
		System.out.print("If Top title in large order is ");
		for(int i = 0; i < print_num; i++) {
			System.out.print(title_arr[i] + "\t");
		}
	}
	
	public double[] CalcSim(KeywordList kl, HashMap hashMap, String[] title_arr) {
		String[] key_arr = new String[kl.size()];
		int[] value_arr = new int[kl.size()];
		double[] inner_arr = InnerProduct(kl, hashMap, title_arr);
		double[] result_arr = new double[title_arr.length];
		
		for(int i = 0; i < kl.size(); i++) {
			Keyword kwrd = kl.get(i);
			key_arr[i] = kwrd.getString();
			value_arr[i] = kwrd.getCnt();
		}
		
		for(double k = 0; k < title_arr.length; k++) {
			double result = 0.0;
			double Q_value = 0.0;
			double id_value = 0.0;
			for(int i = 0; i < kl.size(); i++) {
				Iterator<String> it = hashMap.keySet().iterator();
				while(it.hasNext()) {
					String post_key = it.next();
					if(key_arr[i].equals(post_key)) {
						ArrayList<Double> value = (ArrayList<Double>) hashMap.get(post_key);
						for(int j = 0; j < value.size() / 2; j++) {
							if(value.get(2 * j) == k + 1) {
								Q_value += value_arr[i] * value_arr[i];
								id_value += value.get(2 * j + 1) * value.get(2 * j + 1);
								break;
							}
						}
						break;
					}
				}
			}
			if(Q_value == 0 || id_value == 0) {
				result_arr[(int) k] = 0;
			}
			else {
				result_arr[(int) k] = inner_arr[(int) k] / (Math.sqrt(Q_value) * Math.sqrt(id_value));				
			}
		}
		return result_arr;
	}
	
	public double[] InnerProduct(KeywordList kl, HashMap hashMap, String[] title_arr) {
		String[] key_arr = new String[kl.size()];
		int[] value_arr = new int[kl.size()];
		double[] result_arr = new double[title_arr.length];
		
		for(int i = 0; i < kl.size(); i++) {
			Keyword kwrd = kl.get(i);
			key_arr[i] = kwrd.getString();
			value_arr[i] = kwrd.getCnt();
		}
		
		for(double k = 0; k < title_arr.length; k++) {
			double result = 0.0;
			for(int i = 0; i < kl.size(); i++) {
				Iterator<String> it = hashMap.keySet().iterator();
				while(it.hasNext()) {
					String post_key = it.next();
					if(key_arr[i].equals(post_key)) {
						ArrayList<Double> value = (ArrayList<Double>) hashMap.get(post_key);
						for(int j = 0; j < value.size() / 2; j++) {
							if(value.get(2 * j) == k + 1) {
								result += value_arr[i] * value.get(2 * j + 1);
								break;
							}
						}
						break;
					}
				}
			}
			System.out.println(result);
			result_arr[(int) k] = result;
		}
		
		return result_arr;
	}
}
