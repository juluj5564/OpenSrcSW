package practice;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.UnsupportedEncodingException;

import org.snu.ids.kkma.index.Keyword;

public class genSnippet {
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		File text_file = new File(args[3]);
		String query_string = args[5];
		
		BufferedReader bufReader = new BufferedReader(new InputStreamReader(new FileInputStream(text_file),"UTF-8"));
		StringBuilder new_String = new StringBuilder("");
    	String line = "";
    	
    	while((line = bufReader.readLine()) != null){
    		new_String.append(line);
    		new_String.append(":");
    	}
    	
    	String[] str_arr = new_String.toString().split(":");
    	String[] query_arr = query_string.split(" ");
    	
    	int[] check_arr = new int[str_arr.length];
    	for(int i = 0; i < check_arr.length; i++) {
    		check_arr[i] = 0;
    	}
    	
    	for(int i = 0; i < str_arr.length; i++) {
    		String[] line_arr = str_arr[i].split(" ");
    		for(int j = 0; j < line_arr.length; j++) {
    			for(int k = 0; k < query_arr.length; k++) {
    				if(line_arr[j] == query_arr[k]) {
    					System.out.println(line_arr[j] + " " + query_arr[k]);
    					check_arr[i]++;
    				}
    			}
    		}
    	}
    	
    	for(int i = 0; i < check_arr.length; i++) {
    		System.out.println(check_arr[i]);
    	}
    	
    	for(int i = 0; i < check_arr.length; i++) {
			for(int j = 0; j < check_arr.length - 1; j++) {
				if(check_arr[j] < check_arr[j + 1]) {
					String str = str_arr[j];
					str_arr[j] = str_arr[j + 1];
					str_arr[j + 1] = str;
				}
			}
		}
    	
    	System.out.println(str_arr[0]);
	}
}