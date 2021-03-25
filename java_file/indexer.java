package practice;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class indexer {

	public indexer(String args) {
		// TODO Auto-generated constructor stub

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
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
		
		HashMap<String, ArrayList<Double>> TfIdfMap = new HashMap<String, ArrayList<Double>>();
		
		for(int j = 1; j < children.getLength() + 1; j++) {
			Element doc = (Element) children.item(j - 1);
			String body_String = doc.getTextContent();
			String[] getdata = body_String.split("#");
			
			for(int k = 0; k < getdata.length; k++) {
				String[] getdata_keynum = getdata[k].split(":");
				String key = getdata_keynum[0];
				int num = Integer.parseInt(getdata_keynum[1]);
				
				if(TfIdfMap.containsKey(key)) {
					ArrayList<Double> data = TfIdfMap.get(key);
					data.add(Double.valueOf(j));
					data.add(Double.valueOf(num));
					TfIdfMap.put(key, data);
				}
				else {
					ArrayList<Double> data = new ArrayList<Double>();
					data.add(Double.valueOf(j));
					data.add(Double.valueOf(num));
					TfIdfMap.put(key, data);
				}
			}
		}
		
		Set<String> keys = TfIdfMap.keySet();
		for (String key : keys) {
			ArrayList<Double> cal_data = TfIdfMap.get(key);
			int size = cal_data.size() / 2;
			for(int i = 0; i < size; i++) {
				double before_data = cal_data.get(2 * i + 1);
				double after_data = Math.round(before_data * Math.log(children.getLength() / size) * 100.0) / 100.0;
				cal_data.set(2 * i + 1, after_data);
			}
		}
		
		//	실제 hashmap에 들어간 데이터 확인.
		/*
		Set<Entry<String, ArrayList<Double>>> prinsd = TfIdfMap.entrySet();
		
		for(Map.Entry<String, ArrayList<Double>> print : prinsd) {
			System.out.print("key : " + print.getKey());
			System.out.println(", Value : " + print.getValue());
		}
		*/
		
		FileOutputStream fileStream = null;
		try {
			fileStream = new FileOutputStream("index.post");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ObjectOutputStream objectOutputStream = null;
		try {
			objectOutputStream = new ObjectOutputStream(fileStream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			objectOutputStream.writeObject(TfIdfMap);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			objectOutputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//  index.post 파일 출력
		/*	
		FileInputStream fileStream2 = null;
		try {
			fileStream2 = new FileInputStream("index.post");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ObjectInputStream objectInputStream = null;
		try {
			objectInputStream = new ObjectInputStream(fileStream2);
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
		
		System.out.println("객체 type -> " + object.getClass());
		
		HashMap hashMap = (HashMap)object;
		Iterator<String> it = hashMap.keySet().iterator();
		
		while(it.hasNext()) {
			String key = it.next();
			ArrayList<Double> value = (ArrayList<Double>) hashMap.get(key);
			System.out.println(key + " -> " + value);
		}
		*/
	}
}
